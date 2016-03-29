package com.jlstudio.main.bean;

/**
 * Created by gzw on 2015/11/21.
 */
public class GridViewItem {
    private int image;
    private String name;

    public GridViewItem(int image, String name) {
        this.image = image;
        this.name = name;
    }

    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
