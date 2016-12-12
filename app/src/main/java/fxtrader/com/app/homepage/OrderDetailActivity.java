package fxtrader.com.app.homepage;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.TextView;

import fxtrader.com.app.R;
import fxtrader.com.app.base.BaseActivity;
import fxtrader.com.app.view.BuildPositionDialog;
import fxtrader.com.app.view.ClosePositionDialog;

/**
 * 订单详情
 * Created by pc on 2016/11/20.
 */
public class OrderDetailActivity extends BaseActivity implements View.OnClickListener {

    private TextView mProductDesTv;

    /**资金盈亏**/
    private TextView mFoundTv;

    private TextView mOrderNumTv;

    /**入仓价**/
    private TextView mBuyPriceTv;

    /**入仓金额**/
    private TextView mBuyAccountTv;

    /**行情**/
    private TextView mPriceTv;

    /**本单余额**/
    private TextView mBalanceTv;

    /**方向**/
    private TextView mDecideTv;

    /**资金盈亏**/
    private TextView mBuyNumTv;

    private TextView mStopProfitTv;

    private TextView mStopLossTv;

    private TextView mRemindTv;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentLayout(R.layout.activity_order_detail);
        initViews();
        setTitleContent(R.string.order_detail);
    }

    private void initViews() {
        mProductDesTv = (TextView) findViewById(R.id.order_detail_product_tv);
        mFoundTv = (TextView) findViewById(R.id.order_detail_fund_tv);
        mOrderNumTv = (TextView) findViewById(R.id.order_detail_order_num_tv);
        mBuyAccountTv = (TextView) findViewById(R.id.order_detail_buy_account_tv);
        mBalanceTv = (TextView) findViewById(R.id.person_account_balance_tv);
        mPriceTv = (TextView) findViewById(R.id.order_detail_price_tv);
        mDecideTv = (TextView) findViewById(R.id.order_detail_decide_tv);
        mBuyNumTv = (TextView) findViewById(R.id.order_detail_buy_num_tv);
        mStopProfitTv = (TextView) findViewById(R.id.order_detail_stop_profit_tv);
        mStopLossTv = (TextView) findViewById(R.id.order_detail_stop_loss_tv);
        mRemindTv = (TextView) findViewById(R.id.order_detail_remind_tv);

        findViewById(R.id.order_detail_change_tv).setOnClickListener(this);
        findViewById(R.id.order_detail_ok_tv).setOnClickListener(this);

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

    private void change(){
        //TODO
        BuildPositionDialog dialog = new BuildPositionDialog(this);
        dialog.show();
    }

    private void confirmClosePosition() {
        //TODO
        ClosePositionDialog dialog = new ClosePositionDialog(this);
        dialog.show();
    }
}
