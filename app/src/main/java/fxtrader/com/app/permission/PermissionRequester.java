package fxtrader.com.app.permission;

import android.content.Context;

/**
 * 权限请求
 * 
 * @author zhangyuzhu
 * 
 */
public final class PermissionRequester {

	private Context mContext;
	private String[] mPermissions;

	private PermissionRequester(Context context) {
		mContext = context;
	}

	public static PermissionRequester with(Context context) {
		return new PermissionRequester(context);
	}

	public PermissionRequester request(String... permissions) {
		mPermissions = permissions;
		return this;
	}
	
	public void subscribe(final IPermissionRequest listener) {

		if (mContext == null || listener == null) {
			return;
		}

		if (PermissionChecker.belowM()) {
			listener.onAllGranted();
			return;
		}

		if (PermissionChecker.isPermissionEmpty(mPermissions)) {
			listener.onAllGranted();
			return;
		}

		PermissionRequestActivity.grantPermissions(mContext, mPermissions,
				new PermissionRequestActivity.PermissionHandler() {

					@Override
					public void onPermissionsResult(
							String[] grantedpermissions,
							String[] denied_permissions) {
						if (PermissionChecker
								.isPermissionEmpty(denied_permissions)) {
							listener.onAllGranted();
						} else {
							listener.onDenied(denied_permissions);
						}
					}
				});
	}
}
