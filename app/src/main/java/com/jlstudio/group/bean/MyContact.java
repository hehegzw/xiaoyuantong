package com.jlstudio.group.bean;

/**
 * Created by gzw on 2015/11/22.
 */
public class MyContact {
    private String name;
    private String groupName;

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    private String uid;
    private boolean isSelected;
    private boolean isRegister;
    private String phone;
    private int registerCount;
    private int totleCount;
    private String pinYinName;

    public String getPinYinName() {
        return pinYinName;
    }

    public void setPinYinName(String pinYinName) {
        this.pinYinName = pinYinName;
    }

    public MyContact() {
    }

    public MyContact(String name, String uid) {
        this.name = name;
        this.uid = uid;
        this.isSelected = false;
    }

    public MyContact(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public boolean isRegister() {
        return isRegister;
    }

    public void setIsRegister(boolean isRegister) {
        this.isRegister = isRegister;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setIsSelected(boolean isSelected) {
        this.isSelected = isSelected;
    }

    public int getRegisterCount() {
        return registerCount;
    }

    public void setRegisterCount(int registerCount) {
        this.registerCount = registerCount;
    }

    public int getTotleCount() {
        return totleCount;
    }

    public void setTotleCount(int totleCount) {
        this.totleCount = totleCount;
    }

    @Override
    public boolean equals(Object o) {
        if (this.name.equals(((MyContact) o).getName())) {
            return true;
        }
        return false;
    }

    @Override
    public String toString() {
        return "MyContact{" +
                "uid='" + uid + '\'' +
                ", name='" + name + '\'' +
                '}';
    }


}
