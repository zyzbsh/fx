package fxtrader.com.app.entity;

/**
 * Created by pc on 2016/12/29.
 */
public class FollowOrderCountEntity extends CommonResponse {

    /**
     * object : {"count":0}
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
         * count : 0
         */

        private int count;

        public int getCount() {
            return count;
        }

        public void setCount(int count) {
            this.count = count;
        }
    }
}
