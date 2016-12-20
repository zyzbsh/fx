package fxtrader.com.app.entity;

/**
 * Created by zhangyuzhu on 2016/12/19.
 */
public class BuildPositionResponseEntity extends CommonResponse {

    /**
     * object : {"autoSale":false,"buyingDate":1482139240991,"buyingRate":5467.5,"consumeAmount":106,"contractCode":"YDHF","customerId":53,"dealCount":1,"dealDirection":"UP","divideStatus":"NOT_DIVIDE","exception":false,"handingChargeAmount":6,"id":"128184813013028864","loss":-1,"memberCode":"FT","payAmount":100,"profit":0,"profitAndLoss":0,"rollBackStatus":"DEFAULT","sale":false,"saleTimestamp":0,"sellingIncome":0,"sellingRate":0,"usedTicket":false,"usedTicketCount":0,"usedTicketId":0,"usedTicketValueAmount":0}
     * object : {"autoSale":false,"buyingDate":1482244703844,"buyingRate":2896,"consumeAmount":10.8,"contractCode":"YDOIL","customerId":53,"dealCount":1,"dealDirection":"UP","divideStatus":"NOT_DIVIDE","exception":false,"handingChargeAmount":0.8,"id":"128627156283412480","loss":-1,"memberCode":"FT","payAmount":10,"profit":0,"profitAndLoss":0,"rollBackStatus":"DEFAULT","sale":false,"saleTimestamp":0,"sellingIncome":0,"sellingRate":0,"usedTicket":false,"usedTicketCount":0,"usedTicketId":0,"usedTicketValueAmount":0}
     */

    private PositionEntity object;

    public PositionEntity getObject() {
        return object;
    }

    public void setObject(PositionEntity object) {
        this.object = object;
    }
}
