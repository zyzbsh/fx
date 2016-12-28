package fxtrader.com.app.entity;

import java.util.List;

/**
 * Created by zhangyuzhu on 2016/12/28.
 */
public class SubscribeListEntity extends CommonResponse{

    private List<UserSubscribeEntity> object;

    public List<UserSubscribeEntity> getObject() {
        return object;
    }

    public void setObject(List<UserSubscribeEntity> object) {
        this.object = object;
    }

    public static class ObjectBean {
    }
}
