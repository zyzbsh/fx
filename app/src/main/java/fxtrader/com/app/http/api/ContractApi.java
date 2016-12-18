package fxtrader.com.app.http.api;

import java.util.Map;

import fxtrader.com.app.entity.CommonResponse;
import fxtrader.com.app.entity.ContractListEntity;
import fxtrader.com.app.entity.MarketEntity;
import retrofit2.Call;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.QueryMap;

/**
 * Created by zhangyuzhu on 2016/12/7.
 */
public interface ContractApi {

    @GET("/api/rates")
    Call<MarketEntity> rates(@QueryMap Map<String, String> params);

    @GET("/api/contracts")
    Call<ContractListEntity> list(@QueryMap Map<String, String> params);

    @FormUrlEncoded
    @POST("/api/storages")
    Call<CommonResponse> buildPosition(@Header("Authorization") String authorization, @FieldMap Map<String, String> params);

    @FormUrlEncoded
    @POST("/api/storages")
    Call<CommonResponse> closePosition(@Header("Authorization") String authorization, @FieldMap Map<String, String> params);

    @FormUrlEncoded
    @POST("/api/storages")
    Call<CommonResponse> setProfitAndLoss(@Header("Authorization") String authorization, @FieldMap Map<String, String> params);

}
