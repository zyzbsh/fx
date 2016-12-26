package fxtrader.com.app.entity;

/**
 * Created by pc on 2016/12/26.
 */
public class ProfitEntity {

    /**
     * contractCode : YDOIL3
     * contractName : 15t粤东油
     * headimgurl :
     * isSubscribe : false
     * nickname : 丶缺
     * subcribeCount : 7
     * userId : 51
     * weekProfitRate : 75
     */

    private String contractCode;
    private String contractName;
    private String headimgurl;
    private boolean isSubscribe;
    private String nickname;
    private int subcribeCount;
    private int userId;
    private double weekProfitRate;

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

    public String getHeadimgurl() {
        return headimgurl;
    }

    public void setHeadimgurl(String headimgurl) {
        this.headimgurl = headimgurl;
    }

    public boolean isIsSubscribe() {
        return isSubscribe;
    }

    public void setIsSubscribe(boolean isSubscribe) {
        this.isSubscribe = isSubscribe;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public int getSubcribeCount() {
        return subcribeCount;
    }

    public void setSubcribeCount(int subcribeCount) {
        this.subcribeCount = subcribeCount;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public double getWeekProfitRate() {
        return weekProfitRate;
    }

    public void setWeekProfitRate(double weekProfitRate) {
        this.weekProfitRate = weekProfitRate;
    }
}
