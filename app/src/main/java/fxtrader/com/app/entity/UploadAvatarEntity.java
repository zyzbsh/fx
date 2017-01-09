package fxtrader.com.app.entity;

/**
 * Created by pc on 2017/1/8.
 */
public class UploadAvatarEntity extends CommonResponse{
    public ObjectBean object;

    public ObjectBean getObject() {
        return object;
    }

    public void setObject(ObjectBean object) {
        this.object = object;
    }

    public static class ObjectBean {
        public String headimgurl;

        public String getHeadimgurl() {
            return headimgurl;
        }

        public void setHeadimgurl(String headimgurl) {
            this.headimgurl = headimgurl;
        }
    }


}
