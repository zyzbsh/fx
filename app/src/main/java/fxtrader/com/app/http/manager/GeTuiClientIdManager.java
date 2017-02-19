package fxtrader.com.app.http.manager;

import android.text.TextUtils;

import java.util.Map;

import fxtrader.com.app.entity.MasterListEntity;
import fxtrader.com.app.entity.SaveClientResponse;
import fxtrader.com.app.http.ParamsUtil;
import fxtrader.com.app.http.RetrofitUtils;
import fxtrader.com.app.http.api.CommunityApi;
import fxtrader.com.app.tools.LogZ;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by zhangyuzhu on 2017/2/16.
 */
public class GeTuiClientIdManager {

    private static GeTuiClientIdManager sManager;

    private GeTuiClientIdManager() {

    }

    public static GeTuiClientIdManager getInstance() {
        if (sManager == null) {
            sManager = new GeTuiClientIdManager();
        }
        return sManager;
    }

    public void save(String clientId) {
        if (TextUtils.isEmpty(clientId)){
            LogZ.i("no client id");
            return;
        }
        CommunityApi communityApi = RetrofitUtils.createApi(CommunityApi.class);
        String token = ParamsUtil.getToken();
        Call<SaveClientResponse> request = communityApi.saveClientId(token, getParams(clientId));
        request.enqueue(new Callback<SaveClientResponse>() {
            @Override
            public void onResponse(Call<SaveClientResponse> call, Response<SaveClientResponse> response) {
                SaveClientResponse clientResponse = response.body();
                LogZ.i(clientResponse.toString());

            }

            @Override
            public void onFailure(Call<SaveClientResponse> call, Throwable t) {
                if (t != null) {
                    LogZ.e(t.getMessage());
                }
            }
        });
    }

    private Map<String, String> getParams(String clientId) {
        final Map<String, String> params = ParamsUtil.getCommonParams();
        params.put("method", "gdiex.users.saveClientId");
        params.put("CID", clientId);
        params.put("sign", ParamsUtil.sign(params));
        return params;
    }
}
