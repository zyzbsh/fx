package fxtrader.com.app.entity;

import android.text.TextUtils;

import java.util.ArrayList;
import java.util.List;

import fxtrader.com.app.http.HttpConstant;

/**
 * Created by pc on 2016/12/13.
 */
public class ContractEntity {

    private String type = "";
    private String name = "";
    private List<ContractInfoEntity> list = new ArrayList<>();

    public ContractEntity(String type) {
        this.type = type;
    }

    public void add(ContractInfoEntity entity) {
        String data = entity.getName();
        if (!TextUtils.isEmpty(data)){
            String unit = "";
            if (data.contains(HttpConstant.ContractUnit.KG)){
                unit = HttpConstant.ContractUnit.KG;
            } else if (this.name.contains(HttpConstant.ContractUnit.T)) {
                unit = HttpConstant.ContractUnit.T;
            }

            String[] a = data.split(unit);
            String spec = a[0];
            this.name = a[1];
            entity.setBaseNum(spec);
            entity.setBaseUnit(unit);
        }
        list.add(entity);
    }

    public List<ContractInfoEntity> getData() {
        return list;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
