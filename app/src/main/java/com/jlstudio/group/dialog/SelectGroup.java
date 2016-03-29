package com.jlstudio.group.dialog;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.jlstudio.R;
import com.jlstudio.main.application.Config;
import com.jlstudio.main.bean.CatchData;
import com.jlstudio.main.db.DBOption;
import com.jlstudio.main.net.GetDataNet;
import com.jlstudio.publish.activity.AddPublishAty;
import com.jlstudio.publish.activity.AddPublishBySMSAty;
import com.jlstudio.publish.adapter.SelectDepartmentadapter;
import com.jlstudio.publish.util.ShowToast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by gzw on 2015/9/26.
 */
public class SelectGroup extends Dialog implements View.OnClickListener, AdapterView.OnItemClickListener {
    private Context context;
    private List<String> groups;
    private SelectDepartmentadapter adapter;
    private ListView listView;
    private DBOption db;
    private GetDataNet gn;
    private CatchData data;//数据库缓存数据
    private String receiver;//好友id
    private CallBack callBack;


    public SelectGroup(Context context,CallBack callBack) {
        super(context);
        this.context = context;
        this.callBack = callBack;
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_select_person);
        gn = GetDataNet.getInstence(context);
        db = new DBOption(context);
        initView();
        getDatas();
        initListView();
    }

    private void getDatas(){
        groups = new ArrayList<>();
        adapter = new SelectDepartmentadapter(context,groups);
        data = db.getCatch(Config.URL + Config.GETCONTACTGROUP + Config.loadUser(context).getUsername());
        if (data == null){
            getDataFromNet();
        }else{
            groups = getGroups(data.getContent());
            adapter.setList(groups);
        }
    }

    private List<String> getGroups(String content) {
        List<String> list = new ArrayList<>();
        try {
            JSONObject json = new JSONObject(content);
            JSONArray array = json.getJSONArray("groupNameList");
            for(int i=0;i<array.length();i++){
                list.add(array.getJSONObject(i).getString("groupName"));
            }
            return list;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    private void initListView() {
        listView = (ListView) findViewById(R.id.listview);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(this);
    }


    private void initView() {
        Window dialogwindow = getWindow();
        TextView title = (TextView) findViewById(R.id.title);
        title.setText("选择分组");
        WindowManager.LayoutParams lp = dialogwindow.getAttributes();
        DisplayMetrics dm = context.getResources().getDisplayMetrics();
        lp.width = (int) (dm.widthPixels * 0.8);
        lp.height = (int) (dm.heightPixels * 0.6);
        dialogwindow.setAttributes(lp);
    }

    @Override
    public void onClick(View v) {
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
        if (callBack!=null){
            callBack.callback(groups.get(position));
        }
        dismiss();
    }

    /**
     * 从网络上获取数据
     */
    private void getDataFromNet() {
        JSONObject json = new JSONObject();
        try {
            json.put("username", Config.loadUser(context).getUsername());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        gn.getData(Config.URL, "groups", new Response.Listener<String>() {

            @Override
            public void onResponse(String s) {
                if (data == null) {
                    db.insertCatch(Config.URL + Config.GETCONTACTGROUP + Config.loadUser(context).getUsername(), s, System.currentTimeMillis() + "");
                } else {
                    db.updateCatch(Config.URL + Config.GETCONTACTGROUP + Config.loadUser(context).getUsername(), s, System.currentTimeMillis() + "");
                }
                groups = getGroups(s);
                adapter.setList(groups);
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Log.d("hehe", "获取数据失败");
            }
        }, json.toString());
    }
    public interface CallBack{
        void callback(String groupname);
    }
}
