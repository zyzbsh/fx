package fxtrader.com.app.permission;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import fxtrader.com.app.R;
import fxtrader.com.app.tools.LogZ;

/**
 * 
 * @ClassName: PermissionReuqstActivity
 * @Description:TODO(申请权限的Activity)
 * @author: ZhangYuzhu
 * @date: 2016-4-11 下午3:35:05
 * 
 */
public class PermissionRequestActivity extends Activity {

	private static final String TAG = PermissionRequestActivity.class
			.getSimpleName();

	private final static String ACTION_PERMISSION_GRANT_RESULT = "action.permissions.grant.result";
	private final static String EXTRA_REQ_HANDLER_ID = "handler.id"; // 用于记录PermissionHandler的ID，在广播接收器中找回
	private final static String EXTRA_REQ_PERMISSIONS = "permissions";
	private final static String EXTRA_RET_PERMISSIONS_GRANTED = "permissions.granted";
	private final static String EXTRA_RET_PERMISSIONS_DENIED = "permissions.denied";
	private final static String ACTION_MANAGER_WRITE_SETTINGS = "android.settings.action.MANAGE_WRITE_SETTINGS";

	private boolean mResumed;
	private String[] mGrantPermissions; // 待授权的权限
	private String mHandlerId;
	private int mReqCode; // 记录请求码
	private int[] mGrantResults;

	public static interface PermissionHandler {
		/**
		 * @param grantedpermissions
		 *            获得授权的权限
		 * @param denied_permissions
		 *            被拒的权限
		 */
		public void onPermissionsResult(String[] grantedpermissions,
										String[] denied_permissions);
	}

	public static void grantPermissions(Context context, String[] permissions,
			PermissionHandler handler) {
		int targetSDKVer = context.getApplicationInfo().targetSdkVersion; // 只有目标SDK大于等于23的才会有授权
		if (targetSDKVer < 23) {
			handler.onPermissionsResult(permissions, null); // 低于Android
			// M版的调用直接当权限被授予
			return;
		}
		String[] denied_permissions = getGrantPermissions(context, permissions,
				PackageManager.PERMISSION_DENIED);
		if (denied_permissions == null) { // 全部被授权，直接回调handler
			handler.onPermissionsResult(permissions, null);
			return;
		}
		
		permissions = denied_permissions;
		String handlerId = generateHandlerId();
		PermissionGrantReceiver receiver = new PermissionGrantReceiver(context,
				handler, handlerId);
		IntentFilter filter = new IntentFilter(ACTION_PERMISSION_GRANT_RESULT);
		context.registerReceiver(receiver, filter);
		Intent intent = getGrantPermissionsIntent(context, permissions,
				handlerId);
		context.startActivity(intent);
		if (containWriteSettingsPermission(denied_permissions)) {
			grantSystemWriteSettings(context, handler); // 以便让权限界面盖在最上面
		}
	}

	public static void grantSystemWriteSettings(Context context,
			PermissionHandler handler) {
		int targetSDKVer = context.getApplicationInfo().targetSdkVersion; // 只有目标SDK大于等于23的才会有授权
		if (targetSDKVer < Build.VERSION_CODES.M || systemCanWrite(context)) {
			handler.onPermissionsResult(
					new String[] { Manifest.permission.WRITE_SETTINGS }, null);// 低于Android
																				// M版的调用直接当允许设置
			return;
		}
		Intent intent = new Intent(context, PermissionRequestActivity.class);
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		intent.setAction(ACTION_MANAGER_WRITE_SETTINGS);
		String handlerId = generateHandlerId();
		intent.putExtra(EXTRA_REQ_HANDLER_ID, handlerId);
		// PermissionGrantReceiver receiver = new
		// PermissionGrantReceiver(context, handler, handlerId);
		// IntentFilter filter = new
		// IntentFilter(ACTION_MANAGER_WRITE_SETTINGS);
		// context.registerReceiver(receiver, filter);
		context.startActivity(intent);
	}

