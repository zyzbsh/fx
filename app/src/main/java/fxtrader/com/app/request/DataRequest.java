//package fxtrader.com.app.request;
//
//import java.util.HashMap;
//import java.util.Map;
//
//import com.android.volley.AuthFailureError;
//import com.android.volley.NetworkResponse;
//import com.android.volley.ParseError;
//import com.android.volley.Response;
//import com.android.volley.Response.ErrorListener;
//import com.android.volley.Response.Listener;
//import com.android.volley.toolbox.StringRequest;
//
//import fxtrader.com.app.tools.Auth;
//
//public class DataRequest extends StringRequest {
//
//	private Map<String, String> params = null;;
//
//
//
//	public DataRequest(int method, String url, Listener<String> listener,
//			ErrorListener errorListener) {
//		super(method, url, listener, errorListener);
//	}
//
//	public DataRequest(String url, Listener<String> listener,
//			ErrorListener errorListener) {
//		super(url, listener, errorListener);
//	}
//
//	@Override
//	protected void deliverResponse(String response) {
//		super.deliverResponse(response);
//	}
//
//	@Override
//	protected Response<String> parseNetworkResponse(NetworkResponse response) {
//		if (200 == response.statusCode) {
//			return super.parseNetworkResponse(response);
//		} else {
//			return Response.error(new ParseError(response));
//		}
//
//	}
//
//	@Override
//	public Map<String, String> getHeaders() throws AuthFailureError {
//		if (null != Auth.getHeader()) {
//			Map<String, String> map = new HashMap<String, String>();
//			map.put(Auth.AUTH, Auth.getHeader());
//			return map;
//		}
//		return super.getHeaders();
//	}
//
//	@Override
//	protected Map<String, String> getParams() throws AuthFailureError {
//		if (null != params) {
//			return params;
//		}
//		return super.getParams();
//	}
//
//	public void setParams(Map<String, String> params) {
//		this.params = params;
//	}
//
//}
