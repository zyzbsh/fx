package fxtrader.com.app.entity;

import java.util.List;

/**
 * Created by zhangyuzhu on 2016/12/7.
 */
public class ContractListEntity extends CommonResponse{

    /**
     * code : 200
     * message : ??
     * object : [{"code":"AG01","dataType":"AG","dealLimit":10,"handingCharge":0.8,"margin":8,"name":"0.1kg白银","plRate":0.1,"plUnit":1,"specification":8,"unit":"8?/?"}]
     * success : true
     */

    private List<ContractInfoEntity> object;

    public List<ContractInfoEntity> getObject() {
        return object;
    }

    public void setObject(List<ContractInfoEntity> object) {
        this.object = object;
    }

    @Override
    public String toString() {
        return super.toString() + "ContractListEntity{" +
                "object=" + object +
                '}';
    }
}
