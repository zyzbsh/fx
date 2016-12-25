package fxtrader.com.app.entity;

/**
 * Created by pc on 2016/12/25.
 */
public class PacketDetailEntity {
    /**
     * allAmount : 0.81
     * allotPersonNum : 10
     * createTime : 1482404208000
     * customerId : 53
     * id : 19
     * metalOrderId : 129269098426740736
     * status : 1
     * truePersonNum : 1
     * type : 1
     * validTime : 1482490608000
     */

    private double allAmount;
    private int allotPersonNum;
    private long createTime;
    private int customerId;
    private int id;
    private String metalOrderId;
    private int status;
    private int truePersonNum;
    private int type;
    private long validTime;

    public double getAllAmount() {
        return allAmount;
    }

    public void setAllAmount(double allAmount) {
        this.allAmount = allAmount;
    }

    public int getAllotPersonNum() {
        return allotPersonNum;
    }

    public void setAllotPersonNum(int allotPersonNum) {
        this.allotPersonNum = allotPersonNum;
    }

    public long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(long createTime) {
        this.createTime = createTime;
    }

    public int getCustomerId() {
        return customerId;
    }

    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getMetalOrderId() {
        return metalOrderId;
    }

    public void setMetalOrderId(String metalOrderId) {
        this.metalOrderId = metalOrderId;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getTruePersonNum() {
        return truePersonNum;
    }

    public void setTruePersonNum(int truePersonNum) {
        this.truePersonNum = truePersonNum;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public long getValidTime() {
        return validTime;
    }

    public void setValidTime(long validTime) {
        this.validTime = validTime;
    }

    @Override
    public String toString() {
        return "PacketDetailEntity{" +
                "allAmount=" + allAmount +
                ", allotPersonNum=" + allotPersonNum +
                ", createTime=" + createTime +
                ", customerId=" + customerId +
                ", id=" + id +
                ", metalOrderId='" + metalOrderId + '\'' +
                ", status=" + status +
                ", truePersonNum=" + truePersonNum +
                ", type=" + type +
                ", validTime=" + validTime +
                '}';
    }
}
