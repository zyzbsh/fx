package fxtrader.com.app.homepage;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.format.DateUtils;
import android.view.View;
import android.widget.TextView;

import java.text.NumberFormat;
import java.util.Map;

import fxtrader.com.app.AppApplication;
import fxtrader.com.app.R;
import fxtrader.com.app.base.BaseActivity;
import fxtrader.com.app.constant.IntentItem;
import fxtrader.com.app.entity.ClosePositionResponse;
import fxtrader.com.app.entity.CommonResponse;
import fxtrader.com.app.entity.ContractInfoEntity;
import fxtrader.com.app.entity.MarketEntity;
import fxtrader.com.app.entity.PositionInfoEntity;
import fxtrader.com.app.entity.PriceEntity;
import fxtrader.com.app.http.HttpConstant;
import fxtrader.com.app.http.ParamsUtil;
import fxtrader.com.app.http.RetrofitUtils;
import fxtrader.com.app.http.api.ContractApi;
import fxtrader.com.app.tools.ContractUtil;
import fxtrader.com.app.tools.DateTools;
import fxtrader.com.app.tools.LogZ;
import fxtrader.com.app.tools.UIUtil;
import fxtrader.com.app.view.BuildPositionDialog;
import fxtrader.com.app.view.ClosePositionDialog;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * 订单详情
 * Created by pc on 2016/11/20.
 */
public class OrderDetailActivity extends BaseActivity implements View.OnClickListener {

    private PositionInfoEntity mPositionInfo;

    private ContractInfoEntity mContractInfo;

    private boolean mIsUp;

    private int mBuyAccount;

    private TextView mProductDesTv;

    /**
     * 资金盈亏
     **/
    private TextView mProfitTv;

    private TextView mOrderNumTv;

    /**
     * 入仓价
     **/
    private TextView mBuyPriceTv;

    /**
     * 入仓金额
     **/
    private TextView mBuyAccountTv;

    /**
     * 行情
     **/
    private TextView mPriceTv;

    /**
     * 本单余额
     **/
    private TextView mBalanceTv;

    /**
     * 方向
     **/
    private TextView mDealDirectionTv;

    /**
     * 资金盈亏
     **/
    private TextView mBuyNumTv;

    private TextView mDateTv;

    private TextView mStopProfitTv;

    private TextView mStopLossTv;

    private TextView mChangeStopPercentTv;

    private TextView mRemindTv;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentLayout(R.layout.activity_order_detail);
        setTitleContent(R.string.order_detail);
        mPositionInfo = (PositionInfoEntity) getIntent().getSerializableExtra(IntentItem.ORDER_DETAIL);
        mIsUp = ContractUtil.isUpDirection(mPositionInfo);
        mContractInfo = ContractUtil.getContractInfoMap().get(mPositionInfo.getContractCode());
        if (mContractInfo != null) {
            LogZ.i(mContractInfo.toString());
        }
        initViews();
        setViews();
        String code = mPositionInfo.getContractCode();
        String queryParam = ContractUtil.getContractInfoMap().get(code).getQueryParam();

        if (queryParam.equals(HttpConstant.PriceCode.YDHF)) {
            mChangeStopPercentTv.setVisibility(View.INVISIBLE);
        } else {
            mChangeStopPercentTv.setVisibility(View.VISIBLE);
        }

