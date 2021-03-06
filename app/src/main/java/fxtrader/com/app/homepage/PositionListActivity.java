package fxtrader.com.app.homepage;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import fxtrader.com.app.AppApplication;
import fxtrader.com.app.R;
import fxtrader.com.app.adapter.ListBaseAdapter;
import fxtrader.com.app.base.BaseActivity;
import fxtrader.com.app.constant.IntentItem;
import fxtrader.com.app.entity.ClosePositionResponse;
import fxtrader.com.app.entity.CommonResponse;
import fxtrader.com.app.entity.MarketEntity;
import fxtrader.com.app.entity.PositionInfoEntity;
import fxtrader.com.app.entity.PositionListEntity;
import fxtrader.com.app.http.HttpConstant;
import fxtrader.com.app.http.ParamsUtil;
import fxtrader.com.app.http.RetrofitUtils;
import fxtrader.com.app.http.api.ContractApi;
import fxtrader.com.app.lrececlerview.interfaces.OnItemClickListener;
import fxtrader.com.app.lrececlerview.recyclerview.LRecyclerView;
import fxtrader.com.app.lrececlerview.recyclerview.LRecyclerViewAdapter;
import fxtrader.com.app.service.PositionService;
import fxtrader.com.app.tools.ContractUtil;
import fxtrader.com.app.tools.LogZ;
import fxtrader.com.app.tools.UIUtil;
import fxtrader.com.app.view.ClosePositionDialog;
import fxtrader.com.app.view.ProfitListPop;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by zhangyuzhu on 2016/11/22.
 * 持仓信息
 */
public class PositionListActivity extends BaseActivity {

    private LRecyclerView mRecyclerView;

    private TextView mTotalBreakEvenTv;

    private TextView mPercentTv;

    private TextView mPositionListSizeTv;

    private CustomAdapter mCustomAdapter;

