package fxtrader.com.app.entity;

/**
 * Created by pc on 2017/3/8.
 */
public class YesterdayRankEntity {
    private long buyingDate;
    private double buyingRate;
    private String codeName;
    private int customerId;
    private int dealCount;
    private int dealDirection;
    private String headImg;
    private String nickname;
    private String profit;
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

    public String getProfit() {
        return profit;
    }

    public void setProfit(String profit) {
        this.profit = profit;
    }

    public boolean isSubscribe() {
        return subscribe;
    }

    public void setSubscribe(boolean subscribe) {
        this.subscribe = subscribe;
    }

    @Override
    public String toString() {
        return "YesterdayRankEntity{" +
                "buyingDate=" + buyingDate +
                ", buyingRate=" + buyingRate +
                ", codeName='" + codeName + '\'' +
                ", customerId=" + customerId +
                ", dealCount=" + dealCount +
                ", dealDirection=" + dealDirection +
                ", headImg='" + headImg + '\'' +
                ", nickname='" + nickname + '\'' +
                ", profit='" + profit + '\'' +
                ", subscribe=" + subscribe +
                '}';
    }
}
