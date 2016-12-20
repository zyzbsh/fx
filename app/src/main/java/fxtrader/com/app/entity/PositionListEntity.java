package fxtrader.com.app.entity;

import java.util.List;

/**
 * Created by pc on 2016/12/20.
 */
public class PositionListEntity extends CommonResponse {

    /**
     * object : {"content":[{"autoSale":false,"buyingDate":1477561516000,"buyingRate":3831,"consumeAmount":0,"contractCode":"AG01","customerId":121,"dealCount":0,"dealDirection":"UP","divideStatus":"DIVIDE","exception":false,"handingChargeAmount":0,"id":"108984446525812736","loss":-1,"memberCode":"HT","payAmount":0,"profit":0,"profitAndLoss":0,"rollBackStatus":"DEFAULT","sale":true,"saleTimestamp":1477562432,"sellingDate":1477562432000,"sellingIncome":0,"sellingRate":3829,"sellingType":"MANUAL","usedTicket":true,"usedTicketCount":1,"usedTicketId":1,"usedTicketValueAmount":8}],"first":true,"last":true,"number":0,"numberOfElements":1,"size":20,"totalElements":1,"totalPages":1}
     */

    private ObjectBean object;

    public ObjectBean getObject() {
        return object;
    }

    public void setObject(ObjectBean object) {
        this.object = object;
    }

    public static class ObjectBean {
        /**
         * content : [{"autoSale":false,"buyingDate":1477561516000,"buyingRate":3831,"consumeAmount":0,"contractCode":"AG01","customerId":121,"dealCount":0,"dealDirection":"UP","divideStatus":"DIVIDE","exception":false,"handingChargeAmount":0,"id":"108984446525812736","loss":-1,"memberCode":"HT","payAmount":0,"profit":0,"profitAndLoss":0,"rollBackStatus":"DEFAULT","sale":true,"saleTimestamp":1477562432,"sellingDate":1477562432000,"sellingIncome":0,"sellingRate":3829,"sellingType":"MANUAL","usedTicket":true,"usedTicketCount":1,"usedTicketId":1,"usedTicketValueAmount":8}]
         * first : true
         * last : true
         * number : 0
         * numberOfElements : 1
         * size : 20
         * totalElements : 1
         * totalPages : 1
         */

        private boolean first;
        private boolean last;
        private int number;
        private int numberOfElements;
        private int size;
        private int totalElements;
        private int totalPages;
        private List<PositionInfoEntity> content;

        public boolean isFirst() {
            return first;
        }

        public void setFirst(boolean first) {
            this.first = first;
        }

        public boolean isLast() {
            return last;
        }

        public void setLast(boolean last) {
            this.last = last;
        }

        public int getNumber() {
            return number;
        }

        public void setNumber(int number) {
            this.number = number;
        }

        public int getNumberOfElements() {
            return numberOfElements;
        }

        public void setNumberOfElements(int numberOfElements) {
            this.numberOfElements = numberOfElements;
        }

        public int getSize() {
            return size;
        }

        public void setSize(int size) {
            this.size = size;
        }

        public int getTotalElements() {
            return totalElements;
        }

        public void setTotalElements(int totalElements) {
            this.totalElements = totalElements;
        }

        public int getTotalPages() {
            return totalPages;
        }

        public void setTotalPages(int totalPages) {
            this.totalPages = totalPages;
        }

        public List<PositionInfoEntity> getContent() {
            return content;
        }

        public void setContent(List<PositionInfoEntity> content) {
            this.content = content;
        }

    }
}
