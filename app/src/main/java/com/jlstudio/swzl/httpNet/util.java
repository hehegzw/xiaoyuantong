package com.jlstudio.swzl.httpNet;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.jlstudio.main.bean.CatchData;
import com.jlstudio.main.db.DBOption;
import com.jlstudio.swzl.activity.LostAndFound;
import com.jlstudio.swzl.bean.commons;
import com.jlstudio.swzl.bean.lostfound;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Joe on 2015/10/26.
 */
public class util {
    public static boolean flag;
    public static DBOption dbOption = null;
    private static CatchData cd = new CatchData(false);

    /**
     * 判断数据的时间是否过期
     * @return 是或否
     */
    public static boolean IsOutTime(DBOption dbOption, CatchData cd, String httpurl) {
        if( cd == null){
            //判断为第一次登陆，没有该数据
            dbOption.insertCatch(httpurl, "", getcurTime());
            return true;
        }
        else{
            //判断是否是过期数据
            long t = CompareTime(getcurTime(),cd.getTime());
            //当时间相差30s，即视为过期数据
            if(t >= 15){
                return true;
            }
            else
                return false;
        }
    }


    //获取当前时间的函数
    public static String getcurTime(){
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date curDate = new Date(System.currentTimeMillis());
        return formatter.format(curDate);
    }
    //判断两个时间相减的秒数
    public static long CompareTime(String t1,String t2){
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        long diff;
        try {
            Date d1 = df.parse(t1);
            Date d2 = df.parse(t2);
            diff = d1.getTime() - d2.getTime();
        } catch (ParseException e) {
            Log.d("util类中","时间比较 时间格式转换异常");
            e.printStackTrace();
            diff = -1;
        }
        return diff/1000;
    }
}