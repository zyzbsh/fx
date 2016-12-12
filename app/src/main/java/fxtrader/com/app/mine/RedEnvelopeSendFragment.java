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

import fxtrader.com.app.R;
import fxtrader.com.app.adapter.ListBaseAdapter;
import fxtrader.com.app.base.BaseFragment;
import fxtrader.com.app.entity.CurrencyDetailEntity;
import fxtrader.com.app.lrececlerview.recyclerview.LRecyclerView;
import fxtrader.com.app.lrececlerview.recyclerview.LRecyclerViewAdapter;

/**
 * 发送的红包
 * Created by zhangyuzhu on 2016/12/11.
 */
public class RedEnvelopeSendFragment extends BaseFragment {

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
    }

    class MyAdapter extends ListBaseAdapter<CurrencyDetailEntity> {
        private LayoutInflater mLayoutInflater;

        public MyAdapter(Context context) {
            mLayoutInflater = LayoutInflater.from(context);
            mContext = context;
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new ViewHolder(mLayoutInflater.inflate(R.layout.item_red_envelope_send, parent, false));
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            CurrencyDetailEntity item = mDataList.get(position);
            ViewHolder viewHolder = (ViewHolder) holder;
        }


        class ViewHolder extends RecyclerView.ViewHolder {

            private TextView nameTv;
            private TextView timeTv;

            public ViewHolder(View view) {
                super(view);
                nameTv = (TextView) view.findViewById(R.id.item_red_envelope_send_name_tv);
                timeTv = (TextView) view.findViewById(R.id.item_red_envelope_send_time_tv);
            }
        }
    }

}
