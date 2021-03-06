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
import fxtrader.com.app.tools.LogZ;

/**
 * 建仓
 * Created by pc on 2016/11/19.
 */
public class BuildPositionDialog extends Dialog {

    private RecyclerView mStopProfitRec;

    private RecyclerView mStopLossRec;

    private double mStopProfit;

    private double mStopLoss;

    private boolean mChecked = false;

    private State mStopProfitState;

    private State mStopLossState;

    public interface OkListener {
        public void ok(int stopProfit, int stopLoss);
    }


    public BuildPositionDialog(Context context, double stopProfit, double stopLoss) {
        super(context, R.style.BuyDialogTheme);
        this.setCanceledOnTouchOutside(false);
        mStopProfit = stopProfit;
        mStopLoss = stopLoss;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_build_position);
        setCancelTv();
        setStopProfitView();
        setStopLossView();
    }

    public void setOkListener(final OkListener listener) {
        findViewById(R.id.dialog_build_sure_tv).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
                listener.ok(mStopProfitState.stopPercent, mStopLossState.stopPercent);
            }
        });
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
        final CustomAdapter adapter = new CustomAdapter(getContext(), getData(mStopProfit));
        GridLayoutManager manager = new GridLayoutManager(getContext(), 5);
        mStopProfitRec.setLayoutManager(manager);
        mStopProfitRec.setAdapter(adapter);
        adapter.setOnItemClickListener(new CustomAdapter.OnRecyclerViewItemClickListener() {
            @Override
            public void onItemClick(View view, State data, int position) {
                mStopProfitState = data;
            }
        });

    }

    private void setStopLossView() {
        mStopLossRec = (RecyclerView) findViewById(R.id.dialog_build_stop_loss_rec);
        final CustomAdapter adapter = new CustomAdapter(getContext(), getData(mStopLoss));
        GridLayoutManager manager = new GridLayoutManager(getContext(), 5);
        mStopLossRec.setLayoutManager(manager);
        mStopLossRec.setAdapter(adapter);
        adapter.setOnItemClickListener(new CustomAdapter.OnRecyclerViewItemClickListener() {
            @Override
            public void onItemClick(View view, State data, int position) {
                mStopLossState = data;
            }
        });
    }


    private int getColor(int colorRes) {
        return ContextCompat.getColor(getContext(), colorRes);
    }

    private List<State> getData(double stopPercent) {
        int percent = (int) Math.abs(stopPercent * 10);
        List<State> data = new ArrayList<>();
        State first = new State();
        first.show = "不设";
        if (percent == 0) {
            first.checked = true;
        }
        for (int i = 1; i < 10; i++) {
            State state = new State();
            state.show = i * 10 + "%";
            state.stopPercent = i;
            if (percent == i) {
                state.checked = true;
            }
            data.add(state);
        }
        LogZ.i("size = " + data.size());
        return data;
    }


    static class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.MyViewHolder> {
        private Context context;
        private List<State> data;
        private OnRecyclerViewItemClickListener mOnItemClickListener;
        private MyViewHolder holder;
        private int layoutPosition;

        public interface OnRecyclerViewItemClickListener {
            void onItemClick(View view, State data, int position);
        }

        public CustomAdapter(Context context, List<State> data) {
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
            State state = data.get(position);
            holder.textView.setText(state.show);
            holder.itemView.setTag(state);
            if (state.checked) {
                layoutPosition = position;
                state.checked =false;
            }
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //获取当前点击的位置
                    layoutPosition = holder.getLayoutPosition();
                    notifyDataSetChanged();
                    mOnItemClickListener.onItemClick(holder.itemView, (State) holder.itemView.getTag(), layoutPosition);
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

    class State {
        int stopPercent;
        String show;
        boolean checked = false;
    }
}
