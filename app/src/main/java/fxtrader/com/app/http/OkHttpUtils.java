package fxtrader.com.app.http;

import android.content.Context;

import java.io.File;
import java.util.concurrent.TimeUnit;

import okhttp3.Cache;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;

/**
 * Created by zhangyuzhu on 2016/12/6.
 */
public class OkHttpUtils {

    private final static File RESPONSE_CACHE = NetUtils.netConfig.RESPONSE_CACHE;
    private final static int RESPONSE_CACHE_SIZE = NetUtils.netConfig.RESPONSE_CACHE_SIZE;
    private final static int CONNECT_TIMEOUT = NetUtils.netConfig.CONNET_TIMEOUT;
    private final static int READ_TIMEOUT = NetUtils.netConfig.READ_TIMEOUT;
    private static OkHttpClient singleton;

    public static OkHttpClient getSingleton(final Context context){
        if(singleton == null){
            synchronized (OkHttpUtils.class){
                if(singleton == null){
                    HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
                    logging.setLevel(HttpLoggingInterceptor.Level.BODY);
                    singleton = new OkHttpClient().newBuilder()
                            .cache(new Cache(RESPONSE_CACHE != null?RESPONSE_CACHE:new File(context.getCacheDir(),"defalut_cache"),RESPONSE_CACHE_SIZE))
                            .addInterceptor(logging)
                            .connectTimeout(CONNECT_TIMEOUT, TimeUnit.MILLISECONDS)
                            .readTimeout(READ_TIMEOUT, TimeUnit.MILLISECONDS)
                            .build();
                }
            }
        }
        return singleton;
    }


}
