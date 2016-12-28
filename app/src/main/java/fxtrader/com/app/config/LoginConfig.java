package fxtrader.com.app.config;

import android.content.Context;
import android.content.SharedPreferences;
import android.provider.Settings;
import android.text.TextUtils;

import fxtrader.com.app.AppApplication;
import fxtrader.com.app.http.HttpConstant;

/**
 * Created by zhangyuzhu on 2016/12/6.
 */
public final class LoginConfig {

    public static final String ACCOUNT = "account";
    public static final String TOKEN = "token";
    public static final String TIME = "time";
    public static final String ID = "id";
    public static final String TEL_NUMBER = "tel_number";
    public static final String ORGAN_ID = "organ_id";

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
        editor.putString(TIME, String.valueOf(System.currentTimeMillis()));
        editor.commit();
    }

    public void saveInfo(String id, String telNumber, int organId, int customerId) {
        SharedPreferences.Editor editor = mSp.edit();
        editor.putString(id, id);
        editor.putString(telNumber, telNumber);
        editor.putInt(ORGAN_ID, organId);
        editor.commit();
    }

    public String getId() {
        return mSp.getString(ID, "");
    }

    public String getTelNumber() {
        return mSp.getString(TEL_NUMBER, "");
    }

    public int getOrganId(){
        return  mSp.getInt(ORGAN_ID, HttpConstant.DEFAULT_ORGAN_ID);
    }

    public String getAccount(){
        return mSp.getString(ACCOUNT, "");
    }

    public String getToken() {
        return mSp.getString(TOKEN, "");
    }

    public boolean isLogin() {
        long curTime = System.currentTimeMillis();
        String recordTime = mSp.getString(TIME, "");
        if (TextUtils.isEmpty(recordTime)) {
            return false;
        }
        long time = Long.parseLong(recordTime);
        if (curTime - time < 50 * 60 * 1000) {
            String token = getToken();
            return !TextUtils.isEmpty(token);
        } else {
            return false;
        }

    }


}
