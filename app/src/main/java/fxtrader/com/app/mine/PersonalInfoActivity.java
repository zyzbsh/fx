package fxtrader.com.app.mine;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import fxtrader.com.app.R;
import fxtrader.com.app.base.BaseActivity;

/**
 * Created by zhangyuzhu on 2016/11/26.
 */
public class PersonalInfoActivity extends BaseActivity implements View.OnClickListener{
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentLayout(R.layout.activity_personal_info);
        initViews();
    }

    private void initViews() {
        findViewById(R.id.personal_info_sex_layout).setOnClickListener(this);
        findViewById(R.id.personal_info_bind_layout).setOnClickListener(this);
        findViewById(R.id.personal_info_change_pwd_layout).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.personal_info_sex_layout://性别
                break;
            case R.id.personal_info_bind_layout://绑定手机
                openActivity(BindPhoneActivity.class);
                break;
            case R.id.personal_info_change_pwd_layout://修改密码
                openActivity(ChangePwdActivity.class);
                break;
            default:
                break;
        }
    }
}
