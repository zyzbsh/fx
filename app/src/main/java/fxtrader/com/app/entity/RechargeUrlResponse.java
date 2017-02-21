package fxtrader.com.app.entity;

/**
 * Created by zhangyuzhu on 2017/2/21.
 */
public class RechargeUrlResponse extends CommonResponse {
    private String object = "";

    public String getObject() {
        return object;
    }

    public void setObject(String object) {
        this.object = object;
    }

    @Override
    public String toString() {
        return "RechargeUrlResponse{" +
                "object='" + object + '\'' +
                '}';
    }
}
