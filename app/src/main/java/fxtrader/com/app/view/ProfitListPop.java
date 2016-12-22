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

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import fxtrader.com.app.R;
import fxtrader.com.app.adapter.ListBaseAdapter;
import fxtrader.com.app.constant.IntentItem;
import fxtrader.com.app.entity.CommonResponse;
import fxtrader.com.app.entity.PositionInfoEntity;
import fxtrader.com.app.entity.PositionListEntity;
import fxtrader.com.app.homepage.OrderDetailActivity;
import fxtrader.com.app.homepage.PositionListActivity;
import fxtrader.com.app.http.ParamsUtil;
import fxtrader.com.app.http.RetrofitUtils;
import fxtrader.com.app.http.api.ContractApi;
import fxtrader.com.app.lrececlerview.interfaces.OnItemClickListener;
import fxtrader.com.app.lrececlerview.recyclerview.LRecyclerView;
import fxtrader.com.app.lrececlerview.recyclerview.LRecyclerViewAdapter;
import fxtrader.com.app.tools.LogZ;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by pc on 2016/11/20.
 */
public class ProfitListPop extends PopupWindow {

    public static final int MAX_SHOW_COUNT = 4;

    private Context mContext;

    private TextView mRecordTv;

    LRecyclerViewAdapter mHeaderAndFooterRecyclerViewAdapter;

    private CustomAdapter mAdapter;

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
        mRecordTv = (TextView) view.findViewById(R.id.pop_profit_list_record_tv);
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
        LRecyclerView recyclerView = (LRecyclerView) view.findViewById(R.id.pop_profit_list_rec);
        mAdapter = new CustomAdapter(mContext);
        LinearLayoutManager manager = new LinearLayoutManager(mContext);
        recyclerView.setLayoutManager(manager);
        mHeaderAndFooterRecyclerViewAdapter = new LRecyclerViewAdapter(mContext, mAdapter);
        recyclerView.setAdapter(mHeaderAndFooterRecyclerViewAdapter);
        mHeaderAndFooterRecyclerViewAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Intent intent = new Intent(mContext, OrderDetailActivity.class);
                intent.putExtra(IntentItem.ORDER_DETAIL, mAdapter.getDataList().get(position));
                mContext.startActivity(intent);
            }

            @Override
            public void onItemLongClick(View view, int position) {

            }
        });
    }

    public void addData(List<PositionInfoEntity> data, int size) {
        mRecordTv.setText(mContext.getString(R.string.profit_list_num, String.valueOf(size)));
        mAdapter.setDataList(data);
        mAdapter.notifyDataSetChanged();
    }


    class CustomAdapter extends ListBaseAdapter<PositionInfoEntity> {
        private ViewHolder holder;
        private int layoutPosition;
        private Context context;

        public CustomAdapter(Context context) {
            this.context = context;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(context).inflate(R.layout.item_pop_profit_list, parent, false);
            holder = new ViewHolder(itemView);
            return holder;
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
            final PositionInfoEntity info = mDataList.get(position);
            LogZ.i(info.toString());
            ViewHolder holder = (ViewHolder) viewHolder;
            ProfitInfo profitInfo = getPrifit(info);
            if (profitInfo.isProfit){
                holder.profitTv.setTextColor(Color.parseColor("#e83743"));
            } else {
                holder.profitTv.setTextColor(Color.parseColor("#09cd29"));
            }
            holder.profitTv.setText(profitInfo.floatProfit);
            holder.stopProfitTv.setText(info.getProfit() * 10 + "%");
            holder.buyPriceTv.setText("买入价:" + String.valueOf(info.getBuyingRate()));
            holder.specificationTv.setText(info.getSpecification());
            holder.dealCountTv.setText(String.valueOf(info.getDealCount()) + " 手");
            String dealDirection;
            if ("UP".equals(info.getDealDirection())) {
                dealDirection = "买涨";
            } else {
                dealDirection = "买跌";
            }
            holder.dealDirectionTv.setText(dealDirection);

            final String id = info.getId();
            holder.closePositionTv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    closePosition(id);
                }
            });
        }

        private ProfitInfo getPrifit(PositionInfoEntity entity) {
            ProfitInfo info = new ProfitInfo();
            double rate = entity.getPlRate();
            double unit = entity.getPlUnit();
            double buyPrice = entity.getBuyingRate();
            double latestPrice = Double.valueOf(entity.getLatestPrice());
            double diff = latestPrice - buyPrice;
            if ("UP".equals(entity.getDealDirection())) { //买涨
                info.isProfit = (diff > 0) ? true : false;
            } else {
                info.isProfit = (diff > 0) ? false : true;
            }
            double result = diff * rate * unit;
            BigDecimal big = new BigDecimal(result);
            double num = big.setScale(1, BigDecimal.ROUND_HALF_UP).doubleValue();
            if (info.isProfit && (num < 0)) {
                double d = 0 - num;
                info.floatProfit = String.valueOf(d);
            } else if (!info.isProfit && (num > 0)) {

            }
            info.floatProfit = String.valueOf(num);
            return info;
        }

        private void closePosition(String id) {
            ContractApi dataApi = RetrofitUtils.createApi(ContractApi.class);
            String token = ParamsUtil.getToken();
            Call<CommonResponse> respon = dataApi.closePosition(token, getClosePositionParams(id));
            respon.enqueue(new Callback<CommonResponse>() {
                @Override
                public void onResponse(Call<CommonResponse> call, Response<CommonResponse> response) {
                    CommonResponse common = response.body();
                }

                @Override
                public void onFailure(Call<CommonResponse> call, Throwable t) {

                }
            });
        }

        private Map<String, String> getClosePositionParams(String id) {
            final Map<String, String> params = ParamsUtil.getCommonParams();
            params.put("method", "gdiex.storage.close");
            params.put("storageId", id);
            params.put("sign", ParamsUtil.sign(params));
            return params;
        }




        class ViewHolder extends RecyclerView.ViewHolder {
            private TextView profitTv;
            private TextView stopProfitTv;
            private TextView buyPriceTv;
            private TextView specificationTv;
            private TextView dealCountTv;
            private TextView closePositionTv;
            private TextView dealDirectionTv;

            public ViewHolder(View itemView) {
                super(itemView);
                profitTv = (TextView) itemView.findViewById(R.id.item_pop_profit_list_profit_tv);
                stopProfitTv = (TextView) itemView.findViewById(R.id.item_pop_profit_list_stop_profit_tv);
                buyPriceTv = (TextView) itemView.findViewById(R.id.item_pop_profit_list_buy_price_tv);
                specificationTv = (TextView) itemView.findViewById(R.id.item_pop_profit_list_specification_tv);
                dealCountTv = (TextView) itemView.findViewById(R.id.item_pop_profit_list_deal_count_tv);
                dealDirectionTv = (TextView) itemView.findViewById(R.id.item_pop_profit_list_deal_direction_tv);
                closePositionTv = (TextView) itemView.findViewById(R.id.item_pop_profit_list_close_position_tv);
            }
        }
    }

    class ProfitInfo {
        boolean isProfit;
        String floatProfit;
    }

}
