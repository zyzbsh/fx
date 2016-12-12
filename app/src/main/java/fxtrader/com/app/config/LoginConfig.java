package fxtrader.com.app.config;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;

import fxtrader.com.app.AppApplication;

/**
 * Created by zhangyuzhu on 2016/12/6.
 */
public final class LoginConfig {

    public static final String ACCOUNT = "account";
    public static final String TOKEN = "token";

    private static LoginConfig sConfig;
    private SharedPreferences mSp;

    private LoginConfig() {
        mSp = AppApplication.getInstance().getBaseContext().getSharedPreferences("user", Context.MODE_PRIVATE);
    }

    public static LoginConfig getInstance() {
        if (sConfig == null) {
            sConfig = new LoginConfig();
        }
        return sConfig;
    }

    public void saveUser(String account, String token) {
        SharedPreferences.Editor editor = mSp.edit();
        editor.putString(ACCOUNT, account);
        editor.putString(TOKEN, token);
        editor.commit();
    }

    public String getAccount(){
        return mSp.getString(ACCOUNT, "");
    }

    public String getToken() {
        return mSp.getString(TOKEN, "");
    }

    public boolean isLogin() {
        String token = getToken();
        return !TextUtils.isEmpty(token);
    }


}
