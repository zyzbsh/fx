package fxtrader.com.app.entity;

import android.text.TextUtils;
import android.util.Log;

import java.util.HashMap;
import java.util.Map;

/**
 * 实时行情
 * Created by zhangyuzhu on 2016/12/6.
 */
public class MarketEntity extends CommonResponse{

    private Data object;

    private String latestPrice = "";

    private String openPrice = "";

    private String lastClosePrice = "";

    private String highestPrice = "";

    private String lowestPrice = "";

    private boolean isMarketOpen = false;

    private Map<String, String> data;

    public void init() {
        if (data == null) {
            data = new HashMap<>();
        }
        data.put("AG", object.AG);
        data.put("CU", object.CU);
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

    static class Data {
        String CU;
        String AG;

        @Override
        public String toString() {
            return "Data{" +
                    "CU='" + CU + '\'' +
                    ", AG='" + AG + '\'' +
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
