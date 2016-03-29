package com.jlstudio.weather.util;

import android.content.Context;
import android.content.SharedPreferences;

import com.jlstudio.main.application.MyApplication;


/**
 * Created by gzw on 2015/10/5.
 */
public class Config {
    public static String CONFIG = "config";
    public static void saveDatas(String result){
        SharedPreferences sh = MyApplication.getContext().getSharedPreferences(CONFIG, Context.MODE_PRIVATE);
        sh.edit().putString("datas", result).commit();
    }
    public static String loadDatas(){
        SharedPreferences sh = MyApplication.getContext().getSharedPreferences(CONFIG, Context.MODE_PRIVATE);
        return sh.getString("datas",null);
    }
    public static void saveArea(String area){
        SharedPreferences sh = MyApplication.getContext().getSharedPreferences(CONFIG, Context.MODE_PRIVATE);
        sh.edit().putString("area", area).commit();
    }
    public static String loadArea(){
        SharedPreferences sh = MyApplication.getContext().getSharedPreferences(CONFIG, Context.MODE_PRIVATE);
        return sh.getString("area",null);
    }
    public static boolean isFirstLogin(){
        SharedPreferences sh = MyApplication.getContext().getSharedPreferences(CONFIG, Context.MODE_PRIVATE);
        if(sh.getBoolean("isfirstlogin",true)){
            sh.edit().putBoolean("isfirstlogin", false).commit();
            return true;
        }
        return false;
    }
}
