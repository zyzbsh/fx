//package fxtrader.com.app.http;
//
//import java.io.IOException;
//
//import fxtrader.com.app.tools.StringUtil;
//import okhttp3.HttpUrl;
//import okhttp3.Interceptor;
//import okhttp3.Request;
//import okhttp3.Response;
//
///**
// * Created by zhangyuzhu on 2016/12/6.
// */
//public class CommonInterceptor implements Interceptor {
//
//    public CommonInterceptor() {
//    }
//
//    @Override public Response intercept(Interceptor.Chain chain) throws IOException {
//        Request oldRequest = chain.request();
//
//        // 添加新的参数
//        HttpUrl.Builder authorizedUrlBuilder = oldRequest.url()
//                .newBuilder()
//                .scheme(oldRequest.url().scheme())
//                .host(oldRequest.url().host())
//                .addQueryParameter("client_id", "gdiex");
////                .addQueryParameter("timestamp", String.valueOf(System.currentTimeMillis()))
////                .addQueryParameter("random_str", StringUtil.getRandomString(11));
//
//        // 新的请求
//        Request newRequest = oldRequest.newBuilder()
//                .method(oldRequest.method(), oldRequest.body())
//                .url(authorizedUrlBuilder.build())
//                .build();
//
//        return chain.proceed(newRequest);
//    }
//}
