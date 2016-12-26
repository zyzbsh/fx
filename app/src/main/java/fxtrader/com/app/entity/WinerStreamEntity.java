package fxtrader.com.app.entity;

/**
 * Created by pc on 2016/12/26.
 */
public class WinerStreamEntity {

    /**
     * customerId : 221
     * headImg :
     * id : 0
     * nickname : 竹子
     * organId : 0
     * score : 3
     * subscribe : false
     */

    private int customerId;
    private String headImg;
    private int id;
    private String nickname;
    private int organId;
    private int score;
    private boolean subscribe;

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

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public int getOrganId() {
        return organId;
    }

    public void setOrganId(int organId) {
        this.organId = organId;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public boolean isSubscribe() {
        return subscribe;
    }

    public void setSubscribe(boolean subscribe) {
        this.subscribe = subscribe;
    }
}
