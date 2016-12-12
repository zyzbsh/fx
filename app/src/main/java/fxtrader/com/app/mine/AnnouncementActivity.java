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
 * Created by zhangyuzhu on 2016/11/22.
 * 公告
 */
public class AnnouncementActivity extends BaseActivity{

    private LRecyclerView mRecyclerView;

    private TextView mTotalBreakEvenTv;

    private TextView mPositionListSizeTv;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentLayout(R.layout.activity_announcement);
        initViews();
        setTitleContent(R.string.announcement);
    }

    private void initViews(){

        mRecyclerView = (LRecyclerView) findViewById(R.id.announcement_rec);
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
            return new ViewHolder(mLayoutInflater.inflate(R.layout.item_announcement, parent, false));
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            ItemModel item = mDataList.get(position);

            ViewHolder viewHolder = (ViewHolder) holder;
            viewHolder.nameTv.setText("关于黄金波动报告");
//            viewHolder.timeTv.setText(item.buyType);
            viewHolder.stateTv.setText(R.string.readed);

            viewHolder.seeTv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                   //TODO 查看
                }
            });
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

    class ItemModel{
        public String breakEven = "+100";
        public String production = "3000g粤油";
        public String buyType = "3手买跌";
        public String price = "买入价：3606";

    }

}
