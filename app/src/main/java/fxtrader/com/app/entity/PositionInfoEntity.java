package fxtrader.com.app.entity;

import java.io.Serializable;

/**
 * Created by pc on 2016/12/21.
 */
public class PositionInfoEntity implements Serializable{
    /**
     * autoSale : false
     * buyingDate : 1477561516000
     * buyingRate : 3831
     * consumeAmount : 0
     * contractCode : AG01
     * customerId : 121
     * dealCount : 0
     * dealDirection : UP
     * divideStatus : DIVIDE
     * exception : false
     * handingChargeAmount : 0
     * id : 108984446525812736
     * loss : -1
     * memberCode : HT
     * payAmount : 0
     * profit : 0
     * profitAndLoss : 0
     * rollBackStatus : DEFAULT
     * sale : true
     * saleTimestamp : 1477562432
     * sellingDate : 1477562432000
     * sellingIncome : 0
     * sellingRate : 3829
     * sellingType : MANUAL
     * usedTicket : true
     * usedTicketCount : 1
     * usedTicketId : 1
     * usedTicketValueAmount : 8
     */

    private boolean autoSale;
    private long buyingDate;
    private double buyingRate;
    private double consumeAmount;
    private String contractCode;
    private int customerId;
    private int dealCount;
    private String dealDirection;
    private String divideStatus;
    private boolean exception;
    private double handingChargeAmount;
    private String id;
    private double loss;
    private String memberCode;
    private int payAmount;
    private double profit;
    private double profitAndLoss;
    private String rollBackStatus;
    private boolean sale;
    private int saleTimestamp;
    private long sellingDate;
    private int sellingIncome;
    private int sellingRate;
    private String sellingType;
    private boolean usedTicket;
    private int usedTicketCount;
    private int usedTicketId;
    private int usedTicketValueAmount;

    private double plRate;
    private double plUnit;
    private String specification;
    private double latestPrice;
    private String contractName = "";


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

    public void setBuyingRate(int buyingRate) {
        this.buyingRate = buyingRate;
    }

    public double getConsumeAmount() {
        return consumeAmount;
    }

    public void setConsumeAmount(double consumeAmount) {
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

    public double getLoss() {
        return loss;
    }

    public void setLoss(double loss) {
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

    public long getSellingDate() {
        return sellingDate;
    }

    public void setSellingDate(long sellingDate) {
        this.sellingDate = sellingDate;
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

    public String getSellingType() {
        return sellingType;
    }

    public void setSellingType(String sellingType) {
        this.sellingType = sellingType;
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

    public double getPlRate() {
        return plRate;
    }

    public void setPlRate(double plRate) {
        this.plRate = plRate;
    }

    public double getPlUnit() {
        return plUnit;
    }

    public void setPlUnit(double plUnit) {
        this.plUnit = plUnit;
    }

    public void setBuyingRate(double buyingRate) {
        this.buyingRate = buyingRate;
    }

    public String getSpecification() {
        return specification;
    }

    public void setSpecification(String specification) {
        this.specification = specification;
    }

    public double getLatestPrice() {
        return latestPrice;
    }

    public void setLatestPrice(double latestPrice) {
        this.latestPrice = latestPrice;
    }

    public String getContractName() {
        return contractName;
    }

    public void setContractName(String contractName) {
        this.contractName = contractName;
    }

    @Override
    public String toString() {
        return "PositionInfoEntity{" +
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
                ", sellingDate=" + sellingDate +
                ", sellingIncome=" + sellingIncome +
                ", sellingRate=" + sellingRate +
                ", sellingType='" + sellingType + '\'' +
                ", usedTicket=" + usedTicket +
                ", usedTicketCount=" + usedTicketCount +
                ", usedTicketId=" + usedTicketId +
                ", usedTicketValueAmount=" + usedTicketValueAmount +
                ", plRate=" + plRate +
                ", plUnit=" + plUnit +
                ", specification='" + specification + '\'' +
                ", latestPrice=" + latestPrice +
                ", contractName='" + contractName + '\'' +
                '}';
    }
}
