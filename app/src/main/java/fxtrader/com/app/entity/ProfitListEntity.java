package fxtrader.com.app.entity;

import java.util.List;

/**
 * Created by pc on 2016/12/26.
 */
public class ProfitListEntity extends CommonResponse{

    private List<ProfitEntity> object;

    public List<ProfitEntity> getObject() {
        return object;
    }

    public void setObject(List<ProfitEntity> object) {
        this.object = object;
    }
}
