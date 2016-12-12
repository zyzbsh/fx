package fxtrader.com.app.http;

/**
 * Created by zhangyuzhu on 2016/12/6.
 */
public abstract class NetUtils {
    public static NetConfig netConfig;

    public static void setNetConfig(NetConfig netConfig){
        NetUtils.netConfig = netConfig;
    }

    public static <T> T createApi(Class<T> clazz, String host){
        if(netConfig.context == null)
            throw new IllegalArgumentException("must be set Context,use NetUtils.setNetConfig() at once");
        return RetrofitUtils.createApi(clazz);
    }
}
