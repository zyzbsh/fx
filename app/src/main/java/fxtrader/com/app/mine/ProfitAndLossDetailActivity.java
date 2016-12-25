package fxtrader.com.app.mine;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import fxtrader.com.app.R;
import fxtrader.com.app.adapter.ListBaseAdapter;
import fxtrader.com.app.base.BaseActivity;
import fxtrader.com.app.entity.CurrencyDetailEntity;
import fxtrader.com.app.entity.CurrencyListEntity;
import fxtrader.com.app.http.HttpConstant;
import fxtrader.com.app.http.ParamsUtil;
import fxtrader.com.app.http.RetrofitUtils;
import fxtrader.com.app.http.api.UserApi;
import fxtrader.com.app.lrececlerview.interfaces.OnItemClickListener;
import fxtrader.com.app.lrececlerview.recyclerview.LRecyclerView;
import fxtrader.com.app.lrececlerview.recyclerview.LRecyclerViewAdapter;
import fxtrader.com.app.tools.DateTools;
import fxtrader.com.app.tools.LogZ;
import fxtrader.com.app.view.PaymentDetailDialog;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * 收支明细
 * Created by zhangyuzhu on 2016/11/28.
 */
public class ProfitAndLossDetailActivity extends BaseActivity {

    private static final int PAGE_SIZE = 20;

    private LRecyclerView mRecyclerView;

    private MyAdapter mAdapter;

    private int mPage = 0;

    private int mTotal = 0;

    private int mCount = 0;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentLayout(R.layout.activity_profit_and_loss_detail);
        initView();
    }

    private void initView(){
        mRecyclerView = (LRecyclerView) findViewById(R.id.profit_and_loss_detail_rec);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setPullRefreshEnabled(false);
        mAdapter = new MyAdapter(this);
        LRecyclerViewAdapter mHeaderAndFooterRecyclerViewAdapter = new LRecyclerViewAdapter(this, mAdapter);
        mRecyclerView.setAdapter(mHeaderAndFooterRecyclerViewAdapter);
        mRecyclerView.setLScrollListener(new MyListener());
        mHeaderAndFooterRecyclerViewAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                CurrencyDetailEntity entity = mAdapter.getDataList().get(position);
                PaymentDetailDialog dialog = new PaymentDetailDialog(ProfitAndLossDetailActivity.this, entity);
                dialog.show();
            }

            @Override
            public void onItemLongClick(View view, int position) {

            }
        });
        getData();
    }

    private class MyListener implements LRecyclerView.LScrollListener{

        @Override
        public void onRefresh() {

        }

        @Override
        public void onScrollUp() {

        }

        @Override
        public void onScrollDown() {

        }

        @Override
        public void onBottom() {
            if (mCount < mTotal) {
                getData();
            }
        }

        @Override
        public void onScrolled(int distanceX, int distanceY) {

        }
    }


    private void getData() {
        showProgressDialog();
        UserApi userApi = RetrofitUtils.createApi(UserApi.class);
        String token = ParamsUtil.getToken();
        Call<CurrencyListEntity> respo = userApi.currencyDetail(token, getParams());
        respo.enqueue(new Callback<CurrencyListEntity>() {
            @Override
            public void onResponse(Call<CurrencyListEntity> call, Response<CurrencyListEntity> response) {
                CurrencyListEntity entity = response.body();
                LogZ.i(entity.toString());
                List<CurrencyDetailEntity> list = entity.getObject().getContent();
                if (mTotal == 0 && entity.getObject() != null) {
                    mTotal = entity.getObject().getTotalElements();
                    LogZ.i("total = " + mTotal);
                }
                if (list != null) {
                    LogZ.i("list = " + list.toString());
                    mCount = mCount + list.size();
                    mAdapter.addAll(list);
                    mAdapter.notifyDataSetChanged();
                    mPage++;
                }
                dismissProgressDialog();
            }

            @Override
            public void onFailure(Call<CurrencyListEntity> call, Throwable t) {
                LogZ.i(t.toString());
                dismissProgressDialog();
            }
        });
    }

    private Map<String, String> getParams(){
        final Map<String, String> params = ParamsUtil.getCommonParams();
        params.put("method", "gdiex.currency.listDetails");
        params.put("page", String.valueOf(mPage));
        params.put("size", String.valueOf(PAGE_SIZE));
        params.put("sort", "date,DESC");
        params.put("sign", ParamsUtil.sign(params));
        return params;
    }

    class MyAdapter extends ListBaseAdapter<CurrencyDetailEntity> {
        private LayoutInflater mLayoutInflater;

        public MyAdapter(Context context) {
            mLayoutInflater = LayoutInflater.from(context);
            mContext = context;
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new ViewHolder(mLayoutInflater.inflate(R.layout.item_profit_and_loss_detail, parent, false));
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            CurrencyDetailEntity item = mDataList.get(position);
            ViewHolder viewHolder = (ViewHolder) holder;
            String type = item.getTransactionType();
            if (HttpConstant.TransactionType.BUILD_METALORDER.equals(type)) {
                viewHolder.operateTv.setText(R.string.build_position);
            } else {
                viewHolder.operateTv.setText(R.string.close_position);
            }
            double money = item.getTransactionMoney();
            String moneyShow;
            if (money >= 0) {
                viewHolder.iconIm.setImageResource(R.mipmap.icon_plus_circle);
                viewHolder.numTv.setTextColor(Color.parseColor("#f43e3b"));
                moneyShow = "+" + String.valueOf(money);
            } else {
                viewHolder.iconIm.setImageResource(R.mipmap.icon_minus_circle);
                viewHolder.numTv.setTextColor(Color.parseColor("#42d55a"));
                moneyShow = "-" + String.valueOf(money);
            }
            viewHolder.numTv.setText(moneyShow);
            viewHolder.dateTv.setText(DateTools.changeToDate(item.getDate()));
            viewHolder.accountTv.setText(getString(R.string.account_num, String.valueOf(item.getRemainingMoney())));
        }


        class ViewHolder extends RecyclerView.ViewHolder {

            private ImageView iconIm;
            private TextView operateTv;
            private TextView numTv;
            private TextView dateTv;
            private TextView accountTv;

            public ViewHolder(View view) {
                super(view);
                iconIm = (ImageView) view.findViewById(R.id.item_profit_and_loss_im);
                operateTv = (TextView) view.findViewById(R.id.item_profit_and_loss_operate_tv);
                numTv = (TextView) view.findViewById(R.id.item_profit_and_loss_num_tv);
                dateTv = (TextView) view.findViewById(R.id.item_profit_and_loss_date_tv);
                accountTv = (TextView) view.findViewById(R.id.item_profit_and_loss_account_tv);
            }
        }
    }
}
