package fxtrader.com.app.mine;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.Map;

import fxtrader.com.app.R;
import fxtrader.com.app.adapter.ListBaseAdapter;
import fxtrader.com.app.base.BaseActivity;
import fxtrader.com.app.config.LoginConfig;
import fxtrader.com.app.constant.IntentItem;
import fxtrader.com.app.entity.SubscribeListEntity;
import fxtrader.com.app.entity.UserSubscribeEntity;
import fxtrader.com.app.http.ParamsUtil;
import fxtrader.com.app.http.RetrofitUtils;
import fxtrader.com.app.http.api.CommunityApi;
import fxtrader.com.app.lrececlerview.interfaces.OnItemClickListener;
import fxtrader.com.app.lrececlerview.recyclerview.LRecyclerView;
import fxtrader.com.app.lrececlerview.recyclerview.LRecyclerViewAdapter;
import fxtrader.com.app.tools.LogZ;
import fxtrader.com.app.view.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by zhangyuzhu on 2016/12/28.
 */
public class MyFollowActivity extends BaseActivity{

    private LRecyclerView mRecyclerView;

    private TextView mPersonNumTv;

    private DataAdapter mAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentLayout(R.layout.activity_follow_list);
        initViews();
        requestData();
    }

    private void initViews(){
        mPersonNumTv = (TextView) findViewById(R.id.follow_person_num_tv);
        mRecyclerView = (LRecyclerView) findViewById(R.id.follow_list_rec);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mAdapter = new DataAdapter(this);
        LRecyclerViewAdapter mHeaderAndFooterRecyclerViewAdapter = new LRecyclerViewAdapter(this, mAdapter);
        mRecyclerView.setAdapter(mHeaderAndFooterRecyclerViewAdapter);
        mHeaderAndFooterRecyclerViewAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Intent intent = new Intent(MyFollowActivity.this, PositionsFollowedActivity.class);
                intent.putExtra(IntentItem.CUSTOMER_ID, mAdapter.getDataList().get(position).getCustomerId() + "");
                startActivity(intent);
            }

            @Override
            public void onItemLongClick(View view, int position) {

            }
        });
    }

    private void requestData(){
        showProgressDialog();
        CommunityApi communityApi = RetrofitUtils.createApi(CommunityApi.class);
        String token = ParamsUtil.getToken();
        Call<SubscribeListEntity> request = communityApi.subscribes(token, getParams());
        request.enqueue(new Callback<SubscribeListEntity>() {
            @Override
            public void onResponse(Call<SubscribeListEntity> call, Response<SubscribeListEntity> response) {
                dismissProgressDialog();
                SubscribeListEntity entity = response.body();
                if (entity.isSuccess()) {
                    mPersonNumTv.setText(entity.getObject().size() + "");
                    mAdapter.setDataList(entity.getObject());
                } else {
                    showToastShort(entity.getMessage());
                }
            }

            @Override
            public void onFailure(Call<SubscribeListEntity> call, Throwable t) {
                dismissProgressDialog();
                LogZ.e(t.toString());
            }
        });
    }

    private Map<String, String> getParams(){
        final Map<String, String> params = ParamsUtil.getCommonParams();
        params.put("method", "gdiex.community.getSubscripts");
        params.put("customerId", LoginConfig.getInstance().getId());
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
        }

        class ViewHolder extends RecyclerView.ViewHolder {
            ImageView avatarIm;
            TextView nameTv;
            TextView followTv;

            public ViewHolder(View view) {
                super(view);
                avatarIm = (CircleImageView) view.findViewById(R.id.item_follow_avatar_im);
                nameTv = (TextView) view.findViewById(R.id.item_follow_name_tv);
                followTv = (TextView) view.findViewById(R.id.item_follow_tv);
            }
        }
    }
}
