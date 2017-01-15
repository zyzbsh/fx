package fxtrader.com.app.http;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import fxtrader.com.app.AppApplication;
import fxtrader.com.app.tools.LogZ;

/**
 * 监听网络状态广播
 */
public class NetworkConnectChangedReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        boolean flag = intent.getBooleanExtra(ConnectivityManager.EXTRA_NO_CONNECTIVITY, false);
        NetworkStatus networkStatus = AppApplication.getInstance().getNetworkStatus();
        LogZ.d(
                "action = " + intent.getAction() + ",  flag = " + flag + ", NetWorkStatus = "
                        + networkStatus.toString());
        String action = intent.getAction();
        if (action.equals(ConnectivityManager.CONNECTIVITY_ACTION) && !flag) {
            ConnectivityManager manager = (ConnectivityManager) context
                    .getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo info = manager.getActiveNetworkInfo();
            if (info != null && networkStatus.isConnected()) {// 已经连接
                int type = NetworkUtil.getConnectionType(info);
                if (type != networkStatus.getNetworkType()) {
                    networkStatus.setNetworkType(type);
                    if (type == NetworkUtil.CONNECTIONTYPE_WIFI) { // 当前网络为wifi，开启课程缓存服务
                        LogZ.d("当前网络为wifi，开启课程缓存服务");
//                        if (UserHelper.isLogined()) {
//                            this.startRcoverService(context);
//                        }
                    }
                }
            } else {// 还未连接
                if (info != null && info.isAvailable()) {// 网络连接已建立
                    networkStatus.setConnected(true);
                    networkStatus.setNetworkType(NetworkUtil.getConnectionType(info));
                    LogZ.d("建立网络连接 " + networkStatus.toString());
//                    this.startRcoverService(context);
                } else {
                    LogZ.d("没有网络");
                    networkStatus.clear();
                }
            }
        } else {
            LogZ.d("网络连接断开！");
            if (networkStatus.getNetworkType() == NetworkUtil.CONNECTIONTYPE_WIFI) {
//                UIUtil.stopDownloadCourseService(context);
            }
            networkStatus.clear();
        }
    }


}