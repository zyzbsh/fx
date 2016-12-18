package fxtrader.com.app.login;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Map;

import fxtrader.com.app.R;
import fxtrader.com.app.base.BaseActivity;
import fxtrader.com.app.config.LoginConfig;
import fxtrader.com.app.constant.IntentItem;
import fxtrader.com.app.entity.CouponListEntity;
import fxtrader.com.app.entity.LoginResponseEntity;
import fxtrader.com.app.entity.TicketListEntity;
import fxtrader.com.app.http.ParamsUtil;
import fxtrader.com.app.http.RetrofitUtils;
import fxtrader.com.app.http.api.UserApi;
import fxtrader.com.app.tools.Base64;
import fxtrader.com.app.tools.EncryptionTool;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * 登录
 * Created by pc on 2016/11/17.
 */
public class LoginActivity extends BaseActivity implements View.OnClickListener {

    private EditText mAccountEdt;

    private EditText mPwdEdt;

    private TextView mRegisterTipTv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initViews();
        setView();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
    }

    private void initViews() {
        mAccountEdt = (EditText) findViewById(R.id.login_account_edt);
        mPwdEdt = (EditText) findViewById(R.id.login_pwd_edt);
        mRegisterTipTv = (TextView) findViewById(R.id.login_register_tv);
        findViewById(R.id.login_btn).setOnClickListener(this);
        findViewById(R.id.login_pwd_forgot_tv).setOnClickListener(this);
        mRegisterTipTv.setOnClickListener(this);

        mAccountEdt.setText("13668902696");
        mPwdEdt.setText("123456");
    }

    private void setView() {
        boolean login = getIntent().getBooleanExtra(IntentItem.LOGIN, false);
        if (login) {
            mAccountEdt.setVisibility(View.GONE);
            mRegisterTipTv.setVisibility(View.GONE);
        }
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.login_btn:
                login();
                break;
            case R.id.login_pwd_forgot_tv:
                forgetPwd();
                break;
            case R.id.login_register_tv:
                register();
                break;
            default:
                break;
        }
    }

    private void login() {
        final String account = mAccountEdt.getText().toString().trim();
        if (TextUtils.isEmpty(account)) {
            Toast.makeText(this, R.string.remind_login_account, Toast.LENGTH_SHORT).show();
            return;
        }

        final String pwd = mPwdEdt.getText().toString().trim();
        if (TextUtils.isEmpty(pwd)) {
            Toast.makeText(this, R.string.remind_login_pwd, Toast.LENGTH_SHORT).show();
            return;
        }
        try {
            UserApi userApi = RetrofitUtils.createApi(UserApi.class);
            Call<LoginResponseEntity> repos = userApi.login(getParams(account, pwd));
            repos.enqueue(new Callback<LoginResponseEntity>() {
                @Override
                public void onResponse(Call<LoginResponseEntity> call, Response<LoginResponseEntity> response) {
                    LoginResponseEntity entity = response.body();
                    LoginConfig.getInstance().saveUser(account, entity.getAccess_token());
                    getTickets();
                }

                @Override
                public void onFailure(Call<LoginResponseEntity> call, Throwable t) {
                    Log.e("zyu", t.getMessage());
                }
            });
        } catch (Exception e) {

        }

    }

    private void forgetPwd() {
        //TODO
    }

    private void register() {
        openActivity(RegisterActivity.class);
    }

    private Map<String, String> getParams(String account, String pwd) throws Exception {
        final Map<String, String> params = ParamsUtil.getCommonParams();
        params.put("method", "gdiex.oauth.token");
        params.put("grant_type", "password");
        String str = account + ":" + pwd;
        byte[] base64 = Base64.encode(str.getBytes(), Base64.NO_WRAP);
        byte[] aes = EncryptionTool.aes(base64, ParamsUtil.CLIENT_SECRET.getBytes());
        params.put("subject", new String(Base64.encode(aes, Base64.NO_WRAP)));
        params.put("sign", ParamsUtil.sign(params));
        return params;
    }

    private void getTickets() {
        UserApi userApi = RetrofitUtils.createApi(UserApi.class);
        Call<TicketListEntity> repos = userApi.tickets(getTicketsParams());
        repos.enqueue(new Callback<TicketListEntity>() {
            @Override
            public void onResponse(Call<TicketListEntity> call, Response<TicketListEntity> response) {
                TicketListEntity entity = response.body();
                getUserCoupon();
            }

            @Override
            public void onFailure(Call<TicketListEntity> call, Throwable t) {
                Log.e("zyu", t.getMessage());
            }
        });
    }

    private Map<String, String> getTicketsParams(){
        final Map<String, String> params = ParamsUtil.getCommonParams();
        params.put("method", "gdiex.tickets.list");
        params.put("sign", ParamsUtil.sign(params));
        return params;
    }

    private void getUserCoupon() {
        UserApi userApi = RetrofitUtils.createApi(UserApi.class);
        String token = ParamsUtil.getToken();
        Call<CouponListEntity> repos = userApi.coupons(token, getCouponParams());
        repos.enqueue(new Callback<CouponListEntity>() {
            @Override
            public void onResponse(Call<CouponListEntity> call, Response<CouponListEntity> response) {
                CouponListEntity entity = response.body();
                setResult(RESULT_OK);
                finish();
            }

            @Override
            public void onFailure(Call<CouponListEntity> call, Throwable t) {
                Log.e("zyu", t.getMessage());
            }
        });
    }

    private Map<String, String> getCouponParams(){
        final Map<String, String> params = ParamsUtil.getCommonParams();
        params.put("method", "gdiex.coupon.users");
        params.put("sign", ParamsUtil.sign(params));
        return params;
    }

}
