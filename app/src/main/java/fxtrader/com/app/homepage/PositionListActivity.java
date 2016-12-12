package fxtrader.com.app.homepage;

import android.content.ClipData;
import android.content.ContentValues;
import android.content.Context;
import android.content.Entity;
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

/**
 * Created by zhangyuzhu on 2016/11/22.
 * 持仓信息
 */
public class PositionListActivity extends BaseActivity{

    private LRecyclerView mRecyclerView;

    private TextView mTotalBreakEvenTv;

    private TextView mPositionListSizeTv;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentLayout(R.layout.activity_position_list);
        initViews();
        setTitleContent(R.string.position_list);
    }

    private void initViews(){

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
            return new ViewHolder(mLayoutInflater.inflate(R.layout.item_position_list, parent, false));
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            ItemModel item = mDataList.get(position);

            ViewHolder viewHolder = (ViewHolder) holder;
            viewHolder.breakEvenTv.setText(item.breakEven);
            viewHolder.buyTypeTv.setText(item.buyType);
            viewHolder.priceTv.setText(item.price);
            viewHolder.productionTv.setText(item.production);

            viewHolder.closePositionTv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(mContext, OrderDetailActivity.class);
                    mContext.startActivity(intent);
                }
            });
        }


        class ViewHolder extends RecyclerView.ViewHolder {

            private TextView breakEvenTv;
            private TextView priceTv;
            private TextView productionTv;
            private TextView buyTypeTv;
            private TextView closePositionTv;

            public ViewHolder(View itemView) {
                super(itemView);
                breakEvenTv = (TextView) itemView.findViewById(R.id.item_position_list_break_even_tv);
                productionTv = (TextView) itemView.findViewById(R.id.item_position_list_production_tv);
                buyTypeTv = (TextView) itemView.findViewById(R.id.item_position_list_buy_type_tv);
                priceTv = (TextView) itemView.findViewById(R.id.item_position_list_price_tv);
                closePositionTv = (TextView) itemView.findViewById(R.id.item_pop_profit_list_close_position_tv);
            }
        }
    }

    class ItemModel{
        public String breakEven = "+100";
        public String production = "3000g粤油";
        public String buyType = "3手买跌";
        public String price = "买入价：3606";

    }

}
