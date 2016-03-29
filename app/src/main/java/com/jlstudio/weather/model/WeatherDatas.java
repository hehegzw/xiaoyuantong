package com.jlstudio.weather.model;

import java.io.Serializable;

/**
 * Created by gzw on 2015/10/5.
 */
public class WeatherDatas implements Serializable {
    private String name;
    private String fengxiang;
    private String fengli;
    private String height;
    private String type;
    private String low;
    private String date;
    private String wendu;
    private String ganmao;

    public WeatherDatas(String name, String fengxiang, String fengli, String height, String type, String low, String date, String wendu, String ganmao) {
        this.name = name;
        this.fengxiang = fengxiang;
        this.fengli = fengli;
        this.height = height;
        this.type = type;
        this.low = low;
        this.date = date;
        this.wendu = wendu;
        this.ganmao = ganmao;
    }

    public WeatherDatas() {
    }

    public String getWendu() {
        return wendu;
    }

    public void setWendu(String wendu) {
        this.wendu = wendu;
    }

    public String getGanmao() {
        return ganmao;
    }

    public void setGanmao(String ganmao) {
        this.ganmao = ganmao;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFengxiang() {
        return fengxiang;
    }

    public void setFengxiang(String fengxiang) {
        this.fengxiang = fengxiang;
    }

    public String getFengli() {
        return fengli;
    }

    public void setFengli(String fengli) {
        this.fengli = fengli;
    }

    public String getHeight() {
        return height;
    }

    public void setHeight(String height) {
        this.height = height;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getLow() {
        return low;
    }

    public void setLow(String low) {
        this.low = low;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
    public String toString(){
        return name+" "+fengxiang+" "+fengli+" "+height+" "+type+" "+low+" "+date;
    }
}
