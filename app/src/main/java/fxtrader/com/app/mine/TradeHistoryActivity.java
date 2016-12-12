package fxtrader.com.app.mine;

import android.content.Context;
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

/**
 * 交易历史
 * Created by zhangyuzhu on 2016/11/22.
 */
public class TradeHistoryActivity extends BaseActivity{

    private LRecyclerView mRecyclerView;

    private TextView mTotalBreakEvenTv;

    private TextView mPositionListSizeTv;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentLayout(R.layout.activity_trade_history);
        initViews();
        setTitleContent(R.string.trade_history);
    }

    private void initViews(){

        mTotalBreakEvenTv = (TextView) findViewById(R.id.trade_history_break_even_num_tv);
        mPositionListSizeTv = (TextView) findViewById(R.id.trade_history_size_tv);

        mRecyclerView = (LRecyclerView) findViewById(R.id.trade_history_rec);
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
            return new ViewHolder(mLayoutInflater.inflate(R.layout.item_trade_history, parent, false));
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            ItemModel item = mDataList.get(position);

            ViewHolder viewHolder = (ViewHolder) holder;
            viewHolder.breakEvenTv.setText(item.breakEven);
            viewHolder.timeTv.setText(item.buyType);
            viewHolder.priceTv.setText(item.price);
            viewHolder.nameTv.setText(item.production);
        }


        class ViewHolder extends RecyclerView.ViewHolder {

            private TextView breakEvenTv;
            private TextView priceTv;
            private TextView nameTv;
            private TextView timeTv;

            public ViewHolder(View itemView) {
                super(itemView);
                breakEvenTv = (TextView) itemView.findViewById(R.id.item_trade_history_break_even_tv);
                nameTv = (TextView) itemView.findViewById(R.id.item_trade_history_name_tv);
                timeTv = (TextView) itemView.findViewById(R.id.item_trade_history_time_tv);
                priceTv = (TextView) itemView.findViewById(R.id.item_trade_history_price_tv);
            }
        }
    }

    class ItemModel{
        public String breakEven = "-14";
        public String production = "3000g粤油";
        public String buyType = "2015-12-13 15:30:45";
        public String price = "3手买跌";

    }

}
