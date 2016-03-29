package com.jlstudio.group.util;

import android.content.Context;

/**
 * Created by gzw on 2016/3/12.
 */
public class DpPxUtil {
    public static int getPx(Context context,int dp){
        float density = context.getResources().getDisplayMetrics().density;
        int temp = (int) (dp/density);
        return temp;
    }
    public static int getDp(Context context,int px){
        float density = context.getResources().getDisplayMetrics().density*3;
        int temp = (int) (px/density);
        return temp;
    }
}
