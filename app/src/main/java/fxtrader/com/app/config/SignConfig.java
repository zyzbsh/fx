package fxtrader.com.app.config;

import android.content.Context;
import android.content.SharedPreferences;

import fxtrader.com.app.AppApplication;
import fxtrader.com.app.tools.DateTools;
import fxtrader.com.app.tools.LogZ;

/**
 * Created by pc on 2016/12/24.
 */
public class SignConfig {
    public static final String SIGN = "sign";

    private static SignConfig sSignConfig;

    private static SharedPreferences sSp;


    private SignConfig(){
        sSp = AppApplication.getInstance().getBaseContext().getSharedPreferences("sign_record", Context.MODE_PRIVATE);
        if (sSp == null) {
            LogZ.i("null");
        }
    }

    public static SignConfig getInstance(){
        if (sSignConfig == null) {
            sSignConfig = new SignConfig();
        }
        return sSignConfig;
    }

    public static void save(){
        SharedPreferences.Editor editor = sSp.edit();
        editor.clear();
        editor.commit();
        editor.putBoolean(DateTools.getCurrentData(), true);
        editor.commit();
    }

    public static boolean isSigned() {
        return sSp.getBoolean(DateTools.getCurrentData(), false);
    }

}
