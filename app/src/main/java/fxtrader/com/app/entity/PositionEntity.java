package fxtrader.com.app.entity;

/**
 * Created by zhangyuzhu on 2016/12/19.
 */
public class PositionEntity {
    /**
     * autoSale : false
     * buyingDate : 1482139240991
     * buyingRate : 5467.5
     * consumeAmount : 106
     * contractCode : YDHF
     * customerId : 53
     * dealCount : 1
     * dealDirection : UP
     * divideStatus : NOT_DIVIDE
     * exception : false
     * handingChargeAmount : 6
     * id : 128184813013028864
     * loss : -1
     * memberCode : FT
     * payAmount : 100
     * profit : 0
     * profitAndLoss : 0
     * rollBackStatus : DEFAULT
     * sale : false
     * saleTimestamp : 0
     * sellingIncome : 0
     * sellingRate : 0
     * usedTicket : false
     * usedTicketCount : 0
     * usedTicketId : 0
     * usedTicketValueAmount : 0
     */

    private boolean autoSale;
    private long buyingDate;
    private double buyingRate;
    private String consumeAmount;
    private String contractCode;
    private int customerId;
    private int dealCount;
    private String dealDirection;
    private String divideStatus;
    private boolean exception;
    private double handingChargeAmount;
    private String id;
    private int loss;
    private String memberCode;
    private int payAmount;
    private int profit;
    private int profitAndLoss;
    private String rollBackStatus;
    private boolean sale;
    private int saleTimestamp;
    private int sellingIncome;
    private int sellingRate;
    private boolean usedTicket;
    private int usedTicketCount;
    private int usedTicketId;
    private int usedTicketValueAmount;

    public boolean isAutoSale() {
        return autoSale;
    }

    public void setAutoSale(boolean autoSale) {
        this.autoSale = autoSale;
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

    public String getConsumeAmount() {
        return consumeAmount;
    }

    public void setConsumeAmount(String consumeAmount) {
        this.consumeAmount = consumeAmount;
    }

    public String getContractCode() {
        return contractCode;
    }

    public void setContractCode(String contractCode) {
        this.contractCode = contractCode;
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

    public String getDealDirection() {
        return dealDirection;
    }

    public void setDealDirection(String dealDirection) {
        this.dealDirection = dealDirection;
    }

    public String getDivideStatus() {
        return divideStatus;
    }

    public void setDivideStatus(String divideStatus) {
        this.divideStatus = divideStatus;
    }

    public boolean isException() {
        return exception;
    }

    public void setException(boolean exception) {
        this.exception = exception;
    }

    public double getHandingChargeAmount() {
        return handingChargeAmount;
    }

    public void setHandingChargeAmount(double handingChargeAmount) {
        this.handingChargeAmount = handingChargeAmount;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getLoss() {
        return loss;
    }

    public void setLoss(int loss) {
        this.loss = loss;
    }

    public String getMemberCode() {
        return memberCode;
    }

    public void setMemberCode(String memberCode) {
        this.memberCode = memberCode;
    }

    public int getPayAmount() {
        return payAmount;
    }

    public void setPayAmount(int payAmount) {
        this.payAmount = payAmount;
    }

    public int getProfit() {
        return profit;
    }

    public void setProfit(int profit) {
        this.profit = profit;
    }

    public int getProfitAndLoss() {
        return profitAndLoss;
    }

    public void setProfitAndLoss(int profitAndLoss) {
        this.profitAndLoss = profitAndLoss;
    }

    public String getRollBackStatus() {
        return rollBackStatus;
    }

    public void setRollBackStatus(String rollBackStatus) {
        this.rollBackStatus = rollBackStatus;
    }

    public boolean isSale() {
        return sale;
    }

    public void setSale(boolean sale) {
        this.sale = sale;
    }

    public int getSaleTimestamp() {
        return saleTimestamp;
    }

    public void setSaleTimestamp(int saleTimestamp) {
        this.saleTimestamp = saleTimestamp;
    }

    public int getSellingIncome() {
        return sellingIncome;
    }

    public void setSellingIncome(int sellingIncome) {
        this.sellingIncome = sellingIncome;
    }

    public int getSellingRate() {
        return sellingRate;
    }

    public void setSellingRate(int sellingRate) {
        this.sellingRate = sellingRate;
    }

    public boolean isUsedTicket() {
        return usedTicket;
    }

    public void setUsedTicket(boolean usedTicket) {
        this.usedTicket = usedTicket;
    }

    public int getUsedTicketCount() {
        return usedTicketCount;
    }

    public void setUsedTicketCount(int usedTicketCount) {
        this.usedTicketCount = usedTicketCount;
    }

    public int getUsedTicketId() {
        return usedTicketId;
    }

    public void setUsedTicketId(int usedTicketId) {
        this.usedTicketId = usedTicketId;
    }

    public int getUsedTicketValueAmount() {
        return usedTicketValueAmount;
    }

    public void setUsedTicketValueAmount(int usedTicketValueAmount) {
        this.usedTicketValueAmount = usedTicketValueAmount;
    }

    @Override
    public String toString() {
        return "PositionEntity{" +
                "autoSale=" + autoSale +
                ", buyingDate=" + buyingDate +
                ", buyingRate=" + buyingRate +
                ", consumeAmount=" + consumeAmount +
                ", contractCode='" + contractCode + '\'' +
                ", customerId=" + customerId +
                ", dealCount=" + dealCount +
                ", dealDirection='" + dealDirection + '\'' +
                ", divideStatus='" + divideStatus + '\'' +
                ", exception=" + exception +
                ", handingChargeAmount=" + handingChargeAmount +
                ", id='" + id + '\'' +
                ", loss=" + loss +
                ", memberCode='" + memberCode + '\'' +
                ", payAmount=" + payAmount +
                ", profit=" + profit +
                ", profitAndLoss=" + profitAndLoss +
                ", rollBackStatus='" + rollBackStatus + '\'' +
                ", sale=" + sale +
                ", saleTimestamp=" + saleTimestamp +
                ", sellingIncome=" + sellingIncome +
                ", sellingRate=" + sellingRate +
                ", usedTicket=" + usedTicket +
                ", usedTicketCount=" + usedTicketCount +
                ", usedTicketId=" + usedTicketId +
                ", usedTicketValueAmount=" + usedTicketValueAmount +
                '}';
    }
}
