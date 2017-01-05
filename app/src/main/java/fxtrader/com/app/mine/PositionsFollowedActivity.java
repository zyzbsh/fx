package fxtrader.com.app.mine;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.List;
import java.util.Map;

import fxtrader.com.app.AppApplication;
import fxtrader.com.app.R;
import fxtrader.com.app.adapter.ListBaseAdapter;
import fxtrader.com.app.base.BaseActivity;
import fxtrader.com.app.constant.IntentItem;
import fxtrader.com.app.entity.ContractEntity;
import fxtrader.com.app.entity.ContractInfoEntity;
import fxtrader.com.app.entity.FollowOrderCountEntity;
import fxtrader.com.app.entity.MarketEntity;
import fxtrader.com.app.entity.PositionInfoEntity;
import fxtrader.com.app.entity.PriceEntity;
import fxtrader.com.app.entity.SubscribedPositionListEntity;
import fxtrader.com.app.entity.UserSubscribeEntity;
import fxtrader.com.app.homepage.BuildPositionActivity;
import fxtrader.com.app.homepage.HFBuildPositionActivity;
import fxtrader.com.app.http.HttpConstant;
import fxtrader.com.app.http.ParamsUtil;
import fxtrader.com.app.http.RetrofitUtils;
import fxtrader.com.app.http.api.CommunityApi;
import fxtrader.com.app.lrececlerview.recyclerview.LRecyclerView;
import fxtrader.com.app.lrececlerview.recyclerview.LRecyclerViewAdapter;
import fxtrader.com.app.tools.ContractUtil;
import fxtrader.com.app.tools.DateTools;
import fxtrader.com.app.tools.LogZ;
import fxtrader.com.app.view.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by pc on 2016/12/28.
 */
public class PositionsFollowedActivity extends BaseActivity{

    private String mCustomerId;

    private LRecyclerView mRecyclerView;

    private TextView mPersonNumTv;

