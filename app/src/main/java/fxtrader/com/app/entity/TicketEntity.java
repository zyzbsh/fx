package fxtrader.com.app.entity;

import java.util.List;

/**
 * Created by pc on 2016/12/18.
 */
public class TicketEntity {
    /**
     * canUseList : ["AG10","CU100"]
     * id : 4
     * value : 80
     */

    private int id;
    private int value;
    private List<String> canUseList;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public List<String> getCanUseList() {
        return canUseList;
    }

    public void setCanUseList(List<String> canUseList) {
        this.canUseList = canUseList;
    }

    @Override
    public String toString() {
        return "TicketEntity{" +
                "id=" + id +
                ", value=" + value +
                ", canUseList=" + canUseList +
                '}';
    }
}
