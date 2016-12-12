package fxtrader.com.app.view;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import fxtrader.com.app.R;
import fxtrader.com.app.entity.CurrencyDetailEntity;
import fxtrader.com.app.tools.DateTools;

/**
 * 收支详情
 * Created by zhangyuzhu on 2016/11/29.
 */
public class PaymentDetailDialog extends Dialog implements View.OnClickListener{

    private CurrencyDetailEntity mEntity;

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
        TextView typeTv = (TextView) findViewById(R.id.dialog_payment_detail_type_tv);
        TextView timeTv = (TextView) findViewById(R.id.dialog_payment_detail_time_tv);
        TextView paymentTv = (TextView) findViewById(R.id.dialog_payment_detail_payment_tv);
        TextView balanceTv = (TextView) findViewById(R.id.dialog_payment_detail_balance_tv);

        idTv.setText(mEntity.getId());
        typeTv.setText(mEntity.getMemberCode());
        timeTv.setText(DateTools.changeToDate(mEntity.getDate()));
        paymentTv.setText(String.valueOf(mEntity.getTransactionMoney()));
        balanceTv.setText(String.valueOf(mEntity.getRemainingMoney()));
    }


}
