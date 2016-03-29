package com.jlstudio.market.bean;

/**
 * Created by gzw on 2016/3/24.
 */
public class Chat {
    private String chatId;
    private String userid;
    private String text;
    private boolean isLeft = true;

    public Chat(String userid, String text) {
        this.userid = userid;
        this.text = text;
    }

    public boolean isLeft() {
        return isLeft;
    }

    public void setLeft(boolean left) {
        isLeft = left;
    }

    public String getChatId() {
        return chatId;
    }

    public void setChatId(String chatId) {
        this.chatId = chatId;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
