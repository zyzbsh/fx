package fxtrader.com.app;

import android.app.Activity;
import android.app.Application;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.HandlerThread;

import fxtrader.com.app.entity.MarketEntity;
import fxtrader.com.app.entity.PriceEntity;
import fxtrader.com.app.entity.UserEntity;
import fxtrader.com.app.entity.UserInfoVo;
import fxtrader.com.app.http.NetConfig;
import fxtrader.com.app.http.NetConfigBuilder;
import fxtrader.com.app.http.NetUtils;
import fxtrader.com.app.http.NetworkStatus;
import fxtrader.com.app.tools.MyUncaughtExceptionHandler;
import fxtrader.com.app.update.UpdateConfig;

/**
 * Created by pc on 2016/11/17.
 */
public class AppApplication extends Application{

    private static AppApplication sApp;

    private static Activity sActivity;

    private static SharedPreferences preferences;

    private MarketEntity mMarketEntity;

    private UserEntity mUser;

    private NetworkStatus mNetworkStatus;

    Handler handler;

    HandlerThread localHandlerThread;

    private String mGeTuiClientId;

    @Override
    public void onCreate() {
        super.onCreate();
        sApp = this;
        initNetConfig();
        setupExceptionCaught();
        UpdateConfig.initGet(this);
    }

    private void initNetConfig() {
        /**
         * net config
         */
        final NetConfig netConfig = new NetConfigBuilder()
                .context(this)
//                .responseCacheDir(new File(context.getCacheDir(),"mycache"))
//                .responseCacheSize(1024 * 1024 * 100)
                .readTimeout(2000)
                .createNetConfig();
        NetUtils.setNetConfig(netConfig);
    }

    public static AppApplication getInstance() {
        return sApp;
    }

    public void setActivity(Activity activity){
        sActivity = activity;
    }


    public Activity getActivity() {
        return sActivity;
    }

    public static SharedPreferences getUserInfoPreferences() {
        if (null == preferences) {
            if (null == AppApplication.getInstance().getActivity()) {
                return null;
            }
            preferences = AppApplication
                    .getInstance()
                    .getActivity()
                    .getSharedPreferences(UserInfoVo.USERINFOVO,
                            Activity.MODE_PRIVATE);
        }
        return preferences;
    }

    public MarketEntity getMarketEntity() {
        return mMarketEntity;
    }

    public void setMarketEntity(MarketEntity mMarketEntity) {
        this.mMarketEntity = mMarketEntity;
    }

    public UserEntity getUser() {
        return mUser;
    }

    public void setUser(UserEntity mUser) {
        this.mUser = mUser;
    }

    public NetworkStatus getNetworkStatus() {
        if (mNetworkStatus == null) {
            mNetworkStatus = new NetworkStatus(getApplicationContext());
        }
        return mNetworkStatus;
    }

    private void setupExceptionCaught() {
        localHandlerThread = new HandlerThread("fxtrader");
        localHandlerThread.start();
        handler = new Handler(localHandlerThread.getLooper());
        Thread.setDefaultUncaughtExceptionHandler(new MyUncaughtExceptionHandler());
    }

    public String getGeTuiClientId(){
        return mGeTuiClientId;
    }

    public void setGeTuiClientId(String clientId){
        mGeTuiClientId = clientId;
    }

}
