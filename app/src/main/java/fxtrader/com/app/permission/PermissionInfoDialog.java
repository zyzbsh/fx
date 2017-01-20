package fxtrader.com.app.permission;

import android.annotation.TargetApi;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import fxtrader.com.app.R;

public final class PermissionInfoDialog extends Dialog {

	private String mTitle = "";
	private String mContent = "";
	private OnExitListener mExitBtnListener;
	private OnSettingListener mSettingBtnListener;

	private PermissionInfoDialog(Context context) {
		super(context, R.style.iclouddialog);
	}

	public static PermissionInfoDialog build(Context context) {
		return new PermissionInfoDialog(context);
	}

	public PermissionInfoDialog setTitle(String title) {
		mTitle = title;
		return this;
	}

	public PermissionInfoDialog setContent(String content) {
		mContent = getContext().getString(R.string.permission_notice_content,
				content);
		return this;
	}

	public PermissionInfoDialog setExitBtnListener(OnExitListener listener) {
		mExitBtnListener = listener;
		return this;
	}

	public PermissionInfoDialog setSettingBtnListener(OnSettingListener listener) {
		mSettingBtnListener = listener;
		return this;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.permission_request_dialog);
		setCanceledOnTouchOutside(false);
		initView();
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK){
			return true;
		}
		return false;
	}

	private void initView() {
		initTitleTv();
		initContentTv();
		initExitBtn();
		initSettingBtn();
	}

	private void initTitleTv() {
		TextView titleTv = (TextView) findViewById(R.id.permission_dialog_title_tv);
		if (isExistStr(mTitle)) {
			titleTv.setText(mTitle);
		}
	}

	private void initContentTv() {
		TextView contentTv = (TextView) findViewById(R.id.permission_dialog_content_tv);
		if (isExistStr(mContent)) {
			contentTv.setText(mContent);
		}
	}

	private void initExitBtn() {
		Button btn = (Button) findViewById(R.id.permission_dialog_exit_btn);
		btn.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				if (mExitBtnListener != null) {
					mExitBtnListener.onExit();
					dismiss();
				}
			}
		});
	}

	private void initSettingBtn() {
		Button btn = (Button) findViewById(R.id.permission_dialog_setting_btn);
		btn.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				if (mSettingBtnListener != null) {
					mSettingBtnListener.onSetting();
					dismiss();
				}
			}
		});
	}

	private boolean isExistStr(String str) {
		return str != null && !"".equals(str);
	}

	public static Dialog getDefault(final Context context, String content) {
		String title = context.getString(R.string.permission_notice_title);
		Dialog dialog = PermissionInfoDialog.build(context).setTitle(title)
				.setContent(content).setExitBtnListener(new OnExitListener() {

					@Override
					public void onExit() {
						System.exit(0);
					}
				}).setSettingBtnListener(new OnSettingListener() {

					@Override
					public void onSetting() {
						startAppSettings(context);
					}
				});
		return dialog;
	}

	public interface OnExitListener {
		public void onExit();
	}

	public interface OnSettingListener {
		public void onSetting();
	}
	
	@TargetApi(Build.VERSION_CODES.GINGERBREAD)
	private static void startAppSettings(Context context) {
		Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
		intent.setData(Uri.parse("package:" + context.getPackageName()));
		context.startActivity(intent);
	}

}
