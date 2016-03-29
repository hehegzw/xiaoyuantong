package com.jlstudio.swzl.bean;

import java.io.Serializable;

/**
 * Created by Joe on 2015/10/23.
 */
public class lostfound implements Serializable {
    private int id_lostfound;//id
    private int ownerId;//发布者id
    private int pic;//物品图片
    private int integral;//悬赏积分
    private String time;//物品丢失或捡到日期
    private int flag;//0代表失物，1代表招领
    private String nickname;//发布者的昵称
    private String title;//标题
    private String describe;//物品描述
    private int common_count;//评论数量
    public lostfound(int ownerId, String describe, int pic, int integral, String time, int flag, int common_count, String nickname, String title,int id_lostfound) {
        this.ownerId = ownerId;
        this.describe = describe;
        this.pic = pic;
        this.integral = integral;
        this.time = time;
        this.flag = flag;
        this.common_count = common_count;
        this.nickname = nickname;
        this.title = title;
        this.id_lostfound = id_lostfound;
    }
    public lostfound() {
    }

    public int getId_lostfound() {
        return id_lostfound;
    }

    public void setId_lostfound(int id_lostfound) {
        this.id_lostfound = id_lostfound;
    }

    public int getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(int ownerId) {
        this.ownerId = ownerId;
    }

    public int getPic() {
        return pic;
    }

    public void setPic(int pic) {
        this.pic = pic;
    }

    public int getIntegral() {
        return integral;
    }

    public void setIntegral(int integral) {
        this.integral = integral;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public int getFlag() {
        return flag;
    }

    public void setFlag(int flag) {
        this.flag = flag;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescribe() {
        return describe;
    }

    public void setDescribe(String describe) {
        this.describe = describe;
    }

    public int getCommon_count() {
        return common_count;
    }

    public void setCommon_count(int common_count) {
        this.common_count = common_count;
    }



}