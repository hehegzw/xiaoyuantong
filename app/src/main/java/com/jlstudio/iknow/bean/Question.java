package com.jlstudio.iknow.bean;

import java.io.Serializable;

/**
 * Created by gzw on 2015/10/21.
 */
public class Question implements Serializable{
    private String titile;
    private String content;
    private String id;
    private String time;
    private String count;
    private String user;
    private String score;

    public Question(String titile, String content, String id, String time,String count,String user,String score) {
        this.titile = titile;
        this.content = content;
        this.id = id;
        this.time = time;
        this.count = count;
        this.user = user;
        this.score = score;
    }
    public Question(String titile, String content, String id, String time,String count) {
        this.titile = titile;
        this.content = content;
        this.id = id;
        this.time = time;
        this.count = count;
    }

    public String getScore() {
        return score;
    }

    public void setScore(String score) {
        this.score = score;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getCount() {
        return count;
    }

    public void setCount(String count) {
        this.count = count;
    }

    public String getTitile() {
        return titile;
    }

    public void setTitile(String titile) {
        this.titile = titile;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
