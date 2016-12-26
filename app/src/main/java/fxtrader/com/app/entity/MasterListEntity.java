package fxtrader.com.app.entity;

import java.util.List;

/**
 * Created by pc on 2016/12/26.
 */
public class MasterListEntity extends CommonResponse {

    private List<MasterEntity> object;

    public List<MasterEntity> getObject() {
        return object;
    }

    public void setObject(List<MasterEntity> object) {
        this.object = object;
    }
}
