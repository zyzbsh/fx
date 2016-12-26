package fxtrader.com.app.http.manager;

import java.util.Map;

import fxtrader.com.app.entity.WinerStreamListEntity;
import fxtrader.com.app.http.HttpConstant;
import fxtrader.com.app.http.ParamsUtil;
import fxtrader.com.app.http.RetrofitUtils;
import fxtrader.com.app.http.api.CommunityApi;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * 连胜榜单
 * Created by pc on 2016/12/26.
 */
public class WinerStreamListManager {
    private static WinerStreamListManager sWinerStreamListManager;
    private WinerStreamListManager(){}

    public static WinerStreamListManager getInstance(){
        if (sWinerStreamListManager == null) {
            sWinerStreamListManager = new WinerStreamListManager();
        }
        return sWinerStreamListManager;
    }

    public void getWiners(final ResponseListener<WinerStreamListEntity> listener) {
        if (listener == null) {
            throw new IllegalArgumentException("MasterListener is null");
        }
        CommunityApi communityApi = RetrofitUtils.createApi(CommunityApi.class);
        Call<WinerStreamListEntity> respon = communityApi.winers(getWinersParams());
        respon.enqueue(new Callback<WinerStreamListEntity>() {
            @Override
            public void onResponse(Call<WinerStreamListEntity> call, Response<WinerStreamListEntity> response) {
                WinerStreamListEntity entity = response.body();
                listener.success(entity);
            }

            @Override
            public void onFailure(Call<WinerStreamListEntity> call, Throwable t) {
                listener.error(t.getMessage());
            }
        });
    }

    private Map<String, String> getWinersParams(){
        final Map<String, String> params = ParamsUtil.getCommonParams();
        params.put("method", "gdiex.community.getWinningStreak2");
        params.put("organ_id", HttpConstant.DEFAULT_ORGAN_ID + "");
        params.put("limit", "10");
        params.put("sign", ParamsUtil.sign(params));
        return params;
    }

    public void getWinersLongined(String organId, String customerId, final ResponseListener<WinerStreamListEntity> listener) {
        if (listener == null) {
            throw new IllegalArgumentException("MasterListener is null");
        }
        CommunityApi communityApi = RetrofitUtils.createApi(CommunityApi.class);
        String token = ParamsUtil.getToken();
        Call<WinerStreamListEntity> respon = communityApi.winersLogined(token, getWinersLonginedParams(organId, customerId));
        respon.enqueue(new Callback<WinerStreamListEntity>() {
            @Override
            public void onResponse(Call<WinerStreamListEntity> call, Response<WinerStreamListEntity> response) {
                WinerStreamListEntity entity = response.body();
                listener.success(entity);
            }

            @Override
            public void onFailure(Call<WinerStreamListEntity> call, Throwable t) {
                listener.error(t.getMessage());
            }
        });
    }

    private Map<String, String> getWinersLonginedParams(String organId, String customerId){
        final Map<String, String> params = ParamsUtil.getCommonParams();
        params.put("method", "gdiex.community.getWinningStreak");
        params.put("organ_id", organId);
        params.put("limit", "10");
        params.put("customerId", customerId);
        params.put("sign", ParamsUtil.sign(params));
        return params;
    }

}
