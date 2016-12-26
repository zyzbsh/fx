package fxtrader.com.app.entity;

import java.util.List;

/**
 * Created by zhangyuzhu on 2016/12/26.
 */
public class RedEnvelopeListEntity extends CommonResponse{

    private List<RedEnvelopeEntity> object;

    public List<RedEnvelopeEntity> getObject() {
        return object;
    }

    public void setObject(List<RedEnvelopeEntity> object) {
        this.object = object;
    }

    @Override
    public String toString() {
        return "RedEnvelopeListEntity{" +
                "object=" + object +
                '}';
    }
}
