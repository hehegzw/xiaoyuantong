package com.jlstudio.swzl.httpNet;

import android.util.Log;

import com.jlstudio.swzl.bean.commons;
import com.jlstudio.swzl.bean.lostfound;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Administrator on 2015/11/5.
 */
public class ChangeJson {
    /**
     * 将json字符串转化为对象集合
     * @param json
     * @return 失物招领数据
     */
    public static ArrayList<lostfound> L_changeJsonToArrayDate(String json){
        Log.d("ChangeJson", "进入了json转化");
        ArrayList<lostfound> list = new ArrayList<lostfound>();
        try {
            JSONObject jsonObject = new JSONObject(json);
            JSONArray array = jsonObject.getJSONArray("data");
            //遍历json对象集合
            for (int i = 0; i < array.length(); i++) {
                JSONObject o = array.getJSONObject(i);
                list.add(new lostfound(o.getInt("userid"),//发布者的id
                        o.getString("describe"),//描述
                        1,//图片id
                        o.getInt("intergral"),//悬赏积分
                        o.getString("datetime"),//时间
                        o.getInt("flag"),//失物还是招领的标志
                        o.getInt("common_count"),//评论数量
                        o.getString("userrealname"),//昵称
                        o.getString("title")//标题
                        ,o.getInt("id")));//失物招领id
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    /**
     * 将json字符串转化为一个评论的对象集合
     * @param s
     * @return
     */
    public static ArrayList<commons> C_changeJsonToArrayDate(String s) {
        ArrayList<commons> list = new ArrayList<commons>();
        try {
            JSONObject jsonObject = new JSONObject(s);
            JSONArray array = jsonObject.getJSONArray("data");
            //遍历json对象集合
            for (int i = 0; i < array.length(); i++) {
                JSONObject o = array.getJSONObject(i);
                //获取服务器传过来的对象
                list.add(new commons(1,o.getString("lfctext"),"1",o.getString("datetime"),"1",o.getString("username")));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }
}
