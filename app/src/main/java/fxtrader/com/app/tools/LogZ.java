package fxtrader.com.app.tools;

import java.io.IOException;
import java.nio.channels.SelectionKey;
import java.util.HashMap;

import android.text.TextUtils;
import android.util.Log;

/**
 * Print my log, if the string that inputing is "", the log is not display; if
 * the string is null, the log throw NullPointerExceptions.
 * 
 * @author ZhangYuzhu
 */
public class LogZ {
	public final static String TAG = "zyu";
	private final static HashMap<String, LogZ> logMap = new HashMap<String, LogZ>();
	private final static int logLevel = true ? Log.VERBOSE : Log.ASSERT;
	private final static String userName = "zyu";

	private LogZ() {
	}

	// private MyLog(String name)
	// {
	// userName = name;
	// }

	/**
	 * @param userName
	 * @return MyLog
	 */
	// public static MyLog getLog(String userName)
	// {
	// MyLog classLog = (MyLog) logMap.get(userName);
	// if(classLog == null)
	// {
	// classLog = new MyLog(userName);
	// logMap.put(userName, classLog);
	// }
	// return classLog;
	// }
	/**
	 * @return MyLog
	 */
	public static LogZ getInstance() {
		LogZ classLog = (LogZ) logMap.get(userName);
		if (classLog == null) {
			classLog = new LogZ();
			logMap.put(userName, classLog);
		}
		return classLog;
	}

	/**
	 * Get The Current Function Info
	 * 
	 * @return
	 */
	private static String getFunctionInfo() {
		StackTraceElement[] sts = Thread.currentThread().getStackTrace();
		if (sts == null) {
			return null;
		}
		for (StackTraceElement st : sts) {
			if (st.isNativeMethod()) {
				continue;
			}
			if (st.getClassName().equals(Thread.class.getName())) {
				continue;
			}
			if (st.getClassName().equals(LogZ.class.getName())) {
				continue;
			}
			// return className + "[ " + Thread.currentThread().getName() + ": "
			// + st.getFileName() + ":" + st.getLineNumber() + " "
			// + st.getMethodName() + " ]";
			return new StringBuffer(userName).append("| ").append(Thread.currentThread().getName())
					.append(": ").append(st.getFileName()).append(":").append(st.getLineNumber())
					.append(" ").append(st.getMethodName()).toString();
		}
		return null;
	}

	/**
	 * The Log Level:i
	 * 
	 * @param str
	 */
	public static void i(String str) {
		if (logLevel <= Log.INFO) {
			String name = getFunctionInfo();
			if (name != null) {
				Log.i(name, str);
			} else {
				Log.i(name, str);
			}
		}
	}

	/**
	 * The Log Level:d
	 * 
	 * @param str
	 */
	public static void d(String str) {
		if (logLevel <= Log.DEBUG) {
			String name = getFunctionInfo();
			if (name != null) {
				Log.d(name, str);
			} else {
				Log.d(name, str);
			}
		}
	}

	/**
	 * The Log Level:V
	 * 
	 * @param str
	 */
	public static void v(String str) {
		if (logLevel <= Log.VERBOSE) {
			String name = getFunctionInfo();
			if (name != null) {
				Log.v(name, str);
			} else {
				Log.v(name, str);
			}
		}
	}

	/**
	 * The Log Level:w
	 * 
	 * @param str
	 */
	public static void w(String str) {
		if (logLevel <= Log.WARN) {
			String name = getFunctionInfo();
			if (name != null) {
				Log.w(name, str);
			} else {
				Log.w(name, str);
			}
		}
	}

	/**
	 * The Log Level:e
	 * 
	 * @param str
	 */
	public static void e(String str) {
		if (logLevel <= Log.ERROR) {
			String name = getFunctionInfo();
			if (TextUtils.isEmpty(str)) {
				str = "缺少错误信息。";
			}
			if (name != null) {
				Log.e(name, str);
			} else {
				Log.e(name, str);
			}
		}
	}

	/**
	 * The Log Level:e
	 * 
	 * @param ex
	 */
	public static void e(Exception ex) {
		if (logLevel <= Log.ERROR) {
			Log.e(TAG, "error", ex);
		}
	}

	/**
	 * The Log Level:e
	 * 
	 * @param log
	 * @param tr
	 */
	public static void e(String log, Throwable tr) {
		String line = getFunctionInfo();
		Log.e(TAG, "{Thread:" + Thread.currentThread().getName() + "}" + "[" + userName + line
				+ ":] " + log + "\n", tr);
	}

	/**
	 * IEvent.java 网络事件处理器，当Selector可以进行操作时，调用这个接口中的方法.
	 */
	public interface IEvent {
		/** */
		/**
		 * 当channel得到connect事件时调用这个方法.
		 * 
		 * @param key
		 * @throws IOException
		 */
		void connect(SelectionKey key) throws IOException;

		/** */
		/**
		 * 当channel可读时调用这个方法.
		 * 
		 * @param key
		 * @throws IOException
		 * @throws Exception
		 */
		void read(SelectionKey key) throws IOException, Exception;

		/** */
		/**
		 * 当channel可写时调用这个方法.
		 * 
		 * @throws IOException
		 * @throws Exception
		 */
		void write() throws IOException, Exception;

		/** */
		/**
		 * 当channel发生错误时调用.
		 * 
		 * @param e
		 */
		void error(Exception e);
	}
}
