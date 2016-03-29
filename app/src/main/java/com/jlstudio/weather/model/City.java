package com.jlstudio.weather.model;

/**
 * Created by gzw on 2015/10/5.
 */
public class City {
    private String name;
    private String code;
    private String province;

    public City(String code, String name, String province) {
        this.name = name;
        this.code = code;
        this.province = province;
    }

    public City() {
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

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }
}
