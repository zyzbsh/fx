package fxtrader.com.app.http;

import android.text.TextUtils;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import fxtrader.com.app.config.LoginConfig;
import fxtrader.com.app.tools.EncryptionTool;
import fxtrader.com.app.tools.StringUtil;

/**
 * Created by wasser on 16/12/5.
 */
public class ParamsUtil {

    public static final String CLIENT_SECRET = "ht_caifu88888888";

    public void rates() throws IOException, URISyntaxException {
        final Map<String, String> params = new HashMap<>();
        params.put("client_id", "cf");
        params.put("method", "gdiex.market.kLine");
        params.put("random_str", StringUtil.getRandomString(11));
        params.put("timestamp", String.valueOf(new Date().getTime()));

        params.put("contract","CU");
        params.put("number","10");
        params.put("type","1");
        params.put("sign", sign(params));

    }

    public static Map<String, String> getCommonParams() {
        HashMap<String, String> params = new HashMap<>();
        params.put("client_id", "cf");
        params.put("random_str", StringUtil.getRandomString(11));
        params.put("timestamp", String.valueOf(new Date().getTime()));
        return params;
    }

    public static String sign(final Map<String,String> params){
        //先把参数取出封装成ArrayList,然后排序
        final List<String> paramNames = new ArrayList<>();
        for (Map.Entry<String, String> entry : params.entrySet()) {
            final String key = entry.getKey();
            if (!TextUtils.isEmpty(key) && !"sign".equals(key)) {
                paramNames.add(key);
            }
        }

        Collections.sort(paramNames);

        //将参数拼接成一个字符串
        final StringBuilder signBuilder = new StringBuilder();
        signBuilder.append(CLIENT_SECRET);

        for (String key : paramNames) {
            final String value = params.get(key);
            if (!TextUtils.isEmpty(value)) {
                signBuilder.append(key).append(value);
            }
        }

        signBuilder.append("#").append(CLIENT_SECRET);
        return EncryptionTool.md5(signBuilder.toString()).toUpperCase();
    }

    public static String getToken(){
        return "Bearer " + LoginConfig.getInstance().getToken();
    }

}
