package fxtrader.com.app.permission;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;

/**
 * 
 * @ClassName: PermissionChecker
 * @Description:TODO(权限检查，target M)
 * @author: ZhangYuzhu
 * @date: 2016-4-11 下午3:35:40
 * 
 */
public class PermissionChecker {

	public static final String ITEM_ID = "id";
	public static final String ITEM_PERMISSONS = "permissions";
	private Context mContext;
	private String[] mPermissions;

	private PermissionChecker() {
	}

	public static boolean checkSelfPermissionCompatWithResult(Context context,
			String permission, int permission_result) {
		Object obj = ReflectHelper.callMethod(context, "checkSelfPermission",
				new Class<?>[] { String.class }, new Object[] { permission });
		if (obj != null) {
			if (obj instanceof Integer) {
				return ((Integer) obj).intValue() == permission_result;
			}
		}
		return false;
	}

	public static PermissionChecker with(Context context) {
		return new PermissionChecker();
	}

	public PermissionChecker check(String... permissions) {
		mPermissions = permissions;
		return this;
	}

	public void subscribe(IPermissionCheck listener) {
		
		if (listener == null) {
			return;
		}
		
		List<String> deniedPermissions = new ArrayList<String>();
		if (belowM()) {
			listener.result(deniedPermissions);
			return;
		}
		
		if (isPermissionEmpty(mPermissions)) {
			listener.result(deniedPermissions);
			return;
		}
		
		for (String permission : mPermissions) {
			if (!check(permission)) {
				deniedPermissions.add(permission);
			}
		}
		listener.result(deniedPermissions);
	}

	private boolean check(String permission) {
		return checkSelfPermissionCompatWithResult(mContext, permission,
				PackageManager.PERMISSION_GRANTED);
	}
	
	public static boolean isPermissionEmpty(String[] permissions) {
		return permissions == null || permissions.length == 0;
	}
	
	public static boolean belowM() {
		return Build.VERSION.SDK_INT < Build.VERSION_CODES.M;
	}
	
}
