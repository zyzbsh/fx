package fxtrader.com.app.permission;

public interface IPermissionRequest {
	public void onAllGranted();
	public void onDenied(String[] permissions);
}
