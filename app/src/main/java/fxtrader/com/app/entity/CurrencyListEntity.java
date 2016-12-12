package fxtrader.com.app.entity;

import java.util.List;

/**
 * Created by zhangyuzhu on 2016/12/7.
 */
public class CurrencyListEntity extends CommonResponse{

    /**
     * code : 200
     * message : ??
     * object : {"content":[{"context":"108984446525812736","contextClazz":"java.lang.String","date":1477561516000,"id":"108984446592921600","memberCode":"HT","remainingMoney":0,"transactionMoney":0,"transactionType":"BUILD_METALORDER"}],"first":true,"last":true,"number":0,"numberOfElements":1,"size":20,"totalElements":1,"totalPages":1}
     * success : true
     */
    private ObjectBean object;


    public ObjectBean getObject() {
        return object;
    }

    public void setObject(ObjectBean object) {
        this.object = object;
    }

    @Override
    public String toString() {
        return super.toString() + "CurrencyListEntity{" +
                "object=" + object +
                '}';
    }

    public static class ObjectBean {
        /**
         * content : [{"context":"108984446525812736","contextClazz":"java.lang.String","date":1477561516000,"id":"108984446592921600","memberCode":"HT","remainingMoney":0,"transactionMoney":0,"transactionType":"BUILD_METALORDER"}]
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
        private List<CurrencyDetailEntity> content;

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

        public List<CurrencyDetailEntity> getContent() {
            return content;
        }

        public void setContent(List<CurrencyDetailEntity> content) {
            this.content = content;
        }

        @Override
        public String toString() {
            return "ObjectBean{" +
                    "first=" + first +
                    ", last=" + last +
                    ", number=" + number +
                    ", numberOfElements=" + numberOfElements +
                    ", size=" + size +
                    ", totalElements=" + totalElements +
                    ", totalPages=" + totalPages +
                    ", content=" + content +
                    '}';
        }
    }
}
