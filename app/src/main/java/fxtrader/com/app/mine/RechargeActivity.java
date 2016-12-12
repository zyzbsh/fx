package fxtrader.com.app.mine;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import fxtrader.com.app.R;
import fxtrader.com.app.base.BaseActivity;

/**
 * Created by zhangyuzhu on 2016/12/10.
 */
public class RechargeActivity extends BaseActivity implements View.OnClickListener{

    private ImageView mAvatarIm;

    private TextView mNameTv;

    private TextView mBalanceTv;

    private TextView mCashCouponTv;

    private TextView mReceiveTv;

    private EditText mRechargeEdt;

    private String[] mMoneyNum = {"100", "500", "1000", "2000", "3000", "5000"};

    private int[] mMoneyTvIds = {R.id.recharge_100_tv, R.id.recharge_500_tv, R.id.recharge_500_tv, R.id.recharge_2000_tv, R.id.recharge_3000_tv, R.id.recharge_5000_tv};

    private TextView[] mMoneyTvs = new TextView[mMoneyTvIds.length];

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentLayout(R.layout.activity_recharge);
        initViews();
    }

    private void initViews() {
        initUserLayout();
        initMoneyLayout();
        initPayLayout();
    }

    private void initUserLayout() {
        mAvatarIm = (ImageView) findViewById(R.id.recharge_person_avatar_im);
        mNameTv = (TextView) findViewById(R.id.recharge_name_tv);
        mBalanceTv = (TextView) findViewById(R.id.recharge_account_balance_tv);
        mCashCouponTv = (TextView) findViewById(R.id.recharge_cash_coupon_count_tv);
        mReceiveTv = (TextView) findViewById(R.id.recharge_receive_tv);
        mRechargeEdt = (EditText) findViewById(R.id.recharge_edt);
    }

    private void initPayLayout() {
        findViewById(R.id.recharge_wx_layout).setOnClickListener(this);
        findViewById(R.id.recharge_yh_layout).setOnClickListener(this);
    }

    private void initMoneyLayout() {
        OnMoneyClickedListener listener = new OnMoneyClickedListener();
        for(int i = 0; i < mMoneyTvIds.length; i++){
            mMoneyTvs[i] = (TextView) findViewById(mMoneyTvIds[i]);
            mMoneyTvs[i].setOnClickListener(listener);
        }
    }

    private class OnMoneyClickedListener implements View.OnClickListener{
        @Override
        public void onClick(View v) {
            for (int i = 0; i < mMoneyTvIds.length; i++) {
                if (v.getId() == mMoneyTvIds[i]) {
                    mRechargeEdt.setText(mMoneyNum[i]);
                    mMoneyTvs[i].setBackgroundResource(R.drawable.shape_red_corner_3);
                    mMoneyTvs[i].setTextColor(Color.WHITE);
                } else {
                    mMoneyTvs[i].setBackgroundResource(R.drawable.shape_white_corner_3);
                    mMoneyTvs[i].setTextColor(getColor(R.color.red_text));
                }
            }
        }
    }



    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.recharge_wx_layout:
                //TODO 微信支付
                break;
            case R.id.recharge_yh_layout:
                //TODO 银行支付
                break;
            default:
                break;
        }

    }
}
