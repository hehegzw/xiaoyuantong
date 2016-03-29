package com.jlstudio.iknow.util;

import com.jlstudio.iknow.bean.Answer;
import com.jlstudio.iknow.bean.Question;
import com.jlstudio.iknow.bean.Schedule;
import com.jlstudio.iknow.bean.ScoreItem;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by gzw on 2015/10/22.
 */
public class JsonUtil {
    public static List<Schedule> getSchedules(String data){
        List<Schedule> list = new ArrayList<>();
        try {
            JSONObject json = new JSONObject(data);
            JSONArray array = json.getJSONArray("datas");
            for(int i=0;i<array.length();i++){
                JSONObject o = array.getJSONObject(i);
                list.add(new Schedule(o.getString("第一节"),o.getString("第三节"),o.getString("第五节"),o.getString("第七节"),o.getString("第九节")));
            }
            return list;
        } catch (JSONException e) {
            e.printStackTrace();
            return list;
        }
    }
    public static List<ScoreItem> getScores(String data){
        List<ScoreItem> list = new ArrayList<>();
        try {
            JSONObject json = new JSONObject(data);
            JSONArray array = json.getJSONArray("datas");
            for (int i=0;i<array.length();i++){
                try {
                    JSONObject o = array.getJSONObject(i);
                    list.add(new ScoreItem(o.getString("课程名称"),o.getString("学分"),o.getString("绩点"),o.getString("成绩")));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            Collections.sort(list,new ListCompare());
            return list;
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }
}
