package fxtrader.com.app.find;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import fxtrader.com.app.R;
import fxtrader.com.app.adapter.ListBaseAdapter;
import fxtrader.com.app.base.BaseFragment;
import fxtrader.com.app.config.LoginConfig;
import fxtrader.com.app.entity.MasterEntity;
import fxtrader.com.app.entity.MasterListEntity;
import fxtrader.com.app.entity.ProfitEntity;
import fxtrader.com.app.entity.SubscribeEntity;
import fxtrader.com.app.homepage.ProfitListActivity;
import fxtrader.com.app.http.manager.MasterListManager;
import fxtrader.com.app.http.manager.ResponseListener;
import fxtrader.com.app.http.manager.SubscribeManager;
import fxtrader.com.app.lrececlerview.recyclerview.LRecyclerView;
import fxtrader.com.app.lrececlerview.recyclerview.LRecyclerViewAdapter;
import fxtrader.com.app.tools.ContractUtil;
import fxtrader.com.app.tools.LogZ;
import fxtrader.com.app.view.CircleImageView;
import fxtrader.com.app.view.MyDecoration;

/**
 * 高手热点
 * Created by zhangyuzhu on 2016/12/4.
 */
public class MasterHotFragment extends BaseFragment implements View.OnClickListener {

    private LRecyclerView mRecyclerView;

    private DataAdapter mAdapter;

    private boolean mIsLogin;

