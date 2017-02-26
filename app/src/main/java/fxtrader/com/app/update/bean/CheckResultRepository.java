package fxtrader.com.app.update.bean;

public class CheckResultRepository {

    /**
     * code : 0
     * data : {"download_url":"http://wap.apk.anzhi.com/data3/apk/201512/20/55089e385f6e9f350e6455f995ca3452_26503500.apk","force":false,"update_content":"测试更新接口","v_code":"10","v_name":"v1.0.0.16070810","v_sha1":"7db76e18ac92bb29ff0ef012abfe178a78477534","v_size":12365909}
     */
    private int code;

    private UpdateBean data;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public UpdateBean getData() {
        return data;
    }

    public void setData(UpdateBean data) {
        this.data = data;
    }
}
