package fxtrader.com.app.http;

/**
 * Created by zhangyuzhu on 2016/12/7.
 */
public final class HttpConstant {

    public static String BASE_URL = "http://121.9.227.58:13516/";

    public static String TEST_URL = " http://125.88.152.51:15516/";

    public final static long REFRESH_TIME = 8000;

    public final static int REFRESH_POSITION_LIST = 10000;

    public final static int REFRESH_USER_INFO = 10000;

    public static final int DEFAULT_ORGAN_ID = 0;

    public static class PriceCode{
        public static final String YDHF = "YDHF";
        public static final String YDCL = "YDCL";
        public static final String YDOIL = "YDOIL";
    }

    public static class ContractUnit{
        public static final String KG = "kg";
        public static final String T = "t";
    }

    public static class KType{
//        1:1分钟，2:5分钟，3:15分钟，4:30分钟，5:1小时，6:2小时，7:4小时， 8:1天，9:1星
//        期，10:1个月，11:1季度，12:一年
        public static final int MIN_1 = 1;
        public static final int MIN_5 = 2;
        public static final int MIN_15 = 3;
        public static final int MIN_30 = 4;
        public static final int HOUR_1 = 5;
        public static final int HOUR_2 = 6;
        public static final int HOUR_4 = 7;
        public static final int DAY = 8;
        public static final int WEEK = 9;
        public static final int MONTH = 10;
        public static final int QUARTER = 11;
        public static final int YEAR = 12;
    }

    public static class SexType{
        public static final int MALE = 1;
        public static final int FEMALE = 2;
    }

    /**
     * 交易类型
     */
    public static class TransactionType {
        /**建仓**/
        public static final String BUILD_METALORDER = "BUILD_METALORDER";
        /**平仓**/
        public static final String SELL_METALORDER = "SELL_METALORDER";
        /**充值**/
        public static final String RECHARGE = "RECHARGE";
        /**补偿**/
        public static final String COMPENSATION = "COMPENSATION";
        /**提现申请**/
        public static final String WITHDRAWALS_APPLY = "WITHDRAWALS_APPLY";
        /**提现成功**/
        public static final String WITHDRAWALS_SUCCESS = "WITHDRAWALS_SUCCESS";
        /**提现失败**/
        public static final String WITHDRAWALS_FAIL = "WITHDRAWALS_FAIL";
        /**红包**/
        public static final String REDPACKET = "REDPACKET";
    }

    public static class DealDirection{
        public static final String UP = "UP";
        public static final String DROP = "DROP";

        public static final int C_UP = 0;
        public static final int C_DROP = 1;
    }

    public static class VerificationCode {
        /**注册经纪人**/
        public static final String APPLY_BROKER = "APPLY_BROKER";
        /**找回密码**/
        public static final String FIND_PWD = "FIND_PWD";
        /**注册**/
        public static final String REGISTER = "REGISTER";
        /**经纪人找回密码**/
        public static final String BROKER_FIND_PWD = "BROKER_FIND_PWD";
        /**申请提现**/
        public static final String WITHDRAW_PWD = "WITHDRAW_PWD";
    }

    public static class RedPacketType{
        public static final int UNSEND = 0;
        public static final int SEND = 1;
    }

    public static class SellingType{
        /**手动结算**/
        public static final String MANUAL = "MANUAL";
        /**交易日结算**/
        public static final String BUINESS_SETTLEMENT = "BUINESS_SETTLEMENT";
        /**止盈平仓**/
        public static final String AUTO_PROFIT = "AUTO_PROFIT";
        /**止损平仓**/
        public static final String AUTO_LOSS = "AUTO_LOSS";
        /**爆仓**/
        public static final String BLAST = "BLAST";
    }
}
