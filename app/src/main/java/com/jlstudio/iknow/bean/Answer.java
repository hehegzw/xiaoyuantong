package com.jlstudio.iknow.bean;

/**
 * Created by gzw on 2015/10/22.
 */
public class Answer {
    private String answer;
    private String user;
    private String time;
    private String id;

    public Answer(String answer, String user, String time,String id) {
        this.answer = answer;
        this.user = user;
        this.time = time;
        this.id = id;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
