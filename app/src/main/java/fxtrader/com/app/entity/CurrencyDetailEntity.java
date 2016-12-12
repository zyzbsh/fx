package fxtrader.com.app.entity;

/**
 * Created by zhangyuzhu on 2016/12/7.
 */
public class CurrencyDetailEntity {
    /**
     * context : 108984446525812736
     * contextClazz : java.lang.String
     * date : 1477561516000
     * id : 108984446592921600
     * memberCode : HT
     * remainingMoney : 0
     * transactionMoney : 0
     * transactionType : BUILD_METALORDER
     */

    private String context;
    private String contextClazz;
    private long date;
    private String id;
    private String memberCode;
    private int remainingMoney;
    private int transactionMoney;
    private String transactionType;

    public String getContext() {
        return context;
    }

    public void setContext(String context) {
        this.context = context;
    }

    public String getContextClazz() {
        return contextClazz;
    }

    public void setContextClazz(String contextClazz) {
        this.contextClazz = contextClazz;
    }

    public long getDate() {
        return date;
    }

    public void setDate(long date) {
        this.date = date;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMemberCode() {
        return memberCode;
    }

    public void setMemberCode(String memberCode) {
        this.memberCode = memberCode;
    }

    public int getRemainingMoney() {
        return remainingMoney;
    }

    public void setRemainingMoney(int remainingMoney) {
        this.remainingMoney = remainingMoney;
    }

    public int getTransactionMoney() {
        return transactionMoney;
    }

    public void setTransactionMoney(int transactionMoney) {
        this.transactionMoney = transactionMoney;
    }

    public String getTransactionType() {
        return transactionType;
    }

    public void setTransactionType(String transactionType) {
        this.transactionType = transactionType;
    }

    @Override
    public String toString() {
        return "CurrencyDetailEntity{" +
                "context='" + context + '\'' +
                ", contextClazz='" + contextClazz + '\'' +
                ", date=" + date +
                ", id='" + id + '\'' +
                ", memberCode='" + memberCode + '\'' +
                ", remainingMoney=" + remainingMoney +
                ", transactionMoney=" + transactionMoney +
                ", transactionType='" + transactionType + '\'' +
                '}';
    }
}