	public static void requestSystemWriteSettings(Context context) {
		Uri selfPackageUri = Uri.parse("package:" + context.getPackageName());
		Intent intent = new Intent(
				"android.settings.action.MANAGE_WRITE_SETTINGS", selfPackageUri);
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		context.startActivity(intent);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setWindow();
		setContentView(getContentView());
		requestPermission();
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		final Intent intent = getIntent();
		// String action = intent != null ? intent.getAction() : null;
		if (!mResumed) {
			mResumed = true;
		} else /* if (ACTION_MANAGER_WRITE_SETTINGS.equals(action)) */{
			finish();
		}
	}
	
	@Override
	public void finish() {
		final Intent intent = getIntent();
		if (ACTION_MANAGER_WRITE_SETTINGS.equals(intent.getAction())) {
			Intent resultIntent = new Intent(ACTION_MANAGER_WRITE_SETTINGS);
			resultIntent.setPackage(getPackageName());
			resultIntent.putExtra(EXTRA_REQ_HANDLER_ID, mHandlerId);
			sendBroadcast(resultIntent);
		} else {
			String[] granted_permissions = null; // 已授权的
			String[] denied_permissions = null; // 被拒绝的
			if (mGrantResults == null && mGrantPermissions != null) {
				mGrantResults = new int[mGrantPermissions.length];
				String s;
				for (int k = 0; k < mGrantPermissions.length; k++) {
					s = mGrantPermissions[k];
					if (isWriteSettingsPermission(s)) {
						if (checkWriteSettingsResult(this, s,
								PackageManager.PERMISSION_GRANTED)) {
							mGrantResults[k] = PackageManager.PERMISSION_GRANTED;
						} else {
							mGrantResults[k] = PackageManager.PERMISSION_DENIED;
						}
					} else {
						if (checkSelfPermissionCompat(this, s,
								PackageManager.PERMISSION_GRANTED)) {
							mGrantResults[k] = PackageManager.PERMISSION_GRANTED;
						} else {
							mGrantResults[k] = PackageManager.PERMISSION_DENIED;
						}
					}
				}
			}
			if (mGrantResults != null) {
				granted_permissions = getGrantPermissions(mGrantPermissions,
						mGrantResults, PackageManager.PERMISSION_GRANTED);
				denied_permissions = getGrantPermissions(mGrantPermissions,
						mGrantResults, PackageManager.PERMISSION_DENIED);
			} else {
				denied_permissions = mGrantPermissions;
			}
			Intent resultIntent = new Intent(ACTION_PERMISSION_GRANT_RESULT);
			resultIntent.setPackage(getPackageName());
			if (granted_permissions != null) {
				resultIntent.putExtra(EXTRA_RET_PERMISSIONS_GRANTED,
						granted_permissions);
			}
			if (denied_permissions != null) {
				resultIntent.putExtra(EXTRA_RET_PERMISSIONS_DENIED,
						denied_permissions);
			}
			resultIntent.putExtra(EXTRA_REQ_HANDLER_ID, mHandlerId);
			sendBroadcast(resultIntent);
		}
		super.finish();
	}

	private void setWindow() {
		Window window = getWindow();
		WindowManager.LayoutParams lp = window.getAttributes();
		lp.alpha = 0.3f; // 不能全透，否会会出现onRequestPermissionsResult不回调
		window.setAttributes(lp);
		window.setBackgroundDrawable(new ColorDrawable(0));
	}

	private View getContentView() {
		View dummyView = new View(this);
		dummyView.setBackgroundResource(R.color.transparent);
		return dummyView;
	}

	private void requestPermission() {
		final Intent intent = getIntent();
		String action = intent.getAction();
		mHandlerId = intent.getStringExtra(EXTRA_REQ_HANDLER_ID);
		if (ACTION_MANAGER_WRITE_SETTINGS.equals(action)) {
			requestSystemWriteSettings(this);
		} else {
			mGrantPermissions = intent
					.getStringArrayExtra(EXTRA_REQ_PERMISSIONS);
			if (mGrantPermissions == null || mGrantPermissions.length == 0) {
				finish();
			} else {
				Random rand = new Random();
				mReqCode = rand.nextInt() & 0x7fffffff;
				String[] req_permissions = getShouldShowRequestPermissionRationale(mGrantPermissions);
				if (req_permissions == null || req_permissions.length == 0) { // 不需要显示授权，直接当已授权
					mGrantResults = new int[mGrantPermissions.length];
					for (int k = 0; k < mGrantResults.length; k++) {
						mGrantResults[k] = PackageManager.PERMISSION_GRANTED;
					}
					finish();
				} else {
					requestPermissionsCompat(req_permissions, mReqCode);
				}
			}
		}
	}

