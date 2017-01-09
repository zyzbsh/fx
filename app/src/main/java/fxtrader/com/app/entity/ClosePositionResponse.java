package fxtrader.com.app.entity;

/**
 * Created by pc on 2017/1/9.
 */
public class ClosePositionResponse extends CommonResponse {
    public PositionInfoEntity object;

    public PositionInfoEntity getObject() {
        return object;
    }

    public void setObject(PositionInfoEntity object) {
        this.object = object;
    }

    @Override
    public String toString() {
        return "ClosePositionResponse{" +
                "object=" + object +
                '}';
    }
}
