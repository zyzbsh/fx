package fxtrader.com.app.entity;

/**
 * Created by zhangyuzhu on 2017/2/21.
 */
public class AppUpdateResponse extends CommonResponse {

    /**
     * object : {"appVersions":"0.01","createTime":"1487239525000","downloadUrl":"www.baidu.com","forceUpdate":true,"id":1,"mobileDevices":"ios","updateContent":"1.测试"}
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
         * appVersions : 0.01
         * createTime : 1487239525000
         * downloadUrl : www.baidu.com
         * forceUpdate : true
         * id : 1
         * mobileDevices : ios
         * updateContent : 1.测试
         */

        private String appVersions;
        private String createTime;
        private String downloadUrl;
        private boolean forceUpdate;
        private int id;
        private String mobileDevices;
        private String updateContent;

        public String getAppVersions() {
            return appVersions;
        }

        public void setAppVersions(String appVersions) {
            this.appVersions = appVersions;
        }

        public String getCreateTime() {
            return createTime;
        }

        public void setCreateTime(String createTime) {
            this.createTime = createTime;
        }

        public String getDownloadUrl() {
            return downloadUrl;
        }

        public void setDownloadUrl(String downloadUrl) {
            this.downloadUrl = downloadUrl;
        }

        public boolean isForceUpdate() {
            return forceUpdate;
        }

        public void setForceUpdate(boolean forceUpdate) {
            this.forceUpdate = forceUpdate;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getMobileDevices() {
            return mobileDevices;
        }

        public void setMobileDevices(String mobileDevices) {
            this.mobileDevices = mobileDevices;
        }

        public String getUpdateContent() {
            return updateContent;
        }

        public void setUpdateContent(String updateContent) {
            this.updateContent = updateContent;
        }

        @Override
        public String toString() {
            return "ObjectBean{" +
                    "appVersions='" + appVersions + '\'' +
                    ", createTime='" + createTime + '\'' +
                    ", downloadUrl='" + downloadUrl + '\'' +
                    ", forceUpdate=" + forceUpdate +
                    ", id=" + id +
                    ", mobileDevices='" + mobileDevices + '\'' +
                    ", updateContent='" + updateContent + '\'' +
                    '}';
        }
    }

    @Override
    public String toString() {
        return "AppUpdateResponse{" +
                "object=" + object +
                '}';
    }
}