        MarketEntity vo = AppApplication.getInstance().getMarketEntity();
        if (vo != null) {
            String data = vo.getData(queryParam);
            LogZ.i("code = " + code + "queryParam = " + queryParam + ", data = " + data);
            PriceEntity entity = new PriceEntity(data);
            double latestPrice = Double.parseDouble(entity.getLatestPrice());
            setPrice(latestPrice);
        }
    }

    private void initViews() {
        mProductDesTv = (TextView) findViewById(R.id.order_detail_product_tv);
        mBuyPriceTv = (TextView) findViewById(R.id.order_detail_buy_price_tv);
        mProfitTv = (TextView) findViewById(R.id.order_detail_profit_tv);
        mOrderNumTv = (TextView) findViewById(R.id.order_detail_order_num_tv);
        mBuyAccountTv = (TextView) findViewById(R.id.order_detail_buy_account_tv);
        mBalanceTv = (TextView) findViewById(R.id.order_detail_balance_tv);
        mPriceTv = (TextView) findViewById(R.id.order_detail_price_tv);
        mDealDirectionTv = (TextView) findViewById(R.id.order_detail_deal_direction_tv);
        mBuyNumTv = (TextView) findViewById(R.id.order_detail_buy_num_tv);
        mDateTv = (TextView) findViewById(R.id.order_detail_build_time_tv);
        mStopProfitTv = (TextView) findViewById(R.id.order_detail_stop_profit_tv);
        mStopLossTv = (TextView) findViewById(R.id.order_detail_stop_loss_tv);
        mRemindTv = (TextView) findViewById(R.id.order_detail_remind_tv);

        mChangeStopPercentTv = (TextView) findViewById(R.id.order_detail_change_tv);
        mChangeStopPercentTv.setOnClickListener(this);
        findViewById(R.id.order_detail_ok_tv).setOnClickListener(this);
    }

    private void setViews() {
        mProductDesTv.setText(getString(R.string.order_detail_product_des, mContractInfo.getName(), String.valueOf(mContractInfo.getMargin())));
        mOrderNumTv.setText(getString(R.string.order_detail_order_num, mPositionInfo.getId()));
        mBuyPriceTv.setText(getString(R.string.order_detail_buy_price_num, String.valueOf(mPositionInfo.getBuyingRate())));
        if (mIsUp) {
            mDealDirectionTv.setText(getString(R.string.order_detail_decide_type, "买涨"));
        } else {
            mDealDirectionTv.setText(getString(R.string.order_detail_decide_type, "买跌"));
        }
        mBuyNumTv.setText(getString(R.string.order_detail_buy_num, String.valueOf(mPositionInfo.getDealCount())));
        mBuyAccount = mPositionInfo.getDealCount() * mContractInfo.getMargin();
        mBuyAccountTv.setText(getString(R.string.order_detail_buy_account_num, String.valueOf(mBuyAccount)));
        String date = DateTools.changeToDate2(mPositionInfo.getBuyingDate());
        mDateTv.setText(getString(R.string.order_detail_build_time, date));

        String lastTime = "";
        long dateLong = mPositionInfo.getBuyingDate();
        long time = dateLong % DateTools.DAY;
        if (time < 4 * DateTools.HOUR){
            lastTime = DateTools.changeToDay(dateLong) + " 04:00:00";
        } else { //time > 8 * DateTools.HOUR
            lastTime = DateTools.changeToDay(dateLong + DateTools.DAY) + " 04:00:00";
        }
        mRemindTv.setText(getString(R.string.order_detail_remind_time, lastTime));
        setStopProfitTv(mPositionInfo.getProfit());
        setStopLossTv(mPositionInfo.getLoss());
    }

    private void setStopProfitTv(double profit) {
        LogZ.i("set profit = " + profit);
        String str;
        if (profit < 10E-6) {
            str = "不设";
        } else {
            int percent = (int) (profit * 100);
            str = percent + "%";
        }
        LogZ.i("profit = " + profit + ", str = " + str);
        mStopProfitTv.setText(getString(R.string.stop_profit_num, str));
    }

    private void setStopLossTv(double loss) {
        LogZ.i("set loss = " + loss);
        String str;
        if (loss == -1) {
            str = "不设";
        } else {
            int percent = (int) (loss * 100);
            str = Math.abs(percent) + "%";
        }
        LogZ.i("loss = " + loss + ", str = " + str);
        mStopLossTv.setText(getString(R.string.stop_loss_num, str));
    }

    @Override
    protected void onStart() {
        super.onStart();
        registerPriceReceiver();
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mPriceReceiver != null) {
            unregisterReceiver(mPriceReceiver);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_OK) {
            return;
        }
        if (requestCode == IntentItem.REQUEST_SET_PROFIT_AND_LOSS && data != null) {
            int profit = data.getIntExtra("profitPercent", 100);
            int loss = data.getIntExtra("lossPercent", 100);
            if (profit != 100) {
                if (profit == 0) {
                    setStopProfitTv(profit);
                } else {
                    double p = Double.parseDouble("0." + profit);
                    setStopProfitTv(p);
                }
            }
            if (loss != 100) {
                if (loss == 0) {
                    setStopLossTv(loss);
                } else {
                    double l = Double.parseDouble("0." + loss);
                    setStopLossTv(l);
                }
            }
            LogZ.i("改变止盈止损");
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.order_detail_change_tv://修改
                change();
                break;
            case R.id.order_detail_ok_tv://确定平仓
                confirmClosePosition();
                break;
            default:
                break;
        }
    }

    private BuildPositionDialog mDialog;

    private void change() {
//        double stopProfit = mPositionInfo.getProfit();
//        double lossProfit = mPositionInfo.getLoss();
//        mDialog = new BuildPositionDialog(this, stopProfit, lossProfit);
//        mDialog.show();
//        mDialog.setOkListener(new BuildPositionDialog.OkListener() {
//            @Override
//            public void ok(int stopProfit, int stopLoss) {
//                int profitPercent = (int) Math.abs(mPositionInfo.getProfit() * 10);
//                int lossPercent = (int) Math.abs(mPositionInfo.getLoss() * 10);
//                if (stopProfit == profitPercent && stopLoss == lossPercent) {
//                    return;
//                } else {
//                    setProfitAndLoss(mPositionInfo.getId(), stopProfit, stopLoss);
//                }
//            }
//        });

        Intent intent = new Intent(this, ProfitAndLossActivity.class);
        intent.putExtra(IntentItem.POSITION_INFO, mPositionInfo);
        startActivityForResult(intent, IntentItem.REQUEST_SET_PROFIT_AND_LOSS);
    }



    private void confirmClosePosition() {
        ClosePositionDialog dialog = new ClosePositionDialog(this, mPositionInfo.getContractName());
        dialog.setDialogListener(new ClosePositionDialog.DialogListener() {
            @Override
            public void ok() {
                closePositionRequest(mPositionInfo.getId());
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
                        Intent intent = getIntent();
                        intent.putExtra(IntentItem.POSITION_ID, tempId);
                        setResult(RESULT_OK, intent);
                        finish();
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
            vo.init();
            String code = mPositionInfo.getContractCode();
            String paramsCode = ContractUtil.getContractInfoMap().get(code).getQueryParam();
            String type = vo.getData(paramsCode);
            PriceEntity price = new PriceEntity(type);
            double latestPrice = Double.parseDouble(price.getLatestPrice());
            setPrice(latestPrice);
        }
    }

    private void setPrice(double latestPrice) {
        double diff = latestPrice - mPositionInfo.getBuyingRate();
        boolean profit = false;
        if (mIsUp) {
            if (diff > 0) {
                profit = true;
            } else {
                profit = false;
            }
        } else {
            if (diff < 0) {
                profit = true;
                diff = Math.abs(diff);
            } else {
                profit = false;
                diff = 0 - diff;
            }
        }
        if (profit) {
            mProfitTv.setTextColor(Color.parseColor("#e83743"));
            mPriceTv.setTextColor(Color.parseColor("#e83743"));
        } else {
            mProfitTv.setTextColor(Color.parseColor("#09cd29"));
            mPriceTv.setTextColor(Color.parseColor("#09cd29"));
        }
        mPriceTv.setText(String.valueOf(latestPrice));
        double sum = diff * mPositionInfo.getDealCount() * mContractInfo.getPlRate() * mContractInfo.getPlUnit();
        double result = ContractUtil.getDouble(sum, 2);
        double percent = result / (mPositionInfo.getDealCount() * mContractInfo.getMargin());
        NumberFormat nt = NumberFormat.getPercentInstance();
        //设置百分数精确度2即保留两位小数
        nt.setMinimumFractionDigits(2);
        mProfitTv.setText(getString(R.string.order_detail_fund_num, String.valueOf(result), nt.format(percent)));
        double balance = result + mBuyAccount;
        mBalanceTv.setText(getString(R.string.order_detail_balance_num, String.valueOf(ContractUtil.getDouble(balance, 2))));
    }
}
