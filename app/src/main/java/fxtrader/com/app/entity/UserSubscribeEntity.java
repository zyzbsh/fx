package fxtrader.com.app.entity;

/**
 * Created by zhangyuzhu on 2016/12/28.
 */
public class UserSubscribeEntity {

    /**
     * bLAST_LOSS : -1
     * buyingRate : 0
     * customerId : 171
     * dataType : 0
     * dealCount : 0
     * dealDirection : 0
     * handingChargeAmount : 0
     * headImgUrl :
     * loss : -1
     * nickname : pure
     * payAmount : 0
     * profit : 0
     * profitAndLoss : 0
     * sale : false
     * sellingIncome : 0
     * sellingRate : 0
     * subscribe : true
     * ticketCount : 0
     */

    private int bLAST_LOSS;
    private double buyingRate;
    private int customerId;
    private int dataType;
    private int dealCount;
    private int dealDirection;
    private int handingChargeAmount;
    private String headImgUrl;
    private int loss;
    private String nickname;
    private double payAmount;
    private double profit;
    private double profitAndLoss;
    private boolean sale;
    private double sellingIncome;
    private double sellingRate;
    private boolean subscribe;
    private int ticketCount;

    public int getBLAST_LOSS() {
        return bLAST_LOSS;
    }

    public void setBLAST_LOSS(int bLAST_LOSS) {
        this.bLAST_LOSS = bLAST_LOSS;
    }

    public double getBuyingRate() {
        return buyingRate;
    }

    public void setBuyingRate(double buyingRate) {
        this.buyingRate = buyingRate;
    }

    public int getCustomerId() {
        return customerId;
    }

    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }

    public int getDataType() {
        return dataType;
    }

    public void setDataType(int dataType) {
        this.dataType = dataType;
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

    public int getHandingChargeAmount() {
        return handingChargeAmount;
    }

    public void setHandingChargeAmount(int handingChargeAmount) {
        this.handingChargeAmount = handingChargeAmount;
    }

    public String getHeadImgUrl() {
        return headImgUrl;
    }

    public void setHeadImgUrl(String headImgUrl) {
        this.headImgUrl = headImgUrl;
    }

    public int getLoss() {
        return loss;
    }

    public void setLoss(int loss) {
        this.loss = loss;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public double getPayAmount() {
        return payAmount;
    }

    public void setPayAmount(double payAmount) {
        this.payAmount = payAmount;
    }

    public double getProfit() {
        return profit;
    }

    public void setProfit(double profit) {
        this.profit = profit;
    }

    public double getProfitAndLoss() {
        return profitAndLoss;
    }

    public void setProfitAndLoss(double profitAndLoss) {
        this.profitAndLoss = profitAndLoss;
    }

    public boolean isSale() {
        return sale;
    }

    public void setSale(boolean sale) {
        this.sale = sale;
    }

    public double getSellingIncome() {
        return sellingIncome;
    }

    public void setSellingIncome(double sellingIncome) {
        this.sellingIncome = sellingIncome;
    }

    public double getSellingRate() {
        return sellingRate;
    }

    public void setSellingRate(double sellingRate) {
        this.sellingRate = sellingRate;
    }

    public boolean isSubscribe() {
        return subscribe;
    }

    public void setSubscribe(boolean subscribe) {
        this.subscribe = subscribe;
    }

    public int getTicketCount() {
        return ticketCount;
    }

    public void setTicketCount(int ticketCount) {
        this.ticketCount = ticketCount;
    }
}
