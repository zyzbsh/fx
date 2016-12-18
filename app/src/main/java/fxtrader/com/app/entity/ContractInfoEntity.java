package fxtrader.com.app.entity;

import android.text.TextUtils;

import java.io.Serializable;

import fxtrader.com.app.http.HttpConstant;

/**
 * Created by zhangyuzhu on 2016/12/9.
 */
public class ContractInfoEntity implements Serializable{
    /**
     * code : AG01
     * dataType : AG
     * dealLimit : 10
     * handingCharge : 0.8
     * margin : 8
     * name : 0.1kg白银
     * plRate : 0.1
     * plUnit : 1
     * specification : 8
     * unit : 8?/?
     */

    private String code;
    private String dataType;
    private int dealLimit;
    private double handingCharge;
    private int margin;
    private String name;
    private double plRate;
    private int plUnit;
    private int specification;
    private String unit;
    private String baseUnit = "";
    private String baseNum = "";
    private String queryParam = "";

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getDataType() {
        return dataType;
    }

    public void setDataType(String dataType) {
        this.dataType = dataType;
    }

    public int getDealLimit() {
        return dealLimit;
    }

    public void setDealLimit(int dealLimit) {
        this.dealLimit = dealLimit;
    }

    public double getHandingCharge() {
        return handingCharge;
    }

    public void setHandingCharge(double handingCharge) {
        this.handingCharge = handingCharge;
    }

    public int getMargin() {
        return margin;
    }

    public void setMargin(int margin) {
        this.margin = margin;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getPlRate() {
        return plRate;
    }

    public void setPlRate(double plRate) {
        this.plRate = plRate;
    }

    public int getPlUnit() {
        return plUnit;
    }

    public void setPlUnit(int plUnit) {
        this.plUnit = plUnit;
    }

    public int getSpecification() {
        return specification;
    }

    public void setSpecification(int specification) {
        this.specification = specification;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public String getBaseUnit() {
        return baseUnit;
    }

    public void setBaseUnit(String baseUnit) {
        this.baseUnit = baseUnit;
    }

    public String getBaseNum() {
        return baseNum;
    }

    public void setBaseNum(String baseNum) {
        this.baseNum = baseNum;
    }

    public String getQueryParam() {
        return queryParam;
    }

    public void setQueryParam(String queryParam) {
        this.queryParam = queryParam;
    }

    @Override
    public String toString() {
        return "ContractInfoEntity{" +
                "code='" + code + '\'' +
                ", dataType='" + dataType + '\'' +
                ", dealLimit=" + dealLimit +
                ", handingCharge=" + handingCharge +
                ", margin=" + margin +
                ", name='" + name + '\'' +
                ", plRate=" + plRate +
                ", plUnit=" + plUnit +
                ", specification=" + specification +
                ", unit='" + unit + '\'' +
                ", baseUnit='" + baseUnit + '\'' +
                ", baseNum='" + baseNum + '\'' +
                ", queryParam=" + queryParam +
                '}';
    }
}
