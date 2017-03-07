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

import com.bumptech.glide.Glide;

import java.io.IOException;
import java.util.Map;

import fxtrader.com.app.R;
import fxtrader.com.app.adapter.ListBaseAdapter;
import fxtrader.com.app.base.BaseFragment;
import fxtrader.com.app.config.LoginConfig;
import fxtrader.com.app.entity.ProfitEntity;
import fxtrader.com.app.entity.ProfitListEntity;
import fxtrader.com.app.entity.SubscribeEntity;
import fxtrader.com.app.entity.YesterdayRankEntity;
import fxtrader.com.app.entity.YesterdayRankResponse;
import fxtrader.com.app.homepage.ProfitListActivity;
import fxtrader.com.app.http.ParamsUtil;
import fxtrader.com.app.http.RetrofitUtils;
import fxtrader.com.app.http.api.CommunityApi;
import fxtrader.com.app.http.manager.ProfitListManager;
import fxtrader.com.app.http.manager.ResponseListener;
import fxtrader.com.app.http.manager.SubscribeManager;
import fxtrader.com.app.lrececlerview.recyclerview.LRecyclerView;
import fxtrader.com.app.lrececlerview.recyclerview.LRecyclerViewAdapter;
import fxtrader.com.app.tools.LogZ;
import fxtrader.com.app.view.CircleImageView;
import fxtrader.com.app.view.MyDecoration;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * 昨日排名
 * Created by zhangyuzhu on 2016/12/4.
 */
public class YesterdayRankingFragment extends BaseFragment implements View.OnClickListener {

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
        requestRank();
    }

    @Override
    public void onResume() {
        super.onResume();
        requestRank();
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (!hidden) {
            mIsLogin = LoginConfig.getInstance().isLogin();
            requestRank();
        }
    }

    private void requestRank(){
        CommunityApi communityApi = RetrofitUtils.createApi(CommunityApi.class);
        Call<YesterdayRankResponse> request = communityApi.getAceRank(getRankParams());
        request.enqueue(new Callback<YesterdayRankResponse>() {
            @Override
            public void onResponse(Call<YesterdayRankResponse> call, Response<YesterdayRankResponse> response) {
                YesterdayRankResponse yesterdayRankResponse = response.body();
                if (yesterdayRankResponse != null && yesterdayRankResponse.isSuccess()) {
                    if (yesterdayRankResponse.getObject() != null && !yesterdayRankResponse.getObject().isEmpty()) {
                        mEmptyTv.setVisibility(View.GONE);
                        mRecyclerView.setVisibility(View.VISIBLE);
                        mAdapter.setDataList(yesterdayRankResponse.getObject());
                    } else {
                        mRecyclerView.setVisibility(View.GONE);
                        mEmptyTv.setVisibility(View.VISIBLE);
                    }
                }
            }

            @Override
            public void onFailure(Call<YesterdayRankResponse> call, Throwable t) {

            }
        });
    }

    private Map<String, String> getRankParams(){
        final Map<String, String> params = ParamsUtil.getCommonParams();
        params.put("method", "gdiex.community.getAceRank");
        params.put("limit", "10");
        params.put("sign", ParamsUtil.sign(params));
        return params;
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

    class DataAdapter extends ListBaseAdapter<YesterdayRankEntity> {

        private LayoutInflater mLayoutInflater;

        private Context mContext;

        public DataAdapter(Context context) {
            mLayoutInflater = LayoutInflater.from(context);
            mContext = context;
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new ViewHolder(mLayoutInflater.inflate(R.layout.find_item_yesterday_rank, parent, false));
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            LogZ.i("position = " + position);
            YesterdayRankEntity entity = getDataList().get(position);
            ViewHolder viewHolder = (ViewHolder) holder;
            viewHolder.nameTv.setText(entity.getNickname());
            viewHolder.contractNameTv.setText(entity.getNickname());
            viewHolder.coinsTv.setText(entity.getProfit());
            setTagImWithPosition(viewHolder.tagIm, position);
            Glide.with(mContext)
                    .load(entity.getHeadImg())
                    .centerCrop()
//                    .placeholder(R.drawable.loading_spinner)
                    .crossFade()
                    .into(viewHolder.avatarIm);
            setFollowTv(viewHolder.followTv, entity, position);
        }

        private void setTagImWithPosition(ImageView im, int position) {
            if (position == 0) {
                im.setVisibility(View.VISIBLE);
                im.setImageResource(R.mipmap.icon_rank_first);
            } else if (position == 1) {
                im.setVisibility(View.VISIBLE);
                im.setImageResource(R.mipmap.icon_rank_second);
            } else if (position == 2) {
                im.setVisibility(View.VISIBLE);
                im.setImageResource(R.mipmap.icon_rank_third);
            } else if(position == 3) {
                im.setVisibility(View.VISIBLE);
                im.setImageResource(R.mipmap.icon_rank_forth);
            } else {
                im.setVisibility(View.INVISIBLE);
            }
        }

        private void setFollowTv(TextView followTv, final YesterdayRankEntity entity, final int position) {

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

            ImageView tagIm;
            CircleImageView avatarIm;
            TextView nameTv;
            TextView contractNameTv;
            TextView coinsTv;
            TextView followTv;

            public ViewHolder(View view) {
                super(view);
                tagIm = (ImageView) view.findViewById(R.id.find_item_yesterday_rank_tag_im);
                avatarIm = (CircleImageView) view.findViewById(R.id.find_item_yesterday_rank_avatar_im);
                nameTv = (TextView) view.findViewById(R.id.find_item_yesterday_rank_name_tv);
                contractNameTv = (TextView) view.findViewById(R.id.find_item_contract_name_tv);
                coinsTv = (TextView) view.findViewById(R.id.find_item_yesterday_coins_tv);
                followTv = (TextView) view.findViewById(R.id.find_item_yesterday_rank_follow_tv);
            }
        }
    }

}
