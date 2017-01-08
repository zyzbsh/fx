package fxtrader.com.app.entity;

/**
 * Created by pc on 2017/1/8.
 */
public class OrderDetailEntity extends CommonResponse{
    private PositionInfoEntity object;

    public PositionInfoEntity getObject() {
        return object;
    }

    public void setObject(PositionInfoEntity object) {
        this.object = object;
    }
}
