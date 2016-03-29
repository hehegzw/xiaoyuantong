package com.jlstudio.publish.bean;

import java.io.Serializable;

/**
 * Created by gzw on 2015/10/16.
 */
public class PublishData implements Serializable{
    private String title;//消息标题
    private String time;//消息时间
    private String id;//消息id
    private String content;//消息内容
    private String replyedCount;//回执人数
    private String unReplyCount;//未回执人数
    private String flag;//标志位，针对收到的消息，1为可删除，2为可回复
    private String filename;//附件的名称
    private String filePath;//附件的路径
    private String userFrom;//附件的路径
    private String noticegroup;//组id
    private String noticefromid;//发送人id
    private String noticefromname;//发送人姓名

    public PublishData() {
    }

    public String getFlag() {
        return flag;
    }

    public void setFlag(String flag) {
        this.flag = flag;
    }

    public PublishData(String title, String time,String content) {
        this.title = title;
        this.time = time;
        this.content = content;
    }
    public PublishData(String title, String time,String content,String id) {
        this.title = title;
        this.time = time;
        this.content = content;
        this.id = id;
    }

    public PublishData(String title, String time, String content, String replyedCount, String unReplyCount) {
        this.title = title;
        this.time = time;
        this.content = content;
        this.replyedCount = replyedCount;
        this.unReplyCount = unReplyCount;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getReplyedCount() {
        return replyedCount;
    }

    public void setReplyedCount(String replyedCount) {
        this.replyedCount = replyedCount;
    }

    public String getUnReplyCount() {
        return unReplyCount;
    }

    public void setUnReplyCount(String unReplyCount) {
        this.unReplyCount = unReplyCount;
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

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public String getUserFrom() {
        return userFrom;
    }

    public void setUserFrom(String userFrom) {
        this.userFrom = userFrom;
    }

    public String getNoticegroup() {
        return noticegroup;
    }

    public void setNoticegroup(String noticegroup) {
        this.noticegroup = noticegroup;
    }

    public String getNoticefromid() {
        return noticefromid;
    }

    public void setNoticefromid(String noticefromid) {
        this.noticefromid = noticefromid;
    }

    public String getNoticefromname() {
        return noticefromname;
    }

    public void setNoticefromname(String noticefromname) {
        this.noticefromname = noticefromname;
    }
}
