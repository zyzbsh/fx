package fxtrader.com.app.entity;

/**
 * Created by pc on 2016/12/24.
 */
public class WithdrawError {

    /**
     * message : 非法银行卡号
     * withdrawApplyForm : {"amount":235,"blank":"中国银行","blankBranch":"得等","blankCardNum":"123456789","blankUseName":"额的","city":"滨海新区","customerId":0,"province":"天津","redirectUri":"http://example.com/withdrawalCallBack","ticketTimestamp":0}
     * code : -255
     * object : null
     */

    private String message;
    private WithdrawApplyFormBean withdrawApplyForm;
    private int code;
    private String object;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public WithdrawApplyFormBean getWithdrawApplyForm() {
        return withdrawApplyForm;
    }

    public void setWithdrawApplyForm(WithdrawApplyFormBean withdrawApplyForm) {
        this.withdrawApplyForm = withdrawApplyForm;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getObject() {
        return object;
    }

    public void setObject(String object) {
        this.object = object;
    }

    public static class WithdrawApplyFormBean {
        /**
         * amount : 235
         * blank : 中国银行
         * blankBranch : 得等
         * blankCardNum : 123456789
         * blankUseName : 额的
         * city : 滨海新区
         * customerId : 0
         * province : 天津
         * redirectUri : http://example.com/withdrawalCallBack
         * ticketTimestamp : 0
         */

        private int amount;
        private String blank;
        private String blankBranch;
        private String blankCardNum;
        private String blankUseName;
        private String city;
        private int customerId;
        private String province;
        private String redirectUri;
        private int ticketTimestamp;

        public int getAmount() {
            return amount;
        }

        public void setAmount(int amount) {
            this.amount = amount;
        }

        public String getBlank() {
            return blank;
        }

        public void setBlank(String blank) {
            this.blank = blank;
        }

        public String getBlankBranch() {
            return blankBranch;
        }

        public void setBlankBranch(String blankBranch) {
            this.blankBranch = blankBranch;
        }

        public String getBlankCardNum() {
            return blankCardNum;
        }

        public void setBlankCardNum(String blankCardNum) {
            this.blankCardNum = blankCardNum;
        }

        public String getBlankUseName() {
            return blankUseName;
        }

        public void setBlankUseName(String blankUseName) {
            this.blankUseName = blankUseName;
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

        public String getProvince() {
            return province;
        }

        public void setProvince(String province) {
            this.province = province;
        }

        public String getRedirectUri() {
            return redirectUri;
        }

        public void setRedirectUri(String redirectUri) {
            this.redirectUri = redirectUri;
        }

        public int getTicketTimestamp() {
            return ticketTimestamp;
        }

        public void setTicketTimestamp(int ticketTimestamp) {
            this.ticketTimestamp = ticketTimestamp;
        }
    }
}
