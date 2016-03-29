
package com.jlstudio.group.util;

import android.content.Context;
import android.util.Log;

import com.jlstudio.group.activity.ShowContactsActivity;
import com.jlstudio.group.bean.Contacts;
import com.jlstudio.group.bean.Groups;
import com.jlstudio.main.application.Config;
import com.jlstudio.main.bean.CatchData;
import com.jlstudio.main.db.DBOption;
import com.jlstudio.publish.util.JsonToPubhlishData;

import java.util.ArrayList;

/**
 * Created by Joe on 2015/11/26.
 */
public class SearchUtil {

    /**
     * 只用于 网络搜索json解析
     * @param string json字符串
     * @param list 需要改动的联系人集合
     * @return
     */
    public static ArrayList<Contacts> getContactsFromJson(String string,ArrayList<Contacts> list) {
//        if(list.size() != 0)
//            list.clear();
//        try {
//            JSONObject json = new JSONObject(string);
//            JSONArray array = json.getJSONArray("datas");
//            for (int i = 0; i < array.length(); i++) {
//                JSONObject o = array.getJSONObject(i);
//                list.add(new Contacts(o.getString("realname"), o.getString("username")));
//            }
//        } catch (JSONException e) {
//            e.printStackTrace();
//            Log.d("SearchActivity", "进方法解析失败");
//            return null;
//        }
        /* ************下边是写死的数据************ */
//        list.add(new Contacts("张三","zhangsan"));
//        list.add(new Contacts("张三2","zhangsan"));
//        list.add(new Contacts("张三3","zhangsan"));
//        list.add(new Contacts("张三4","zhangsan"));
//        list.add(new Contacts("张三5","zhangsan"));
//        list.add(new Contacts("张三6","zhangsan"));
        return list;
    }

    /**
     * 只用于搜索  * 本地的好友 *
     * 判断当前用户是什么用户
     * @param string json字符串
     * @param list 需要改变的集合
     * @param context 上下文对象
     * @return 解析后的用户列表集合
     */
    private static ArrayList<Contacts> newPersons;
    private static DBOption db;
    private static CatchData catchData;
    private static ArrayList<Groups> group_list;
    private static ArrayList<Contacts> sort_persons;

    public static ArrayList<Contacts> getContactsFromLocalJson(ArrayList<Contacts> list,Context context){
        if("普通用户".equals(Config.loadUser(context).getRole())){
            //当前用户 普通学生
            return analysisJsonStu(list,context);
        }else{
            //当前用户 领导
            return analysisJson(list,context);
        }
    }

    /**
     * 领导json的解析方法
     * @return
     */
    private static ArrayList<Contacts> analysisJson(ArrayList<Contacts> list, Context context) {
        return null;
    }

    /**
     * 一般学生json的解析方法
     * @return
     */
    public static ArrayList<Contacts> analysisJsonStu(ArrayList<Contacts> list,Context context) {
        newPersons = new ArrayList<>();
        catchData = db.getCatch(ShowContactsActivity.personalUrl);
        String s = catchData.getContent();
        group_list = (ArrayList<Groups>) JsonToPubhlishData.getGroup2(s);
        sort_persons = (ArrayList<Contacts>) JsonToPubhlishData.MygetContacts(s, group_list);
        return sort_persons;
    }
}
