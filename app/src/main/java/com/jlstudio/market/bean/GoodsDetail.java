package com.jlstudio.market.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by zmzmd on 2016/3/24.
 */
public class GoodsDetail implements Serializable{
    private int id;//物品id
    private String face;//用户头像
    private String userid;//用户id
    private String username;//用户姓名
    private String fromWhere;//用户来自哪里
    private String description;//物品描述
    private int views;//浏览次数
    private long datetime;//发布时间
    private int closeFlag;//是否下架
    private double price;//物品价格
    private List<String> images;//物品配图，给图片全路径

    public GoodsDetail() {
    }

    public GoodsDetail(int id, String userid, String description, int views, long datetime, double price) {
        this.id = id;
        this.userid = userid;
        this.description = description;
        this.views = views;
        this.datetime = datetime;
        this.price = price;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getViews() {
        return views;
    }

    public void setViews(int views) {
        this.views = views;
    }

    public long getDatetime() {
        return datetime;
    }

    public void setDatetime(long datetime) {
        this.datetime = datetime;
    }

    public int getCloseFlag() {
        return closeFlag;
    }

    public void setCloseFlag(int closeFlag) {
        this.closeFlag = closeFlag;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public List<String> getImages() {
        return images;
    }

    public void setImages(List<String> images) {
        this.images = images;
    }

    public String getFace() {
        return face;
    }

    public void setFace(String face) {
        this.face = face;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getFromWhere() {
        return fromWhere;
    }

    public void setFromWhere(String fromWhere) {
        this.fromWhere = fromWhere;
    }
}
