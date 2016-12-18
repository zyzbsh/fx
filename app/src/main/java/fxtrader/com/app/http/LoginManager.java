package fxtrader.com.app.http;

import android.content.Context;

/**
 * Created by pc on 2016/12/18.
 */
public class LoginManager {

    private static LoginManager sLoginManager;

    private Context mContext;

    private LoginManager(Context context) {
        mContext = context;
    }

    public static LoginManager instance(Context context) {
        if (sLoginManager == null) {
            sLoginManager = new LoginManager(context);
        }
        return sLoginManager;
    }

    public void login() {

    }
}
