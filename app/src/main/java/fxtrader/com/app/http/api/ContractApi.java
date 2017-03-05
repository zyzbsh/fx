package fxtrader.com.app.http.api;

import java.util.Map;

import fxtrader.com.app.entity.BuildPositionResponseEntity;
import fxtrader.com.app.entity.ClosePositionResponse;
import fxtrader.com.app.entity.CommonResponse;
import fxtrader.com.app.entity.ContractListEntity;
import fxtrader.com.app.entity.MarketEntity;
import fxtrader.com.app.entity.OrderDetailEntity;
import fxtrader.com.app.entity.ParticipantsEntity;
import fxtrader.com.app.entity.PositionListEntity;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.QueryMap;

/**
 * Created by zhangyuzhu on 2016/12/7.
 */
public interface ContractApi {

    @GET("/api/rates")
    Call<ResponseBody> rates(@QueryMap Map<String, String> params);

    @GET("/api/contracts")
    Call<ContractListEntity> list(@QueryMap Map<String, String> params);

    @FormUrlEncoded
    @POST("/api/storages")
    Call<BuildPositionResponseEntity> buildPosition(@Header("Authorization") String authorization, @FieldMap Map<String, String> params);

    @PUT("/api/storages")
    Call<ClosePositionResponse> closePosition(@Header("Authorization") String authorization, @QueryMap Map<String, String> params);

    @PUT("/api/storages")
    Call<CommonResponse> setProfitAndLoss(@Header("Authorization") String authorization, @QueryMap Map<String, String> params);

    @GET("/api/storages")
    Call<PositionListEntity> positionList(@Header("Authorization") String authorization, @QueryMap Map<String, String> params);

    @GET("/api/storages/{storageId}")
    Call<OrderDetailEntity> orderDetail(@Header("Authorization") String authorization, @Path("storageId") String storageId, @QueryMap Map<String, String> params);


    @GET("/api/getRiseOrFall")
    Call<ParticipantsEntity> participants(@QueryMap Map<String, String> params);
}
