package fxtrader.com.app.find;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.Map;

import fxtrader.com.app.R;
import fxtrader.com.app.adapter.ListBaseAdapter;
import fxtrader.com.app.base.BaseFragment;
import fxtrader.com.app.config.LoginConfig;
import fxtrader.com.app.entity.RedEnvelopeEntity;
import fxtrader.com.app.entity.RedEnvelopeListEntity;
import fxtrader.com.app.homepage.ProfitListActivity;
import fxtrader.com.app.http.CommunityRetrofitUtils;
import fxtrader.com.app.http.HttpConstant;
import fxtrader.com.app.http.ParamsUtil;
import fxtrader.com.app.http.RetrofitUtils;
import fxtrader.com.app.http.api.UserApi;
import fxtrader.com.app.lrececlerview.recyclerview.LRecyclerView;
import fxtrader.com.app.lrececlerview.recyclerview.LRecyclerViewAdapter;
import fxtrader.com.app.tools.DateTools;
import fxtrader.com.app.view.CircleImageView;
import fxtrader.com.app.view.MyDecoration;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * 发现红包榜
 * Created by zhangyuzhu on 2016/12/4.
 */
public class RedEnvelopeFragment extends BaseFragment implements View.OnClickListener {
    private LRecyclerView mRecyclerView;
    private TextView mEmptyTv;
    private DataAdapter mAdapter;
    private HeadListener mHeadListener;
    private boolean mIsShow = true;
    private int mDisy;

    private boolean mIsTitleHide = false;
    private boolean mIsAnim = false;
    private float lastX = 0;
    private float lastY = 0;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.view_find, container, false);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
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

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (!hidden) {
            requestData();
        }
    }

    public void addHeadListener(HeadListener listener) {
        mHeadListener = listener;
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

    private void requestData() {
        UserApi dataApi = RetrofitUtils.createApi(UserApi.class);
        final Call<RedEnvelopeListEntity> respon = dataApi.redEnvelopeList(getParams());
        respon.enqueue(new Callback<RedEnvelopeListEntity>() {
            @Override
            public void onResponse(Call<RedEnvelopeListEntity> call, Response<RedEnvelopeListEntity> response) {
                RedEnvelopeListEntity object = response.body();
                if (object.isSuccess()) {
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
            public void onFailure(Call<RedEnvelopeListEntity> call, Throwable t) {

            }
        });
    }

    private Map<String, String> getParams() {
        final Map<String, String> params = ParamsUtil.getCommonParams();
        params.put("method", "gdiex.community.getP2PRedPacket");
        int id = HttpConstant.DEFAULT_ORGAN_ID;
        if (LoginConfig.getInstance().isLogin()) {
            id = LoginConfig.getInstance().getOrganId();
        }
        params.put("organ_id", id + "");
        params.put("limit", "10");
        params.put("sign", ParamsUtil.sign(params));
        return params;
    }


    class DataAdapter extends ListBaseAdapter<RedEnvelopeEntity> {

        private LayoutInflater mLayoutInflater;

        private Context mContext;

        public DataAdapter(Context context) {
            mLayoutInflater = LayoutInflater.from(context);
            mContext = context;
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new ViewHolder(mLayoutInflater.inflate(R.layout.item_red_envelop, parent, false));
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            RedEnvelopeEntity entity = getDataList().get(position);
            ViewHolder viewHolder = (ViewHolder) holder;
            Glide.with(mContext)
                    .load(entity.getHeadImg())
                    .centerCrop()
//                    .placeholder(R.drawable.loading_spinner)
                    .crossFade()
                    .into(viewHolder.avatarIm);
            viewHolder.nameTv.setText(entity.getNickname());
            viewHolder.timeTv.setText(DateTools.changeToDate2(entity.getCreateTime()));
            viewHolder.contentTv.setText("给高手发出1个红包");
            viewHolder.profitTv.setText(String.valueOf(entity.getAllAmount()));
        }


        class ViewHolder extends RecyclerView.ViewHolder {

            private CircleImageView avatarIm;
            private TextView nameTv;
            private TextView timeTv;
            private TextView contentTv;
            private TextView profitTv;

            public ViewHolder(View view) {
                super(view);
                avatarIm = (CircleImageView) view.findViewById(R.id.item_red_envelop_avatar_im);
                nameTv = (TextView) view.findViewById(R.id.item_red_envelop_name_tv);
                timeTv = (TextView) view.findViewById(R.id.item_red_envelop_time_tv);
                contentTv = (TextView) view.findViewById(R.id.item_red_envelop_content_tv);
                profitTv = (TextView) view.findViewById(R.id.item_red_envelop_profit_num_tv);
            }
        }
    }
}
