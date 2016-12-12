package fxtrader.com.app.tools;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo.State;

public class NetTools {

	public static boolean isWiFiState(Context context) {
//		ConnectivityManager connManager = (ConnectivityManager) context
//				.getSystemService(Context.CONNECTIVITY_SERVICE);
//		State state = connManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI)
//				.getState();
//		if (State.CONNECTED == state) {
//			return true;
//		}
		return true;
	}

}
