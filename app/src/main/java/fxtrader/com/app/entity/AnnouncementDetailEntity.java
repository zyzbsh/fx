package fxtrader.com.app.entity;

import java.io.Serializable;

/**
 * Created by pc on 2017/1/7.
 */
public class AnnouncementDetailEntity implements Serializable{

    /**
     * content : 测测更健康
     * date : 1464086524
     * id : 1
     * memberId : 0
     * title :
     */

    private String content;
    private long date;
    private int id;
    private int memberId;
    private String title;

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public long getDate() {
        return date;
    }

    public void setDate(long date) {
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

    @Override
    public String toString() {
        return "AnnouncementDetailEntity{" +
                "content='" + content + '\'' +
                ", date=" + date +
                ", id=" + id +
                ", memberId=" + memberId +
                ", title='" + title + '\'' +
                '}';
    }
}
