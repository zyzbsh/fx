package fxtrader.com.app.mine;

import android.content.Context;
import android.content.Intent;
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

import java.util.Map;

import fxtrader.com.app.R;
import fxtrader.com.app.adapter.ListBaseAdapter;
import fxtrader.com.app.base.BaseActivity;
import fxtrader.com.app.constant.IntentItem;
import fxtrader.com.app.entity.FollowOrderCountEntity;
import fxtrader.com.app.entity.SubscribedPositionListEntity;
import fxtrader.com.app.entity.UserSubscribeEntity;
import fxtrader.com.app.http.ParamsUtil;
import fxtrader.com.app.http.RetrofitUtils;
import fxtrader.com.app.http.api.CommunityApi;
import fxtrader.com.app.lrececlerview.recyclerview.LRecyclerView;
import fxtrader.com.app.lrececlerview.recyclerview.LRecyclerViewAdapter;
import fxtrader.com.app.tools.ContractUtil;
import fxtrader.com.app.tools.DateTools;
import fxtrader.com.app.view.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by pc on 2016/12/28.
 */
public class PositionsFollowedActivity extends BaseActivity{

    private String mCustomerId;

    private String mCurrentPrice = "";

    private LRecyclerView mRecyclerView;

    private TextView mPersonNumTv;

    private DataAdapter mAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentLayout(R.layout.activity_followed_position_list);
        mCustomerId = getIntent().getStringExtra(IntentItem.CUSTOMER_ID);
        initViews();
        requestFollowOrderCount();
        requestFollowedPosition();
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

    class DataAdapter extends ListBaseAdapter<UserSubscribeEntity> {
        private LayoutInflater mLayoutInflater;

        private Context mContext;

        public DataAdapter(Context context) {
            mLayoutInflater = LayoutInflater.from(context);
            mContext = context;
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new ViewHolder(mLayoutInflater.inflate(R.layout.item_follow, parent, false));
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            UserSubscribeEntity entity = mDataList.get(position);
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
            if (entity.getDealDirection() == 1) {
                viewHolder.dealDirectionTv.setText("买涨");
            } else {
                viewHolder.dealDirectionTv.setText("买跌");
            }

            viewHolder.buildTimeTv.setText(DateTools.changeToDate3(entity.getBuyingDate()));
            viewHolder.buildPriceTv.setText(ContractUtil.getDouble(entity.getBuyingRate(), 1) + "");
            viewHolder.currentPriceTv.setText(mCurrentPrice);
            viewHolder.handChargeTv.setText(entity.getHandingChargeAmount() + "");
            viewHolder.profitAndLossTv.setText(entity.getProfitAndLoss() + "");
            if (entity.getProfit() < 10E-6) {
                viewHolder.stopProfitTv.setText("不设");
            } else {
                viewHolder.stopProfitTv.setText(entity.getProfit() * 100 + "%");
            }

            if (Math.abs(entity.getLoss()) == 1) {
                viewHolder.stopLossTv.setText("不设");
            } else {
                viewHolder.stopLossTv.setText(Math.abs(entity.getLoss()) * 100 + "%");
            }

            viewHolder.myButTv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //参考下单
                }
            });

        }

        class ViewHolder extends RecyclerView.ViewHolder {
            ImageView avatarIm;
            TextView nameTv;
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
