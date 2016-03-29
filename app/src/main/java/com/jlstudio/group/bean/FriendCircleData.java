package com.jlstudio.group.bean;

/**
 * Created by NewOrin on 2015/10/23.
 */
public class FriendCircleData {
    private String name;
    private String time;
    private String comment;

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public FriendCircleData(String name, String time, String comment) {
        this.name = name;
        this.time = time;
        this.comment = comment;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
