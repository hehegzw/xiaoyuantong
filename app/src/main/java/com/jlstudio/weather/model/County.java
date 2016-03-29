package com.jlstudio.weather.model;

/**
 * Created by gzw on 2015/10/5.
 */
public class County {
    private String name;
    private String code;
    private String city;

    public County(String code, String name, String city) {
        this.name = name;
        this.code = code;
        this.city = city;
    }

    public County() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }
}
