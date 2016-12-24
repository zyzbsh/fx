package fxtrader.com.app.config;

import android.content.Context;
import android.content.SharedPreferences;

import fxtrader.com.app.AppApplication;

/**
 * Created by pc on 2016/12/24.
 */
public class UserRecordConfig {
    public static final String GUIDE = "guided";

    private static UserRecordConfig sUserRecordConfig;

    private static SharedPreferences sSp;


    private UserRecordConfig(){
        sSp = AppApplication.getInstance().getBaseContext().getSharedPreferences("record", Context.MODE_PRIVATE);
    }

    public static UserRecordConfig getInstance(){
        if (sUserRecordConfig == null) {
            sUserRecordConfig = new UserRecordConfig();
        }
        return sUserRecordConfig;
    }

    public static void guided(){
        SharedPreferences.Editor editor = sSp.edit();
        editor.putBoolean(GUIDE, true);
        editor.commit();
    }

    public static boolean isGuided() {
        return sSp.getBoolean(GUIDE, false);
    }

}
