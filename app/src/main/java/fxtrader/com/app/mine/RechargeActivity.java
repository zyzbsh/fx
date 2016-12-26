package fxtrader.com.app.mine;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.io.IOException;
import java.util.Map;

import fxtrader.com.app.R;
import fxtrader.com.app.base.BaseActivity;
import fxtrader.com.app.constant.IntentItem;
import fxtrader.com.app.entity.UserEntity;
import fxtrader.com.app.http.ParamsUtil;
import fxtrader.com.app.http.RetrofitUtils;
import fxtrader.com.app.http.manager.UserInfoManager;
import fxtrader.com.app.http.api.UserApi;
import fxtrader.com.app.tools.ContractUtil;
import fxtrader.com.app.tools.LogZ;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by zhangyuzhu on 2016/12/10.
 */
public class RechargeActivity extends BaseActivity implements View.OnClickListener{

    private ImageView mAvatarIm;

    private TextView mNameTv;

    private TextView mBalanceTv;

    private TextView mCashCouponTv;

    private TextView mReceiveTv;

    private EditText mRechargeEdt;

    private String[] mMoneyNum = {"100", "500", "1000", "2000", "3000", "5000"};

    private int[] mMoneyTvIds = {R.id.recharge_100_tv, R.id.recharge_500_tv, R.id.recharge_1000_tv, R.id.recharge_2000_tv, R.id.recharge_3000_tv, R.id.recharge_5000_tv};

    private TextView[] mMoneyTvs = new TextView[mMoneyTvIds.length];

    private UserEntity mUserEntity;

    private double mRechargeAcount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentLayout(R.layout.activity_recharge);
        initViews();
        getUserInfo();
    }

    private void initViews() {
        initUserLayout();
        initMoneyLayout();
        initPayLayout();
    }

    private void initUserLayout() {
        mAvatarIm = (ImageView) findViewById(R.id.recharge_person_avatar_im);
        mNameTv = (TextView) findViewById(R.id.recharge_name_tv);
        mBalanceTv = (TextView) findViewById(R.id.recharge_account_balance_tv);
        mCashCouponTv = (TextView) findViewById(R.id.recharge_cash_coupon_count_tv);
        mReceiveTv = (TextView) findViewById(R.id.recharge_receive_tv);
        mRechargeEdt = (EditText) findViewById(R.id.recharge_edt);
    }

    private void initPayLayout() {
        findViewById(R.id.recharge_wx_layout).setOnClickListener(this);
        findViewById(R.id.recharge_yh_layout).setOnClickListener(this);
    }

    private void initMoneyLayout() {
        OnMoneyClickedListener listener = new OnMoneyClickedListener();
        for(int i = 0; i < mMoneyTvIds.length; i++){
            mMoneyTvs[i] = (TextView) findViewById(mMoneyTvIds[i]);
            mMoneyTvs[i].setOnClickListener(listener);
        }
    }

    private class OnMoneyClickedListener implements View.OnClickListener{
        @Override
        public void onClick(View v) {
            for (int i = 0; i < mMoneyTvIds.length; i++) {
                if (v.getId() == mMoneyTvIds[i]) {
                    mRechargeEdt.setText(mMoneyNum[i]);
                    mMoneyTvs[i].setBackgroundResource(R.drawable.shape_red_corner_3);
                    mMoneyTvs[i].setTextColor(Color.WHITE);
                } else {
                    mMoneyTvs[i].setBackgroundResource(R.drawable.shape_white_corner_3);
                    mMoneyTvs[i].setTextColor(getColor(R.color.red_text));
                }
            }
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_OK) {
            return;
        }

        if (requestCode == IntentItem.REQUEST_RECHARGE) {
            showProgressDialog();
            UserInfoManager.getInstance().get(new UserInfoManager.UserInfoListener() {
                @Override
                public void onSuccess(UserEntity user) {
                    double found = user.getObject().getFunds();
                    double diff = found - mUserEntity.getObject().getFunds() - mRechargeAcount;
                    if (diff > -10E-6 && diff < 10E-6) {
                        double d = ContractUtil.getDouble(mRechargeAcount, 2);
                        mReceiveTv.setText(String.valueOf(d));
                    }
                    dismissProgressDialog();
                }

                @Override
                public void onHttpFailure() {
                    dismissProgressDialog();
                }
            });
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.recharge_wx_layout:
                //TODO 微信支付
                break;
            case R.id.recharge_yh_layout:
                bankRecharge();
                break;
            default:
                break;
        }

    }

    private void getUserInfo(){
        mUserEntity = (UserEntity) getIntent().getSerializableExtra(IntentItem.USER_INFO);
        if (mUserEntity != null) {
            setUserView();
        } else {
            requestUserInfo();
        }
    }

    private void requestUserInfo(){
        showProgressDialog();
        UserInfoManager.getInstance().get(new UserInfoManager.UserInfoListener() {
            @Override
            public void onSuccess(UserEntity user) {
                mUserEntity = user;
                setUserView();
                dismissProgressDialog();
            }

            @Override
            public void onHttpFailure() {
                dismissProgressDialog();
            }
        });
    }

    private void setUserView(){
        Glide.with(this).load(mUserEntity.getObject().getHeadimgurl()).into(mAvatarIm);
        mNameTv.setText(mUserEntity.getObject().getNickname());
        mBalanceTv.setText(getString(R.string.balance_num, String.valueOf(mUserEntity.getObject().getFunds())));
        mCashCouponTv.setText(String.valueOf(mUserEntity.getObject().getCouponAmount()));
    }

    private void bankRecharge(){
        String acount = mRechargeEdt.getText().toString().trim();
        if (TextUtils.isEmpty(acount)) {
            return;
        }
        try {
            double d = Double.parseDouble(acount);
            if (d < 20 || d > 5000) {
                showToastShort("请输入20元以上，5000元以下的金额");
                return;
            }
            mRechargeAcount = d;
        } catch (Exception e) {
            showToastShort("请输入具体金额");
            return;
        }
//        openActivity(RechargeWebActivity.class);
        showProgressDialog();
        UserApi userApi = RetrofitUtils.createTestApi(UserApi.class);
        String token = ParamsUtil.getToken();
        final Call<ResponseBody> respo = userApi.bankRecharge(token, getRechargerParams(acount));
        respo.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                dismissProgressDialog();
                try {
                    String str = new String(response.body().bytes());
                    Intent intent = new Intent(RechargeActivity.this, WebHtmlActivity.class);
                    intent.putExtra(IntentItem.RECHARGE_HTML, str);
                    startActivityForResult(intent, IntentItem.REQUEST_RECHARGE);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                LogZ.d(response.body().source().toString());
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                dismissProgressDialog();
                LogZ.d(t.toString());
            }
        });
    }

    private Map<String, String> getRechargerParams(String amount) {
        final Map<String, String> params = ParamsUtil.getCommonParams();
        params.put("method", "gdiex.currency.unionpay");
        params.put("amount", amount);
        params.put("sign", ParamsUtil.sign(params));
        return params;
    }

}
