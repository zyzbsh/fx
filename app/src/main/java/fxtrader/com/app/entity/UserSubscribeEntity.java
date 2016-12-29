package fxtrader.com.app.entity;

/**
 * Created by zhangyuzhu on 2016/12/28.
 */
public class UserSubscribeEntity {


    /**
     * bLAST_LOSS : -1
     * buyingDate : 1482388762000
     * buyingRate : 2833.7
     * contractCode : YDOIL
     * contractName : 0.2t粤东油
     * customerId : 207
     * dataType : 0
     * dealCount : 1
     * dealDirection : 0
     * handingChargeAmount : 0
     * headImgUrl :
     * id : 129231384042090496
     * loss : -1
     * nickname : MADAO。
     * payAmount : 0
     * profit : 0
     * profitAndLoss : 0
     * sale : false
     * sellingIncome : 0
     * sellingRate : 0
     * subscribe : false
     * ticketCount : 0
     * "usedTicketCount": 0//使用礼券数量
     */

    private int bLAST_LOSS;
    private long buyingDate;
    private double buyingRate;
    private String contractCode;
    private String contractName;
    private int customerId;
    private int dataType;
    private int dealCount;
    private int dealDirection;
    private int handingChargeAmount;
    private String headImgUrl;
    private String id;
    private double loss;
    private String nickname;
    private double payAmount;
    private double profit;
    private double profitAndLoss;
    private boolean sale;
    private double sellingIncome;
    private double sellingRate;
    private boolean subscribe;
    private int ticketCount;
    private int usedTicketCount;

    public int getBLAST_LOSS() {
        return bLAST_LOSS;
    }

    public void setBLAST_LOSS(int bLAST_LOSS) {
        this.bLAST_LOSS = bLAST_LOSS;
    }

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

    public String getContractCode() {
        return contractCode;
    }

    public void setContractCode(String contractCode) {
        this.contractCode = contractCode;
    }

    public String getContractName() {
        return contractName;
    }

    public void setContractName(String contractName) {
        this.contractName = contractName;
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

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public double getLoss() {
        return loss;
    }

    public void setLoss(double loss) {
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

    public int getUsedTicketCount() {
        return usedTicketCount;
    }

    public void setUsedTicketCount(int usedTicketCount) {
        this.usedTicketCount = usedTicketCount;
    }

    @Override
    public String toString() {
        return "UserSubscribeEntity{" +
                "bLAST_LOSS=" + bLAST_LOSS +
                ", buyingDate=" + buyingDate +
                ", buyingRate=" + buyingRate +
                ", contractCode='" + contractCode + '\'' +
                ", contractName='" + contractName + '\'' +
                ", customerId=" + customerId +
                ", dataType=" + dataType +
                ", dealCount=" + dealCount +
                ", dealDirection=" + dealDirection +
                ", handingChargeAmount=" + handingChargeAmount +
                ", headImgUrl='" + headImgUrl + '\'' +
                ", id='" + id + '\'' +
                ", loss=" + loss +
                ", nickname='" + nickname + '\'' +
                ", payAmount=" + payAmount +
                ", profit=" + profit +
                ", profitAndLoss=" + profitAndLoss +
                ", sale=" + sale +
                ", sellingIncome=" + sellingIncome +
                ", sellingRate=" + sellingRate +
                ", subscribe=" + subscribe +
                ", ticketCount=" + ticketCount +
                ", usedTicketCount=" + usedTicketCount +
                '}';
    }
}
