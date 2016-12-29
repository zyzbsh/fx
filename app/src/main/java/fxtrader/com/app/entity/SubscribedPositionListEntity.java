package fxtrader.com.app.entity;

import java.util.List;

/**
 * Created by pc on 2016/12/29.
 */
public class SubscribedPositionListEntity extends CommonResponse{

    private List<UserSubscribeEntity> object;

    public List<UserSubscribeEntity> getObject() {
        return object;
    }

    public void setObject(List<UserSubscribeEntity> object) {
        this.object = object;
    }
}
