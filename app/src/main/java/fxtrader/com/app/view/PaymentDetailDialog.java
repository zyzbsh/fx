package fxtrader.com.app.view;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import java.util.Map;

import fxtrader.com.app.R;
import fxtrader.com.app.entity.CurrencyDetailEntity;
import fxtrader.com.app.entity.OrderDetailEntity;
import fxtrader.com.app.entity.PositionInfoEntity;
import fxtrader.com.app.http.HttpConstant;
import fxtrader.com.app.http.ParamsUtil;
import fxtrader.com.app.http.RetrofitUtils;
import fxtrader.com.app.http.api.ContractApi;
import fxtrader.com.app.tools.DateTools;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * 收支详情
 * Created by zhangyuzhu on 2016/11/29.
 */
public class PaymentDetailDialog extends Dialog implements View.OnClickListener{

    private CurrencyDetailEntity mEntity;

    TextView typeTv;

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.dialog_payment_detail_close_btn:
                this.dismiss();
                break;
            default:
                break;
        }
    }

    public PaymentDetailDialog(Context context, CurrencyDetailEntity entity) {
        super(context);
        mEntity = entity;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_payment_detail);
        findViewById(R.id.dialog_payment_detail_close_btn).setOnClickListener(this);
        TextView idTv = (TextView) findViewById(R.id.dialog_payment_detail_id_tv);
        typeTv = (TextView) findViewById(R.id.dialog_payment_detail_type_tv);
        TextView timeTv = (TextView) findViewById(R.id.dialog_payment_detail_time_tv);
        TextView paymentTv = (TextView) findViewById(R.id.dialog_payment_detail_payment_tv);
        TextView balanceTv = (TextView) findViewById(R.id.dialog_payment_detail_balance_tv);

        idTv.setText(mEntity.getId());
        timeTv.setText(DateTools.changeToDate(mEntity.getDate()));
        paymentTv.setText(String.valueOf(mEntity.getTransactionMoney()));
        balanceTv.setText(String.valueOf(mEntity.getRemainingMoney()));
        String type = mEntity.getTransactionType();
        if (HttpConstant.TransactionType.BUILD_METALORDER.equals(type)) {
            typeTv.setText("建仓");
        } else {
            requestData();
        }
    }

    private void requestData(){
        ContractApi dataApi = RetrofitUtils.createApi(ContractApi.class);
        String token = ParamsUtil.getToken();
        Call<OrderDetailEntity> request = dataApi.orderDetail(token, mEntity.getId(), getParams());
        request.enqueue(new Callback<OrderDetailEntity>() {
            @Override
            public void onResponse(Call<OrderDetailEntity> call, Response<OrderDetailEntity> response) {
                OrderDetailEntity entity = response.body();
                if (entity != null && entity.getObject() != null) {
                    typeTv.setText(getClosePositionType(entity.getObject().getSellingType()));
                }
            }

            @Override
            public void onFailure(Call<OrderDetailEntity> call, Throwable t) {

            }
        });
    }
    private Map<String, String> getParams() {
        final Map<String, String> params = ParamsUtil.getCommonParams();
        params.put("method", "gdiex.storage.get");
        params.put("sign", ParamsUtil.sign(params));
        return params;
    }

    private String getClosePositionType(String type) {
        String value = "";
        if (HttpConstant.SellingType.MANUAL.equals(type)) {
            value = "手动结算";
        } else if (HttpConstant.SellingType.BUINESS_SETTLEMENT.equals(type)) {
            value = "交易日结算";
        } else if (HttpConstant.SellingType.AUTO_PROFIT.equals(type)) {
            value = "止盈平仓";
        } else if (HttpConstant.SellingType.AUTO_LOSS.equals(type)){
            value = "止损平仓";
        } else if (HttpConstant.SellingType.BLAST.equals(type)) {
            value = "爆仓";
        }
        return value;
    }


}
