package com.jlstudio.weather.util;

import android.app.Activity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by gzw on 2015/10/5.
 */
public class ActivityControl {
    public static List<Activity> activitys =  new ArrayList<>();

    public static void addAty(Activity aty){
        activitys.add(aty);
    }
    public static void removeAty(Activity aty){
        activitys.remove(aty);
        aty.finish();
    }
    public static void destroryAllAty(){
        if(activitys!=null){
            for(Activity aty : activitys){
                aty.finish();
            }
        }
    }
}
