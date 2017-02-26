package fxtrader.com.app.update;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.Date;
import java.util.Map;
import java.util.Random;
import java.util.TreeMap;

import fxtrader.com.app.entity.AppUpdateResponse;
import fxtrader.com.app.http.HttpConstant;
import fxtrader.com.app.http.ParamsUtil;
import fxtrader.com.app.tools.LogZ;
import fxtrader.com.app.tools.StringUtil;
import fxtrader.com.app.update.bean.CheckResultRepository;
import fxtrader.com.app.update.bean.Update;
import fxtrader.com.app.update.bean.UpdateBean;
import fxtrader.com.app.update.type.RequestType;

public class UpdateConfig {


    /**
     * get方式请求的案例
     */
    public static void initGet(Context context) {
        UpdateHelper.init(context);
        UpdateHelper.getInstance()
                /**可填：请求方式*/
                .setMethod(RequestType.get)
                /**必填：数据更新接口，该方法一定要在setDialogLayout的前面,因为这方法里面做了重置DialogLayout的操作*/
                .setCheckUrl(HttpConstant.BASE_URL + "/api/getNewAppVersion", getCheckAppParams(context))
                /**可填：清除旧的自定义布局设置。之前有设置过自定义布局，建议这里调用下*/
                .setClearCustomLayoutSetting()
                /**可填：自定义更新弹出的dialog的布局样式，主要案例中的布局样式里面的id为（jjdxm_update_content、jjdxm_update_id_ok、jjdxm_update_id_cancel）的view类型和id不能修改，其他的都可以修改或删除*/
//                .setDialogLayout(R.layout.custom_update_dialog)
                /**可填：自定义更新状态栏的布局样式，主要案例中的布局样式里面的id为（jjdxm_update_iv_icon、jjdxm_update_rich_notification_continue、jjdxm_update_rich_notification_cancel、jjdxm_update_title、jjdxm_update_progress_text、jjdxm_update_progress_bar）的view类型和id不能修改，其他的都可以修改或删除*/
//                .setStatusBarLayout(R.layout.custom_download_notification)
                /**可填：自定义强制更新弹出的下载进度的布局样式，主要案例中的布局样式里面的id为(jjdxm_update_progress_bar、jjdxm_update_progress_text)的view类型和id不能修改，其他的都可以修改或删除*/
//                .setDialogDownloadLayout(R.layout.custom_download_dialog)
                /**必填：用于从数据更新接口获取的数据response中。解析出Update实例。以便框架内部处理*/
                .setCheckJsonParser(new ParseData() {
                    @Override
                    public Update parse(String response) {
                        LogZ.i(response);
                        Gson gson = new Gson();
                        AppUpdateResponse appUpdateResponse = gson.fromJson(response, AppUpdateResponse.class);
                        Update update = new Update();
                        update.setUpdateUrl(appUpdateResponse.getObject().getDownloadUrl());
                        update.setVersionCode(Integer.parseInt(appUpdateResponse.getObject().getAppVersions()));
                        update.setApkSize(10);
                        update.setVersionName("1.0.0");
                        update.setUpdateContent(appUpdateResponse.getObject().getUpdateContent());
                        update.setForce(appUpdateResponse.getObject().isForceUpdate());
                        return update;
                    }
                });
    }

    /**分离网络使用的初始化*/
    public static void initNoUrl(Context context) {
        UpdateHelper.init(context);
        UpdateHelper.getInstance()
                /**可填：清除旧的自定义布局设置。之前有设置过自定义布局，建议这里调用下*/
                .setClearCustomLayoutSetting()
                /**可填：自定义更新弹出的dialog的布局样式，主要案例中的布局样式里面的id为（jjdxm_update_content、jjdxm_update_id_ok、jjdxm_update_id_cancel）的view类型和id不能修改，其他的都可以修改或删除*/
//                .setDialogLayout(R.layout.custom_update_dialog)
                /**可填：自定义更新状态栏的布局样式，主要案例中的布局样式里面的id为（jjdxm_update_rich_notification_continue、jjdxm_update_rich_notification_cancel、jjdxm_update_title、jjdxm_update_progress_text、jjdxm_update_progress_bar）的view类型和id不能修改，其他的都可以修改或删除*/
//                .setStatusBarLayout(R.layout.custom_download_notification)
                /**可填：自定义强制更新弹出的下载进度的布局样式，主要案例中的布局样式里面的id为(jjdxm_update_progress_bar、jjdxm_update_progress_text)的view类型和id不能修改，其他的都可以修改或删除*/
//                .setDialogDownloadLayout(R.layout.custom_download_dialog)
                /**必填：用于从数据更新接口获取的数据response中。解析出Update实例。以便框架内部处理*/
                .setCheckJsonParser(new ParseData() {
                    @Override
                    public Update parse(String response) {
                        /**真实情况下使用的解析  response接口请求返回的数据*/
                        Gson gson = new Gson();
                        CheckResultRepository checkResultRepository = gson.fromJson(response,CheckResultRepository.class);
                        /**这里是模拟后台接口返回的json数据解析的bean，需要根据真实情况来写*/
                        UpdateBean updateBean = checkResultRepository.getData();
                        Update update = new Update();
                        /**必填：此apk包的下载地址*/
                        update.setUpdateUrl(updateBean.getDownload_url());
                        /**必填：此apk包的版本号*/
                        update.setVersionCode(updateBean.getV_code());
                        /**可填：此apk包的版本号*/
                        update.setApkSize(updateBean.getV_size());
                        /**必填：此apk包的版本名称*/
                        update.setVersionName(updateBean.getV_name());
                        /**可填：此apk包的更新内容*/
                        update.setUpdateContent(updateBean.getUpdate_content());
                        /**可填：此apk包是否为强制更新*/
                        update.setForce(updateBean.isForce());
                        return update;
                    }
                });
    }

    private static TreeMap<String, Object> getCheckAppParams(Context context) {
        final TreeMap<String, Object> params = new TreeMap<>();
        params.put("client_id", "cf");
        params.put("random_str", StringUtil.getRandomString(11));
        params.put("timestamp", String.valueOf(new Date().getTime()));
        params.put("mobileDevices", "android");
        params.put("appVersions", getVersionCode(context) + "");
        return params;
    }

    public static int getVersionCode(Context context) {
        PackageManager pm = context.getPackageManager();
        try {
            PackageInfo packageInfo = pm.getPackageInfo(context.getPackageName(), 0);
            return packageInfo.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return 0;
    }

}
