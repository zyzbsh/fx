package fxtrader.com.app.mine;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import fxtrader.com.app.R;
import fxtrader.com.app.base.BaseActivity;

/**
 * 修改密码
 * Created by zhangyuzhu on 2016/11/26.
 */
public class ChangePwdActivity extends BaseActivity {

    private EditText mRechargeEdt;

    private EditText mNewEdt;

    private EditText mConfirmEdt;

    private TextView mConfirmTv;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentLayout(R.layout.activity_change_pwd);
        initViews();
    }

    private void initViews() {
        mRechargeEdt = (EditText) findViewById(R.id.change_pwd_recharge_edt);
        mNewEdt = (EditText) findViewById(R.id.change_pwd_new_edt);
        mConfirmEdt = (EditText) findViewById(R.id.change_pwd_confirm_edt);
        mConfirmTv = (TextView) findViewById(R.id.change_pwd_confirm_tv);

        mConfirmTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO
            }
        });
    }

}
