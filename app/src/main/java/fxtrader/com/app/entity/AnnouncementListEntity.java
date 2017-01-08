package fxtrader.com.app.entity;

import java.util.List;

/**
 * Created by pc on 2017/1/7.
 */
public class AnnouncementListEntity extends CommonResponse{

    /**
     * object : {"content":[{"content":"测测更健康","date":1464086524,"id":1,"memberId":0,"title":"我来啦"},{"content":"哈哈哈哈，加班好玩","date":1464089921,"id":2,"memberId":18,"title":"我又来了"}],"first":true,"last":true,"number":0,"numberOfElements":2,"size":20,"totalElements":2,"totalPages":1}
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
         * content : [{"content":"测测更健康","date":1464086524,"id":1,"memberId":0,"title":"我来啦"},{"content":"哈哈哈哈，加班好玩","date":1464089921,"id":2,"memberId":18,"title":"我又来了"}]
         * first : true
         * last : true
         * number : 0
         * numberOfElements : 2
         * size : 20
         * totalElements : 2
         * totalPages : 1
         */

        private boolean first;
        private boolean last;
        private int number;
        private int numberOfElements;
        private int size;
        private int totalElements;
        private int totalPages;
        private List<AnnouncementDetailEntity> content;

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

        public List<AnnouncementDetailEntity> getContent() {
            return content;
        }

        public void setContent(List<AnnouncementDetailEntity> content) {
            this.content = content;
        }

        public static class ContentBean {
            /**
             * content : 测测更健康
             * date : 1464086524
             * id : 1
             * memberId : 0
             * title : 我来啦
             */

            private String content;
            private int date;
            private int id;
            private int memberId;
            private String title;

            public String getContent() {
                return content;
            }

            public void setContent(String content) {
                this.content = content;
            }

            public int getDate() {
                return date;
            }

            public void setDate(int date) {
                this.date = date;
            }

            public int getId() {
                return id;
            }

            public void setId(int id) {
                this.id = id;
            }

            public int getMemberId() {
                return memberId;
            }

            public void setMemberId(int memberId) {
                this.memberId = memberId;
            }

            public String getTitle() {
                return title;
            }

            public void setTitle(String title) {
                this.title = title;
            }
        }
    }
}
