package fxtrader.com.app.entity;

/**
 * Created by pc on 2016/12/29.
 */
public class SubscribedOrderEntity extends CommonResponse{

    /**
     * object : {}
     */

    private UserSubscribeEntity object;

    public UserSubscribeEntity getObject() {
        return object;
    }

    public void setObject(UserSubscribeEntity object) {
        this.object = object;
    }
}
