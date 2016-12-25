package fxtrader.com.app.mine;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;
import java.util.Map;

import fxtrader.com.app.AppApplication;
import fxtrader.com.app.R;
import fxtrader.com.app.adapter.ListBaseAdapter;
import fxtrader.com.app.base.BaseFragment;
import fxtrader.com.app.config.LoginConfig;
import fxtrader.com.app.entity.OpenPacket;
import fxtrader.com.app.entity.PacketDetailEntity;
import fxtrader.com.app.entity.PacketListEntity;
import fxtrader.com.app.entity.CurrencyDetailEntity;
import fxtrader.com.app.entity.UserEntity;
import fxtrader.com.app.http.ParamsUtil;
import fxtrader.com.app.http.RetrofitUtils;
import fxtrader.com.app.http.api.UserApi;
import fxtrader.com.app.lrececlerview.recyclerview.LRecyclerView;
import fxtrader.com.app.lrececlerview.recyclerview.LRecyclerViewAdapter;
import fxtrader.com.app.tools.ContractUtil;
import fxtrader.com.app.tools.DateTools;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * 收到的红包
 * Created by zhangyuzhu on 2016/12/11.
 */
public class RedEnvelopeReceiveFragment extends BaseFragment {

    private TextView mMoneyTv;
    private TextView mNumTv;
    private LRecyclerView mRecyclerView;
    private MyAdapter mAdapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_red_envelope_receive, container, false);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mMoneyTv = (TextView) view.findViewById(R.id.red_envelope_receive_money_tv);
        mNumTv = (TextView) view.findViewById(R.id.red_envelope_receive_num_tv);

        mRecyclerView = (LRecyclerView) view.findViewById(R.id.red_envelope_receive_recycler_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecyclerView.setPullRefreshEnabled(false);
        mAdapter = new MyAdapter(getActivity());
        LRecyclerViewAdapter mHeaderAndFooterRecyclerViewAdapter = new LRecyclerViewAdapter(getActivity(), mAdapter);
        mRecyclerView.setAdapter(mHeaderAndFooterRecyclerViewAdapter);
        getReceivedPackets();
    }

    private void getReceivedPackets() {
        showProgressDialog();
        UserApi userApi = RetrofitUtils.createApi(UserApi.class);
        String token = ParamsUtil.getToken();
        final Call<PacketListEntity> respon = userApi.receivePackets(token, getParams());
        respon.enqueue(new Callback<PacketListEntity>() {
            @Override
            public void onResponse(Call<PacketListEntity> call, Response<PacketListEntity> response) {
                PacketListEntity entity = response.body();
                if (entity != null && entity.getObject() != null) {
                    setViews(entity);
                    List<PacketDetailEntity> list = entity.getObject().getRedPacketOut();
                    if (list != null) {
                        mAdapter.setDataList(entity.getObject().getRedPacketOut());
                    }
                }
                dismissProgressDialog();
            }

            @Override
            public void onFailure(Call<PacketListEntity> call, Throwable t) {
                dismissProgressDialog();
            }
        });
    }

    private Map<String, String> getParams(){
        final Map<String, String> params = ParamsUtil.getCommonParams();
        params.put("method", "redhouse.checkRedPacketIn");
        String id = LoginConfig.getInstance().getId();
        params.put("customerId", id);
        params.put("sign", ParamsUtil.sign(params));
        return params;
    }

    private void setViews(PacketListEntity entity) {
        PacketListEntity.Packets packets = entity.getObject();
        if (packets != null) {
            mMoneyTv.setText(ContractUtil.getDouble(entity.getObject().getAmount(), 2) + "");
            mNumTv.setText(entity.getObject().getCount() + "");
        }
    }

    class MyAdapter extends ListBaseAdapter<PacketDetailEntity> {
        private LayoutInflater mLayoutInflater;

        public MyAdapter(Context context) {
            mLayoutInflater = LayoutInflater.from(context);
            mContext = context;
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new ViewHolder(mLayoutInflater.inflate(R.layout.item_red_envelope_receive, parent, false));
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            PacketDetailEntity item = mDataList.get(position);
            ViewHolder viewHolder = (ViewHolder) holder;
            viewHolder.nameTv.setText(item.getCustomerId());
            viewHolder.timeTv.setText(DateTools.changeToDate2(item.getCreateTime()));
            if (item.getStatus() == 1) {//1是未拆开2是拆开
                viewHolder.moneyTv.setVisibility(View.VISIBLE);
                viewHolder.mMoneyLayout.setVisibility(View.GONE);
                viewHolder.mOpenTv.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        openPacket();
                    }
                });
            } else {
                viewHolder.moneyTv.setVisibility(View.GONE);
                viewHolder.mMoneyLayout.setVisibility(View.VISIBLE);
                viewHolder.moneyTv.setText(String.valueOf(item.getAllAmount()));
            }
        }



        class ViewHolder extends RecyclerView.ViewHolder {

            private TextView nameTv;
            private TextView timeTv;
            private TextView mOpenTv;
            private LinearLayout mMoneyLayout;
            private TextView moneyTv;

            public ViewHolder(View view) {
                super(view);
                nameTv = (TextView) view.findViewById(R.id.item_red_envelope_receive_name_tv);
                timeTv = (TextView) view.findViewById(R.id.item_red_envelope_receive_time_tv);
                mOpenTv = (TextView) view.findViewById(R.id.item_red_envelope_open_tv);
                mMoneyLayout = (LinearLayout) view.findViewById(R.id.item_red_envelope_money_layout);
                moneyTv = (TextView) view.findViewById(R.id.item_red_envelope_receive_money_tv);
            }
        }
    }

    private void openPacket() {
        showProgressDialog();
        UserApi userApi = RetrofitUtils.createApi(UserApi.class);
        String token = ParamsUtil.getToken();
        final Call<OpenPacket> respon = userApi.openPacket(token, getOpenPacketParams());
        respon.enqueue(new Callback<OpenPacket>() {
            @Override
            public void onResponse(Call<OpenPacket> call, Response<OpenPacket> response) {
                OpenPacket entity = response.body();
                if (entity.isSuccess()) {
                    getReceivedPackets();
                }
                dismissProgressDialog();
            }

            @Override
            public void onFailure(Call<OpenPacket> call, Throwable t) {
                dismissProgressDialog();
            }
        });
    }

    private Map<String, String> getOpenPacketParams(){
        final Map<String, String> params = ParamsUtil.getCommonParams();
        params.put("method", "redhouse.checkRedPacketIn");
        String id = LoginConfig.getInstance().getId();
        params.put("customerId", id);
        params.put("sign", ParamsUtil.sign(params));
        return params;
    }


}
