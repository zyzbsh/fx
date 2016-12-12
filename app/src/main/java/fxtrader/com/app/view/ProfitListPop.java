package fxtrader.com.app.view;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import fxtrader.com.app.R;
import fxtrader.com.app.homepage.OrderDetailActivity;
import fxtrader.com.app.homepage.PositionListActivity;

/**
 * Created by pc on 2016/11/20.
 */
public class ProfitListPop extends PopupWindow {

    private Context mContext;

    public ProfitListPop(Context context) {
        super(context);
        this.setFocusable(true);
        this.setTouchable(true);
        this.setOutsideTouchable(true);
        this.setWidth(LinearLayout.LayoutParams.MATCH_PARENT);
        this.setHeight(LinearLayout.LayoutParams.WRAP_CONTENT);
        this.setBackgroundDrawable(new ColorDrawable(0)); // 一定要设置，否则点击不消失

        mContext = context;
        View view = LayoutInflater.from(context).inflate(R.layout.pop_profit_list, null, false);
        setContentView(view);
        setRecView(view);
        setShowTv(view);
    }

    public void show(View view) {
        if (view == null) {
            throw new IllegalArgumentException("view should not be null");
        }
        int[] xy = new int[2];
        view.getLocationOnScreen(xy);
        this.showAtLocation(view, Gravity.TOP, 0, view.getHeight() + xy[1]);
    }

    private void setShowTv(View view){
        TextView recordTv = (TextView) view.findViewById(R.id.pop_profit_list_record_tv);
        TextView moreTv = (TextView)view.findViewById(R.id.pop_profit_list_more_tv);
        moreTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, PositionListActivity.class);
                mContext.startActivity(intent);
            }
        });
    }


    private void setRecView(View view) {
        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.pop_profit_list_rec);
        final CustomAdapter adapter = new CustomAdapter(mContext, getData());
        LinearLayoutManager manager = new LinearLayoutManager(mContext);
        recyclerView.setLayoutManager(manager);
        recyclerView.setAdapter(adapter);
        adapter.setOnItemClickListener(new CustomAdapter.OnRecyclerViewItemClickListener() {
            @Override
            public void onItemClick(View view, TestInfo data, int position) {
                Intent intent = new Intent(mContext, OrderDetailActivity.class);
                mContext.startActivity(intent);
            }
        });
    }

    static class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.MyViewHolder> {
        private Context context;
        private List<TestInfo> data;
        private OnRecyclerViewItemClickListener mOnItemClickListener;
        private MyViewHolder holder;
        private int layoutPosition;

        public interface OnRecyclerViewItemClickListener {
            void onItemClick(View view, TestInfo data, int position);
        }

        public CustomAdapter(Context context, List<TestInfo> data) {
            this.context = context;
            this.data = data;
        }

        public void setOnItemClickListener(OnRecyclerViewItemClickListener listener) {
            this.mOnItemClickListener = listener;
        }

        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(context).inflate(R.layout.item_pop_profit_list, parent, false);
            holder = new MyViewHolder(itemView);
            return holder;
        }

        @Override

        public void onBindViewHolder(final MyViewHolder holder, final int position) {
            TestInfo info = data.get(position);
            holder.profitTv.setText(info.getProfit());
            String price = this.context.getString(R.string.buying_price_num, info.getPrice());
            holder.priceTv.setText(price);
            holder.numTv.setText(info.getNum());
            holder.timeTv.setText(info.getTime());
            holder.itemView.setTag(info);
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //获取当前点击的位置
                    layoutPosition = holder.getLayoutPosition();
                    notifyDataSetChanged();
                    mOnItemClickListener.onItemClick(holder.itemView, (TestInfo) holder.itemView.getTag(), layoutPosition);
                }
            });
            holder.closePositionTv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //TODO平仓
                }
            });
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public int getItemCount() {
            return data.size();
        }

        class MyViewHolder extends RecyclerView.ViewHolder {
            private TextView profitTv;
            private TextView priceTv;
            private TextView numTv;
            private TextView timeTv;
            private TextView closePositionTv;

            public MyViewHolder(View itemView) {
                super(itemView);
                profitTv = (TextView) itemView.findViewById(R.id.item_pop_profit_list_profit_tv);
                priceTv = (TextView) itemView.findViewById(R.id.item_pop_profit_list_price_tv);
                numTv = (TextView) itemView.findViewById(R.id.item_pop_profit_list_num_tv);
                timeTv = (TextView) itemView.findViewById(R.id.item_pop_profit_list_time_tv);
                closePositionTv = (TextView) itemView.findViewById(R.id.item_pop_profit_list_close_position_tv);
            }
        }
    }

    private List<TestInfo> getData() {
        List<TestInfo> data = new ArrayList<>();
        TestInfo info1 = new TestInfo("+100.00", "1928.0", "0.1吨", "10手买涨");
        TestInfo info2 = new TestInfo("+100.00", "1928.0", "0.1吨", "10手买涨");
        TestInfo info3 = new TestInfo("+100.00", "1928.0", "0.1吨", "10手买涨");
        TestInfo info4 = new TestInfo("+100.00", "1928.0", "0.1吨", "10手买涨");

        data.add(info1);
        data.add(info2);
        data.add(info3);
        data.add(info4);

        return data;
    }


    private class TestInfo {
        private String profit = "";
        private String price = "";
        private String num = "";
        private String time = "";

        public TestInfo(String profit, String price, String num, String time) {
            this.profit = profit;
            this.price = price;
            this.num = num;
            this.time = time;
        }

        public String getProfit() {
            return profit;
        }

        public void setProfit(String profit) {
            this.profit = profit;
        }

        public String getPrice() {
            return price;
        }

        public void setPrice(String price) {
            this.price = price;
        }

        public String getNum() {
            return num;
        }

        public void setNum(String num) {
            this.num = num;
        }

        public String getTime() {
            return time;
        }

        public void setTime(String time) {
            this.time = time;
        }
    }


}