    private MarketEntity mMarketEntity;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentLayout(R.layout.activity_position_list);
        mMarketEntity = AppApplication.getInstance().getMarketEntity();
        initViews();
        setTitleContent(R.string.position_list);
        setBackListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                back();
            }
        });
        startService(new Intent(this, PositionService.class));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != RESULT_OK) {
            return;
        }
        if (requestCode == IntentItem.REQUEST_ORDER_DETAIL){
            PositionInfoEntity tempInfoEntity = null;
            if (data == null) {
                return;
            }
            String tempId = data.getStringExtra(IntentItem.POSITION_ID);
            if (TextUtils.isEmpty(tempId)) {
                return;
            }
            for (PositionInfoEntity infoEntity : mCustomAdapter.getDataList()) {

                if (tempId.equals(infoEntity.getId())) {
                    tempInfoEntity = infoEntity;
                    break;
                }
            }
            if (tempInfoEntity != null) {
                mCustomAdapter.getDataList().remove(tempInfoEntity);
                LogZ.i("size = " + mCustomAdapter.getDataList().size());
                if (mMarketEntity != null) {
                    setProfitAndLoss(mCustomAdapter.getDataList());
                }
                String str = getString(R.string.position_list_num, mCustomAdapter.getDataList().size());
                UIUtil.setForegroundColor(mPositionListSizeTv, str, Color.parseColor("#e83743"), 1, 1);
            }
        }
    }

    @Override
    public void onBackPressed() {
        back();
    }

    private void back(){
        setResult(RESULT_OK);
        finish();
    }

    @Override
    protected void onStart() {
        super.onStart();
        registerPriceReceiver();
        registerPositionListResevier();
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mPriceReceiver != null) {
            unregisterReceiver(mPriceReceiver);
        }
        if (mPositionListReceiver != null) {
            unregisterReceiver(mPositionListReceiver);
        }
    }

    private void initViews() {

        mTotalBreakEvenTv = (TextView) findViewById(R.id.position_list_break_even_num_tv);
        mPercentTv = (TextView) findViewById(R.id.position_list_break_even_percent_tv);
        mPositionListSizeTv = (TextView) findViewById(R.id.position_list_size_tv);

        mRecyclerView = (LRecyclerView) findViewById(R.id.position_list_rec);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setPullRefreshEnabled(false);

        mCustomAdapter = new CustomAdapter(this);
        LRecyclerViewAdapter mHeaderAndFooterRecyclerViewAdapter = new LRecyclerViewAdapter(this, mCustomAdapter);
        mRecyclerView.setAdapter(mHeaderAndFooterRecyclerViewAdapter);
        mHeaderAndFooterRecyclerViewAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Intent intent = new Intent(PositionListActivity.this, OrderDetailActivity.class);
                intent.putExtra(IntentItem.ORDER_DETAIL, mCustomAdapter.getDataList().get(position));
                startActivityForResult(intent, IntentItem.REQUEST_ORDER_DETAIL);
            }

            @Override
            public void onItemLongClick(View view, int position) {

            }
        });
    }

    private List<PositionInfoEntity> mPositionInfoList;

    private BroadcastReceiver mPriceReceiver;

    private void registerPriceReceiver() {
        mPriceReceiver = new PriceReceiver();
        IntentFilter filter = new IntentFilter(IntentItem.ACTION_PRICE);
        registerReceiver(mPriceReceiver, filter);
    }


    class PriceReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            MarketEntity vo = (MarketEntity) intent.getSerializableExtra(IntentItem.PRICE);
            if (vo != null) {
                vo.init();
                mMarketEntity = vo;
            }
        }
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
            View itemView = LayoutInflater.from(context).inflate(R.layout.item_position_list, parent, false);
            holder = new ViewHolder(itemView);
            return holder;
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            final PositionInfoEntity info = mDataList.get(position);
            LogZ.i(info.toString());
            ViewHolder viewHolder = (ViewHolder) holder;
            ProfitInfo profitInfo = getProfit(info);
            if (profitInfo.isProfit){
                viewHolder.breakEvenTv.setTextColor(Color.parseColor("#e83743"));
                viewHolder.breakEvenTv.setText(profitInfo.floatProfit);
            } else {
                viewHolder.breakEvenTv.setTextColor(Color.parseColor("#09cd29"));
                viewHolder.breakEvenTv.setText("-" + profitInfo.floatProfit);
            }
            viewHolder.byuPriceTv.setText("买入价:" + String.valueOf(info.getBuyingRate()));
            viewHolder.productionTv.setText(info.getContractName());
            String type = String.valueOf(info.getDealCount()) + " 手";
            String dealDirection;
            if ("UP".equals(info.getDealDirection())) {
                dealDirection = "买涨";
            } else {
                dealDirection = "买跌";
            }
            viewHolder.buyTypeTv.setText(type + dealDirection);

            viewHolder.closePositionTv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    closePosition(info);
                }
            });
            info.getContractName();
        }

        private ProfitInfo getProfit(PositionInfoEntity entity) {
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
            double result = diff * rate * unit * entity.getDealCount();
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

        private void closePosition(final PositionInfoEntity entity) {
            ClosePositionDialog dialog = new ClosePositionDialog(context, entity.getContractName());
            dialog.setDialogListener(new ClosePositionDialog.DialogListener() {
                @Override
                public void ok() {
                    closePositionRequest(entity.getId());
                }
            });
            dialog.show();
        }

        private void closePositionRequest(String id){
            LogZ.i("closePositionRequest");
            showProgressDialog();
            ContractApi dataApi = RetrofitUtils.createApi(ContractApi.class);
            String token = ParamsUtil.getToken();
            Call<ClosePositionResponse> request = dataApi.closePosition(token, getClosePositionParams(id));
            request.enqueue(new Callback<ClosePositionResponse>() {
                @Override
                public void onResponse(Call<ClosePositionResponse> call, Response<ClosePositionResponse> response) {
                    ClosePositionResponse entity = response.body();
                    LogZ.i("response:  " + entity.toString());
                    if (entity != null) {
                        if (entity.isSuccess()) {
                            String tempId = entity.getObject().getId();
                            LogZ.i("tempId = " + tempId);
                            PositionInfoEntity tempInfoEntity = null;
                            for (PositionInfoEntity infoEntity : getDataList()) {
                                if (tempId.equals(infoEntity.getId())) {
                                    tempInfoEntity = infoEntity;
                                    break;
                                }
                            }
                            if (tempInfoEntity != null) {
                                getDataList().remove(tempInfoEntity);
                                LogZ.i("size = " + getDataList().size());
                                if (mMarketEntity != null) {
                                    setProfitAndLoss(getDataList());
                                }
                                String str = getString(R.string.position_list_num, getDataList().size());
                                UIUtil.setForegroundColor(mPositionListSizeTv, str, Color.parseColor("#e83743"), 1, 1);
                            }
                        } else {
                            showToastShort(entity.getMessage());
                        }
                    }


                    dismissProgressDialog();
                }

                @Override
                public void onFailure(Call<ClosePositionResponse> call, Throwable t) {
                    if (t != null && t.getMessage() != null) {
                        LogZ.e(t.getMessage());
                    } else {
                        LogZ.e("Throwable null");
                    }
                    dismissProgressDialog();
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
            TextView breakEvenTv;
            TextView byuPriceTv;
            TextView productionTv;
            TextView buyTypeTv;
            TextView closePositionTv;

            public ViewHolder(View itemView) {
                super(itemView);
                breakEvenTv = (TextView) itemView.findViewById(R.id.item_position_list_break_even_tv);
                byuPriceTv = (TextView) itemView.findViewById(R.id.item_position_list_buy_price_tv);
                productionTv = (TextView) itemView.findViewById(R.id.item_position_list_name_tv);
                buyTypeTv = (TextView) itemView.findViewById(R.id.item_position_list_buy_type_tv);

                closePositionTv = (TextView) itemView.findViewById(R.id.item_pop_profit_list_close_position_tv);
            }
        }
    }

    class ProfitInfo {
        boolean isProfit;
        String floatProfit;
    }

    private BroadcastReceiver mPositionListReceiver;

    private void registerPositionListResevier(){
        mPositionListReceiver = new PositionListReceiver();
        IntentFilter filter = new IntentFilter(IntentItem.ACTION_POSITION_LIST);
        registerReceiver(mPositionListReceiver, filter);
    }

    class PositionListReceiver extends BroadcastReceiver{
        @Override
        public void onReceive(Context context, Intent intent) {
            mPositionInfoList = (List<PositionInfoEntity>) intent.getSerializableExtra(IntentItem.POSITION_LIST);
            int size;
            if (mPositionInfoList != null) {
                if (mMarketEntity != null) {
                    setProfitAndLoss(mPositionInfoList);
                    mCustomAdapter.setDataList(mPositionInfoList);
                }
                size =  mPositionInfoList.size();
            } else {
                size = 0;
            }
            String str = getString(R.string.position_list_num, size);
            UIUtil.setForegroundColor(mPositionListSizeTv, str, Color.parseColor("#e83743"), 1, 1);
        }
    }

    private void setProfitAndLoss(List<PositionInfoEntity> list){
        double sum = ContractUtil.initProfitInfoList(mMarketEntity, list);
        int dealCount = 0;

        int color;
        if (sum >= 0) {
            color = getResources().getColor(R.color.red_text);
        } else {
            color = getResources().getColor(R.color.green);
        }
        mTotalBreakEvenTv.setTextColor(color);
        mPercentTv.setTextColor(color);
        mTotalBreakEvenTv.setText(String.valueOf(sum));

        for (PositionInfoEntity entity : list) {
            dealCount = entity.getDealCount() + dealCount;
        }

        double percent = 0;
        if (dealCount != 0) {
            percent = sum / dealCount;
        }
        mPercentTv.setText("(" + ContractUtil.getDouble(percent, 2) + "%)" );
    }

}
