package fxtrader.com.app.entity;

/**
 * Created by pc on 2016/12/17.
 */
public class PriceEntity {

    private String latestPrice = "";

    private String openPrice = "";

    private String lastClosePrice = "";

    private String highestPrice = "";

    private String lowestPrice = "";

    private boolean isMarketOpen = false;

    public PriceEntity(String data) {
        try {
            String[] a = data.split(",");
            latestPrice = a[0];
            openPrice = a[1];
            lastClosePrice = a[2];
            highestPrice = a[3];
            lowestPrice = a[4];
            isMarketOpen = Boolean.parseBoolean(a[5]);
        } catch (Exception e) {

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

    @Override
    public String toString() {
        return "PriceEntity{" +
                "latestPrice='" + latestPrice + '\'' +
                ", openPrice='" + openPrice + '\'' +
                ", lastClosePrice='" + lastClosePrice + '\'' +
                ", highestPrice='" + highestPrice + '\'' +
                ", lowestPrice='" + lowestPrice + '\'' +
                ", isMarketOpen=" + isMarketOpen +
                '}';
    }
}
