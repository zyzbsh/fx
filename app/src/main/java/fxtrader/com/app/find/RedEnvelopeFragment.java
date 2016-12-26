package fxtrader.com.app.find;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import fxtrader.com.app.R;
import fxtrader.com.app.adapter.ListBaseAdapter;
import fxtrader.com.app.base.BaseFragment;
import fxtrader.com.app.config.LoginConfig;
import fxtrader.com.app.entity.RedEnvelopeListEntity;
import fxtrader.com.app.homepage.ProfitListActivity;
import fxtrader.com.app.http.HttpConstant;
import fxtrader.com.app.http.ParamsUtil;
import fxtrader.com.app.http.RetrofitUtils;
import fxtrader.com.app.http.api.UserApi;
import fxtrader.com.app.lrececlerview.recyclerview.LRecyclerView;
import fxtrader.com.app.lrececlerview.recyclerview.LRecyclerViewAdapter;
import fxtrader.com.app.tools.LogZ;
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
        mRecyclerView = (LRecyclerView) view.findViewById(R.id.find_recycler_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecyclerView.setPullRefreshEnabled(false);
        List<String> data = getTestData();
        DataAdapter adapter = new DataAdapter(getActivity());
        adapter.setDataList(data);
        LRecyclerViewAdapter mHeaderAndFooterRecyclerViewAdapter = new LRecyclerViewAdapter(getContext(), adapter);
        mRecyclerView.setAdapter(mHeaderAndFooterRecyclerViewAdapter);
        mRecyclerView.addItemDecoration(new MyDecoration(getActivity(), MyDecoration.VERTICAL_LIST));

    }

    public void addHeadListener(HeadListener listener) {
        mHeadListener = listener;
    }

    private List<String> getTestData() {
        List<String> list = new ArrayList<>();
        list.add("1");
        list.add("2");
        list.add("3");
        list.add("4");
        list.add("4");
        list.add("4");
        list.add("4");
        list.add("4");
        list.add("4");
        list.add("4");
        list.add("4");
        list.add("4");
        list.add("4");
        return list;
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

    private void requestRedEnvelopes() {
        UserApi dataApi = RetrofitUtils.createApi(UserApi.class);
        final Call<RedEnvelopeListEntity> respon = dataApi.redEnvelopeList(getParams());
        respon.enqueue(new Callback<RedEnvelopeListEntity>() {
            @Override
            public void onResponse(Call<RedEnvelopeListEntity> call, Response<RedEnvelopeListEntity> response) {
            }

            @Override
            public void onFailure(Call<RedEnvelopeListEntity> call, Throwable t) {

            }
        });
    }

    private Map<String, String> getParams() {
        final Map<String, String> params = ParamsUtil.getCommonParams();
        params.put("method", "gdiex.community.getRiseOrFall");
        int id = HttpConstant.DEFAULT_ORGAN_ID;
        if (LoginConfig.getInstance().isLogin()) {
            id = LoginConfig.getInstance().getOrganId();
        }
        params.put("organ_id", id + "");
        params.put("limit", "10");
        params.put("sign", ParamsUtil.sign(params));
        return params;
    }


    class DataAdapter extends ListBaseAdapter<String> {

        private LayoutInflater mLayoutInflater;

        private Context mContext;

        public DataAdapter(Context context) {
            mLayoutInflater = LayoutInflater.from(context);
            mContext = context;
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new ViewHolder(mLayoutInflater.inflate(R.layout.item_red_envelop_list, parent, false));
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            ViewHolder viewHolder = (ViewHolder) holder;
            viewHolder.nameTv.setText("EDITD");
            viewHolder.timeTv.setText("20:00");
            viewHolder.contentTv.setText("给高手发出1个红包");
            viewHolder.profitTv.setText("盈利   101元");
            viewHolder.stateTv.setText("跟单");
        }


        class ViewHolder extends RecyclerView.ViewHolder {

            private CircleImageView avatarIm;
            private TextView nameTv;
            private TextView timeTv;
            private TextView contentTv;
            private TextView profitTv;
            private TextView stateTv;

            public ViewHolder(View view) {
                super(view);
                avatarIm = (CircleImageView) view.findViewById(R.id.item_red_envelop_avatar_im);
                nameTv = (TextView) view.findViewById(R.id.item_red_envelop_name_tv);
                timeTv = (TextView) view.findViewById(R.id.item_red_envelop_time_tv);
                contentTv = (TextView) view.findViewById(R.id.item_red_envelop_content_tv);
                profitTv = (TextView) view.findViewById(R.id.item_red_envelop_profit_tv);
                stateTv = (TextView) view.findViewById(R.id.item_red_envelop_state_tv);
            }
        }
    }

    class ItemModel {
        public String breakEven = "+100";
        public String production = "3000g粤油";
        public String buyType = "3手买跌";
        public String price = "买入价：3606";

    }
}
