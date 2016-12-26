package fxtrader.com.app.entity;

/**
 * Created by zhangyuzhu on 2016/12/26.
 */
public class RedEnvelopeEntity {

    /**
     * allAmount : 1.02
     * createTime : 1482214536000
     * customerId : 227
     * headImg :
     * nickname : Smile
     * subscribe : false
     */

    private double allAmount;
    private long createTime;
    private int customerId;
    private String headImg;
    private String nickname;
    private boolean subscribe;

    public double getAllAmount() {
        return allAmount;
    }

    public void setAllAmount(double allAmount) {
        this.allAmount = allAmount;
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

    public boolean isSubscribe() {
        return subscribe;
    }

    public void setSubscribe(boolean subscribe) {
        this.subscribe = subscribe;
    }

    @Override
    public String toString() {
        return "RedEnvelopeEntity{" +
                "allAmount=" + allAmount +
                ", createTime=" + createTime +
                ", customerId=" + customerId +
                ", headImg='" + headImg + '\'' +
                ", nickname='" + nickname + '\'' +
                ", subscribe=" + subscribe +
                '}';
    }
}
