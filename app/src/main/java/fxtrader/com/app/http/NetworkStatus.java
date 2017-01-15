package fxtrader.com.app.http;


import android.content.Context;

/**
 * 网络状态
 * 
 * @author zyu
 * 
 */
public class NetworkStatus {
	private static final int DEFAULT_NETWORK_TYPE = -1;
	private boolean connected = false;
	private int networkType = DEFAULT_NETWORK_TYPE;

	public NetworkStatus(Context context) {
		this.networkType = NetworkUtil.getConnectionType(context);
		if (this.networkType != NetworkUtil.CONNECTIONTYPE_NONE) {
			this.connected = true;
		}
	}

	public void clear() {
		this.connected = false;
		this.networkType = DEFAULT_NETWORK_TYPE;
	}

	public boolean isConnected() {
		return connected;
	}

	public void setConnected(boolean hasConnected) {
		this.connected = hasConnected;
	}

	public int getNetworkType() {
		return networkType;
	}

	public void setNetworkType(int networkType) {
		this.networkType = networkType;
	}

	@Override
	public String toString() {
		return "NetworkStatus [connected=" + connected + ", networkType=" + networkType + "]";
	}

}
