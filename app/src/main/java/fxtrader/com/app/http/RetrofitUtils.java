package fxtrader.com.app.http;

import android.content.Context;

import fxtrader.com.app.AppApplication;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by zhangyuzhu on 2016/12/6.
 */
public class RetrofitUtils {

    private static Retrofit singleton;

    private static String BASE_URL = "http://121.9.227.58:13516/";

    public static <T> T createApi(Class<T> clazz){
        if(singleton == null){
            synchronized (RetrofitUtils.class){
                if(singleton == null){
                    singleton = new Retrofit.Builder()
//                            .baseUrl("http://125.88.152.51:15516/")
                            .baseUrl(BASE_URL)
                            .addConverterFactory(GsonConverterFactory.create())
                            .client(OkHttpUtils.getSingleton(AppApplication.getInstance().getBaseContext()))
                            .build();
                }
            }
        }


        return singleton.create(clazz);
    }

}