    private TextView mEmptyTv;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.view_find, container, false);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mIsLogin = LoginConfig.getInstance().isLogin();
        view.findViewById(R.id.find_more_tv).setOnClickListener(this);
        mEmptyTv = (TextView) view.findViewById(R.id.find_empty_tv);

        mRecyclerView = (LRecyclerView) view.findViewById(R.id.find_recycler_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecyclerView.setPullRefreshEnabled(false);
        mAdapter = new DataAdapter(getActivity());
        LRecyclerViewAdapter mHeaderAndFooterRecyclerViewAdapter = new LRecyclerViewAdapter(getContext(), mAdapter);
        mRecyclerView.setAdapter(mHeaderAndFooterRecyclerViewAdapter);
        mRecyclerView.addItemDecoration(new MyDecoration(getActivity(), MyDecoration.VERTICAL_LIST));
        View emptyView = LayoutInflater.from(getContext()).inflate(R.layout.view_empty_recycler_view, null);
        mRecyclerView.setEmptyView(emptyView);

        requestData();
    }

    public void refresh(){
        mIsLogin = LoginConfig.getInstance().isLogin();
        requestData();
    }

    @Override
    public void onResume() {
        super.onResume();
        mIsLogin = LoginConfig.getInstance().isLogin();
        requestData();
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (!hidden) {
            mIsLogin = LoginConfig.getInstance().isLogin();
            requestData();
        }
    }

    private void requestData(){
        MyResponseListener listener = new MyResponseListener();
        if (LoginConfig.getInstance().isLogin()) {
            String organId = "" + LoginConfig.getInstance().getOrganId();
            String customerId = LoginConfig.getInstance().getId();
            MasterListManager.getInstance().getMastersWithLogined(organId, customerId, listener);
        } else {
            MasterListManager.getInstance().getMasters(listener);
        }
    }

    private class MyResponseListener implements ResponseListener<MasterListEntity>{
        @Override
        public void success(MasterListEntity object) {
            if (object != null && object.isSuccess()) {
                if (object.getObject() != null && !object.getObject().isEmpty()) {
                    mEmptyTv.setVisibility(View.GONE);
                    mRecyclerView.setVisibility(View.VISIBLE);
                    mAdapter.setDataList(object.getObject());
                } else {
                    mRecyclerView.setVisibility(View.GONE);
                    mEmptyTv.setVisibility(View.VISIBLE);
                }
            }
        }

        @Override
        public void error(String error) {

        }
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.find_more_tv://更多
                openActivity(ProfitListActivity.class);
                break;
            default:
                break;
        }
    }

    class DataAdapter extends ListBaseAdapter<MasterEntity> {

        private LayoutInflater mLayoutInflater;

        private Context mContext;

        public DataAdapter(Context context) {
            mLayoutInflater = LayoutInflater.from(context);
            mContext = context;
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new ViewHolder(mLayoutInflater.inflate(R.layout.item_master_hot, parent, false));
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            MasterEntity item = mDataList.get(position);

            ViewHolder viewHolder = (ViewHolder) holder;
            viewHolder.avatarNameTv.setText(item.getNickname());
            viewHolder.amountTv.setText("" + ContractUtil.getDouble(item.getProfit(), 1));
            viewHolder.buyPriceTv.setText("买入价");
            viewHolder.buyPriceNumTv.setText("1930.2");
            viewHolder.buyNameTv.setText(item.getCodeName());
            String dealDirection = (item.getDealDirection() == 1) ? "买涨" : "买跌";
            viewHolder.buySelectTv.setText(dealDirection);
            viewHolder.buySelectNumTv.setText(item.getDealCount() + "手");
            setFollowTv(viewHolder.followTv, item, position);
        }

        private void setFollowTv(TextView followTv, final MasterEntity entity, final int position) {

            if (mIsLogin) {
                final String subscribeId = entity.getCustomerId() + "";
                if (subscribeId.equals(LoginConfig.getInstance().getId())) {
                    followTv.setVisibility(View.GONE);
                    return;
                } else {
                    followTv.setVisibility(View.VISIBLE);
                }
                followTv.setVisibility(View.VISIBLE);
                boolean isSubscribed = entity.isSubscribe();

                if (isSubscribed){
                    followTv.setText("取消关注");
                } else {
                    followTv.setText("关注");
                }
                followTv.setTag(isSubscribed);
                followTv.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        boolean subscribe = (boolean) view.getTag();
                        LogZ.i(entity.toString());
                        LogZ.i("subscribe = " + subscribe);
                        if (subscribe) {
                            cancelFollow(subscribeId, position);
                        } else {
                            addFollow(subscribeId, position);
                        }
                    }
                });
            } else {
                followTv.setVisibility(View.GONE);
            }
        }

        private void addFollow(final String subscribeId, final int position){
            SubscribeManager.getInstance().add(LoginConfig.getInstance().getId(), subscribeId, new ResponseListener<SubscribeEntity>() {
                @Override
                public void success(SubscribeEntity object) {
                    if (object.isSuccess()) {
                        if (position < getDataList().size()) {
                            getDataList().get(position).setSubscribe(true);
                            notifyDataSetChanged();
                            showToast(object.getObject());
                        }
                    } else {
                        showToast(object.getMessage());
                    }
                }

                @Override
                public void error(String error) {
                    LogZ.e(error);
                }
            });
        }

        private void cancelFollow(final String subscribeId, final int position){
            SubscribeManager.getInstance().cancel(LoginConfig.getInstance().getId(), subscribeId, new ResponseListener<SubscribeEntity>() {
                @Override
                public void success(SubscribeEntity object) {
                    if (object.isSuccess()) {
                        if (position < getDataList().size()) {
                            getDataList().get(position).setSubscribe(false);
                            notifyDataSetChanged();
                            showToast(object.getObject());
                        }
                    } else {
                        showToast(object.getMessage());
                    }
                }

                @Override
                public void error(String error) {
                    LogZ.e(error);
                }
            });
        }

        private void showToast(String msg) {
            Toast.makeText(getContext(), msg, Toast.LENGTH_SHORT).show();
        }


        class ViewHolder extends RecyclerView.ViewHolder {

            private ImageView avatarIm;
            private TextView avatarNameTv;
            private TextView amountTv;
            private TextView buyPriceTv;
            private TextView buyPriceNumTv;
            private TextView buyNameTv;
            private TextView buySelectTv;
            private TextView buySelectNumTv;
            private TextView followTv;

            public ViewHolder(View view) {
                super(view);
                avatarIm = (CircleImageView) view.findViewById(R.id.main_item_master_avatar_im);
                avatarNameTv = (TextView) view.findViewById(R.id.main_item_master_avatar_name_tv);
                amountTv = (TextView) view.findViewById(R.id.main_item_master_amount_tv);
                buyPriceTv = (TextView) view.findViewById(R.id.main_item_master_buy_price_tv);
                buyPriceNumTv = (TextView) view.findViewById(R.id.main_item_master_buy_price_num_tv);
                buyNameTv = (TextView) view.findViewById(R.id.main_item_master_buy_name_tv);
                buySelectTv = (TextView) view.findViewById(R.id.main_item_master_buy_select_tv);
                buySelectNumTv = (TextView) view.findViewById(R.id.main_item_master_buy_select_num_tv);
                followTv = (TextView) view.findViewById(R.id.main_item_master_follow_tv);
            }


        }
    }

}
