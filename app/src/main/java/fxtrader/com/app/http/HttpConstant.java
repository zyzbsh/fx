package fxtrader.com.app.http;

/**
 * Created by zhangyuzhu on 2016/12/7.
 */
public final class HttpConstant {

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
        /**提现申请**/
        public static final String WITHDRAWALS_APPLY = "WITHDRAWALS_APPLY";
        /**提现成功**/
        public static final String WITHDRAWALS_SUCCESS = "WITHDRAWALS_SUCCESS";
        /**提现失败**/
        public static final String WITHDRAWALS_FAIL = "WITHDRAWALS_FAIL";
    }
}
