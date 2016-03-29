package com.jlstudio.publish.bean;

import android.graphics.drawable.Drawable;
import android.widget.Button;

/**
 * Created by gzw on 2015/10/21.
 */
public class Type {
    private Button bt;
    private boolean isSelect;
    private Drawable drawable;

    public Type(Button bt,Drawable drawable) {
        this.bt = bt;
        this.drawable = drawable;
    }

    public Button getBt() {
        return bt;
    }


    public void setBt(Button bt) {
        this.bt = bt;
    }

    public boolean isSelect() {
        return isSelect;
    }

    public void setIsSelect(boolean isSelect) {
        this.isSelect = isSelect;
    }

    public Drawable getDrawable() {
        return drawable;
    }

    public void setDrawable(Drawable drawable) {
        this.drawable = drawable;
    }
}
