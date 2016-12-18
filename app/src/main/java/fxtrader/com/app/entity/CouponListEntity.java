package fxtrader.com.app.entity;

import java.util.List;

/**
 * Created by pc on 2016/12/18.
 */
public class CouponListEntity extends CommonResponse{

    private List<CouponDetailEntity> object;

    public List<CouponDetailEntity> getObject() {
        return object;
    }

    public void setObject(List<CouponDetailEntity> object) {
        this.object = object;
    }

    @Override
    public String toString() {
        return "CouponList{" +
                "object=" + object +
                '}';
    }
}
