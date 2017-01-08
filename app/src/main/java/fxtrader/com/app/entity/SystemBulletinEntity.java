package fxtrader.com.app.entity;

/**
 * Created by pc on 2017/1/7.
 */
public class SystemBulletinEntity extends CommonResponse{

    /**
     * code : 200
     * message : 成功
     * object : {"bulletin":"","createDate":1482391081213,"id":0}
     * success : true
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
         * bulletin :
         * createDate : 1482391081213
         * id : 0
         */

        private String bulletin;
        private long createDate;
        private int id;

        public String getBulletin() {
            return bulletin;
        }

        public void setBulletin(String bulletin) {
            this.bulletin = bulletin;
        }

        public long getCreateDate() {
            return createDate;
        }

        public void setCreateDate(long createDate) {
            this.createDate = createDate;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        @Override
        public String toString() {
            return "ObjectBean{" +
                    "bulletin='" + bulletin + '\'' +
                    ", createDate=" + createDate +
                    ", id=" + id +
                    '}';
        }
    }

    @Override
    public String toString() {
        return "SystemBulletinEntity{" +
                "object=" + object +
                '}';
    }
}
