package fxtrader.com.app.entity;

/**
 * Created by pc on 2017/3/6.
 */
public class RankResponse extends CommonResponse {

    /**
     * object : {"profitRate":0,"rankNo":0}
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
         * profitRate : 0
         * rankNo : 0
         */

        private double profitRate;
        private int rankNo;

        public double getProfitRate() {
            return profitRate;
        }

        public void setProfitRate(double profitRate) {
            this.profitRate = profitRate;
        }

        public int getRankNo() {
            return rankNo;
        }

        public void setRankNo(int rankNo) {
            this.rankNo = rankNo;
        }
    }
}
