package fxtrader.com.app.view;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import fxtrader.com.app.R;

/**
 * 建仓
 * Created by pc on 2016/11/19.
 */
public class BuildPositionDialog extends Dialog {

    private TextView mProductTypeTv;

    private TextView mBuyingPriceTv;

    private TextView mBuyingNumTv;

    private RecyclerView mStopProfitRec;

    private RecyclerView mStopLossRec;

    public interface OkListener {
        public void ok();
    }


    public BuildPositionDialog(Context context) {
        super(context, R.style.BuyDialogTheme);
        this.setCanceledOnTouchOutside(false);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_build_position);
        initView();
        setCancelTv();
        setStopProfitView();
        setStopLossView();
    }

    public void setOkListener(final OkListener listener) {
        findViewById(R.id.dialog_build_sure_tv).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
                listener.ok();
            }
        });
    }


    private void initView() {
        mProductTypeTv = (TextView) findViewById(R.id.dialog_build_product_type_tv);
        mBuyingPriceTv = (TextView) findViewById(R.id.dialog_build_buying_price_tv);
        mBuyingNumTv = (TextView) findViewById(R.id.dialog_build_buying_num_tv);
    }


    private void setCancelTv() {
        findViewById(R.id.dialog_build_cancel_tv).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });
    }

    private void setStopProfitView() {
        mStopProfitRec = (RecyclerView) findViewById(R.id.dialog_build_stop_profit_rec);
        final CustomAdapter adapter = new CustomAdapter(getContext(), getData());
        GridLayoutManager manager = new GridLayoutManager(getContext(), 5);
        mStopProfitRec.setLayoutManager(manager);
        mStopProfitRec.setAdapter(adapter);
        adapter.setOnItemClickListener(new CustomAdapter.OnRecyclerViewItemClickListener() {
            @Override
            public void onItemClick(View view, String data, int position) {
//data为内容，position为点击的位置              ToastUtil.getShortToastByString(MainActivity.this,"data: "+data+",position: "+position);
            }
        });

    }

    private void setStopLossView() {
        mStopLossRec = (RecyclerView) findViewById(R.id.dialog_build_stop_loss_rec);
        final CustomAdapter adapter = new CustomAdapter(getContext(), getData());
        GridLayoutManager manager = new GridLayoutManager(getContext(), 5);
        mStopLossRec.setLayoutManager(manager);
        mStopLossRec.setAdapter(adapter);
//        mStopProfitRec.addItemDecoration(new RecyclerView.ItemDecoration() {
//            @Override
//            public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
//                outRect.left = 0;
//                outRect.right = 0;
//            }
//        });
        adapter.setOnItemClickListener(new CustomAdapter.OnRecyclerViewItemClickListener() {
            @Override
            public void onItemClick(View view, String data, int position) {
//data为内容，position为点击的位置              ToastUtil.getShortToastByString(MainActivity.this,"data: "+data+",position: "+position);
            }
        });
    }


    private int getColor(int colorRes) {
        return ContextCompat.getColor(getContext(), colorRes);
    }

    private List<String> getData() {
        List<String> data = new ArrayList<>();
        data.add("不设");
        for (int i = 1; i < 10; i++) {
            data.add(i * 10 + "%");
        }
        return data;
    }


    static class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.MyViewHolder> {
        private Context context;
        private List<String> data;
        private OnRecyclerViewItemClickListener mOnItemClickListener;
        private MyViewHolder holder;
        private int layoutPosition;

        public interface OnRecyclerViewItemClickListener {
            void onItemClick(View view, String data, int position);
        }

        public CustomAdapter(Context context, List<String> data) {
            this.context = context;
            this.data = data;
        }

        public void setOnItemClickListener(OnRecyclerViewItemClickListener listener) {
            this.mOnItemClickListener = listener;
        }

        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = View.inflate(context, R.layout.item_dialog_build_position, null);
            holder = new MyViewHolder(itemView);
            return holder;
        }

        @Override

        public void onBindViewHolder(final MyViewHolder holder, final int position) {
            holder.textView.setText(data.get(position));
            holder.itemView.setTag(data.get(position));
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //获取当前点击的位置
                    layoutPosition = holder.getLayoutPosition();
                    notifyDataSetChanged();
                    mOnItemClickListener.onItemClick(holder.itemView, (String) holder.itemView.getTag(), layoutPosition);
                }
            });

            //更改状态
            if (position == layoutPosition) {
                holder.textView.setBackgroundResource(R.drawable.shape_item_build_percent);
                holder.textView.setTextColor(Color.parseColor("#ffffff"));
            } else {
                holder.textView.setBackgroundColor(Color.parseColor("#ffffff"));
                holder.textView.setTextColor(Color.parseColor("#666666"));
            }
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
            private final TextView textView;

            public MyViewHolder(View itemView) {
                super(itemView);
                textView = (TextView) itemView.findViewById(R.id.dialog_build_item_tv);
            }
        }
    }
}
