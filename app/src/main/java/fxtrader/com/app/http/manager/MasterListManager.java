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

    public static final int LIMIT_LOGINED = 10;

    public static final int LIMIT_UNLOGINED = 4;

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
        params.put("limit", String.valueOf(LIMIT_LOGINED));
        params.put("customerId", customerId);
        MarketEntity market = AppApplication.getInstance().getMarketEntity();
        String agRate = "10";
        String auRate = "10";
        String oilRate = "10";
        String tdagRate = "10";
        String tdauRate = "10";
        if (market != null) {
            PriceEntity agPrice = new PriceEntity(market.getData(HttpConstant.PriceCode.AG));
            agRate = agPrice.getLatestPrice();

            PriceEntity auPrice = new PriceEntity(market.getData(HttpConstant.PriceCode.AU));
            auRate = auPrice.getLatestPrice();

            PriceEntity oilPrice = new PriceEntity(market.getData(HttpConstant.PriceCode.OIL));
            oilRate = oilPrice.getLatestPrice();

            PriceEntity tdagPrice = new PriceEntity(market.getData(HttpConstant.PriceCode.TDAG));
            tdagRate = tdagPrice.getLatestPrice();

            PriceEntity tdauPrice = new PriceEntity(market.getData(HttpConstant.PriceCode.TDAU));
            tdauRate = tdauPrice.getLatestPrice();
        }
        params.put("agRate", agRate);
        params.put("auRate", auRate);
        params.put("oilRate", oilRate);
        params.put("tdagRate", tdagRate);
        params.put("tdauRate", tdauRate);
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
        params.put("limit", String.valueOf(LIMIT_UNLOGINED));
        MarketEntity market = AppApplication.getInstance().getMarketEntity();
        PriceEntity agPrice = new PriceEntity(market.getData(HttpConstant.PriceCode.AG));
        params.put("agRate", agPrice.getLatestPrice());

            PriceEntity auPrice = new PriceEntity(market.getData(HttpConstant.PriceCode.AU));
        params.put("auRate", auPrice.getLatestPrice());

            PriceEntity oilPrice = new PriceEntity(market.getData(HttpConstant.PriceCode.OIL));
        params.put("oilRate", oilPrice.getLatestPrice());

            PriceEntity tdagPrice = new PriceEntity(market.getData(HttpConstant.PriceCode.TDAG));
        params.put("tdagRate", tdagPrice.getLatestPrice());

            PriceEntity tdauPrice = new PriceEntity(market.getData(HttpConstant.PriceCode.TDAU));
        params.put("tdauRate", tdauPrice.getLatestPrice());
        params.put("sign", ParamsUtil.sign(params));
        return params;
    }

}
