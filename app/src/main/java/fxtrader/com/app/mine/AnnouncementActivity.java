package fxtrader.com.app.mine;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import fxtrader.com.app.R;
import fxtrader.com.app.adapter.ListBaseAdapter;
import fxtrader.com.app.base.BaseActivity;
import fxtrader.com.app.config.AnnouncementConfig;
import fxtrader.com.app.constant.IntentItem;
import fxtrader.com.app.entity.AnnouncementDetailEntity;
import fxtrader.com.app.entity.AnnouncementListEntity;
import fxtrader.com.app.http.ParamsUtil;
import fxtrader.com.app.http.RetrofitUtils;
import fxtrader.com.app.http.api.UserApi;
import fxtrader.com.app.lrececlerview.recyclerview.LRecyclerView;
import fxtrader.com.app.lrececlerview.recyclerview.LRecyclerViewAdapter;
import fxtrader.com.app.tools.DateTools;
import fxtrader.com.app.tools.LogZ;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by zhangyuzhu on 2016/11/22.
 * 公告
 */
public class AnnouncementActivity extends BaseActivity {

    private LRecyclerView mRecyclerView;

    private DataAdapter mAdapter;

    private int mPage = 0;

    private int mTotal = 0;

    private int mCount = 0;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentLayout(R.layout.activity_announcement);
        initViews();
        requestData();
    }

    private void initViews() {

        mRecyclerView = (LRecyclerView) findViewById(R.id.announcement_rec);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setPullRefreshEnabled(false);
        mAdapter = new DataAdapter(this);
        LRecyclerViewAdapter mHeaderAndFooterRecyclerViewAdapter = new LRecyclerViewAdapter(this, mAdapter);
        mRecyclerView.setAdapter(mHeaderAndFooterRecyclerViewAdapter);
        mRecyclerView.setLScrollListener(new LRecyclerView.LScrollListener() {
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
                    requestData();
                }
            }

            @Override
            public void onScrolled(int distanceX, int distanceY) {

            }
        });
    }

    private void requestData() {
        showProgressDialog();
        UserApi userApi = RetrofitUtils.createApi(UserApi.class);
        Call<AnnouncementListEntity> request = userApi.announcements(getParams());
        request.enqueue(new Callback<AnnouncementListEntity>() {
            @Override
            public void onResponse(Call<AnnouncementListEntity> call, Response<AnnouncementListEntity> response) {
                dismissProgressDialog();
                AnnouncementListEntity entity = response.body();
                LogZ.i("return");
                if (entity != null && entity.getObject() != null && entity.getObject().getContent() != null) {
                    LogZ.i(entity.toString());
                    mAdapter.addAll(entity.getObject().getContent());
                    mAdapter.notifyDataSetChanged();
                    if (mTotal == 0) {
                        mTotal = entity.getObject().getTotalElements();
                    }
                    mCount = mCount + entity.getObject().getContent().size();
                    mPage++;
                }
            }

            @Override
            public void onFailure(Call<AnnouncementListEntity> call, Throwable t) {
                dismissProgressDialog();
            }
        });
    }

    private Map<String, String> getParams() {
        final Map<String, String> params = ParamsUtil.getCommonParams();
        params.put("method", "gdiex.users.msgs");
        params.put("page", mPage + "");
        params.put("sign", ParamsUtil.sign(params));
        return params;
    }

    class DataAdapter extends ListBaseAdapter<AnnouncementDetailEntity> {

        private LayoutInflater mLayoutInflater;

        private Context mContext;

        public DataAdapter(Context context) {
            mLayoutInflater = LayoutInflater.from(context);
            mContext = context;
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new ViewHolder(mLayoutInflater.inflate(R.layout.item_announcement, parent, false));
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            final AnnouncementDetailEntity entity = mDataList.get(position);

            ViewHolder viewHolder = (ViewHolder) holder;
            viewHolder.nameTv.setText(entity.getTitle());
            String date = DateTools.changeToDate2(entity.getDate());
            viewHolder.timeTv.setText(getString(R.string.time_num, date));
            if (AnnouncementConfig.getInstance().hasReaded(entity.getId())) {
                viewHolder.stateTv.setText(R.string.readed);
                setDrawableLeft(viewHolder.stateTv, R.mipmap.icon_has_readed);
            } else {
                viewHolder.stateTv.setText(R.string.unread);
                setDrawableLeft(viewHolder.stateTv, R.mipmap.icon_unread);
            }

            viewHolder.seeTv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(AnnouncementActivity.this, AnnouncementDetailActivity.class);
                    intent.putExtra(IntentItem.ANNOUNCEMENT_DETAIL, entity);
                    startActivity(intent);
                }
            });
        }

        private void setDrawableLeft(TextView tv, int drawableId) {
            Drawable drawable = getResources().getDrawable(drawableId);
            drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
            tv.setCompoundDrawables(drawable, null, null, null);
        }


        class ViewHolder extends RecyclerView.ViewHolder {

            private TextView nameTv;
            private TextView timeTv;
            private TextView stateTv;
            private TextView seeTv;

            public ViewHolder(View itemView) {
                super(itemView);
                nameTv = (TextView) itemView.findViewById(R.id.item_announcement_name_tv);
                timeTv = (TextView) itemView.findViewById(R.id.item_announcement_time_tv);
                stateTv = (TextView) itemView.findViewById(R.id.item_announcement_state_tv);
                seeTv = (TextView) itemView.findViewById(R.id.item_announcement_see_tv);
            }
        }
    }
}
