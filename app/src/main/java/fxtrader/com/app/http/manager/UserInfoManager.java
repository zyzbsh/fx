package fxtrader.com.app.http.manager;

import java.util.Map;

import fxtrader.com.app.entity.UserEntity;
import fxtrader.com.app.http.ParamsUtil;
import fxtrader.com.app.http.RetrofitUtils;
import fxtrader.com.app.http.api.UserApi;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by pc on 2016/12/20.
 */
public class UserInfoManager {

    private static UserInfoManager sManager;

    private UserInfoManager(){

    }

    public static UserInfoManager getInstance(){
        if(sManager == null) {
            sManager = new UserInfoManager();
        }
        return sManager;
    }

    public void get(final UserInfoListener listener) {
        UserApi userApi = RetrofitUtils.createApi(UserApi.class);
        String token = ParamsUtil.getToken();
        Call<UserEntity> respo = userApi.info(token, getUserParams());
        respo.enqueue(new Callback<UserEntity>() {
            @Override
            public void onResponse(Call<UserEntity> call, Response<UserEntity> response) {
                UserEntity entity = response.body();
                if (entity == null) {
                    listener.onHttpFailure();
                    return;
                }
                if (entity.isSuccess()) {
                    listener.onSuccess(entity);
                } else {
                    listener.onError(entity.getMessage());
                }
            }

            @Override
            public void onFailure(Call<UserEntity> call, Throwable t) {
                listener.onHttpFailure();
            }
        });
    }

    private Map<String, String> getUserParams(){
        final Map<String, String> params = ParamsUtil.getCommonParams();
        params.put("method", "gdiex.users.get");
        params.put("sign", ParamsUtil.sign(params));
        return params;
    }

    public interface UserInfoListener{
        public void onSuccess(UserEntity user);
        public void onError(String msg);
        public void onHttpFailure();
    }
}
