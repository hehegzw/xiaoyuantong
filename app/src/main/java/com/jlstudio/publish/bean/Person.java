package com.jlstudio.publish.bean;

/**
 * Created by gzw on 2015/10/20.
 */
public class Person {
    private String username;
    private String name;
    private String classes;
    private String phone;
    private boolean isSelect = false;

    public Person(String name) {
        this.name = name;
    }

    public Person(String username, String name, String classes, String phone) {
        this.username = username;
        this.name = name;
        this.classes = classes;
        this.phone = phone;
    }

    public Person(String name, String phone) {
        this.name = name;
        this.phone = phone;
        this.username = phone;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getClasses() {
        return classes;
    }

    public void setClasses(String classes) {
        this.classes = classes;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public boolean isSelect() {
        return isSelect;
    }

    public void setIsSelect(boolean isSelect) {
        this.isSelect = isSelect;
    }
}
