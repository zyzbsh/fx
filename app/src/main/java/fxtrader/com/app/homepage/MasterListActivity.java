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
import android.widget.ImageView;
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
 * 高手热点
 * Created by zhangyuzhu on 2016/11/22.
 */
public class MasterListActivity extends BaseActivity {

    private LRecyclerView mRecyclerView;

    private TextView mTotalBreakEvenTv;

    private TextView mPositionListSizeTv;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentLayout(R.layout.activity_master_list);
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
            return new ViewHolder(mLayoutInflater.inflate(R.layout.main_item_master, parent, false));
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            ItemModel item = mDataList.get(position);

            ViewHolder viewHolder = (ViewHolder) holder;
            viewHolder.avatarNameTv.setText("NICHOLAS");
            viewHolder.amountTv.setText("2306.00");
            viewHolder.buyPriceTv.setText("买入价");
            viewHolder.buyPriceNumTv.setText("1930.2");
            viewHolder.buyNameTv.setText("0.2t粤东有");
            viewHolder.buySelectTv.setText("买跌");
            viewHolder.buySelectNumTv.setText("5手");
            viewHolder.followTv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //TODO
                }
            });
        }


        class ViewHolder extends RecyclerView.ViewHolder {

            private ImageView avatarIm;
            private TextView avatarNameTv;
            private TextView amountTv;
            private TextView buyPriceTv;
            private TextView buyPriceNumTv;
            private TextView buyNameTv;
            private TextView buySelectTv;
            private TextView buySelectNumTv;
            private TextView followTv;

            public ViewHolder(View view) {
                super(view);
                avatarIm = (CircleImageView) view.findViewById(R.id.main_item_master_avatar_im);
                avatarNameTv = (TextView) view.findViewById(R.id.main_item_master_avatar_name_tv);
                amountTv = (TextView) view.findViewById(R.id.main_item_master_amount_tv);
                buyPriceTv = (TextView) view.findViewById(R.id.main_item_master_buy_price_tv);
                buyPriceNumTv = (TextView) view.findViewById(R.id.main_item_master_buy_price_num_tv);
                buyNameTv = (TextView) view.findViewById(R.id.main_item_master_buy_name_tv);
                buySelectTv = (TextView) view.findViewById(R.id.main_item_master_buy_select_tv);
                buySelectNumTv = (TextView) view.findViewById(R.id.main_item_master_buy_select_num_tv);
                followTv = (TextView) view.findViewById(R.id.main_item_master_follow_tv);
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
