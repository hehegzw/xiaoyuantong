package com.jlstudio.group.bean;

/**
 * Created by gzw on 2016/3/8.
 */
public class LeaveMsg {
    private String time;
    private String content;
    private String from;//本条发送人
    private String fromname;//本条发送人姓名
    private String to;//本条接收人
    private String toname;//本条接收人姓名
    private String id;//本条消息
    private int type;
    private int squence;
    private String oriFrom;
    private String oriFromname;
    private String oriId;
    private String oriToname;

    public LeaveMsg() {
    }

    public LeaveMsg(String content, String from, String to, String id) {
        this.content = content;
        this.from = from;
        this.to = to;
        this.id = id;
    }

    public LeaveMsg(int type,int squence,String oriId,String oriFrom) {
        this.type = type;
        this.squence = squence;
        this.oriId = oriId;
        this.oriFrom = oriFrom;
    }

    public LeaveMsg(int type, String content,int squence) {
        this.type = type;
        this.content = content;
        this.squence = squence;
    }

    public LeaveMsg(String time, String content, String from, String to, String id) {
        this.time = time;
        this.content = content;
        this.from = from;
        this.to = to;
        this.id = id;
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

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getSquence() {
        return squence;
    }

    public void setSquence(int squence) {
        this.squence = squence;
    }

    public String getOriFrom() {
        return oriFrom;
    }

    public void setOriFrom(String oriFrom) {
        this.oriFrom = oriFrom;
    }

    public String getOriId() {
        return oriId;
    }

    public void setOriId(String oriId) {
        this.oriId = oriId;
    }

    public String getFromname() {
        return fromname;
    }

    public void setFromname(String fromname) {
        this.fromname = fromname;
    }

    public String getToname() {
        return toname;
    }

    public void setToname(String toname) {
        this.toname = toname;
    }

    public String getOriFromname() {
        return oriFromname;
    }

    public void setOriFromname(String oriFromname) {
        this.oriFromname = oriFromname;
    }

    public String getOriToname() {
        return oriToname;
    }

    public void setOriToname(String oriToname) {
        this.oriToname = oriToname;
    }

    @Override
    public String toString() {
        return "LeaveMsg{" +
                "time='" + time + '\'' +
                ", content='" + content + '\'' +
                ", from='" + from + '\'' +
                ", to='" + to + '\'' +
                ", id='" + id + '\'' +
                '}';
    }
}
