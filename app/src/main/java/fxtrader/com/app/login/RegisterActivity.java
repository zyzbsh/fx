package fxtrader.com.app.login;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
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
import fxtrader.com.app.view.ValidationCode;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * 注册
 * Created by zhangyuzhu on 2016/12/6.
 */
public class RegisterActivity extends BaseActivity implements View.OnClickListener {

    private EditText mAccountEdt;

    private EditText mAgentEdt;

    private EditText mNicknameEdt;

    private EditText mPwdEdt;

    private EditText mPwdAgainEdt;

    private EditText mGraphicalCodeEdt;

    private EditText mPhoneCodeEdt;

    private ValidationCode mValidationView;

    private TextView mGetPhoneCodeTv;

    private TextView mSubmitTv;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentLayout(R.layout.activity_register);
        initViews();
    }

    private void initViews() {
        mAccountEdt = (EditText) findViewById(R.id.register_account_edt);
        mAgentEdt = (EditText) findViewById(R.id.register_agent_edt);
        mNicknameEdt = (EditText) findViewById(R.id.register_nickname_edt);
        mPwdEdt = (EditText) findViewById(R.id.register_pwd_edt);
        mPwdAgainEdt = (EditText) findViewById(R.id.register_pwd_again_edt);
        mGraphicalCodeEdt = (EditText) findViewById(R.id.register_graphical_verification_code_edt);
        mValidationView = (ValidationCode) findViewById(R.id.register_graphical_verification_code_view);
        mPhoneCodeEdt = (EditText) findViewById(R.id.register_verification_code_edt);
        mGetPhoneCodeTv = (TextView) findViewById(R.id.register_get_verification_code_tv);
        mSubmitTv = (TextView) findViewById(R.id.register_submit_tv);

        mGetPhoneCodeTv.setOnClickListener(this);
        mSubmitTv.setOnClickListener(this);
        mAccountEdt.setText("13539881187");
        mPwdEdt.setText("123456");
        mAgentEdt.setText("1");
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.register_get_verification_code_tv:
                getVerificationCode();
                break;
            case R.id.register_submit_tv:
                submit();
                break;
            default:
                break;
        }
    }

    private void getVerificationCode() {
        showProgressDialog();
        String phoneNum = mAccountEdt.getText().toString().trim();
        UserApi userApi = RetrofitUtils.createApi(UserApi.class);
        final Call<CommonResponse> respo = userApi.versificationCode(getVersificationCodeParams(phoneNum));
        respo.enqueue(new Callback<CommonResponse>() {
            @Override
            public void onResponse(Call<CommonResponse> call, Response<CommonResponse> response) {
                Log.i("zyu", response.body().toString());
                dismissProgressDialog();
            }

            @Override
            public void onFailure(Call<CommonResponse> call, Throwable t) {
                dismissProgressDialog();
                if (t != null && t.getMessage() != null) {
                    Log.i("zyu", t.getMessage());
                }
            }
        });
    }

    private Map<String, String> getVersificationCodeParams(String phoneNum) {
        final Map<String, String> params = ParamsUtil.getCommonParams();
        params.put("method", "gdiex.sms.send");
        params.put("telNumber", phoneNum);
        params.put("reason", "REGISTER");
        params.put("sign", ParamsUtil.sign(params));
        return params;
    }

    private void submit() {
        final String account = mAccountEdt.getText().toString().trim();
        final String agentId = mAgentEdt.getText().toString().trim();
        final String nickname = mNicknameEdt.getText().toString().trim();
        final String pwd = mPwdEdt.getText().toString().trim();
        final String pwdAgain = mPwdAgainEdt.getText().toString().trim();
        final String mGraphicalCode = mGraphicalCodeEdt.getText().toString().trim();
        final String mPhoneCode = mPhoneCodeEdt.getText().toString().trim();

        UserApi userApi = RetrofitUtils.createApi(UserApi.class);
        final Call<CommonResponse> respo = userApi.register(getRegisterParams(account, pwd, agentId));
        respo.enqueue(new Callback<CommonResponse>() {
            @Override
            public void onResponse(Call<CommonResponse> call, Response<CommonResponse> response) {
                CommonResponse entity = response.body();
                if (entity != null) {
                    showToastShort(entity.getMessage());
                }
            }

            @Override
            public void onFailure(Call<CommonResponse> call, Throwable t) {
                if (t != null && t.getMessage() != null) {
                    LogZ.e(t.getMessage());
                }
            }
        });
    }



    private Map<String, String> getRegisterParams(String phoneNum, String password, String agentId) {
        final Map<String, String> params = ParamsUtil.getCommonParams();
        params.put("method", "gdiex.users.register");
        params.put("telNumber", phoneNum);
        params.put("password", password);
        params.put("agentId", agentId);
        params.put("registerIp", "");
        params.put("sign", ParamsUtil.sign(params));
        return params;
    }
}
