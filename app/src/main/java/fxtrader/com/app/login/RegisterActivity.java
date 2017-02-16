package fxtrader.com.app.login;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.igexin.sdk.PushManager;

import java.util.Map;

import fxtrader.com.app.R;
import fxtrader.com.app.base.BaseActivity;
import fxtrader.com.app.config.LoginConfig;
import fxtrader.com.app.constant.IntentItem;
import fxtrader.com.app.entity.CommonResponse;
import fxtrader.com.app.entity.LoginResponseEntity;
import fxtrader.com.app.http.HttpConstant;
import fxtrader.com.app.http.ParamsUtil;
import fxtrader.com.app.http.RetrofitUtils;
import fxtrader.com.app.http.api.UserApi;
import fxtrader.com.app.tools.Base64;
import fxtrader.com.app.tools.EncryptionTool;
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

        if (!pwdAgain.equals(pwd)) {
            showToastShort("请再次输入密码");
            return;
        }

        UserApi userApi = RetrofitUtils.createApi(UserApi.class);
        final Call<CommonResponse> respo = userApi.register(getRegisterParams(account, pwd, agentId));
        respo.enqueue(new Callback<CommonResponse>() {
            @Override
            public void onResponse(Call<CommonResponse> call, Response<CommonResponse> response) {
                CommonResponse entity = response.body();
                if (entity != null) {
                    if (entity.isSuccess()) {
                        login(account, pwd);
                    } else {
                        showToastShort(entity.getMessage());
                    }
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

    private void update(){
        String nickname = mNicknameEdt.getText().toString().trim();
        if (TextUtils.isEmpty(nickname)) {
            showToastShort("请输入昵称");
            return;
        }
        showProgressDialog();
        UserApi userApi = RetrofitUtils.createApi(UserApi.class);
        String token = ParamsUtil.getToken();
        Call<CommonResponse> request = userApi.updateInfo(token, getParams());
        request.enqueue(new Callback<CommonResponse>() {
            @Override
            public void onResponse(Call<CommonResponse> call, Response<CommonResponse> response) {
                dismissProgressDialog();
                CommonResponse entity = response.body();
                showToastShort("登录成功");
                setResult(RESULT_OK);
                finish();
            }

            @Override
            public void onFailure(Call<CommonResponse> call, Throwable t) {
                dismissProgressDialog();
                showToastShort("修改昵称失败");
            }
        });
    }

    private Map<String, String> getParams(){
        final Map<String, String> params = ParamsUtil.getCommonParams();
        params.put("method", "gdiex.users.updateUserInfo");
//        if (TextUtils.isEmpty(mUploadAvatarUrl)) {
//            mUploadAvatarUrl = mUser.getObject().getHeadimgurl();
//        }
//        params.put("headimgurl", mUploadAvatarUrl);
//        String nickname = mNicknameEdt.getText().toString().trim();
//        params.put("nickname", mNicknameEdt.getText().toString().trim());
//        int sex;
//        if ("男".equals(mSexChecked)) {
//            sex = HttpConstant.SexType.MALE;
//        } else {
//            sex = HttpConstant.SexType.FEMALE;
//        }
//        params.put("sex", sex + "");
//        params.put("sign", ParamsUtil.sign(params));
        return params;
    }

    private void login(final String account, final String pwd){
        try {
            UserApi userApi = RetrofitUtils.createApi(UserApi.class);
            Call<LoginResponseEntity> repos = userApi.login(getLoginParams(account, pwd));
            repos.enqueue(new Callback<LoginResponseEntity>() {
                @Override
                public void onResponse(Call<LoginResponseEntity> call, Response<LoginResponseEntity> response) {
                    LoginResponseEntity entity = response.body();
                    String token = entity.getAccess_token();
                    if (TextUtils.isEmpty(token)) {
                        dismissProgressDialog();
                        showToastShort("登录失败");
                        return;
                    } else {
                        LoginConfig.getInstance().saveUser(account, pwd, entity.getAccess_token());
                        PushManager.getInstance().initialize(getApplicationContext(), fxtrader.com.app.service.PushService.class);
                        PushManager.getInstance().registerPushIntentService(getApplicationContext(), fxtrader.com.app.service.PushIntentService.class);
                        update();
                    }

                }

                @Override
                public void onFailure(Call<LoginResponseEntity> call, Throwable t) {
                    if (t != null && t.getMessage() != null) {
                        Log.e("zyu", t.getMessage());
                    }
                }
            });
        } catch (Exception e) {
            LogZ.e(e.getMessage());
        }
    }

    private Map<String, String> getLoginParams(String account, String pwd) throws Exception {
        final Map<String, String> params = ParamsUtil.getCommonParams();
        params.put("method", "gdiex.oauth.token");
        params.put("grant_type", "password");
//        params.put("membercode", "RV");
        String str = account + ":" + pwd;
        byte[] base64 = Base64.encode(str.getBytes(), Base64.NO_WRAP);
        byte[] aes = EncryptionTool.aes(base64, ParamsUtil.CLIENT_SECRET.getBytes());
        params.put("subject", new String(Base64.encode(aes, Base64.NO_WRAP)));
        params.put("sign", ParamsUtil.sign(params));
        return params;
    }
}
