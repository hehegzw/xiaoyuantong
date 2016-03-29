package com.jlstudio.weather.model;

/**
 * Created by gzw on 2015/10/5.
 */
public class Province {
    private String name;
    private String code;

    public Province(String code, String name) {
        this.name = name;
        this.code = code;
    }

    public Province() {
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
}
