package fxtrader.com.app.mine;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import fxtrader.com.app.R;
import fxtrader.com.app.base.BaseActivity;

/**
 * 绑定手机
 * Created by zhangyuzhu on 2016/11/26.
 */
public class BindPhoneActivity extends BaseActivity implements View.OnClickListener{

    private EditText mPhoneEdt;

    private EditText mCodeEdt;

    private TextView mGetCodeTv;

    private TextView mConfirmTv;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentLayout(R.layout.activity_bind_phone);
        initViews();
    }

    private void initViews() {
        mGetCodeTv = (TextView) findViewById(R.id.bind_phone_get_code_tv);
        mConfirmTv = (TextView) findViewById(R.id.bind_phone_confirm_tv);

        mGetCodeTv.setOnClickListener(this);
        mConfirmTv.setOnClickListener(this);

        mPhoneEdt = (EditText) findViewById(R.id.bind_phone_phone_edt);
        mCodeEdt = (EditText) findViewById(R.id.bind_phone_code_edt);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bind_phone_get_code_tv:
                break;
            case R.id.bind_phone_confirm_tv:
                break;
            default:
                break;
        }
    }
}
