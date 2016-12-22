package fxtrader.com.app.tools;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.TimeZone;

import android.annotation.SuppressLint;

@SuppressLint("SimpleDateFormat")
public class DateTools {

	public static String getCurrentData() {
		return new SimpleDateFormat("yyyyMMdd").format(new Date());
	}

	public static String getCurrentData2() {
		return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
	}

	public static String changeToDate(long time){
		DateFormat formatter = new SimpleDateFormat("yyyy/MM/dd");
		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(time);
		return formatter.format(calendar.getTime());
	}

	public static String changeToDate2(long time) {
		DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(time);
		return formatter.format(calendar.getTime());
	}

	public static String getBeforeDate(int before) {
		Date date = new Date();
		Calendar calendar = new GregorianCalendar();
		calendar.setTime(date);
		calendar.add(Calendar.DATE, -1 * (before));
		date = calendar.getTime();
		String dateString = new SimpleDateFormat("yyyy-MM-dd").format(date);
		return dateString;
	}

	public static boolean isWeekend(int before) {
		Date date = new Date();
		Calendar calendar = Calendar.getInstance();
		calendar.setTimeZone(TimeZone.getTimeZone("GMT+8:00"));
		calendar.setTime(date);
		calendar.add(Calendar.DATE, -1 * before);
		int i = calendar.get(Calendar.DAY_OF_WEEK);
		if (1 == i || 7 == i) {
			return true;
		}
		return false;
	}

	public static String getCurrentDateMin() {
		return new SimpleDateFormat("yyyy-MM-dd HH:mm").format(new Date());
	}

	public static int getCurrentHour() {
		return Integer.parseInt(new SimpleDateFormat("HH").format(new Date()));
	}

	public static String getLastDay() {
		long last = Long.parseLong(getCurrentData());
		last--;
		return String.valueOf((last));
	}

	public static int getWeek() {
		Calendar c = Calendar.getInstance();
		c.setTimeZone(TimeZone.getTimeZone("GMT+8:00"));
		return c.get(Calendar.DAY_OF_WEEK);
	}

	public static Date StrToData(String str) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		try {
			Date date = null;
			date = sdf.parse(str);
			return date;
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static long DateToLong(Date date) {
		return date.getTime();
	}

}
