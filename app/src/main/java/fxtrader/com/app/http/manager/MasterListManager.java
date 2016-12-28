package fxtrader.com.app.http.manager;

import java.util.Map;

import fxtrader.com.app.AppApplication;
import fxtrader.com.app.entity.MarketEntity;
import fxtrader.com.app.entity.MasterListEntity;
import fxtrader.com.app.entity.PriceEntity;
import fxtrader.com.app.http.HttpConstant;
import fxtrader.com.app.http.ParamsUtil;
import fxtrader.com.app.http.RetrofitUtils;
import fxtrader.com.app.http.api.CommunityApi;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * 高手热点
 * Created by pc on 2016/12/26.
 */
public class MasterListManager {

    private static MasterListManager sMasterManager;

    private MasterListManager(){

    }

    public static MasterListManager getInstance(){
        if (sMasterManager == null) {
            sMasterManager = new MasterListManager();
        }
        return sMasterManager;
    }

    public void getMastersWithLogined(String organId, String customerId, final ResponseListener<MasterListEntity> listener){
        if (listener == null) {
            throw new IllegalArgumentException("MasterListener is null");
        }
        CommunityApi communityApi = RetrofitUtils.createApi(CommunityApi.class);
        String token = ParamsUtil.getToken();
        Call<MasterListEntity> respo = communityApi.masterWithLogined(token, getLoginedParams(organId, customerId));
        respo.enqueue(new Callback<MasterListEntity>() {
            @Override
            public void onResponse(Call<MasterListEntity> call, Response<MasterListEntity> response) {
                MasterListEntity entity = response.body();
                listener.success(entity);
            }

            @Override
            public void onFailure(Call<MasterListEntity> call, Throwable t) {
                listener.error(t.getMessage());
            }
        });
    }

    private Map<String, String> getLoginedParams(String organId, String customerId){
        final Map<String, String> params = ParamsUtil.getCommonParams();
        params.put("method", "gdiex.community.getAceHot2");
        params.put("organ_id", organId);
        params.put("limit", "10");
        params.put("customerId", customerId);
        params.put("ydRate", "10.00");
        params.put("hfRate", "10.00");
        params.put("sign", ParamsUtil.sign(params));
        return params;
    }

    public void getMasters(final ResponseListener<MasterListEntity> listener){
        if (listener == null) {
            throw new IllegalArgumentException("MasterListener is null");
        }
        CommunityApi communityApi = RetrofitUtils.createApi(CommunityApi.class);
        Call<MasterListEntity> respo = communityApi.masters(getParams());
        respo.enqueue(new Callback<MasterListEntity>() {
            @Override
            public void onResponse(Call<MasterListEntity> call, Response<MasterListEntity> response) {
                MasterListEntity entity = response.body();
                listener.success(entity);
            }

            @Override
            public void onFailure(Call<MasterListEntity> call, Throwable t) {
                listener.error(t.getMessage());
            }
        });
    }

    private Map<String, String> getParams(){
        final Map<String, String> params = ParamsUtil.getCommonParams();
        params.put("method", "gdiex.community.getAceHot");
        params.put("organ_id", HttpConstant.DEFAULT_ORGAN_ID + "");
        params.put("limit", "10");
        MarketEntity market = AppApplication.getInstance().getMarketEntity();
        PriceEntity ydPrice = new PriceEntity(HttpConstant.PriceCode.YDCL);
        params.put("ydRate", ydPrice.getLatestPrice());
        PriceEntity hfPrice = new PriceEntity(HttpConstant.PriceCode.YDHF);
        params.put("hfRate", hfPrice.getLatestPrice());
        params.put("sign", ParamsUtil.sign(params));
        return params;
    }

}
