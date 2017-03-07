package fxtrader.com.app.entity;

import java.util.List;

/**
 * Created by pc on 2017/3/8.
 */
public class YesterdayRankResponse extends CommonResponse {
    private List<YesterdayRankEntity> object;

    public List<YesterdayRankEntity> getObject() {
        return object;
    }

    public void setObject(List<YesterdayRankEntity> object) {
        this.object = object;
    }

    public boolean isEmpty(){
        return object == null || object.isEmpty();
    }
}
