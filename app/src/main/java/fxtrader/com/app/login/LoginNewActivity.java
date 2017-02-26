package fxtrader.com.app.login;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Map;

import fxtrader.com.app.R;
import fxtrader.com.app.base.BaseActivity;
import fxtrader.com.app.config.LoginConfig;
import fxtrader.com.app.constant.IntentItem;
import fxtrader.com.app.db.helper.TicketsHelper;
import fxtrader.com.app.db.helper.UserCouponsHelper;
import fxtrader.com.app.db.helper.UserInfoHelper;
import fxtrader.com.app.entity.CouponListEntity;
import fxtrader.com.app.entity.LoginResponseEntity;
import fxtrader.com.app.entity.TicketListEntity;
import fxtrader.com.app.entity.UserEntity;
import fxtrader.com.app.http.ParamsUtil;
import fxtrader.com.app.http.RetrofitUtils;
import fxtrader.com.app.http.api.UserApi;
import fxtrader.com.app.http.manager.LoginManager;
import fxtrader.com.app.http.manager.UserInfoManager;
import fxtrader.com.app.tools.Base64;
import fxtrader.com.app.tools.EncryptionTool;
import fxtrader.com.app.tools.LogZ;
import fxtrader.com.app.tools.UIUtil;
import fxtrader.com.app.view.ctr.BannerController;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * 登录
 * Created by pc on 2016/11/17.
 */
public class LoginNewActivity extends BaseActivity implements View.OnClickListener {

    private BannerController mBannerController;

    private EditText mAccountEdt;

    private EditText mPwdEdt;

    private TextView mRegisterTipTv;

    private int mRequest = 0;

    private boolean mFromMine;

    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            mRequest--;
            LogZ.i("mRequest = " + mRequest);
            if (mRequest == 0) {
                if (mFromMine) {
                    Intent intent=new Intent();
                    intent.setAction(IntentItem.ACTION_LOGIN);
                    sendBroadcast(intent);
                }
                setResult(RESULT_OK);
                finish();
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentLayout(R.layout.activity_login_new);
        mFromMine = getIntent().getBooleanExtra(IntentItem.MINE, false);
        initViews();
        setView();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (RESULT_OK != resultCode) {
            return;
        }
        if (requestCode == IntentItem.REQUEST_REGISTER){
            setResult(RESULT_OK);
            finish();
        }
    }


    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
    }

    private void initViews() {
        initBannerView();
        mAccountEdt = (EditText) findViewById(R.id.login_account_edt);
        mPwdEdt = (EditText) findViewById(R.id.login_pwd_edt);
        mRegisterTipTv = (TextView) findViewById(R.id.login_register_tv);
        findViewById(R.id.login_btn).setOnClickListener(this);
        findViewById(R.id.login_pwd_forgot_tv).setOnClickListener(this);
        mRegisterTipTv.setOnClickListener(this);

        String account = LoginConfig.getInstance().getAccount();
        if (!TextUtils.isEmpty(account)) {
            mAccountEdt.setText(account);
        }

        String pwd = LoginConfig.getInstance().getPwd();
        if (!TextUtils.isEmpty(pwd)) {
            mPwdEdt.setText(pwd);
        }
    }

    private void initBannerView(){
        View bannerView = findViewById(R.id.banner_layout);
        mBannerController = new BannerController(this, bannerView);
        mBannerController.init();
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
                findPwd();
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
            mRequest++;
            LoginManager.instance(this).login(account, pwd, new LoginManager.LoginListener() {
                @Override
                public void success() {
                    getTickets();
                    getUserInfo();
                    mHandler.sendEmptyMessage(0);
                }

                @Override
                public void error(String error) {
                    dismissProgressDialog();
                    mRequest--;
                    showToastShort(error);
                }
            });
        } catch (Exception e) {
            LogZ.e(e);
        }

    }

    private void getUserInfo(){
        mRequest++;
        UserInfoManager.getInstance().get(new UserInfoManager.UserInfoListener() {
            @Override
            public void onSuccess(UserEntity user) {
                UserInfoHelper.getInstance().save(user);
                UserEntity.ObjectBean bean = user.getObject();
                if (bean != null) {
                    LoginConfig.getInstance().saveInfo("" + bean.getId(), bean.getTelNumber(), bean.getOrganId());
                }
                mHandler.sendEmptyMessage(0);
            }

            @Override
            public void onHttpFailure() {
                mHandler.sendEmptyMessage(0);
            }
        });
    }

    private void findPwd() {
        openActivity(FindPwdActivity.class);
    }

    private void register() {
        Intent intent = new Intent(this, LoginNewActivity.class);
        startActivityForResult(intent, IntentItem.REQUEST_REGISTER);
    }

    private void getTickets() {
        UserApi userApi = RetrofitUtils.createApi(UserApi.class);
        Call<TicketListEntity> repos = userApi.tickets(getTicketsParams());
        mRequest++;
        repos.enqueue(new Callback<TicketListEntity>() {
            @Override
            public void onResponse(Call<TicketListEntity> call, Response<TicketListEntity> response) {
                TicketListEntity entity = response.body();
                TicketsHelper.getInstance().save(entity);
                getUserCoupon();
                mHandler.sendEmptyMessage(0);
            }

            @Override
            public void onFailure(Call<TicketListEntity> call, Throwable t) {
                Log.e("zyu", t.getMessage());
                mHandler.sendEmptyMessage(0);
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
        mRequest++;
        repos.enqueue(new Callback<CouponListEntity>() {
            @Override
            public void onResponse(Call<CouponListEntity> call, Response<CouponListEntity> response) {
                CouponListEntity entity = response.body();
                UserCouponsHelper.getInstance().save(entity);
                mHandler.sendEmptyMessage(0);
            }

            @Override
            public void onFailure(Call<CouponListEntity> call, Throwable t) {
                Log.e("zyu", t.getMessage());
                mHandler.sendEmptyMessage(0);
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
