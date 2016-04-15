package com.jlstudio.group.activity;

import android.content.Intent;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.jlstudio.R;
import com.jlstudio.group.adapter.GroupManagerAdapter;
import com.jlstudio.group.dialog.EditGroup;
import com.jlstudio.main.activity.BaseActivity;
import com.jlstudio.main.application.Config;
import com.jlstudio.main.bean.CatchData;
import com.jlstudio.main.db.DBOption;
import com.jlstudio.main.net.GetDataNet;
import com.jlstudio.publish.adapter.SelectDepartmentadapter;
import com.jlstudio.publish.server.MyService;
import com.jlstudio.publish.util.ShowToast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class GroupManager extends BaseActivity implements AdapterView.OnItemClickListener, View.OnClickListener {
    private TextView back,title_name,right_icon;
    private ListView grouplist;
    private LinearLayout addgroup;
    private GroupManagerAdapter adapter;
    private List<String> groups;
    private List<String> editgroups;
    private GetDataNet gn;//网络连接
    private DBOption db;//数据库连接
    private CatchData data;//数据库缓存数据
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_manager);
        gn = GetDataNet.getInstence(this);
        db = new DBOption(this);
        initData();
        initView();
    }

    private void initData() {
        groups = new ArrayList<>();
        editgroups = new ArrayList<>();
        adapter = new GroupManagerAdapter(this,groups);
        adapter.setCallBack(new EditGroup.CallBack() {
            @Override
            public void callback(int index, String name) {
                if(groups.get(index).endsWith("我的班级")||groups.get(index).endsWith("我的好友")){
                    ShowToast.show(GroupManager.this,"这个组还是别动的好");
                    return;
                }
                groups.remove(index);
                editgroups.add(name + ",delete$");
                adapter.notifyDataSetChanged();
            }
        });
        data = db.getCatch(Config.URL + Config.GETCONTACTGROUP + Config.loadUser(this).getUsername());
        if (data == null){
            getDataFromNet();
        }else{
            groups = getGroups(data.getContent());
            adapter.setP_list(groups);
        }
    }

    private void initView() {
        back = (TextView) findViewById(R.id.back);
        title_name = (TextView) findViewById(R.id.title_name);
        right_icon = (TextView) findViewById(R.id.right_icon);
        grouplist = (ListView) findViewById(R.id.grouplist);
        addgroup = (LinearLayout) findViewById(R.id.addgroup);
        Typeface icon = Typeface.createFromAsset(getAssets(),"fonts/iconfont.ttf");
        back.setTypeface(icon);
        right_icon.setText("完成");
        title_name.setText("分组管理");
        grouplist.setAdapter(adapter);
        grouplist.setOnItemClickListener(this);
        back.setOnClickListener(this);
        right_icon.setOnClickListener(this);
        addgroup.setOnClickListener(this);
    }
    /**
     * 从网络上获取数据
     */
    private void getDataFromNet() {
        JSONObject json = new JSONObject();
        try {
            json.put("username", Config.loadUser(this).getUsername());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        gn.getData(Config.URL, "groups", new Response.Listener<String>() {

            @Override
            public void onResponse(String s) {
                if (data == null) {
                    db.insertCatch(Config.URL + Config.GETCONTACTGROUP + Config.loadUser(GroupManager.this).getUsername(), s, System.currentTimeMillis() + "");
                } else {
                    db.updateCatch(Config.URL + Config.GETCONTACTGROUP + Config.loadUser(GroupManager.this).getUsername(), s, System.currentTimeMillis() + "");
                }
                groups = getGroups(s);
                adapter.setP_list(groups);
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Log.d("hehe", "获取数据失败");
            }
        }, json.toString());
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

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if(groups.get(position).endsWith("我的班级")||groups.get(position).endsWith("我的好友")){
            ShowToast.show(this,"这个组还是别动的好");
            return;
        }
        new EditGroup(this, position, groups,"edit" ,new EditGroup.CallBack() {
            @Override
            public void callback(int index, String name) {
                String string = groups.remove(index);
                editgroups.add(name+","+string);
                groups.add(index,name);
                adapter.notifyDataSetChanged();
            }
        }).show();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.back:
                finish();
                overridePendingTransition(R.anim.activity_left_in, R.anim.activity_right_out);
                break;
            case R.id.addgroup:
                new EditGroup(this, 0, groups, "add", new EditGroup.CallBack() {
                    @Override
                    public void callback(int index, String name) {
                        groups.add(name);
                        editgroups.add(name+",add$");
                    }
                }).show();
                break;
            case R.id.right_icon:
                complete();
                break;
        }
    }

    private void complete() {
        JSONObject json = new JSONObject();
        JSONArray array = new JSONArray();
        String username = Config.loadUser(this).getUsername();
        if(editgroups == null || editgroups.size() == 0){
            finish();
            return;
        }
        try {
            for (int i = 0; i < editgroups.size(); i++) {
                JSONObject o = new JSONObject();
                o.put("groupname", editgroups.get(i));
                array.put(o);
            }
            json.put("username",username);
            json.put("grouplist",array);
            Log.d("hehe",json.toString());
        }catch (Exception e){
            e.printStackTrace();
        }
        gn.getData(Config.URL, "groupopt", new Response.Listener<String>() {

            @Override
            public void onResponse(String s) {
                if(s.equals("0")){
                    ShowToast.show(GroupManager.this,"修改成功");
                    startService(new Intent(GroupManager.this, MyService.class));
                    finish();
                }else{
                    ShowToast.show(GroupManager.this,"修改失败");
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError volleyError) {
                ShowToast.show(GroupManager.this, "网络连接失败");
            }
        }, json.toString());
    }
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode == KeyEvent.KEYCODE_BACK){
            finish();
            overridePendingTransition(R.anim.activity_left_in, R.anim.activity_right_out);
        }
        return super.onKeyDown(keyCode, event);
    }
}
