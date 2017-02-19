package fxtrader.com.app.http.manager;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import java.util.Map;

import fxtrader.com.app.config.LoginConfig;
import fxtrader.com.app.entity.LoginResponseEntity;
import fxtrader.com.app.http.ParamsUtil;
import fxtrader.com.app.http.RetrofitUtils;
import fxtrader.com.app.http.api.UserApi;
import fxtrader.com.app.tools.Base64;
import fxtrader.com.app.tools.EncryptionTool;
import fxtrader.com.app.tools.LogZ;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

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

    public void login(final String account, final String pwd, final LoginListener listener) throws Exception{
        if (listener == null) {
            throw new IllegalArgumentException("loginListener is null");
        }
        UserApi userApi = RetrofitUtils.createApi(UserApi.class);
        Call<LoginResponseEntity> request = userApi.login(getParams(account, pwd));
        request.enqueue(new Callback<LoginResponseEntity>() {
            @Override
            public void onResponse(Call<LoginResponseEntity> call, Response<LoginResponseEntity> response) {
                LoginResponseEntity entity = response.body();
                String token = entity.getAccess_token();
                if (TextUtils.isEmpty(token)) {
                    listener.error("登录失败，账户或密码错误");
                } else {
                    LoginConfig.getInstance().saveUser(account, pwd, entity.getAccess_token());
                    listener.success();
                }

            }

            @Override
            public void onFailure(Call<LoginResponseEntity> call, Throwable t) {
                if (t != null){
                    LogZ.e(t.getMessage());
                    listener.error("请求失败，请重试");
                }
            }
        });
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

    public interface LoginListener{
        public void success();
        public void error(String error);
    }
}
