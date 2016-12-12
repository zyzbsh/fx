package fxtrader.com.app.tools;

import java.io.UnsupportedEncodingException;

import android.util.Base64;

import fxtrader.com.app.AppApplication;
import fxtrader.com.app.entity.UserInfoVo;

public class Auth {

	public static final String AUTH = "Authorization";

	public static String getAuth(String name, String password) {

		if (null != name && null != password) {

			String account = name + ":" + password;

			String auth = "";
			try {
				auth = "Basic "
						+ Base64.encodeToString(account.getBytes("utf-8"),
								Base64.DEFAULT);

			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			return auth.replaceAll("(\\r|\\n)", "");
		}
		return null;
	}

	public static String getAuthForRecharge(String name, String password) {
		String account = name + ":" + password;
		String auth = "";
		try {
			auth = Base64.encodeToString(account.getBytes("utf-8"),
					Base64.DEFAULT);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return auth.replaceAll("(\\r|\\n)", "");
	}

	public static String getHeader() {
		if (null == AppApplication.getUserInfoPreferences()) {
			return null;
		}
		return getAuth(
				AppApplication.getUserInfoPreferences().getString(
						UserInfoVo.USERNAME, null),
				AppApplication.getUserInfoPreferences().getString(
						UserInfoVo.PASSWORD, null));
	}
}
