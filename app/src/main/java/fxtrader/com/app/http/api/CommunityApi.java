package fxtrader.com.app.http.api;

import java.util.Map;

import fxtrader.com.app.entity.AdEntity;
import fxtrader.com.app.entity.CommonResponse;
import fxtrader.com.app.entity.FollowOrderCountEntity;
import fxtrader.com.app.entity.MasterListEntity;
import fxtrader.com.app.entity.ProfitListEntity;
import fxtrader.com.app.entity.RankResponse;
import fxtrader.com.app.entity.SaveClientResponse;
import fxtrader.com.app.entity.SubscribeEntity;
import fxtrader.com.app.entity.SubscribedPositionListEntity;
import fxtrader.com.app.entity.SubscribeListEntity;
import fxtrader.com.app.entity.SubscribedOrderEntity;
import fxtrader.com.app.entity.SystemBulletinEntity;
import fxtrader.com.app.entity.WinerStreamListEntity;
import fxtrader.com.app.entity.YesterdayRankResponse;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
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

    @GET("/api/checkSubscriber")
    Call<SubscribeEntity> checkSubscribe(@Header("Authorization") String authorization, @QueryMap Map<String, String> params);

    @GET("/api/addSubscriber")
    Call<SubscribeEntity> addSubscribe(@Header("Authorization") String authorization, @QueryMap Map<String, String> params);

    @GET("/api/unsubscribe")
    Call<SubscribeEntity> cancelSubscribe(@Header("Authorization") String authorization, @QueryMap Map<String, String> params);

    @GET("/api/getBulletinBoards")
    Call<AdEntity> boards(@QueryMap Map<String, String> params);

    @FormUrlEncoded
    @POST("/api/metalOrdersFollow")
    Call<CommonResponse> orderFollowed(@Header("Authorization") String authorization, @FieldMap Map<String, String> params);

    @GET("/api/getSystemBulletin")
    Call<SystemBulletinEntity> systembulletin(@QueryMap Map<String, String> params);

    @FormUrlEncoded
    @POST("/api/v3/saveClientId")
    Call<SaveClientResponse> saveClientId(@Header("Authorization") String authorization, @FieldMap Map<String, String> params);

    @GET("/api/rankNo")
    Call<RankResponse> profitRank(@Header("Authorization") String authorization, @QueryMap Map<String, String> params);

    @GET("/api/getAceRank")
    Call<YesterdayRankResponse> getAceRank(@QueryMap Map<String, String> params);
}
