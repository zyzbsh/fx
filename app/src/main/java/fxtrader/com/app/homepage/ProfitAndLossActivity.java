package fxtrader.com.app.homepage;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import fxtrader.com.app.R;
import fxtrader.com.app.base.BaseActivity;
import fxtrader.com.app.constant.IntentItem;
import fxtrader.com.app.entity.CommonResponse;
import fxtrader.com.app.entity.PositionInfoEntity;
import fxtrader.com.app.http.ParamsUtil;
import fxtrader.com.app.http.RetrofitUtils;
import fxtrader.com.app.http.api.ContractApi;
import fxtrader.com.app.tools.LogZ;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by pc on 2016/12/22.
 */
public class ProfitAndLossActivity extends BaseActivity {
    private RecyclerView mStopProfitRec;

    private RecyclerView mStopLossRec;

    private PositionInfoEntity mPositionInfoEntity;

    private double mStopProfit;

    private double mStopLoss;

    private State mStopProfitState = new State();

    private State mStopLossState = new State();

    private int mProfitPosition;

    private int mLossPosition;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profit_and_loss);
        mPositionInfoEntity = (PositionInfoEntity) getIntent().getSerializableExtra(IntentItem.POSITION_INFO);
        mStopProfit = mPositionInfoEntity.getProfit();
        mStopLoss = mPositionInfoEntity.getLoss();
        if (mStopLoss == -1) {
            mStopLoss = 0;
        }
        mProfitPosition = (int) Math.abs(mStopProfit * 10);
        mLossPosition = (int) Math.abs(mStopLoss * 10);
        setCancelTv();
        setStopProfitView();
        setStopLossView();
    }

    private void setCancelTv() {
        findViewById(R.id.dialog_build_cancel_tv).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        findViewById(R.id.dialog_build_sure_tv).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int profitPercent = (int) Math.abs(mPositionInfoEntity.getProfit() * 10);
                double loss = mPositionInfoEntity.getLoss();
                if (loss == -1) {
                    loss = 0;
                }
                int lossPercent = (int) Math.abs(loss * 10);
                if (mStopProfitState == null || mStopLossState == null) {
                    return;
                }
                if (mStopProfitState.stopPercent == profitPercent && mStopLossState.stopPercent == lossPercent) {
                    return;
                }
                setProfitAndLoss();
            }
        });
    }

    private void setProfitAndLoss() {
        showProgressDialog();
        ContractApi api = RetrofitUtils.createApi(ContractApi.class);
        final Call<CommonResponse> respon = api.setProfitAndLoss(ParamsUtil.getToken(), getSetProfitAndLossParams());
        respon.enqueue(new Callback<CommonResponse>() {
            @Override
            public void onResponse(Call<CommonResponse> call, Response<CommonResponse> response) {
                CommonResponse common = response.body();
                if (common.isSuccess()) {
                    setResult(RESULT_OK);
                    finish();
                }
                showToastLong(common.getMessage());
                dismissProgressDialog();
            }

            @Override
            public void onFailure(Call<CommonResponse> call, Throwable t) {
                showToastLong("设置止盈止损失败");
                dismissProgressDialog();
            }
        });
    }

    private Map<String, String> getSetProfitAndLossParams() {
        final Map<String, String> params = ParamsUtil.getCommonParams();
        params.put("method", "gdiex.storage.updatePL");
        params.put("storageId", mPositionInfoEntity.getId());
        params.put("profit", "0." + mStopProfitState.stopPercent);
        int stopLossPercent = mStopLossState.stopPercent;
        String stop = "";
        if (stopLossPercent == 0) {
            stop = "-1";
        } else {
            stop = "-0." + stopLossPercent;
        }
        params.put("loss", stop);
        params.put("sign", ParamsUtil.sign(params));
        return params;
    }

    private void setStopProfitView() {
        mStopProfitRec = (RecyclerView) findViewById(R.id.dialog_build_stop_profit_rec);
        List<State> states = getData();
        mStopProfitState = states.get(mProfitPosition);
        final CustomAdapter adapter = new CustomAdapter(this, states, mProfitPosition);
        GridLayoutManager manager = new GridLayoutManager(this, 5);
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
        List<State> states = getData();
        mStopLossState = states.get(mLossPosition);
        final CustomAdapter adapter = new CustomAdapter(this, states, mLossPosition);
        GridLayoutManager manager = new GridLayoutManager(this, 5);
        mStopLossRec.setLayoutManager(manager);
        mStopLossRec.setAdapter(adapter);
        adapter.setOnItemClickListener(new CustomAdapter.OnRecyclerViewItemClickListener() {
            @Override
            public void onItemClick(View view, State data, int position) {
                mStopLossState = data;
            }
        });
    }


    private List<State> getData() {
        List<State> data = new ArrayList<>();
        State first = new State();
        first.show = "不设";
        data.add(first);
        for (int i = 1; i < 10; i++) {
            State state = new State();
            state.show = i * 10 + "%";
            state.stopPercent = i;
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

        public CustomAdapter(Context context, List<State> data, int position) {
            this.context = context;
            this.data = data;
            this.layoutPosition = position;
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
            //更改状态
            if (position == layoutPosition) {
                holder.textView.setBackgroundResource(R.drawable.shape_item_build_percent);
                holder.textView.setTextColor(Color.parseColor("#ffffff"));
            } else {
                holder.textView.setBackgroundColor(Color.parseColor("#ffffff"));
                holder.textView.setTextColor(Color.parseColor("#666666"));
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
    }
}
