package com.jlstudio.weather.util;


import com.jlstudio.weather.model.WeatherDatas;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by gzw on 2015/10/7.
 */
public class FormatDatas {
    public static WeatherDatas getDatas(String result){
        JSONObject json = null;
        try {
            json = new JSONObject(result);
            JSONObject data = json.getJSONObject("data");
            String wendu = data.getString("wendu");
            String ganmao = data.getString("ganmao");
            JSONArray forecast = data.getJSONArray("forecast");
            JSONObject json2 = forecast.getJSONObject(0);
            WeatherDatas datas = new WeatherDatas(data.getString("city"),json2.getString("fengxiang"),json2.getString("fengli"),json2.getString("high"),json2.getString("type"),json2.getString("low"),json2.getString("date"),wendu,ganmao);
            return datas;
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }

    }
}
