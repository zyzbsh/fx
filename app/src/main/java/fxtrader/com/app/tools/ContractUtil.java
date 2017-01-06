package fxtrader.com.app.tools;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import fxtrader.com.app.entity.ContractEntity;
import fxtrader.com.app.entity.ContractInfoEntity;
import fxtrader.com.app.entity.MarketEntity;
import fxtrader.com.app.entity.PositionInfoEntity;
import fxtrader.com.app.entity.PriceEntity;
import fxtrader.com.app.http.HttpConstant;

/**
 * Created by pc on 2016/12/22.
 */
public class ContractUtil {
    private static LinkedHashMap<String, ContractEntity> sContractMap = new LinkedHashMap<>();

    private static Map<String, ContractInfoEntity> sContractInfoMap = new HashMap<>();

    public static void addContract(ContractInfoEntity info) {
        String dataType = info.getQueryParam();
        if (sContractMap.containsKey(dataType)) {
            ContractEntity contract = sContractMap.get(dataType);
            contract.add(info);
        } else {
            ContractEntity contract = new ContractEntity(dataType);
            contract.add(info);
            sContractMap.put(dataType, contract);
        }
        sContractInfoMap.put(info.getCode(), info);
    }

    public static void clear() {
        sContractMap.clear();
        sContractInfoMap.clear();
    }

    public static Map<String, ContractEntity> getContractMap() {
        return sContractMap;
    }

    public static Map<String, ContractInfoEntity> getContractInfoMap() {
        return sContractInfoMap;
    }

    public static double initProfitInfoList(MarketEntity vo, List<PositionInfoEntity> data){
        if (data == null || data.isEmpty()) {
            return 0;
        }
        double profit = 0;
        int size = data.size();
        for (int i = 0; i < size; i++) {
            PositionInfoEntity positionInfoEntity = data.get(i);
            String code = positionInfoEntity.getContractCode();
            String paramsCode = ContractUtil.getContractInfoMap().get(code).getQueryParam();
            String type = vo.getData(paramsCode);
            PriceEntity price = new PriceEntity(type);
            double latestPrice = Double.parseDouble(price.getLatestPrice());
            positionInfoEntity.setLatestPrice(latestPrice);

            ContractInfoEntity contractInfoEntity = ContractUtil.getContractInfoMap().get(code);
            positionInfoEntity.setPlRate(contractInfoEntity.getPlRate());
            positionInfoEntity.setPlUnit(contractInfoEntity.getPlUnit());
            positionInfoEntity.setContractName(contractInfoEntity.getName());
            String specification;
            if (contractInfoEntity.getName().contains("kg")) {
                specification = contractInfoEntity.getSpecification() + "kg";
            } else {
                specification = contractInfoEntity.getSpecification() + "t";
            }
            positionInfoEntity.setSpecification(specification);
            profit = profit + getProfit(positionInfoEntity);
        }
        BigDecimal big = new BigDecimal(profit);
        double sum = big.setScale(1, BigDecimal.ROUND_HALF_UP).doubleValue();
        return sum;
    }

    public static double getProfit(PositionInfoEntity entity) {
        boolean up = entity.getDealDirection().equals(HttpConstant.DealDirection.UP);
        double rate = entity.getPlRate();
        double unit = entity.getPlUnit();
        double buyPrice = entity.getBuyingRate();
        double latestPrice = Double.valueOf(entity.getLatestPrice());
        double diff = latestPrice - buyPrice;
        double result = diff * rate * unit * entity.getDealCount();
        if (up) {
            return result;
        } else {
            double num = 0.0;
            return num-result;
        }
    }

    public static boolean isUpDirection(PositionInfoEntity entity) {
        return "UP".equals(entity.getDealDirection());
    }

    public static double getDouble(double d, int i) {
        BigDecimal big = new BigDecimal(d);
        double value = big.setScale(i, BigDecimal.ROUND_HALF_UP).doubleValue();
        return value;
    }


}
