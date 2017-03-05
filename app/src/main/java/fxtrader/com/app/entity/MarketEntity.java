package fxtrader.com.app.entity;

import android.text.TextUtils;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import fxtrader.com.app.http.HttpConstant;
import fxtrader.com.app.tools.LogZ;

/**
 * 实时行情
 * Created by zhangyuzhu on 2016/12/6.
 */
public class MarketEntity extends CommonResponse implements Serializable{

    private Data object;

    private String priceJson;

    private String latestPrice = "";

    private String openPrice = "";

    private String lastClosePrice = "";

    private String highestPrice = "";

    private String lowestPrice = "";

    private boolean isMarketOpen = false;

    private Map<String, String> data;

    public String getPriceJson() {
        return priceJson;
    }

    public void setPriceJson(String priceJson) {
        this.priceJson = priceJson;
    }

    public void init() {
        if (data == null) {
            data = new HashMap<>();
        }
        if (!TextUtils.isEmpty(priceJson)) {
            try {
                LogZ.i(priceJson);
                JSONObject jsonObject = new JSONObject(priceJson);
                Iterator keys = jsonObject.keys();
                while(keys.hasNext()){
                    String key = (String) keys.next();
                    data.put(key, jsonObject.optString(key));
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    public String getData(String type) {
        if (data == null) {
            return "";
        }
        return data.get(type);
    }


    private void initData(String data) {
        try {
            String[] a = data.split(",");
            this.latestPrice = a[0];
            this.openPrice = a[1];
            this.lastClosePrice = a[2];
            this.highestPrice = a[3];
            this.lowestPrice = a[4];
            this.isMarketOpen = Boolean.parseBoolean(a[5]);
        } catch(Exception e) {
            Log.e("zyu", e.getMessage());
        }
    }

    public String getLatestPrice() {
        return latestPrice;
    }

    public String getOpenPrice() {
        return openPrice;
    }

    public String getLastClosePrice() {
        return lastClosePrice;
    }

    public String getHighestPrice() {
        return highestPrice;
    }

    public String getLowestPrice() {
        return lowestPrice;
    }

    public boolean isMarketOpen() {
        return isMarketOpen;
    }

    static class Data implements Serializable{
        String YDHF;
        String YDCL;

        @Override
        public String toString() {
            return "Data{" +
                    "YDCL='" + YDCL + '\'' +
                    ", YDHF='" + YDHF + '\'' +
                    '}';
        }
    }

    @Override
    public String toString() {
        return super.toString() + "MarketEntity{" +
                "object=" + object +
                '}';
    }
}
