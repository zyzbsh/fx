package fxtrader.com.app.mine;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import java.util.Map;

import fxtrader.com.app.R;
import fxtrader.com.app.base.BaseActivity;
import fxtrader.com.app.entity.CommonResponse;
import fxtrader.com.app.http.ParamsUtil;
import fxtrader.com.app.http.RetrofitUtils;
import fxtrader.com.app.http.api.UserApi;
import fxtrader.com.app.tools.LogZ;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * 修改密码
 * Created by zhangyuzhu on 2016/11/26.
 */
public class ChangePwdActivity extends BaseActivity implements View.OnClickListener{

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
        findViewById(R.id.change_pwd_confirm_tv).setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.change_pwd_confirm_tv:
                changePwd();
                break;
        }
    }

    private void changePwd(){
        String pwd = getTvStr(mRechargeEdt);
        if (TextUtils.isEmpty(pwd)) {
            showToastShort("请输入充值密码");
            return;
        }
        String newPwd = getTvStr(mNewEdt);
        if (TextUtils.isEmpty(newPwd)){
            showToastShort("请输入新密码");
            return;
        }
        String confirmPwd = getTvStr(mConfirmEdt);
        if (!newPwd.equals(confirmPwd)){
            showToastShort("请重新确认密码");
            return;
        }
        requestChangePwd(pwd, newPwd);
    }

    private String getTvStr(TextView tv){
        return tv.getText().toString().trim();
    }

    private void requestChangePwd(String oldPwd, String newPwd) {
        showProgressDialog();
        UserApi userApi = RetrofitUtils.createApi(UserApi.class);
        String token = ParamsUtil.getToken();
        Call<CommonResponse> request = userApi.changePwd(token, getParams(oldPwd, newPwd));
        request.enqueue(new Callback<CommonResponse>() {
            @Override
            public void onResponse(Call<CommonResponse> call, Response<CommonResponse> response) {
                dismissProgressDialog();
                CommonResponse entity = response.body();
                if (entity != null) {
                    showToastShort(entity.getMessage());
                }
            }

            @Override
            public void onFailure(Call<CommonResponse> call, Throwable t) {
                dismissProgressDialog();
                LogZ.e(t.getMessage());
            }
        });
    }

    private Map<String,String> getParams(String oldPwd, String newPwd) {
        final Map<String, String> params = ParamsUtil.getCommonParams();
        params.put("method", "gdiex.users.changePwd");
        params.put("oldPassword", oldPwd);
        params.put("newPassword", newPwd);
        params.put("sign", ParamsUtil.sign(params));
        return params;
    }
}
