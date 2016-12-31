package fxtrader.com.app.http.manager;

import java.util.Map;

import fxtrader.com.app.entity.SubscribeEntity;
import fxtrader.com.app.http.ParamsUtil;
import fxtrader.com.app.http.RetrofitUtils;
import fxtrader.com.app.http.api.CommunityApi;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * 关注
 * Created by pc on 2016/12/30.
 */
public class SubscribeManager {
    private static SubscribeManager sSubscribeManager;

    private SubscribeManager() {
    }

    public static SubscribeManager getInstance() {
        if (sSubscribeManager == null) {
            sSubscribeManager = new SubscribeManager();
        }
        return sSubscribeManager;
    }

    public void add(String customerId, String subscribeId, final ResponseListener<SubscribeEntity> responseListener) {
        if (responseListener == null) {
            return;
        }
        CommunityApi communityApi = RetrofitUtils.createApi(CommunityApi.class);
        String token = ParamsUtil.getToken();
        final Call<SubscribeEntity> request = communityApi.addSubscribe(token, getaddSubscribeParams(customerId, subscribeId));
        request.enqueue(new Callback<SubscribeEntity>() {
            @Override
            public void onResponse(Call<SubscribeEntity> call, Response<SubscribeEntity> response) {
                SubscribeEntity entity = response.body();
                responseListener.success(entity);
            }

            @Override
            public void onFailure(Call<SubscribeEntity> call, Throwable t) {
                responseListener.error(t.getMessage());
            }
        });
    }

    private Map<String, String> getaddSubscribeParams(String customerId, String subscribeId) {
        final Map<String, String> params = ParamsUtil.getCommonParams();
        params.put("method", "gdiex.community.addSubscriber");
        params.put("customerId", customerId);
        params.put("subscribedId", subscribeId);
        params.put("sign", ParamsUtil.sign(params));
        return params;
    }

    public void cancel(String customerId, String subscribeId,final ResponseListener<SubscribeEntity> responseListener) {
        if (responseListener == null) {
            return;
        }
        CommunityApi communityApi = RetrofitUtils.createApi(CommunityApi.class);
        String token = ParamsUtil.getToken();
        final Call<SubscribeEntity> request = communityApi.cancelSubscribe(token, getCancelParams(customerId, subscribeId));
        request.enqueue(new Callback<SubscribeEntity>() {
            @Override
            public void onResponse(Call<SubscribeEntity> call, Response<SubscribeEntity> response) {
                SubscribeEntity entity = response.body();
                responseListener.success(entity);
            }

            @Override
            public void onFailure(Call<SubscribeEntity> call, Throwable t) {
                responseListener.error(t.getMessage());
            }
        });
    }

    private Map<String, String> getCancelParams(String customerId, String subscribeId) {
        final Map<String, String> params = ParamsUtil.getCommonParams();
        params.put("method", "gdiex.community.unsubscribe");
        params.put("customerId", customerId);
        params.put("subscribedId", subscribeId);
        params.put("sign", ParamsUtil.sign(params));
        return params;
    }

    public void check(String customerId, String subscribeId, final ResponseListener<SubscribeEntity> responseListener) {
        if (responseListener == null) {
            return;
        }
        CommunityApi communityApi = RetrofitUtils.createApi(CommunityApi.class);
        String token = ParamsUtil.getToken();
        final Call<SubscribeEntity> request = communityApi.addSubscribe(token, getCheckParams(customerId, subscribeId));
        request.enqueue(new Callback<SubscribeEntity>() {
            @Override
            public void onResponse(Call<SubscribeEntity> call, Response<SubscribeEntity> response) {
                SubscribeEntity entity = response.body();
                responseListener.success(entity);
            }

            @Override
            public void onFailure(Call<SubscribeEntity> call, Throwable t) {
                responseListener.error(t.getMessage());
            }
        });
    }

    private Map<String, String> getCheckParams(String customerId, String subscribeId) {
        final Map<String, String> params = ParamsUtil.getCommonParams();
        params.put("method", "gdiex.community.checkSubscriber");
        params.put("customerId", customerId);
        params.put("subscribedId", subscribeId);
        params.put("sign", ParamsUtil.sign(params));
        return params;
    }
}
