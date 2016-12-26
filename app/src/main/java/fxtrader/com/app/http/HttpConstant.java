package fxtrader.com.app.http;

/**
 * Created by zhangyuzhu on 2016/12/7.
 */
public final class HttpConstant {

    public static String BASE_URL = "http://121.9.227.58:13516/";

    public static String TEST_URL = " http://125.88.152.51:15516/";

    public final static long REFRESH_TIME = 10000;

    public final static int REFRESH_POSITION_LIST = 10000;

    public final static int REFRESH_USER_INFO = 10000;

    public static final int DEFAULT_ORGAN_ID = 0;

    public static class PriceCode{
        public static final String YDHF = "YDHF";
        public static final String YDCL = "YDCL";
    }

    public static class ContractUnit{
        public static final String KG = "kg";
        public static final String T = "t";
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
    }
}
