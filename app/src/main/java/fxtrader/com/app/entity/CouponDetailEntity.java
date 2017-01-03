package fxtrader.com.app.entity;

/**
 * Created by pc on 2016/12/18.
 */
public class CouponDetailEntity {

    /**
     * activityId : 123456 活动标识
     * drawDate : 1481254610000  领券日期
     * id : 105  礼券标识
     * lastUseTime : 1481513810000 最后使用日期
     * msgId : OTHER
     * owner : 2
     * parentId : 19
     * remarks : test//活动备注
     * status : DRAW
     * ticketId : 1 //ticket标识
     * userId : 121 //用户标识
     */

    private int activityId;
    private long drawDate;
    private int id;
    private long lastUseTime;
    private String msgId;
    private int owner;
    private int parentId;
    private String remarks;
    private String status;
    private int ticketId;
    private int userId;
    private double money;

    public int getActivityId() {
        return activityId;
    }

    public void setActivityId(int activityId) {
        this.activityId = activityId;
    }

    public long getDrawDate() {
        return drawDate;
    }

    public void setDrawDate(long drawDate) {
        this.drawDate = drawDate;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public long getLastUseTime() {
        return lastUseTime;
    }

    public void setLastUseTime(long lastUseTime) {
        this.lastUseTime = lastUseTime;
    }

    public String getMsgId() {
        return msgId;
    }

    public void setMsgId(String msgId) {
        this.msgId = msgId;
    }

    public int getOwner() {
        return owner;
    }

    public void setOwner(int owner) {
        this.owner = owner;
    }

    public int getParentId() {
        return parentId;
    }

    public void setParentId(int parentId) {
        this.parentId = parentId;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getTicketId() {
        return ticketId;
    }

    public void setTicketId(int ticketId) {
        this.ticketId = ticketId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public double getMoney() {
        return money;
    }

    public void setMoney(double money) {
        this.money = money;
    }

    @Override
    public String toString() {
        return "CouponEntity{" +
                "activityId=" + activityId +
                ", drawDate=" + drawDate +
                ", id=" + id +
                ", lastUseTime=" + lastUseTime +
                ", msgId='" + msgId + '\'' +
                ", owner=" + owner +
                ", parentId=" + parentId +
                ", remarks='" + remarks + '\'' +
                ", status='" + status + '\'' +
                ", ticketId=" + ticketId +
                ", userId=" + userId +
                ", money=" + money +
                '}';
    }
}