	private String[] getShouldShowRequestPermissionRationale(
			String[] permissions) {
		List<String> rets = new ArrayList<String>();
		for (String s : permissions) {
			if (checkSelfPermissionCompat(this, s,
					PackageManager.PERMISSION_DENIED)) {
				// if (shouldShowRequestPermissionRationaleCompat(s)) {
				rets.add(s);
				// }
			}
		}
		if (rets.size() > 0) {
			String[] sa = new String[rets.size()];
			rets.toArray(sa);
			return sa;
		} else {
			return null;
		}
	}

	private void requestPermissionsCompat(String[] permissions, int reqCode) {
		// this.requestPermissions(permissions, reqCode);
		ReflectHelper.callMethod(this, "requestPermissions", new Class<?>[] {
				String[].class, int.class }, new Object[] { permissions,
				reqCode });
	}

	// private void checkPermission() {
	// // mCheckId = getIntent().getIntExtra(PermissionChecker.ITEM_ID, 0);
	// String[] permissions = getIntent().getStringArrayExtra(
	// PermissionChecker.ITEM_PERMISSONS);
	// if (PermissionUtil.checkSelfPermissions(this, permissions)) {
	// PermissionChecker.getPermissionHandler().onGranted(permissions);
	// finish();
	// } else {
	// // PermissionUtil.showPermissionPreview(this, permissions,
	// // mCheckId);
	// }
	// }

	@Override
	public void onRequestPermissionsResult(int requestCode,
			String[] permissions, int[] grantResults) {
		if (requestCode != mReqCode) {
			finish();
			LogZ.e("onRequestPermissionsResult return, requestCode="
					+ requestCode + " not equal to " + mReqCode);
		} else {
			try {
				mGrantResults = grantResults;
			} catch (Exception e) {
				LogZ.e("onRequestPermissionsResult call error, reason="
						+ e);
			}
		}
		if (!isFinishing()) {
			finish();
		}
	}

	private static String[] getGrantPermissions(Context context,
			String[] permissions, int check_result) {
		ArrayList<String> rets = new ArrayList<String>();
		for (String s : permissions) {
			if (isWriteSettingsPermission(s)) {
				if (checkWriteSettingsResult(context, s, check_result)) {
					rets.add(s);
				}
			} else {
				if (checkSelfPermissionCompat(context, s, check_result)) {
					rets.add(s);
				}
			}
		}
		if (rets.size() > 0) {
			String[] sa = new String[rets.size()];
			rets.toArray(sa);
			return sa;
		} else {
			return null;
		}
	}

	private static boolean checkSelfPermissionCompat(Context context,
			String permission, int permission_result) {
		// int ret= checkSelfPermission(permission);
		Object obj = ReflectHelper.callMethod(context, "checkSelfPermission",
				new Class<?>[] { String.class }, new Object[] { permission });
		if (obj != null) {
			if (obj instanceof Integer) {
				return ((Integer) obj).intValue() == permission_result;
			}
		} 
		return false;
	}

	/**
	 * 生成PermissionHandler的ID
	 * 
	 * @return
	 */
	private static String generateHandlerId() {
		Random rand = new Random(System.currentTimeMillis());
		long now = System.currentTimeMillis();
		String id = String.format("%x-%x", now, rand.nextLong());
		return id;
	}

