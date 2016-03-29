package com.jlstudio.publish.bean;

public class Contacts {
    private String name;
    private String userid;
    private String groupname;

    public String getGroupname() {
        return groupname;
    }

    public void setGroupname(String groupname) {
        this.groupname = groupname;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    private String pinYinName;

    public Contacts(String name) {
        super();
        this.name = name;
    }

    public Contacts(String name, String pinYinName) {
        this.name = name;
        this.pinYinName = pinYinName;
    }

    public Contacts() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPinYinName() {
        return pinYinName;
    }

    public void setPinYinName(String pinYinName) {
        this.pinYinName = pinYinName;
    }

    @Override
    public String toString() {
        return "Contacts{" +
                "name='" + name + '\'' +
                ", pinYinName='" + pinYinName + '\'' +
                '}';
    }
}
