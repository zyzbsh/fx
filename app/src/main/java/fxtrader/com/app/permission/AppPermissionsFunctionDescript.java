package fxtrader.com.app.permission;//package com.aspire.mm.app;
//
//import java.util.ArrayList;
//import java.util.Arrays;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//
//import android.Manifest;
//import android.content.Context;
//import android.content.pm.PackageManager;
//import android.content.pm.PackageManager.NameNotFoundException;
//import android.content.pm.PermissionInfo;
//
///**
// * 这个描用于描述应用权限的功能，用于替换系统的描述
// * @author lhy
// *
// */
//public class AppPermissionsFunctionDescript {
//    private final static Map<String, String>	gPermissionsDescript = new HashMap<String, String>();
//    static{
//	gPermissionsDescript.put(Manifest.permission.WRITE_SETTINGS, "允许MM商场修改系统设置。");
//	gPermissionsDescript.put(Manifest.permission.SEND_SMS, "允许MM商场发送短信，用于注册登录，发送的短信是免费的。");
//	gPermissionsDescript.put(Manifest.permission.READ_PHONE_STATE, "允许MM商场访问设备的电话功能，用于MM商场注册登录。");
//	gPermissionsDescript.put(Manifest.permission.RECEIVE_SMS, "允许MM商场接收短信，用于接收短信验证码、系统短信等。");
//	gPermissionsDescript.put(Manifest.permission.READ_SMS, gPermissionsDescript.get(Manifest.permission.RECEIVE_SMS)); //指向上一行
//	gPermissionsDescript.put(Manifest.permission.READ_EXTERNAL_STORAGE, "允许MM商场访问SD卡，用于应用下载、图书阅读、漫画阅读、动画视频播放、缓存管理等");
//	gPermissionsDescript.put(Manifest.permission.WRITE_EXTERNAL_STORAGE, gPermissionsDescript.get(Manifest.permission.READ_EXTERNAL_STORAGE));
//	gPermissionsDescript.put(Manifest.permission.READ_CONTACTS, "允许MM商场访问通讯录，用于图书赠予、彩漫推荐。");
//	gPermissionsDescript.put(Manifest.permission.CAMERA, "允许MM商场使用相机，用于二维码扫描。");
//    }
//    public static String  getPermissionLabel(Context context, String permname){
//	PackageManager pm = context.getPackageManager() ;
//	try {
//	    PermissionInfo tmpPermInfo = pm.getPermissionInfo(permname, 0);
//	    return tmpPermInfo.loadLabel(pm).toString();
//	} catch (NameNotFoundException e) {
//	    return permname;
//	}
//    }
//    
//    public static String getPermissionDescription(Context context, String permname){
//	PackageManager pm = context.getPackageManager() ;
//	try {
//	    PermissionInfo tmpPermInfo = pm.getPermissionInfo(permname, 0);
//	    return tmpPermInfo.loadDescription(pm).toString();
//	} catch (NameNotFoundException e) {
//	    return "";
//	}
//    }
//    
//    public static String  getPermissionFunctionDescript(String permname){
//	return gPermissionsDescript.get(permname);
//    }
//    
//    public static String formatAppPermissionsInfo(Context context, String [] required_perms){
//  	List<String> perms = new ArrayList<String>(Arrays.asList(required_perms));
//  	//去重，将相似的权限去掉，剩下一条即可
//  	if (perms.contains(Manifest.permission.READ_SMS) && perms.contains(Manifest.permission.RECEIVE_SMS)){ 
//  	    perms.remove(Manifest.permission.READ_SMS);
//  	}
//  	if (perms.contains(Manifest.permission.READ_EXTERNAL_STORAGE) && perms.contains(Manifest.permission.WRITE_EXTERNAL_STORAGE)){
//  	    perms.remove(Manifest.permission.READ_EXTERNAL_STORAGE);
//  	}
//  	StringBuffer sb = new StringBuffer();
//  	String tmpstr = null ;
//  	int index = 0;
//  	for (String s: perms){
//  	    index ++ ;
//  	    tmpstr = getPermissionLabel(context, s);
//  	    if (perms.size() > 1)
//  		sb.append(String.format("(%d) %S\n",index, tmpstr));
//  	    else
//  		sb.append(String.format("%S\n",tmpstr));
////  	    tmpstr = getPermissionFunctionDescript(s);
////  	    if (TextUtils.isEmpty(tmpstr)){
////  		tmpstr = getPermissionDescription(context, s);
////  	    }
////  	    sb.append(tmpstr).append("\n\n");
//  	}
//  	return sb.toString();
//      }
//}
