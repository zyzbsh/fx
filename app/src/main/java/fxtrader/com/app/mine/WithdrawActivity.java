package fxtrader.com.app.mine;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import fxtrader.com.app.R;
import fxtrader.com.app.base.BaseActivity;
import fxtrader.com.app.constant.IntentItem;
import fxtrader.com.app.entity.BankBindEntity;
import fxtrader.com.app.entity.UserEntity;
import fxtrader.com.app.entity.WithdrawError;
import fxtrader.com.app.http.ParamsUtil;
import fxtrader.com.app.http.RetrofitUtils;
import fxtrader.com.app.http.manager.UserInfoManager;
import fxtrader.com.app.http.api.UserApi;
import fxtrader.com.app.tools.LogZ;
import fxtrader.com.app.tools.ProvinceTool;
import fxtrader.com.app.view.WithdrawBankDialog;
import fxtrader.com.app.view.WithdrawCityDialog;
import fxtrader.com.app.view.WithdrawInputDialog;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * 提现
 * Created by zhangyuzhu on 2016/12/1.
 */
public class WithdrawActivity extends BaseActivity implements View.OnClickListener {
    private TextView mAccountTv;
    private EditText mInputEdt;
    private TextView mBankTv;
    private TextView mProvinceTv;
    private TextView mCityTv;
    private TextView mBranchTv;
    private TextView mCardTv;
    private TextView mNameTv;
    private WithdrawBankDialog mBankDialog;
    private WithdrawCityDialog mCityDialog;
    private WithdrawInputDialog mWithdrawInputDialog;
    private int mClickedId;
    private UserEntity mUserEntity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentLayout(R.layout.activity_withdraw);
        initViews();
        getUserInfo();
    }

    private void initViews() {
        mAccountTv = (TextView) findViewById(R.id.withdraw_account_tv);
        mInputEdt = (EditText) findViewById(R.id.withdraw_input_edt);
        mBankTv = (TextView) findViewById(R.id.withdraw_bank_tv);
        mProvinceTv = (TextView) findViewById(R.id.withdraw_province_tv);
        mCityTv = (TextView) findViewById(R.id.withdraw_city_tv);
        mBranchTv = (TextView) findViewById(R.id.withdraw_branch_tv);
        mCardTv = (TextView) findViewById(R.id.withdraw_card_tv);
        mNameTv = (TextView) findViewById(R.id.withdraw_name_tv);

        findViewById(R.id.withdraw_bank_layout).setOnClickListener(this);
        findViewById(R.id.withdraw_province_layout).setOnClickListener(this);
        findViewById(R.id.withdraw_city_layout).setOnClickListener(this);
        findViewById(R.id.withdraw_branch_layout).setOnClickListener(this);
        findViewById(R.id.withdraw_card_layout).setOnClickListener(this);
        findViewById(R.id.withdraw_name_layout).setOnClickListener(this);

        findViewById(R.id.withdraw_tv).setOnClickListener(this);
    }

    private void getUserInfo(){
        mUserEntity = (UserEntity) getIntent().getSerializableExtra(IntentItem.USER_INFO);
        if (mUserEntity != null) {
            setUserView();
        } else {
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
    }

    private void setUserView() {
        mAccountTv.setText(getString(R.string.withdraw_can_num, mUserEntity.getObject().getFunds()));
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        switch (id) {
            case R.id.withdraw_bank_layout:
                showBankDialog("选择所属银行", getBankInfo(), id);
                break;
            case R.id.withdraw_province_layout:
                showCityDialog("选择所属省份", getProvince(), id);
                break;
            case R.id.withdraw_city_layout:
                String province = mProvinceTv.getText().toString().trim();
                if (TextUtils.isEmpty(province)) {
                    showToastShort("请选择省份");
                    return;
                }
                showCityDialog("选择所属城市", getCity(), id);
                break;
            case R.id.withdraw_branch_layout:
                showInputDialog("银行支行", id);
                break;
            case R.id.withdraw_card_layout:
                showInputDialog("银行卡号", id);
                break;
            case R.id.withdraw_name_layout:
                showInputDialog("持卡人姓名", id);
                break;
            case R.id.withdraw_tv:
                withdraw();
                break;
            default:
                break;
        }
    }

    private void showBankDialog(String title, List<String> data, int id) {
        mClickedId = id;
        mBankDialog = new WithdrawBankDialog(this, title, data, new WithdrawBankDialog.OnItemListener() {
            @Override
            public void onClicked(String name) {
                setTv(name, mClickedId);
            }
        });
        mBankDialog.show();
    }

    private void showCityDialog(String title, String[] data, int id) {
        mClickedId = id;
        mCityDialog = new WithdrawCityDialog(this, title, data, new WithdrawCityDialog.OnItemListener() {
            @Override
            public void onClicked(String name) {
                setTv(name, mClickedId);
            }
        });
        mCityDialog.show();
    }

    private void showInputDialog(String title, int id) {
        mClickedId = id;
        mWithdrawInputDialog = new WithdrawInputDialog(this, title, new WithdrawInputDialog.OnOkListener() {
            @Override
            public void onConfirm(String name) {
                setTv(name, mClickedId);
            }
        });
        mWithdrawInputDialog.show();
    }

    private List<String> getBankInfo() {
        String[] arrays = getResources().getStringArray(R.array.banks);
        List<String> list = new ArrayList<>();
        for (String bank : arrays) {
            list.add(bank);
        }
        return list;
    }

    private String[] getProvince() {
        return ProvinceTool.getProvince();
    }

    private String[] getCity() {
        String province = mProvinceTv.getText().toString().trim();
        return ProvinceTool.getCitise(province);
    }

    private void setTv(String name, int id) {
        LogZ.i(name);
        switch (id) {
            case R.id.withdraw_bank_layout:
                mBankTv.setText(name);
                break;
            case R.id.withdraw_province_layout:
                mProvinceTv.setText(name);
                break;
            case R.id.withdraw_city_layout:
                mCityTv.setText(name);
                break;
            case R.id.withdraw_branch_layout:
                mBranchTv.setText(name);
                break;
            case R.id.withdraw_card_layout:
                mCardTv.setText(name);
                break;
            case R.id.withdraw_name_layout:
                mNameTv.setText(name);
                break;
            default:
                break;
        }
    }

    private void withdraw(){
        final String amount = mInputEdt.getText().toString().trim();
        if (TextUtils.isEmpty(amount)) {
            return;
        }
        try {
            double d = Double.parseDouble(amount);
            if (d < 10) {
                showToastShort("请输入10金币以上金额");
                return;
            }
        } catch (Exception e) {
            showToastShort("请输入具体金额");
            return;
        }
//        openActivity(RechargeWebActivity.class);
        showProgressDialog();
        UserApi userApi = RetrofitUtils.createTestApi(UserApi.class);
        String token = ParamsUtil.getToken();
        final Call<ResponseBody> respo = userApi.withdraw(token, getRechargerParams(amount));
        respo.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                dismissProgressDialog();
                try {
                    String str = new String(response.body().bytes());
                    if (isJson(str)) {
                        Gson gson = new Gson();
                        WithdrawError error = gson.fromJson(str, WithdrawError.class);
                        showToastShort(error.getMessage());
                    } else {
                        Intent intent = new Intent(WithdrawActivity.this, WebHtmlActivity.class);
                        intent.putExtra(IntentItem.RECHARGE_HTML, str);
                        startActivity(intent);
                    }
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
        params.put("method", "gdiex.currency.withdrawal");
        params.put("amount", amount);
        params.put("blank", getText(mBankTv));
        params.put("province", getText(mProvinceTv));
        params.put("city", getText(mCityTv));
        params.put("blankBranch", getText(mBranchTv));
        params.put("blankCardNum", getText(mCardTv));
        params.put("blankUseName", getText(mNameTv));
        params.put("redirectUri", "http://example.com/withdrawalCallBack");
        params.put("sign", ParamsUtil.sign(params));
        return params;
    }

    private String getText(TextView tv) {
        return tv.getText().toString().trim();
    }

    private boolean isJson(String result){
        boolean json = false;
        try {
            JSONObject oject = new JSONObject(result);
            json = true;
        } catch (JSONException e) {
            json = false;
            e.printStackTrace();
        }
        return json;
    }



}
