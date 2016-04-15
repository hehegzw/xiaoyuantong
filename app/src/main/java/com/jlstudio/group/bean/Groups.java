package com.jlstudio.group.bean;

/**
 * Created by gzw on 2015/12/7.
 */
public class Groups {
    private String group_name;//组名
    private int subCounts;//组中包含人数
    private int registerCount;//注册人数
    private boolean isSelected;//是否被选中(选人时候用)
    private boolean isChange;//主要用于点击进入班级之前判断状态有没有改变

    public Groups() {
    }

    public String getGroup_name() {
        return group_name;
    }

    public void setGroup_name(String group_name) {
        this.group_name = group_name;
    }

    public int getSubCounts() {
        return subCounts;
    }

    public void setSubCounts(int subCounts) {
        this.subCounts = subCounts;
    }

    public int getRegisterCount() {
        return registerCount;
    }

    public void setRegisterCount(int registerCount) {
        this.registerCount = registerCount;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setIsSelected(boolean isSelected) {
        this.isSelected = isSelected;
    }

    public boolean isChange() {
        return isChange;
    }

    public void setChange(boolean change) {
        isChange = change;
    }
}
