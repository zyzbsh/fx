package fxtrader.com.app.http.api;

import java.util.Map;

import fxtrader.com.app.entity.ContractListEntity;
import fxtrader.com.app.entity.MarketEntity;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.QueryMap;

/**
 * Created by zhangyuzhu on 2016/12/7.
 */
public interface ContractApi {

    @GET("/api/rates")
    Call<MarketEntity> rates(@QueryMap Map<String, String> params);

    @GET("/api/contracts")
    Call<ContractListEntity> list(@QueryMap Map<String, String> params);
}
