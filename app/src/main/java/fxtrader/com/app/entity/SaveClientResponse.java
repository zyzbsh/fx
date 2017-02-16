package fxtrader.com.app.entity;

/**
 * Created by zhangyuzhu on 2017/2/15.
 */
public class SaveClientResponse extends CommonResponse{
    private boolean object = false;

    public boolean isObject() {
        return object;
    }

    public void setObject(boolean object) {
        this.object = object;
    }

    @Override
    public String toString() {
        return "SaveClientResponse{" +
                "object=" + object +
                '}';
    }
}
