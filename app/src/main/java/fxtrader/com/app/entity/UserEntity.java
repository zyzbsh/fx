package fxtrader.com.app.entity;

/**
 * Created by zhangyuzhu on 2016/12/7.
 */
public class UserEntity {


    /**
     * code : 200
     * message : ??
     * object : {"account":"oMtyLv6T1SuCYFSrqxUNXrwPH5GQ","agent":false,"agentId":0,"headimgurl":"http:wwcwadsvQnVIh0RdsicD98qC8UChG0BMgEicnVbLSdD1s/0","id":121,"memberCode":"HT","memberId":19,"nickname":"?","organId":2,"referrerId":1,"registerDate":1477645795000,"sex":2,"shareId":0,"telNumber":"13668902xxx","wxOpenId":"oMtyLv6T1SuCYFSrqxUNXrwPH5GQ","funds":100,"couponAmount":4}
     * success : true
     */

    private int code;
    private String message;
    private ObjectBean object;
    private boolean success;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public ObjectBean getObject() {
        return object;
    }

    public void setObject(ObjectBean object) {
        this.object = object;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public static class ObjectBean {
        /**
         * account : oMtyLv6T1SuCYFSrqxUNXrwPH5GQ
         * agent : false
         * agentId : 0
         * headimgurl : http:wwcwadsvQnVIh0RdsicD98qC8UChG0BMgEicnVbLSdD1s/0
         * id : 121
         * memberCode : HT
         * memberId : 19
         * nickname : ?
         * organId : 2
         * referrerId : 1
         * registerDate : 1477645795000
         * sex : 2
         * shareId : 0
         * telNumber : 13668902xxx
         * wxOpenId : oMtyLv6T1SuCYFSrqxUNXrwPH5GQ
         * funds : 100.0
         * couponAmount : 4
         */

        private String account;
        private boolean agent;
        private int agentId;
        private String headimgurl;
        private int id;
        private String memberCode;
        private int memberId;
        private String nickname;
        private int organId;
        private int referrerId;
        private long registerDate;
        private int sex;
        private int shareId;
        private String telNumber;
        private String wxOpenId;
        private double funds;
        private int couponAmount;

        public String getAccount() {
            return account;
        }

        public void setAccount(String account) {
            this.account = account;
        }

        public boolean isAgent() {
            return agent;
        }

        public void setAgent(boolean agent) {
            this.agent = agent;
        }

        public int getAgentId() {
            return agentId;
        }

        public void setAgentId(int agentId) {
            this.agentId = agentId;
        }

        public String getHeadimgurl() {
            return headimgurl;
        }

        public void setHeadimgurl(String headimgurl) {
            this.headimgurl = headimgurl;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getMemberCode() {
            return memberCode;
        }

        public void setMemberCode(String memberCode) {
            this.memberCode = memberCode;
        }

        public int getMemberId() {
            return memberId;
        }

        public void setMemberId(int memberId) {
            this.memberId = memberId;
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

        public int getReferrerId() {
            return referrerId;
        }

        public void setReferrerId(int referrerId) {
            this.referrerId = referrerId;
        }

        public long getRegisterDate() {
            return registerDate;
        }

        public void setRegisterDate(long registerDate) {
            this.registerDate = registerDate;
        }

        public int getSex() {
            return sex;
        }

        public void setSex(int sex) {
            this.sex = sex;
        }

        public int getShareId() {
            return shareId;
        }

        public void setShareId(int shareId) {
            this.shareId = shareId;
        }

        public String getTelNumber() {
            return telNumber;
        }

        public void setTelNumber(String telNumber) {
            this.telNumber = telNumber;
        }

        public String getWxOpenId() {
            return wxOpenId;
        }

        public void setWxOpenId(String wxOpenId) {
            this.wxOpenId = wxOpenId;
        }

        public double getFunds() {
            return funds;
        }

        public void setFunds(double funds) {
            this.funds = funds;
        }

        public int getCouponAmount() {
            return couponAmount;
        }

        public void setCouponAmount(int couponAmount) {
            this.couponAmount = couponAmount;
        }
    }
}
