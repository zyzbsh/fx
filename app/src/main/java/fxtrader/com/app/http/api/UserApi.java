package fxtrader.com.app.http.api;

import java.util.Map;

import fxtrader.com.app.entity.CommonResponse;
import fxtrader.com.app.entity.CouponListEntity;
import fxtrader.com.app.entity.CurrencyListEntity;
import fxtrader.com.app.entity.LoginResponseEntity;
import fxtrader.com.app.entity.OpenPacket;
import fxtrader.com.app.entity.PacketListEntity;
import fxtrader.com.app.entity.RedEnvelopeListEntity;
import fxtrader.com.app.entity.TicketListEntity;
import fxtrader.com.app.entity.UserEntity;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.QueryMap;

/**
 * Created by zhangyuzhu on 2016/12/6.
 */
public interface UserApi {
    @Headers("Content-Type:application/x-www-form-urlencoded")
    @POST("/oauth/token")
    Call<LoginResponseEntity> login(@QueryMap Map<String, String> params);

    @POST("/api/sms")
    Call<CommonResponse> versificationCode(@QueryMap Map<String, String> params);

    @FormUrlEncoded
    @POST("/api/v3/users")
    Call<CommonResponse> register(@FieldMap Map<String, String> params);

    @GET("/api/v3/users")
    Call<UserEntity> info(@Header("Authorization") String authorization, @QueryMap Map<String, String> params);

    @GET("/api/coupons")
    Call<CouponListEntity> coupons(@Header("Authorization") String authorization, @QueryMap Map<String, String> params);

    @GET("/api/currency/details")
    Call<CurrencyListEntity> currencyDetail(@Header("Authorization") String authorization, @QueryMap Map<String, String> params);

    @GET("/api/tickets")
    Call<TicketListEntity> tickets(@QueryMap Map<String, String> params);

    @FormUrlEncoded
    @POST("/api/currency/unionpay")
    Call<ResponseBody> bankRecharge(@Header("Authorization") String authorization, @FieldMap Map<String, String> params);

    @FormUrlEncoded
    @POST("/api/currency/withdrawal")
    Call<ResponseBody> withdraw(@Header("Authorization") String authorization, @FieldMap Map<String, String> params);

    @GET("/api/checkRedPacketIn")
    Call<PacketListEntity> receivePackets(@Header("Authorization") String authorization, @QueryMap Map<String, String> params);

    @GET("/api/checkRedPacketOut")
    Call<PacketListEntity> sendedPackets(@Header("Authorization") String authorization, @QueryMap Map<String, String> params);

    @GET("/api/openRedPack")
    Call<OpenPacket> openPacket(@Header("Authorization") String authorization, @QueryMap Map<String, String> params);

    @GET("gdiex.community.getRiseOrFall")
    Call<RedEnvelopeListEntity> redEnvelopeList(@QueryMap Map<String, String> params);
}
