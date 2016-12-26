package fxtrader.com.app.entity;

/**
 * Created by zhangyuzhu on 2016/12/26.
 */
public class ParticipantsEntity extends CommonResponse{

    /**
     * object : {"drop":15,"id":0,"nowDay":1482387114661,"predictFallRate":0,"predictNum":0,"predictRiseRate":0,"predictState":0,"predictSum":0,"up":54}
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
         * drop : 15
         * id : 0
         * nowDay : 1482387114661
         * predictFallRate : 0
         * predictNum : 0
         * predictRiseRate : 0
         * predictState : 0
         * predictSum : 0
         * up : 54
         */

        private int drop;
        private int id;
        private long nowDay;
        private double predictFallRate;
        private int predictNum;
        private double predictRiseRate;
        private int predictState;
        private int predictSum;
        private int up;

        public int getDrop() {
            return drop;
        }

        public void setDrop(int drop) {
            this.drop = drop;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public long getNowDay() {
            return nowDay;
        }

        public void setNowDay(long nowDay) {
            this.nowDay = nowDay;
        }

        public double getPredictFallRate() {
            return predictFallRate;
        }

        public void setPredictFallRate(double predictFallRate) {
            this.predictFallRate = predictFallRate;
        }

        public int getPredictNum() {
            return predictNum;
        }

        public void setPredictNum(int predictNum) {
            this.predictNum = predictNum;
        }

        public double getPredictRiseRate() {
            return predictRiseRate;
        }

        public void setPredictRiseRate(double predictRiseRate) {
            this.predictRiseRate = predictRiseRate;
        }

        public int getPredictState() {
            return predictState;
        }

        public void setPredictState(int predictState) {
            this.predictState = predictState;
        }

        public int getPredictSum() {
            return predictSum;
        }

        public void setPredictSum(int predictSum) {
            this.predictSum = predictSum;
        }

        public int getUp() {
            return up;
        }

        public void setUp(int up) {
            this.up = up;
        }
    }
}
