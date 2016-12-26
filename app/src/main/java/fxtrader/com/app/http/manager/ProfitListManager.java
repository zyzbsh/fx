package fxtrader.com.app.http.manager;

import java.util.Map;

import fxtrader.com.app.entity.ProfitListEntity;
import fxtrader.com.app.entity.ProfitListEntity;
import fxtrader.com.app.http.HttpConstant;
import fxtrader.com.app.http.ParamsUtil;
import fxtrader.com.app.http.RetrofitUtils;
import fxtrader.com.app.http.api.CommunityApi;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by pc on 2016/12/26.
 */
public class ProfitListManager {

    public static ProfitListManager sProfitListManager;

    private ProfitListManager(){}

    public static ProfitListManager getInstance(){
        if (sProfitListManager == null) {
            sProfitListManager = new ProfitListManager();
        }
        return sProfitListManager;
    }


    public void getProfitListWithLogined(final ResponseListener<ProfitListEntity> listener, String organId, String customerId){
        if (listener == null) {
            throw new IllegalArgumentException("MasterListener is null");
        }
        CommunityApi communityApi = RetrofitUtils.createApi(CommunityApi.class);
        String token = ParamsUtil.getToken();
        Call<ProfitListEntity> respo = communityApi.profitListLogined(token, getLoginedParams(organId, customerId));
        respo.enqueue(new Callback<ProfitListEntity>() {
            @Override
            public void onResponse(Call<ProfitListEntity> call, Response<ProfitListEntity> response) {
                ProfitListEntity entity = response.body();
                listener.success(entity);
            }

            @Override
            public void onFailure(Call<ProfitListEntity> call, Throwable t) {
                listener.error(t.getMessage());
            }
        });
    }

    private Map<String, String> getLoginedParams(String organId, String customerId){
        final Map<String, String> params = ParamsUtil.getCommonParams();
        params.put("method", "gdiex.community.getRiseOrFall");
        params.put("organ_id", organId);
        params.put("limit", "10");
        params.put("customerId", customerId);
        params.put("sign", ParamsUtil.sign(params));
        return params;
    }

    public void getProfitList(final ResponseListener<ProfitListEntity> listener){
        if (listener == null) {
            throw new IllegalArgumentException("MasterListener is null");
        }
        CommunityApi communityApi = RetrofitUtils.createApi(CommunityApi.class);
        Call<ProfitListEntity> respo = communityApi.profitList(getParams());
        respo.enqueue(new Callback<ProfitListEntity>() {
            @Override
            public void onResponse(Call<ProfitListEntity> call, Response<ProfitListEntity> response) {
                ProfitListEntity entity = response.body();
                listener.success(entity);
            }

            @Override
            public void onFailure(Call<ProfitListEntity> call, Throwable t) {
                listener.error(t.getMessage());
            }
        });
    }

    private Map<String, String> getParams(){
        final Map<String, String> params = ParamsUtil.getCommonParams();
        params.put("method", "gdiex.community.getAceHot");
        params.put("organ_id", HttpConstant.DEFAULT_ORGAN_ID + "");
        params.put("limit", "10");
        params.put("sign", ParamsUtil.sign(params));
        return params;
    }
}
