//package fxtrader.com.app.request;
//
//import com.android.volley.Response.Listener;
//
//import fxtrader.com.app.view.HorizontalProgressDialog;
//
//public class SuccessListener implements Listener<String> {
//
//	private HorizontalProgressDialog loadDialog = null;
//
//	public SuccessListener(HorizontalProgressDialog loadDialog) {
//		this.loadDialog = loadDialog;
//	}
//
//	@Override
//	public void onResponse(String response) {
//		if (null != loadDialog) {
//			loadDialog.dismiss();
//			loadDialog = null;
//		}
//	}
//
//}
