package fxtrader.com.app.entity;

import java.util.List;

/**
 * Created by pc on 2016/12/18.
 */
public class TicketListEntity extends CommonResponse{


    private List<TicketEntity> object;

    public List<TicketEntity> getObject() {
        return object;
    }

    public void setObject(List<TicketEntity> object) {
        this.object = object;
    }

    @Override
    public String toString() {
        return "TicketListEntity{" +
                "object=" + object +
                '}';
    }
}
