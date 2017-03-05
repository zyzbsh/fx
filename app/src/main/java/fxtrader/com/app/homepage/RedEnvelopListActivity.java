package fxtrader.com.app.homepage;

import android.content.Context;
import android.content.Intent;
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

import fxtrader.com.app.R;
import fxtrader.com.app.adapter.ListBaseAdapter;
import fxtrader.com.app.base.BaseActivity;
import fxtrader.com.app.lrececlerview.recyclerview.LRecyclerView;
import fxtrader.com.app.lrececlerview.recyclerview.LRecyclerViewAdapter;
import fxtrader.com.app.view.CircleImageView;
import fxtrader.com.app.view.MyDecoration;

/**
 * 红包榜
 * Created by zhangyuzhu on 2016/11/22.
 */
public class RedEnvelopListActivity extends BaseActivity {

    private LRecyclerView mRecyclerView;

    private TextView mTotalBreakEvenTv;

    private TextView mPositionListSizeTv;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentLayout(R.layout.activity_red_envelop_list);
        initViews();
        setTitleContent(R.string.app_name_ch);
    }

    private void initViews() {

        mTotalBreakEvenTv = (TextView) findViewById(R.id.position_list_break_even_num_tv);
        mPositionListSizeTv = (TextView) findViewById(R.id.position_list_size_tv);

        mRecyclerView = (LRecyclerView) findViewById(R.id.position_list_rec);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setPullRefreshEnabled(false);

        DataAdapter adapter = new DataAdapter(this);
        List<ItemModel> list = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            list.add(new ItemModel());
        }
        adapter.addAll(list);

        LRecyclerViewAdapter mHeaderAndFooterRecyclerViewAdapter = new LRecyclerViewAdapter(this, adapter);
        mRecyclerView.setAdapter(mHeaderAndFooterRecyclerViewAdapter);
        mRecyclerView.addItemDecoration(new MyDecoration(this, MyDecoration.VERTICAL_LIST));

    }

    class DataAdapter extends ListBaseAdapter<ItemModel> {

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
            ItemModel item = mDataList.get(position);

            ViewHolder viewHolder = (ViewHolder) holder;
            viewHolder.nameTv.setText("EDITD");
            viewHolder.timeTv.setText("20:00");
            viewHolder.contentTv.setText("给高手发出1个红包");
            viewHolder.profitTv.setText("盈利   101金币");
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
                CircleImageView avatarIm = (CircleImageView) view.findViewById(R.id.item_red_envelop_avatar_im);
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
