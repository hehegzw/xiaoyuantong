package com.jlstudio.publish.activity;


import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Parcelable;
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

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class ShowPersonDetailAty extends Activity implements View.OnClickListener, AdapterView.OnItemClickListener {
    private Button sure;
    private ListView showperson;
    private List<MyContact> persons;
    private ShowPersonAdapter adapter;
    private TextView back, title_name;
    private boolean isSelect;//是否选择了整个班级
    private String titlename;//传进来的班级名称
    private GetDataNet gn;//网络连接
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_persondetail);
        gn = GetDataNet.getInstence(this);
        isSelect = getIntent().getBooleanExtra("isSelect",false);
        inintView();
        initDatas();


    }


    private void inintView() {
        titlename = getIntent().getAction();
        back = (TextView) findViewById(R.id.back);
        title_name = (TextView) findViewById(R.id.title_name);
        title_name.setText(titlename);
        sure = (Button) findViewById(R.id.publish);
        showperson = (ListView) findViewById(R.id.showperson);
        persons = new ArrayList<>();
        adapter = new ShowPersonAdapter(this,persons);
        showperson.setAdapter(adapter);
        Typeface iconfont = Typeface.createFromAsset(getAssets(), "fonts/iconfont.ttf");
        back.setTypeface(iconfont);
        back.setOnClickListener(this);
        sure.setOnClickListener(this);
        showperson.setOnItemClickListener(this);


    }

    private void initDatas() {
        persons = (List<MyContact>) getIntent().getSerializableExtra("persons");
        if(persons!=null){
            adapter.setList(persons);
        }else{
            ProgressUtil.showProgressDialog(this,"数据加载中...");
            getDataFromNet();
        }

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
        boolean isHasPersonAllSelect = false;
        for(int i=0;i<persons.size();i++){
            if(!persons.get(i).isSelected()){
                isHasPersonAllSelect = false;
                break;
            }else{
                isHasPersonAllSelect = true;
            }
        }
        Intent intent =getIntent();
        intent.putExtra("persons", (Serializable) persons);
        intent.putExtra("isGroupSelect",isHasPersonAllSelect);
        setResult(1,intent);
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
                refreshListView();

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {

            }
        }, json.toString());
        ProgressUtil.closeProgressDialog();
    }
    private void refreshListView() {
        //是否选中了整个班级
        if(isSelect) {
            for (int i = 0; i < persons.size(); i++) {
                    persons.get(i).setIsSelected(true);
            }
        }
        adapter.setList(persons);
    }
    //resultCode 1为确定 0为取消
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == 1){
            adapter.notifyDataSetChanged();
        }
    }
}

