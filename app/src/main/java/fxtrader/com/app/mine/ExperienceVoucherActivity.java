package fxtrader.com.app.mine;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import fxtrader.com.app.R;
import fxtrader.com.app.adapter.ListBaseAdapter;
import fxtrader.com.app.base.BaseActivity;
import fxtrader.com.app.db.helper.TicketsHelper;
import fxtrader.com.app.db.helper.UserCouponsHelper;
import fxtrader.com.app.entity.CouponDetailEntity;
import fxtrader.com.app.entity.CouponListEntity;
import fxtrader.com.app.entity.TicketEntity;
import fxtrader.com.app.entity.TicketListEntity;
import fxtrader.com.app.http.ParamsUtil;
import fxtrader.com.app.http.RetrofitUtils;
import fxtrader.com.app.http.api.UserApi;
import fxtrader.com.app.tools.ContractUtil;
import fxtrader.com.app.tools.DateTools;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * 体验券
 * Created by zhangyuzhu on 2016/11/28.
 */
public class ExperienceVoucherActivity extends BaseActivity {

    private HashMap<Integer, String> mTicketMap = new HashMap<>();

    private MyAdapter mAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentLayout(R.layout.activity_experience_voucher);
        initViews();
        getTickets();
    }

    private void initViews(){
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.experience_voucher_rec);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        mAdapter = new MyAdapter(this);
        recyclerView.setAdapter(mAdapter);
    }

    private void getTickets() {
        showProgressDialog();
        UserApi userApi = RetrofitUtils.createApi(UserApi.class);
        Call<TicketListEntity> repos = userApi.tickets(getTicketsParams());
        repos.enqueue(new Callback<TicketListEntity>() {
            @Override
            public void onResponse(Call<TicketListEntity> call, Response<TicketListEntity> response) {
                TicketListEntity tickets = response.body();
                if (tickets != null && tickets.isSuccess()) {
                    if (tickets.getObject() != null && !tickets.getObject().isEmpty()){
                        for (TicketEntity ticket : tickets.getObject()){
                            mTicketMap.put(ticket.getId(), ticket.getValue() + "");
                        }
                        TicketsHelper.getInstance().save(tickets);
                        getUserCoupon();
                    } else {
                        dismissProgressDialog();
                    }

                } else {
                    dismissProgressDialog();
                }


            }

            @Override
            public void onFailure(Call<TicketListEntity> call, Throwable t) {
                Log.e("zyu", t.getMessage());
            }
        });
    }

    private Map<String, String> getTicketsParams(){
        final Map<String, String> params = ParamsUtil.getCommonParams();
        params.put("method", "gdiex.tickets.list");
        params.put("sign", ParamsUtil.sign(params));
        return params;
    }

    private void getUserCoupon() {
        UserApi userApi = RetrofitUtils.createApi(UserApi.class);
        String token = ParamsUtil.getToken();
        Call<CouponListEntity> repos = userApi.coupons(token, getCouponParams());
        repos.enqueue(new Callback<CouponListEntity>() {
            @Override
            public void onResponse(Call<CouponListEntity> call, Response<CouponListEntity> response) {
                CouponListEntity entity = response.body();
                if (entity != null && entity.isSuccess()) {
                    showToastShort(entity.getMessage());
                    UserCouponsHelper.getInstance().save(entity);
                    mAdapter.setDataList(entity.getObject());
                }

                dismissProgressDialog();
            }

            @Override
            public void onFailure(Call<CouponListEntity> call, Throwable t) {
                Log.e("zyu", t.getMessage());
                dismissProgressDialog();
            }
        });
    }

    private Map<String, String> getCouponParams(){
        final Map<String, String> params = ParamsUtil.getCommonParams();
        params.put("method", "gdiex.coupon.users");
        params.put("sign", ParamsUtil.sign(params));
        return params;
    }

    class MyAdapter extends ListBaseAdapter<CouponDetailEntity> {
        private LayoutInflater mLayoutInflater;

        public MyAdapter(Context context) {
            mLayoutInflater = LayoutInflater.from(context);
            mContext = context;
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new ViewHolder(mLayoutInflater.inflate(R.layout.item_experience_voucher, parent, false));
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            CouponDetailEntity item = mDataList.get(position);
            ViewHolder viewHolder = (ViewHolder) holder;
            String money = mTicketMap.get(item.getTicketId());
            if (!TextUtils.isEmpty(money)) {
                viewHolder.moneyTv.setText(getString(R.string.money_num, money));
            }
            String startTime = DateTools.changeToDay(item.getDrawDate());
            String endTime = DateTools.changeToDay(item.getLastUseTime());
            viewHolder.validTv.setText(getString(R.string.valid_from_to, startTime, endTime));
        }


        class ViewHolder extends RecyclerView.ViewHolder {

            private TextView experienceVoucherTv;
            private TextView moneyTv;
            private TextView validTv;

            public ViewHolder(View view) {
                super(view);
                experienceVoucherTv = (TextView) view.findViewById(R.id.experience_voucher_tv);
                moneyTv = (TextView) view.findViewById(R.id.experience_voucher_num_tv);
                validTv = (TextView) view.findViewById(R.id.experience_voucher_valid_tv);
            }
        }
    }

}
