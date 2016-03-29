package com.jlstudio.publish.activity;


import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.jlstudio.R;
import com.jlstudio.group.bean.Contacts;
import com.jlstudio.main.application.ActivityContror;
import com.jlstudio.main.application.Config;
import com.jlstudio.main.bean.CatchData;
import com.jlstudio.main.db.DBOption;
import com.jlstudio.main.net.GetDataNet;
import com.jlstudio.main.util.ProgressUtil;
import com.jlstudio.publish.adapter.ShowPersonAdapter;
import com.jlstudio.publish.bean.MyContact;
import com.jlstudio.publish.dialog.UNRegisterQueryDialog;
import com.jlstudio.publish.util.JsonToPubhlishData;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ShowPersonDetailAty extends Activity implements View.OnClickListener, AdapterView.OnItemClickListener {
    private Button sure;
    private ListView showperson;
    private List<Contacts> persons;
    private ShowPersonAdapter adapter;
    private TextView back, title_name;
    private boolean isSelect;//是否选择了整个班级
    private String titlename;//传进来的班级名称
    private GetDataNet gn;//网络连接
    private DBOption db;//数据库连接
    private CatchData data;//获取数据库缓存数据
    private int index;//当前点击的人的位置
    private boolean isSaveMyMean;//是否记住询问用户对于没有注册的处理的对话框
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_persondetail);
        gn = GetDataNet.getInstence(this);
        db = new DBOption(this);
        isSelect = getIntent().getBooleanExtra("isSelect",false);
        initDatas();
        inintView();

    }


    private void inintView() {
        titlename = getIntent().getAction();
        back = (TextView) findViewById(R.id.back);
        title_name = (TextView) findViewById(R.id.title_name);
        sure = (Button) findViewById(R.id.publish);
        Typeface iconfont = Typeface.createFromAsset(getAssets(), "fonts/iconfont.ttf");
        back.setTypeface(iconfont);
        back.setOnClickListener(this);
        sure.setOnClickListener(this);
        title_name.setText(titlename);

    }

    private void initDatas() {
        ProgressUtil.showProgressDialog(this,"数据加载中...");
        titlename = getIntent().getAction();
        getDataFromNet();
//        data = db.getCatch(Config.URL + Config.GETCLASSCONTACT+titlename + Config.loadUser(this).getUsername());
//        if(data == null){
//            getDataFromNet();
//        }else{
//            persons = JsonToPubhlishData.getPersonFromJson(data.getContent());
//            initListView();
//            ProgressUtil.closeProgressDialog();
//        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back:
                ActivityContror.removeActivity(this);
                break;
            case R.id.publish:
                analysis();
                break;
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        //对于没有注册的，询问用户是否以短信形式发送
//        if(!persons.get(position).isRegister()&&!persons.get(position).isSelected()){
//            index = position;
//            startActivityForResult(new Intent(this, UNRegisterQueryDialog.class), 1);
//            return;
//        }
        if(persons.get(position).isSelected()){
            persons.get(position).setIsSelected(false);
        }else if(!persons.get(position).isSelected()){
            persons.get(position).setIsSelected(true);
        }
        adapter.setList(persons);
    }

    /**
     * 把已选中的信息放入全局变量
     */
    private void analysis(){
        for(int i=0;i<persons.size();i++){
            if(persons.get(i).isSelected()){
                //是否已经被加入了
                if(!Config.persons.contains(persons.get(i))){
                    Config.persons.add(persons.get(i));
                }
            }
        }
        for (int i=0;i<Config.groups.size();i++){
            if(Config.groups.get(i).getGroup_name().equals(titlename)){
                Config.groups.remove(i);
            }
        }
        finish();
    }
    private void getDataFromNet(){
        JSONObject json = new JSONObject();
        try {
            json.put("classname",titlename);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        gn.getData(Config.URL, "persons", new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                persons = JsonToPubhlishData.getPersonFromJson(s);
                initListView();
//                if(data == null){
//                    db.insertCatch(Config.URL + Config.GETCLASSCONTACT+titlename,s,System.currentTimeMillis()+"");
//                }else{
//                    db.updateCatch(Config.URL + Config.GETCLASSCONTACT+titlename,s,System.currentTimeMillis()+"");
//                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {

            }
        }, json.toString());
        ProgressUtil.closeProgressDialog();
    }
    private void initListView() {
        //是否选中了整个班级
        if(isSelect) {
            for (int i = 0; i < persons.size(); i++) {
                    persons.get(i).setIsSelected(true);
            }
        }
        showperson = (ListView) findViewById(R.id.showperson);
        adapter = new ShowPersonAdapter(this,persons);
        showperson.setAdapter(adapter);
        showperson.setOnItemClickListener(this);
    }
    //resultCode 1为确定 0为取消
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == 1){
            persons.get(index).setIsSelected(true);
            adapter.notifyDataSetChanged();
        }
    }
}

