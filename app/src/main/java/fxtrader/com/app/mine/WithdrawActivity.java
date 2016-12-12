package fxtrader.com.app.mine;

import android.os.Bundle;
import android.support.annotation.Nullable;

import fxtrader.com.app.R;
import fxtrader.com.app.base.BaseActivity;

/**
 * 提现
 * Created by zhangyuzhu on 2016/12/1.
 */
public class WithdrawActivity extends BaseActivity{
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentLayout(R.layout.activity_withdraw);
    }
}
