package com.jlstudio.publish.bean;

import java.io.Serializable;

/**
 * Created by gzw on 2015/10/16.
 */
public class PublishListData implements Serializable{
    private String title;
    private String time;
    private String noticeid;
    private String flag;//是否已读
    private String noticegroup;//notice组

    public PublishListData(String title, String time, String id, String flag) {
        this.title = title;
        this.time = time;
        this.noticeid = id;
        this.flag = flag;
    }

    public PublishListData() {
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getNoticeid() {
        return noticeid;
    }

    public void setNoticeid(String id) {
        this.noticeid = id;
    }

    public String getFlag() {
        return flag;
    }

    public void setFlag(String flag) {
        this.flag = flag;
    }

    public String getNoticegroup() {
        return noticegroup;
    }

    public void setNoticegroup(String noticegroup) {
        this.noticegroup = noticegroup;
    }
}
