package com.jlstudio.swzl.bean;

import java.io.Serializable;

/**
 * Created by Joe on 2015/10/27.
 */
public class commons implements Serializable {
    private int id_commons;//该评论的id
    private String content;//该评论的内容
    private String userid;//评论人的id
    private String time;//评论时间
    private String for_which;//评论的哪一个条目的id
    private String userNickname;//评论人的昵称

    public String getUserNickname() {
        return userNickname;
    }

    public void setUserNickname(String userNickname) {
        this.userNickname = userNickname;
    }

    public commons(int id_commons, String content, String userid, String time, String for_which,String userNickname) {
        this.id_commons = id_commons;
        this.content = content;
        this.userid = userid;
        this.time = time;
        this.for_which = for_which;
        this.userNickname = userNickname;
    }

    public String getFor_which() {
        return for_which;
    }

    public void setFor_which(String for_which) {
        this.for_which = for_which;
    }

    public int getId_commons() {
        return id_commons;
    }

    public void setId_commons(int id_commons) {
        this.id_commons = id_commons;
    }


    public commons() {
    }


    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
