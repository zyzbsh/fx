package fxtrader.com.app.tools;

import java.util.Random;

public class StringUtil {

	private static final String BASE_STR = "abcdefghijklmnopqrstuvwxyz0123456789";

	public static boolean StrIsNull(String str) {

		if (str == null || str.trim().equals(""))
			return true;

		return false;
	}

	public static boolean isUserNameValidate(String str, int minLength,
			int maxLength) {
		String compare_str = "[a-zA-Z0-9\u4E00-\u9FA5]{" + minLength + ","
				+ maxLength + "}";
		if (str.matches(compare_str)) {
			return true;
		}
		return false;
	}

	public static boolean isPhoneValidate(String str, int minLength,
			int maxLength) {
		String compare_str = "[0-9]{" + minLength + "," + maxLength + "}";
		if (str.matches(compare_str)) {
			return true;
		}
		return false;
	}

	public static boolean isPasswordValidate(String str, int minLength,
			int maxLength) {
		String compare_str = "[a-zA-Z0-9\\@\\!\\#]{" + minLength + ","
				+ maxLength + "}";
		if (str.matches(compare_str)) {
			return true;
		}
		return false;
	}

	public static boolean checkEqual(String str, String str2) {
		if (str.equals(str2)) {
			return true;
		}
		return false;
	}

	public static boolean isEmail(String email) {
		if (email.matches("\\w+@\\w+(\\.\\w+)+")) {

			return true;
		}
		return false;
	}

	private StringUtil() {
	}

	public static String getRandomString(int length) { //length表示生成字符串的长度
		Random random = new Random();
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < length; i++) {
			int number = random.nextInt(BASE_STR.length());
			sb.append(BASE_STR.charAt(number));
		}
		return sb.toString();
	}

}
