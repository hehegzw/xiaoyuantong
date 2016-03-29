package com.jlstudio.main.util;

import android.util.Log;

import com.jlstudio.main.bean.User;
import com.jlstudio.publish.bean.MyContact;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by gzw on 2015/10/30.
 */
public class JsonToBean {
    /**
     * json转换为user类型
     * @param string json字符串
     * @return
     */
    public static User getUser(String string) {
        User user = null;
        if(string != null){
            JSONObject json = null;
            try {
                json = new JSONObject(string);
                user = new User(json.getString("username"), json.getString("password"), json.getString("role"));
                return user;

            } catch (JSONException e) {
                e.printStackTrace();
                user = null;
            }
        }
        return user;

    }


}
