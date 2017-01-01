package fxtrader.com.app.entity;

import java.util.List;

/**
 * Created by pc on 2016/12/31.
 */
public class AdEntity extends CommonResponse{

    /**
     * code : 200
     * message : 成功
     * object : [{"backgroundImageUrl":"","contentUrl":"","description":"","id":16,"rank":0,"title":"1"}]
     * success : true
     */

    private List<ObjectBean> object;

    public List<ObjectBean> getObject() {
        return object;
    }

    public void setObject(List<ObjectBean> object) {
        this.object = object;
    }

    public static class ObjectBean {
        /**
         * backgroundImageUrl :
         * contentUrl :
         * description :
         * id : 16
         * rank : 0
         * title : 1
         */

        private String backgroundImageUrl;
        private String contentUrl;
        private String description;
        private int id;
        private int rank;
        private String title;

        public String getBackgroundImageUrl() {
            return backgroundImageUrl;
        }

        public void setBackgroundImageUrl(String backgroundImageUrl) {
            this.backgroundImageUrl = backgroundImageUrl;
        }

        public String getContentUrl() {
            return contentUrl;
        }

        public void setContentUrl(String contentUrl) {
            this.contentUrl = contentUrl;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public int getRank() {
            return rank;
        }

        public void setRank(int rank) {
            this.rank = rank;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        @Override
        public String toString() {
            return "ObjectBean{" +
                    "backgroundImageUrl='" + backgroundImageUrl + '\'' +
                    ", contentUrl='" + contentUrl + '\'' +
                    ", description='" + description + '\'' +
                    ", id=" + id +
                    ", rank=" + rank +
                    ", title='" + title + '\'' +
                    '}';
        }
    }

    @Override
    public String toString() {
        return "AdEntity{" +
                "object=" + object +
                '}';
    }
}
