package fxtrader.com.app.entity;

import android.text.TextUtils;

import java.util.ArrayList;
import java.util.Arrays;
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
    private double value;
    private List<String> canUseList;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public double getValue() {
        return value;
    }

    public void setValue(double value) {
        this.value = value;
    }

    public List<String> getCanUseList() {
        return canUseList;
    }

    public void setCanUseList(List<String> canUseList) {
        this.canUseList = canUseList;
    }

    public void setDataTypes(String dataTypes) {
        String[] a = dataTypes.split(",");
       this.canUseList = Arrays.asList(a);
    }

    public String getDataTypes() {
        if (this.canUseList == null || this.canUseList.isEmpty()) {
            return "";
        }
        StringBuilder builder = new StringBuilder();
        int size = this.canUseList.size();
        for (int i = 0; i < size; i++) {
            builder.append(this.canUseList.get(i));
            if (i != size - 1) {
                builder.append(",");
            }
        }

        return builder.toString();
    }

    public boolean containDataType(String type) {
        if (this.canUseList == null || this.canUseList.isEmpty()) {
            return false;
        }
        return this.canUseList.contains(type);
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