    private DataAdapter mAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentLayout(R.layout.activity_followed_position_list);
        mCustomerId = getIntent().getStringExtra(IntentItem.CUSTOMER_ID);
        initViews();
        registerPriceReceiver();
        requestFollowOrderCount();
        requestFollowedPosition();
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mPriceReceiver != null) {
            try {
                unregisterReceiver(mPriceReceiver);
            }catch (Exception e) {
                LogZ.e(e.getMessage());
            }
        }
    }

    private void initViews() {
        mRecyclerView = (LRecyclerView) findViewById(R.id.followed_position_rec);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mAdapter = new DataAdapter(this);
        LRecyclerViewAdapter mHeaderAndFooterRecyclerViewAdapter = new LRecyclerViewAdapter(this, mAdapter);
        mRecyclerView.setAdapter(mHeaderAndFooterRecyclerViewAdapter);
    }

    private void requestFollowedPosition(){
        CommunityApi communityApi = RetrofitUtils.createApi(CommunityApi.class);
        String token = ParamsUtil.getToken();
        Call<SubscribedPositionListEntity> request = communityApi.subscribedPosition(token, getFollowedPositionParams());
        request.enqueue(new Callback<SubscribedPositionListEntity>() {
            @Override
            public void onResponse(Call<SubscribedPositionListEntity> call, Response<SubscribedPositionListEntity> response) {
                SubscribedPositionListEntity entity = response.body();
                if (entity.isSuccess()) {
                    mAdapter.setDataList(entity.getObject());
                } else {

                }
            }

            @Override
            public void onFailure(Call<SubscribedPositionListEntity> call, Throwable t) {

            }
        });

    }

    private Map<String, String> getFollowedPositionParams(){
        final Map<String, String> params = ParamsUtil.getCommonParams();
        params.put("method", "gdiex.community.getSubscriptBuyInfo");
        params.put("customerId", mCustomerId);
        params.put("sign", ParamsUtil.sign(params));
        return params;
    }

    private void requestFollowOrderCount(){
        CommunityApi communityApi = RetrofitUtils.createApi(CommunityApi.class);
        String token = ParamsUtil.getToken();
        Call<FollowOrderCountEntity> request = communityApi.followOrderCount(token, getFollowedOrderCountParams());
        request.enqueue(new Callback<FollowOrderCountEntity>() {
            @Override
            public void onResponse(Call<FollowOrderCountEntity> call, Response<FollowOrderCountEntity> response) {
                FollowOrderCountEntity entity = response.body();
                if (entity.isSuccess()) {

                } else {

                }
            }

            @Override
            public void onFailure(Call<FollowOrderCountEntity> call, Throwable t) {

            }
        });
    }

    private Map<String, String> getFollowedOrderCountParams(){
        final Map<String, String> params = ParamsUtil.getCommonParams();
        params.put("method", "gdiex.community.getFollowOrder");
        params.put("customerId", mCustomerId);
        params.put("sign", ParamsUtil.sign(params));
        return params;
    }

    private BroadcastReceiver mPriceReceiver;

    private void registerPriceReceiver() {
        mPriceReceiver = new PriceReceiver();
        IntentFilter filter = new IntentFilter(IntentItem.ACTION_PRICE);
        registerReceiver(mPriceReceiver, filter);
    }


    class PriceReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            MarketEntity vo = (MarketEntity) intent.getSerializableExtra(IntentItem.PRICE);
            vo.init();
            List<UserSubscribeEntity> list = mAdapter.getDataList();
            if (list != null && !list.isEmpty()) {
                for (UserSubscribeEntity entity : list){
                    String contractCode = entity.getContractCode();
                    if (HttpConstant.PriceCode.YDOIL.equals(contractCode)) {
                        contractCode = HttpConstant.PriceCode.YDCL;
                    }
                    String data = vo.getData(contractCode);
                    PriceEntity price = new PriceEntity(data);
                    entity.setLatestPrice(price.getLatestPrice());
                }
            }

            mAdapter.notifyDataSetChanged();

        }
    }

    class DataAdapter extends ListBaseAdapter<UserSubscribeEntity> {
        private LayoutInflater mLayoutInflater;

        private Context mContext;

        public DataAdapter(Context context) {
            mLayoutInflater = LayoutInflater.from(context);
            mContext = context;
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new ViewHolder(mLayoutInflater.inflate(R.layout.item_followed_positions, parent, false));
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            final UserSubscribeEntity entity = mDataList.get(position);
            ViewHolder viewHolder = (ViewHolder) holder;
            Glide.with(mContext)
                    .load(entity.getHeadImgUrl())
                    .centerCrop()
//                    .placeholder(R.drawable.loading_spinner)
                    .crossFade()
                    .into(viewHolder.avatarIm);
            viewHolder.nameTv.setText(entity.getNickname());
            boolean isFollowed = entity.isSubscribe();
            if (isFollowed) {
                viewHolder.followTv.setText(getString(R.string.follow));
            } else {
                viewHolder.followTv.setText(getString(R.string.cancel_follow));
            }
            viewHolder.followTv.setTag(isFollowed);
            viewHolder.followTv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                }
            });

            viewHolder.contractCodeTv.setText(entity.getContractName());

            if (entity.getDealDirection() == 1) {
                viewHolder.dealDirectionTv.setText("买涨");
            } else {
                viewHolder.dealDirectionTv.setText("买跌");
            }

            String buildTime = DateTools.changeToDate3(entity.getBuyingDate());
            viewHolder.buildTimeTv.setText(getString(R.string.followed_position_time, buildTime));

            String buildPrice = ContractUtil.getDouble(entity.getBuyingRate(), 1) + "";
            viewHolder.buildPriceTv.setText(getString(R.string.followed_position_build_price, buildPrice));

            viewHolder.currentPriceTv.setText(getString(R.string.followed_position_current_price, entity.getLatestPrice()));

            viewHolder.handChargeTv.setText(getString(R.string.followed_position_hand_charge, entity.getHandingChargeAmount() + ""));

            viewHolder.profitAndLossTv.setText(entity.getProfitAndLoss() + "");
            String stopProfit;
            if (entity.getProfit() < 10E-6) {
                stopProfit = "不设";
            } else {
                stopProfit = entity.getProfit() * 100 + "%";
            }
            viewHolder.stopProfitTv.setText(getString(R.string.followed_position_stop_profit, stopProfit));

            String stopLoss;
            if (Math.abs(entity.getLoss()) == 1) {
                stopLoss = "不设";
                viewHolder.stopLossTv.setText("不设");
            } else {
                stopLoss = Math.abs(entity.getLoss()) * 100 + "%";
            }
            viewHolder.stopLossTv.setText(getString(R.string.followed_position_stop_loss, stopLoss));
            final String contractCode = entity.getContractCode();
            final boolean up;
            if (entity.getDealDirection() == 1){
                up = true;
            } else {
                up = false;
            }

            viewHolder.myButTv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent;
                    ContractInfoEntity contractInfoEntity = ContractUtil.getContractInfoMap().get(contractCode);
                    String dataType = contractInfoEntity.getDataType();
                    if (dataType.equals(HttpConstant.PriceCode.YDHF)) {
                        intent = new Intent(mContext, HFBuildPositionActivity.class);
                    } else {
                        intent = new Intent(mContext, BuildPositionActivity.class);
                    }
                    intent.putExtra(IntentItem.ORDER_FOLLOWED, entity);
                    ContractEntity contractEntity = ContractUtil.getContractMap().get(dataType);
                    intent.putExtra(IntentItem.PRICE, entity.getLatestPrice());
                    intent.putExtra(IntentItem.CONTARCT_INFO, contractEntity);
                    intent.putExtra(IntentItem.EXCEPTION, up);
                    intent.putExtra(IntentItem.DATA_TYPE, dataType);
                    startActivityForResult(intent, IntentItem.REQUEST_BUILD_POSITION);
                }
            });

        }

        class ViewHolder extends RecyclerView.ViewHolder {
            CircleImageView avatarIm;
            TextView nameTv;
            TextView contractCodeTv;
            TextView followTv;
            TextView dealDirectionTv;
            TextView buildTimeTv;
            TextView buildPriceTv;
            TextView currentPriceTv;
            TextView handChargeTv;
            TextView profitAndLossTv;
            TextView stopProfitTv;
            TextView stopLossTv;
            TextView myButTv;

            public ViewHolder(View view) {
                super(view);
                avatarIm = (CircleImageView) view.findViewById(R.id.followed_position_avatar_im);
                nameTv = (TextView) view.findViewById(R.id.followed_position_name_tv);
                contractCodeTv = (TextView) view.findViewById(R.id.followed_position_contract_code_tv);
                dealDirectionTv = (TextView) view.findViewById(R.id.followed_position_deal_direction_tv);
                followTv = (TextView) view.findViewById(R.id.followed_position_follow_tv);
                buildTimeTv = (TextView) view.findViewById(R.id.followed_position_build_time_tv);
                buildPriceTv = (TextView) view.findViewById(R.id.followed_position_build_price_tv);
                currentPriceTv = (TextView) view.findViewById(R.id.followed_position_current_price_tv);
                handChargeTv = (TextView) view.findViewById(R.id.followed_position_hand_charge_tv);
                profitAndLossTv = (TextView) view.findViewById(R.id.followed_position_profit_and_loss_tv);
                stopProfitTv = (TextView) view.findViewById(R.id.followed_position_stop_profit_tv);
                stopLossTv = (TextView) view.findViewById(R.id.followed_position_stop_loss_tv);
                myButTv = (TextView) view.findViewById(R.id.followed_position_my_buy_tv);
            }
        }
    }
}