	private String[] getGrantPermissions(String[] permissions,
			int[] grant_results, int check_result) {
		ArrayList<String> rets = new ArrayList<String>();
		int k = 0;
		for (k = 0; k < grant_results.length; k++) {
			if (isWriteSettingsPermission(permissions[k])) {
				if (checkWriteSettingsResult(this, permissions[k], check_result)) {
					rets.add(permissions[k]);
				}
			} else if (grant_results[k] == check_result) {
				if (k < permissions.length) {
					rets.add(permissions[k]);
				}
			}
		}
		if (rets.size() > 0) {
			String[] sa = new String[rets.size()];
			rets.toArray(sa);
			return sa;
		} else {
			return null;
		}
	}

	private static boolean isWriteSettingsPermission(String permission) {
		return Manifest.permission.WRITE_SETTINGS.equals(permission);
	}

	private static boolean checkWriteSettingsResult(Context context,
			String permission, int permission_result) {
		if (!isWriteSettingsPermission(permission)) {
			return false;
		}
		if (permission_result == PackageManager.PERMISSION_GRANTED) {
			if (systemCanWrite(context)) {
				return true;
			}
		} else {
			if (!systemCanWrite(context)) {
				return true;
			}
		}
		return false;
	}

	private static boolean systemCanWrite(Context context) {
		Object obj = ReflectHelper.callStaticMethod(Settings.System.class,
				"canWrite", new Class<?>[] { Context.class },
				new Object[] { context });
		if (obj != null && obj instanceof Boolean) {
			return ((Boolean) obj).booleanValue();
		} else {
			return true;// 一旦出现反射结果不正常，就当可以写，以免出现后续流程不正常
		}
	}

	private static Intent getGrantPermissionsIntent(Context context,
			String[] permissions, String handlerId) {
		Intent intent = new Intent(context, PermissionRequestActivity.class);
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		intent.putExtra(EXTRA_REQ_PERMISSIONS, permissions);
		intent.putExtra(EXTRA_REQ_HANDLER_ID, handlerId);
		return intent;
	}

	private static boolean containWriteSettingsPermission(String[] permissions) {
		for (String s : permissions) {
			if (isWriteSettingsPermission(s)) {
				return true;
			}
		}
		return false;
	}

	private static class PermissionGrantReceiver extends BroadcastReceiver
			implements Runnable {
		private Context mRegisterContext;
		private PermissionHandler mPermissionHandler;
		private String mHandlerId;

		private PermissionGrantReceiver(Context context,
				PermissionHandler handler, String handlerId) {
			mRegisterContext = context;
			mPermissionHandler = handler;
			mHandlerId = handlerId;
		}

		@Override
		public void onReceive(Context context, Intent intent) {
			if (intent == null) {
				return;
			}
			String action = intent.getAction();
			String handlerId = intent.getStringExtra(EXTRA_REQ_HANDLER_ID);
			if (handlerId == null) { // 不可能进到这个逻辑
				LogZ.e("onReceiver error, call unRegisterMe ");
				unRegisterMe();
			} else if (mHandlerId.equals(handlerId)) {
				if (ACTION_MANAGER_WRITE_SETTINGS.equals(action)) {
					// Handler handler = new
					// Handler(mRegisterContext.getMainLooper());
					// handler.post(new Runnable() {
					// @Override
					// public void run() {
					// mPermissionHandler.onSystemWriteSettings(systemCanWrite(mRegisterContext));
					// }
					// });
				} else if (ACTION_PERMISSION_GRANT_RESULT.equals(action)) {
					final String[] granted_permissions = intent
							.getStringArrayExtra(EXTRA_RET_PERMISSIONS_GRANTED);
					final String[] denied_permissions = intent
							.getStringArrayExtra(EXTRA_RET_PERMISSIONS_DENIED);
					Handler handler = new Handler(
							mRegisterContext.getMainLooper());
					handler.post(new Runnable() {
						@Override
						public void run() {
							mPermissionHandler.onPermissionsResult(
									granted_permissions, denied_permissions);
						}
					});
				}
				unRegisterMe();
			}
		}

		private void unRegisterMe() {
			Handler handler = new Handler(mRegisterContext.getMainLooper());
			handler.post(this);
		}

		@Override
		public void run() {
			try {
				mRegisterContext.unregisterReceiver(this);
			} catch (Exception e) {
			}
		}
	}

}
