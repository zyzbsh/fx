package fxtrader.com.app.tools;

import com.igexin.sdk.PushManager;

import fxtrader.com.app.AppApplication;

/**
 * Created by zhangyuzhu on 2017/2/16.
 */
public final class GeTuiUtil {
    public static void init(){
        PushManager.getInstance().initialize(AppApplication.getInstance().getBaseContext(), fxtrader.com.app.service.PushService.class);
        PushManager.getInstance().registerPushIntentService(AppApplication.getInstance().getBaseContext(), fxtrader.com.app.service.PushIntentService.class);
    }
}
