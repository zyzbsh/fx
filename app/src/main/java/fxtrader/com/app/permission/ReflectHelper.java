package fxtrader.com.app.permission;

import java.lang.reflect.Method;

import fxtrader.com.app.tools.LogZ;

public class ReflectHelper {

	private static final String TAG = "ReflectHelper";

	public static Object callMethod(Object paramObject, String paramString,
			Class<?>[] paramArrayOfClass, Object[] paramArrayOfObject) {
		if (paramObject == null) {
			return null;
		}
		Class<?> localClass = paramObject.getClass();
		Method localMethod = null;
		Object localObject = null;
		try {
			localMethod = localClass.getMethod(paramString, paramArrayOfClass);
			localObject = localMethod.invoke(paramObject, paramArrayOfObject);
		} catch (NoSuchMethodException localNoSuchMethodException) {
		} catch (Exception e) {
			LogZ.e("callMethod " + paramString + " reason=" + e + " "
					+ e.getMessage() + ",cause=" + e.getCause());
		}
		return localObject;
	}

	public static Object callStaticMethod(Class<?> paramClass,
			String paramString, Class<?>[] paramArrayOfClass,
			Object[] paramArrayOfObject) {
		Method localMethod = null;
		Object localObject = null;
		try {
			(localMethod = paramClass.getDeclaredMethod(paramString,
					paramArrayOfClass)).setAccessible(true);
			localObject = localMethod.invoke(null, paramArrayOfObject);
		} catch (NoSuchMethodException localNoSuchMethodException) {
		} catch (Exception e) {
			LogZ.e("callStaticMethod " + paramString + "," + e + " "
					+ e.getMessage() + ",cause=" + e.getCause());
		}
		return localObject;
	}
}