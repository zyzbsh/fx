package fxtrader.com.app.entity;

/**
 * Created by pc on 2017/2/13.
 */
public class BankBindEntity extends CommonResponse {

    /**
     * object : {"bank":"中国农业银行","bankBranch":"5959","bankCardNum":"585496484418449","bankUseName":"484848","city":"无锡","customerId":267,"memberId":11,"province":"江苏"}
     */

    private ObjectBean object;

    public ObjectBean getObject() {
        return object;
    }

    public void setObject(ObjectBean object) {
        this.object = object;
    }

    public static class ObjectBean {
        /**
         * bank : 中国农业银行
         * bankBranch : 5959
         * bankCardNum : 585496484418449
         * bankUseName : 484848
         * city : 无锡
         * customerId : 267
         * memberId : 11
         * province : 江苏
         */

        private String bank;
        private String bankBranch;
        private String bankCardNum;
        private String bankUseName;
        private String city;
        private int customerId;
        private int memberId;
        private String province;

        public String getBank() {
            return bank;
        }

        public void setBank(String bank) {
            this.bank = bank;
        }

        public String getBankBranch() {
            return bankBranch;
        }

        public void setBankBranch(String bankBranch) {
            this.bankBranch = bankBranch;
        }

        public String getBankCardNum() {
            return bankCardNum;
        }

        public void setBankCardNum(String bankCardNum) {
            this.bankCardNum = bankCardNum;
        }

        public String getBankUseName() {
            return bankUseName;
        }

        public void setBankUseName(String bankUseName) {
            this.bankUseName = bankUseName;
        }

        public String getCity() {
            return city;
        }

        public void setCity(String city) {
            this.city = city;
        }

        public int getCustomerId() {
            return customerId;
        }

        public void setCustomerId(int customerId) {
            this.customerId = customerId;
        }

        public int getMemberId() {
            return memberId;
        }

        public void setMemberId(int memberId) {
            this.memberId = memberId;
        }

        public String getProvince() {
            return province;
        }

        public void setProvince(String province) {
            this.province = province;
        }
    }
}
