package fxtrader.com.app.entity;

/**
 * Created by pc on 2016/12/26.
 */
public class MasterEntity {

    /**
     * buyingDate : 1482388153415
     * buyingRate : 2833.7
     * codeName : 15t粤东油
     * customerId : 207
     * dealCount : 1
     * dealDirection : 1
     * headImg :
     * nickname : MADAO。
     * profit : 2005.4999999999973
     * subscribe : false
     */

    private long buyingDate;
    private double buyingRate;
    private String codeName;
    private int customerId;
    private int dealCount;
    private int dealDirection;
    private String headImg;
    private String nickname;
    private double profit;
    private boolean subscribe;

    public long getBuyingDate() {
        return buyingDate;
    }

    public void setBuyingDate(long buyingDate) {
        this.buyingDate = buyingDate;
    }

    public double getBuyingRate() {
        return buyingRate;
    }

    public void setBuyingRate(double buyingRate) {
        this.buyingRate = buyingRate;
    }

    public String getCodeName() {
        return codeName;
    }

    public void setCodeName(String codeName) {
        this.codeName = codeName;
    }

    public int getCustomerId() {
        return customerId;
    }

    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }

    public int getDealCount() {
        return dealCount;
    }

    public void setDealCount(int dealCount) {
        this.dealCount = dealCount;
    }

    public int getDealDirection() {
        return dealDirection;
    }

    public void setDealDirection(int dealDirection) {
        this.dealDirection = dealDirection;
    }

    public String getHeadImg() {
        return headImg;
    }

    public void setHeadImg(String headImg) {
        this.headImg = headImg;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public double getProfit() {
        return profit;
    }

    public void setProfit(double profit) {
        this.profit = profit;
    }

    public boolean isSubscribe() {
        return subscribe;
    }

    public void setSubscribe(boolean subscribe) {
        this.subscribe = subscribe;
    }
}
