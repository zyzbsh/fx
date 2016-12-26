package fxtrader.com.app.entity;

import java.util.List;

/**
 * Created by pc on 2016/12/26.
 */
public class WinerStreamListEntity extends CommonResponse{

    private List<WinerStreamEntity> object;

    public List<WinerStreamEntity> getObject() {
        return object;
    }

    public void setObject(List<WinerStreamEntity> object) {
        this.object = object;
    }
}
