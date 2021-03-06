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

    private static Retrofit testSingleton;

    public static <T> T createApi(Class<T> clazz){
        if(singleton == null){
            synchronized (RetrofitUtils.class){
                if(singleton == null){
                    singleton = new Retrofit.Builder()
                            .baseUrl(HttpConstant.BASE_URL)
                            .addConverterFactory(GsonConverterFactory.create())
                            .client(OkHttpUtils.getSingleton(AppApplication.getInstance().getBaseContext()))
                            .build();
                }
            }
        }


        return singleton.create(clazz);
    }

    public static <T> T createJsonApi(Class<T> clazz){
        if(singleton == null){
            synchronized (RetrofitUtils.class){
                if(singleton == null){
                    singleton = new Retrofit.Builder()
                            .baseUrl(HttpConstant.BASE_URL)
                            .client(OkHttpUtils.getSingleton(AppApplication.getInstance().getBaseContext()))
                            .build();
                }
            }
        }


        return singleton.create(clazz);
    }

    public static <T> T createApiNoGson(Class<T> clazz){
        if(testSingleton == null){
            synchronized (RetrofitUtils.class){
                if(testSingleton == null){
                    testSingleton = new Retrofit.Builder()
//                            .baseUrl("http://125.88.152.51:15516/")
                            .baseUrl(HttpConstant.BASE_URL)
//                            .addConverterFactory(GsonConverterFactory.create())
                            .client(OkHttpUtils.getSingleton(AppApplication.getInstance().getBaseContext()))
                            .build();
                }
            }
        }


        return testSingleton.create(clazz);
    }

    public static <T> T createTestApi(Class<T> clazz){
        if(testSingleton == null){
            synchronized (RetrofitUtils.class){
                if(testSingleton == null){
                    testSingleton = new Retrofit.Builder()
//                            .baseUrl("http://125.88.152.51:15516/")
                            .baseUrl(HttpConstant.BASE_URL)
//                            .addConverterFactory(GsonConverterFactory.create())
                            .client(OkHttpUtils.getSingleton(AppApplication.getInstance().getBaseContext()))
                            .build();
                }
            }
        }


        return testSingleton.create(clazz);
    }

}
