package fxtrader.com.app.entity;

/**
 * Created by zhangyuzhu on 2017/1/10.
 */
public class PositionInfoSumEntity {
    private double profitAndLoss;
    private int dealCount;

    public double getProfitAndLoss() {
        return profitAndLoss;
    }

    public void setProfitAndLoss(double profitAndLoss) {
        this.profitAndLoss = profitAndLoss;
    }

    public int getDealCount() {
        return dealCount;
    }

    public void setDealCount(int dealCount) {
        this.dealCount = dealCount;
    }

    @Override
    public String toString() {
        return "PositionInfoSumEntity{" +
                "profitAndLoss=" + profitAndLoss +
                ", dealCount=" + dealCount +
                '}';
    }
}
