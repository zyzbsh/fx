package fxtrader.com.app.entity;

/**
 * Created by zhangyuzhu on 2017/2/21.
 */
public class CheckOrgnIdResponse extends CommonResponse {
    private boolean object;

    public boolean isObject() {
        return object;
    }

    public void setObject(boolean object) {
        this.object = object;
    }

    @Override
    public String toString() {
        return "CheckOrgnIdResponse{" +
                "object=" + object +
                '}';
    }
}
