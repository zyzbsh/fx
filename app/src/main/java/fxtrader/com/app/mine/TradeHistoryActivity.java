package fxtrader.com.app.mine;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;
import java.util.Map;

import fxtrader.com.app.R;
import fxtrader.com.app.adapter.ListBaseAdapter;
import fxtrader.com.app.base.BaseActivity;
import fxtrader.com.app.entity.ContractInfoEntity;
import fxtrader.com.app.entity.PositionInfoEntity;
import fxtrader.com.app.entity.PositionListEntity;
import fxtrader.com.app.http.ParamsUtil;
import fxtrader.com.app.http.RetrofitUtils;
import fxtrader.com.app.http.api.ContractApi;
import fxtrader.com.app.lrececlerview.recyclerview.LRecyclerView;
import fxtrader.com.app.lrececlerview.recyclerview.LRecyclerViewAdapter;
import fxtrader.com.app.tools.ContractUtil;
import fxtrader.com.app.tools.DateTools;
import fxtrader.com.app.tools.LogZ;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * 交易历史
 * Created by zhangyuzhu on 2016/11/22.
 */
public class TradeHistoryActivity extends BaseActivity {

    private LRecyclerView mRecyclerView;

    private TextView mTotalBreakEvenTv;

    private TextView mTotalTv;

    private TextView mTotalDealCountTv;

    private DataAdapter mAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentLayout(R.layout.activity_trade_history);
        initViews();
        setTitleContent(R.string.trade_history);
        getPositionHistoryList();
    }

    private void initViews() {

        mTotalDealCountTv = (TextView) findViewById(R.id.trade_history_sum_deal_count_tv);
        mTotalTv = (TextView) findViewById(R.id.trade_history_sum_tv);
        mTotalBreakEvenTv = (TextView) findViewById(R.id.trade_history_break_even_num_tv);

        mRecyclerView = (LRecyclerView) findViewById(R.id.trade_history_rec);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setPullRefreshEnabled(false);

        mAdapter = new DataAdapter(this);
        LRecyclerViewAdapter mHeaderAndFooterRecyclerViewAdapter = new LRecyclerViewAdapter(this, mAdapter);
        mRecyclerView.setAdapter(mHeaderAndFooterRecyclerViewAdapter);
    }

    class DataAdapter extends ListBaseAdapter<PositionInfoEntity> {

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
            PositionInfoEntity info = mDataList.get(position);

            ViewHolder viewHolder = (ViewHolder) holder;
            double profit = info.getProfitAndLoss();
            if (profit > 0) {
                viewHolder.breakEvenTv.setText("+" + profit);
                viewHolder.breakEvenTv.setTextColor(Color.parseColor("#e83743"));
            } else {
                if (profit == 0) {
                    viewHolder.breakEvenTv.setText(profit + "");
                } else {
                    viewHolder.breakEvenTv.setText("-" + profit);
                }
                viewHolder.breakEvenTv.setTextColor(Color.parseColor("#09cd29"));
            }
            String type = String.valueOf(info.getDealCount()) + " 手";
            String dealDirection;
            if ("UP".equals(info.getDealDirection())) {
                dealDirection = "买涨";
            } else {
                dealDirection = "买跌";
            }
            viewHolder.priceTv.setText(type + dealDirection);
            ContractInfoEntity contractInfoEntity = ContractUtil.getContractInfoMap().get(info.getContractCode());
            if (contractInfoEntity != null) {
                viewHolder.nameTv.setText("(" + contractInfoEntity.getName() + ")");
            }
            viewHolder.timeTv.setText(DateTools.changeToDate2(info.getSellingDate()));
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

    private List<PositionInfoEntity> mPositionInfoList;
    private void getPositionHistoryList() {
        showProgressDialog();
        ContractApi dataApi = RetrofitUtils.createApi(ContractApi.class);
        String token = ParamsUtil.getToken();
        Call<PositionListEntity> respon = dataApi.positionList(token, getPositionListParams("true"));
        respon.enqueue(new Callback<PositionListEntity>() {
            @Override
            public void onResponse(Call<PositionListEntity> call, Response<PositionListEntity> response) {
                dismissProgressDialog();
                PositionListEntity entity = response.body();
                if (entity != null && entity.getObject() != null) {
                    mPositionInfoList = entity.getObject().getContent();

                    if (mPositionInfoList != null && !mPositionInfoList.isEmpty()) {
                        double sum = 0;
                        int sumDealCount = 0;
                        for (PositionInfoEntity position : mPositionInfoList) {
                            sum = sum + position.getProfitAndLoss();
                            sumDealCount = sumDealCount + position.getDealCount();
                        }
                        mAdapter.setDataList(mPositionInfoList);
                        if (sum > 0) {
                            mTotalBreakEvenTv.setText("+" + sum);
                            mTotalBreakEvenTv.setTextColor(Color.parseColor("#e83743"));
                        } else {
                            if (sum == 0) {
                                mTotalBreakEvenTv.setText(sum + "");
                            } else {
                                mTotalBreakEvenTv.setText("-" + sum);
                            }
                            mTotalBreakEvenTv.setTextColor(Color.parseColor("#09cd29"));
                        }
                        mTotalTv.setText(getString(R.string.position_list_num,mPositionInfoList.size()));
                        mTotalDealCountTv.setText(getString(R.string.sum_deal_count, sumDealCount));
                    } else {
                        mTotalTv.setText(getString(R.string.position_list_num, 0));
                        mTotalDealCountTv.setText(getString(R.string.sum_deal_count, 0));
                    }
                }
            }

            @Override
            public void onFailure(Call<PositionListEntity> call, Throwable t) {
                LogZ.i(t.toString());
                dismissProgressDialog();
            }
        });
    }

    private Map<String, String> getPositionListParams(String history) {
        final Map<String, String> params = ParamsUtil.getCommonParams();
        params.put("method", "gdiex.storage.list");
        params.put("sale", history);
        params.put("page", "0");
        params.put("size", String.valueOf(Integer.MAX_VALUE));
        params.put("sort", "buyingDate,DESC");
        params.put("sign", ParamsUtil.sign(params));
        return params;
    }

    private void getPositionList() {
        ContractApi dataApi = RetrofitUtils.createApi(ContractApi.class);
        String token = ParamsUtil.getToken();
        Call<PositionListEntity> respon = dataApi.positionList(token, getPositionListParams("true"));
        respon.enqueue(new Callback<PositionListEntity>() {
            @Override
            public void onResponse(Call<PositionListEntity> call, Response<PositionListEntity> response) {
                PositionListEntity entity = response.body();
                if (entity != null && entity.getObject() != null) {
                    List<PositionInfoEntity> list = entity.getObject().getContent();
                }
            }

            @Override
            public void onFailure(Call<PositionListEntity> call, Throwable t) {
                LogZ.i(t.toString());
            }
        });
    }

}
