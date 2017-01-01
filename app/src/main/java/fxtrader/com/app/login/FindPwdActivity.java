package fxtrader.com.app.login;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import java.util.Map;

import fxtrader.com.app.R;
import fxtrader.com.app.base.BaseActivity;
import fxtrader.com.app.entity.CommonResponse;
import fxtrader.com.app.http.HttpConstant;
import fxtrader.com.app.http.ParamsUtil;
import fxtrader.com.app.http.RetrofitUtils;
import fxtrader.com.app.http.api.UserApi;
import fxtrader.com.app.tools.LogZ;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by pc on 2017/1/1.
 */
public class FindPwdActivity extends BaseActivity implements View.OnClickListener{

    private EditText mPhoneEdt;

    private EditText mCodeEdt;

    private EditText mPwdEdt;

    private EditText mConfirmPwdEdt;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentLayout(R.layout.activity_find_pwd);
        initViews();
    }

    private void initViews(){
        mPhoneEdt = (EditText) findViewById(R.id.forget_pwd_phone_edt);
        mCodeEdt = (EditText) findViewById(R.id.forget_pwd_code_edt);
        mPwdEdt = (EditText) findViewById(R.id.forget_pwd_pwd_edt);
        mConfirmPwdEdt = (EditText) findViewById(R.id.forget_pwd_confirm_edt);

        findViewById(R.id.forget_pwd_get_code_tv).setOnClickListener(this);
        findViewById(R.id.forget_pwd_change_tv).setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.forget_pwd_get_code_tv:
                getVerificationCode();
                break;
            case R.id.forget_pwd_change_tv:
                findPwd();
                break;
            default:
                break;
        }
    }

    private void getVerificationCode() {
        showProgressDialog();
        String phoneNum = getTvStr(mPhoneEdt);
        if (TextUtils.isEmpty(phoneNum)){
            showToastShort("请输入手机号码");
            return;
        }
        UserApi userApi = RetrofitUtils.createApi(UserApi.class);
        final Call<CommonResponse> request = userApi.versificationCode(getVersificationCodeParams(phoneNum));
        request.enqueue(new Callback<CommonResponse>() {
            @Override
            public void onResponse(Call<CommonResponse> call, Response<CommonResponse> response) {
                CommonResponse commonResponse = response.body();
                if (commonResponse != null && commonResponse.isSuccess()){
                    showToastShort(commonResponse.getMessage());
                }
                dismissProgressDialog();
            }

            @Override
            public void onFailure(Call<CommonResponse> call, Throwable t) {
                Log.i("zyu", t.getMessage());
                dismissProgressDialog();
            }
        });
    }

    private Map<String, String> getVersificationCodeParams(String phoneNum) {
        final Map<String, String> params = ParamsUtil.getCommonParams();
        params.put("method", "gdiex.sms.send");
        params.put("telNumber", phoneNum);
        params.put("reason", HttpConstant.VerificationCode.FIND_PWD);
        params.put("sign", ParamsUtil.sign(params));
        return params;
    }

    private void findPwd(){
        String phone = getTvStr(mPhoneEdt);
        String verificationCode = getTvStr(mCodeEdt);
        String pwd = getTvStr(mPwdEdt);
        String confirmPwd = getTvStr(mConfirmPwdEdt);

        if (TextUtils.isEmpty(phone)){
            showToastShort("请输入手机号码");
            return;
        }

        if (TextUtils.isEmpty(verificationCode)){
            showToastShort("请输入验证码");
            return;
        }

        if (TextUtils.isEmpty(pwd)){
            showToastShort("请输入密码");
            return;
        }

        if (!pwd.equals(confirmPwd)){
            showToastShort("请确认密码");
            return;
        }
        requestFindPwd(phone, verificationCode, pwd);
    }

    private void requestFindPwd(String phone, String code, String pwd){
        showProgressDialog();
        UserApi userApi = RetrofitUtils.createApi(UserApi.class);
        Call<CommonResponse> request = userApi.findPwd(getFindPwdParams(phone, code, pwd));
        request.enqueue(new Callback<CommonResponse>() {
            @Override
            public void onResponse(Call<CommonResponse> call, Response<CommonResponse> response) {
                CommonResponse commonResponse = response.body();
                if (commonResponse != null && commonResponse.isSuccess()) {
                    showToastShort(commonResponse.getMessage());
                }
                dismissProgressDialog();
            }

            @Override
            public void onFailure(Call<CommonResponse> call, Throwable t) {
                dismissProgressDialog();
                LogZ.e(t.getMessage());
            }
        });
    }

    private Map<String, String> getFindPwdParams(String phoneNum, String code, String pwd) {
        final Map<String, String> params = ParamsUtil.getCommonParams();
        params.put("method", "gdiex.users.forgetPassword");
        params.put("telNumber", phoneNum);
        params.put("code", code);
        params.put("newPassword", pwd);
        params.put("sign", ParamsUtil.sign(params));
        return params;
    }


    private String getTvStr(TextView tv){
        return tv.getText().toString().trim();
    }
}
