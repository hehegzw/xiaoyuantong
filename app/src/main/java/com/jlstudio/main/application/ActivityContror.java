package com.jlstudio.main.application;

import android.app.Activity;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by gzw on 2015/10/14.
 */
public class ActivityContror {
    public static List<Activity> activities = new ArrayList<>();
    public static void addActivity(Activity aty){
        activities.add(aty);
    }
    public static void removeActivity(Activity aty){
        activities.remove(aty);
        aty.finish();
    }
    public static void finish(){
        Iterator<Activity> iter = activities.iterator();
        while(iter.hasNext()){

        }
//        for(Activity aty:activities){这个方法会爆ConcurrentModificationException。
//            removeActivity(aty);这个异常的意思是对迭代器的错误修改
//        }
    }
}
