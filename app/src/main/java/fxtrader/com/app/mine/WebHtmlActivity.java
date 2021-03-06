package fxtrader.com.app.mine;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import java.util.HashMap;
import java.util.Map;

import fxtrader.com.app.R;
import fxtrader.com.app.base.BaseActivity;
import fxtrader.com.app.constant.IntentItem;
import fxtrader.com.app.http.HttpConstant;
import fxtrader.com.app.http.ParamsUtil;
import fxtrader.com.app.tools.LogZ;

/**
 * Created by zhangyuzhu on 2016/12/23.
 */
public class WebHtmlActivity extends BaseActivity {

    private WebView mWebView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentLayout(R.layout.activity_recharge_web);
        setBackListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setResult(RESULT_OK);
            }
        });
        mWebView = (WebView) findViewById(R.id.recharge_web_view);
        WebSettings webSettings = mWebView.getSettings();
        webSettings.setUseWideViewPort(true);//设置此属性，可任意比例缩放
        webSettings.setLoadWithOverviewMode(true);
        webSettings.setJavaScriptEnabled(true);
        mWebView.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
        webSettings.setBuiltInZoomControls(true);
        webSettings.setSupportZoom(true);
//        String ua = mWebView.getSettings().getUserAgentString();
//        webSettings.setUserAgentString(ua + "; Authorization:Bearer /" + ParamsUtil.getToken());
//        Map<String, String> map = new HashMap<>();
//        map.put("Authorization", ParamsUtil.getToken());
//        mWebView.postUrl("http://125.88.152.51:15516/api/currency/unionpay", getData());
        String html = getIntent().getStringExtra(IntentItem.RECHARGE_HTML);
        mWebView.loadDataWithBaseURL(null, html, "text/html", "UTF-8", null);
    }

    private byte[] getData() {
        Map<String, String> map = getRechargerParams("500");
        StringBuilder builder = new StringBuilder();
        for (String key : map.keySet()) {
            builder.append(key).append("=").append(map.get(key)).append("&");
        }
        LogZ.i("http://125.88.152.51:15516/api/currency/unionpay?" + builder.toString());
        return builder.toString().getBytes();
    }

    private String getTestData() {
        Map<String, String> map = getRechargerParams("500");
        StringBuilder builder = new StringBuilder();
        for (String key : map.keySet()) {
            builder.append(key).append("=").append(map.get(key)).append("&");
        }
        LogZ.i("http://125.88.152.51:15516/api/currency/unionpay?" + builder.toString());
        return builder.toString();
    }


    private Map<String, String> getRechargerParams(String amount) {
        final Map<String, String> params = ParamsUtil.getCommonParams();
        params.put("method", "gdiex.currency.unionpay");
        params.put("amount", amount);
        params.put("sign", ParamsUtil.sign(params));
        return params;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        setResult(RESULT_OK);
    }
}
