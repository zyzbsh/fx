package fxtrader.com.app.http.api;

import java.util.Map;

import fxtrader.com.app.entity.FollowOrderCountEntity;
import fxtrader.com.app.entity.MasterListEntity;
import fxtrader.com.app.entity.ProfitListEntity;
import fxtrader.com.app.entity.SubscribedPositionListEntity;
import fxtrader.com.app.entity.SubscribeListEntity;
import fxtrader.com.app.entity.SubscribedOrderEntity;
import fxtrader.com.app.entity.WinerStreamListEntity;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.QueryMap;

/**
 * Created by pc on 2016/12/26.
 */
public interface CommunityApi {
    @GET("/api/getAceHot2")
    Call<MasterListEntity> masterWithLogined(@Header("Authorization") String authorization, @QueryMap Map<String, String> params);

    @GET("/api/getAceHot")
    Call<MasterListEntity> masters(@QueryMap Map<String, String> params);

    @GET("/api/getWinningStreak")
    Call<WinerStreamListEntity> winersLogined(@Header("Authorization") String authorization, @QueryMap Map<String, String> params);

    @GET("/api/getWinningStreak2")
    Call<WinerStreamListEntity> winers(@QueryMap Map<String, String> params);

    @GET("/api/aceBuyInfo2")
    Call<ProfitListEntity> profitList(@QueryMap Map<String, String> params);

    @GET("/api/aceBuyInfo")
    Call<ProfitListEntity> profitListLogined(@Header("Authorization") String authorization, @QueryMap Map<String, String> params);

    @GET("/api/getSubscripts")
    Call<SubscribeListEntity> subscribes(@Header("Authorization") String authorization, @QueryMap Map<String, String> params);

    @GET("/api/metalOrderOne")
    Call<SubscribedOrderEntity> subscribedOrder(@Header("Authorization") String authorization, @QueryMap Map<String, String> params);

    @GET("/api/getSubscriptBuyInfo")
    Call<SubscribedPositionListEntity> subscribedPosition(@Header("Authorization") String authorization, @QueryMap Map<String, String> params);

    @GET("/api/getFollowOrder")
    Call<FollowOrderCountEntity> followOrderCount(@Header("Authorization") String authorization, @QueryMap Map<String, String> params);
}
