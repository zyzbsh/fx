package fxtrader.com.app.http.api;

import fxtrader.com.app.entity.DataVo;
import okhttp3.Response;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by zhangyuzhu on 2016/12/4.
 */
public interface DataLineApi {
    @GET("timeline/query.do")
    Call<DataVo> listTimeLine(@Query("contract") String contract, @Query("number") String number);

    @GET("kliner/query.do")
    Call<DataVo> listCandleLine(@Query("contract") String contract, @Query("type") String type, @Query("number") String number);
}
