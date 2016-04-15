package com.jlstudio.group.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.jlstudio.R;
import com.jlstudio.group.adapter.ContactsActivityAdapter;
import com.jlstudio.group.bean.Contacts;
import com.jlstudio.group.net.GetDataAction;
import com.jlstudio.group.util.SortListUtil;
import com.jlstudio.main.application.ActivityContror;
import com.jlstudio.main.application.Config;
import com.jlstudio.main.bean.CatchData;
import com.jlstudio.main.bean.User;
import com.jlstudio.main.db.DBOption;
import com.jlstudio.main.net.GetDataNet;
import com.jlstudio.main.util.ProgressUtil;
import com.jlstudio.publish.util.JsonToPubhlishData;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * 显示组名下的所有联系人
 */
public class ContactsActivity extends Activity implements AdapterView.OnItemClickListener, View.OnClickListener {

    private ListView listView;
    private TextView grd_contacts_title;
    private TextView grd_contacts_back;
    private ContactsActivityAdapter adapter;
    private String titlename;
    private List<Contacts> sort_persons;
    private GetDataNet gn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grade_contacts);
        ProgressUtil.showProgressDialog(ContactsActivity.this, "正在加载...");
        gn = GetDataNet.getInstence(this);
        initView();
        getData();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }
    /**
     * 从上一个Fragment中获取班级名称
     */
    public void getData() {
        Intent intent = getIntent();
        titlename =intent.getAction();
        grd_contacts_title.setText(titlename);
        getPersonFromNet();
    }

    private void initEvent() {
        listView.setOnItemClickListener(this);
        grd_contacts_back.setOnClickListener(this);
    }
    private void initView() {
        grd_contacts_title = (TextView) findViewById(R.id.grd_contacts_title);
        grd_contacts_title.setText(titlename);
        listView = (ListView) findViewById(R.id.grd_contacts_listView);
        sort_persons = new ArrayList<>();
        adapter = new ContactsActivityAdapter(this,sort_persons);
        listView.setAdapter(adapter);
        grd_contacts_back = (TextView) findViewById(R.id.grd_contacts_back);
        Typeface iconfont = Typeface.createFromAsset(getAssets(), "fonts/iconfont.ttf");
        grd_contacts_back.setTypeface(iconfont);
        initEvent();
    }
    @Override
    public void onClick(View v) {
        finish();
    }

    private void getPersonFromNet(){
        JSONObject json = new JSONObject();
        try {
            json.put("username",Config.loadUser(this).getUsername());
            json.put("groupname",titlename);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        gn.getData(Config.URL,"groupperson",new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                sort_persons = JsonToPubhlishData.getSingleContacts(s);
                adapter.onDataChanged(sort_persons);
                ProgressUtil.closeProgressDialog();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Log.i("Test", "网络错误!");
                //Toast.makeText(ContactsActivity.this, "删除失败", Toast.LENGTH_SHORT).show();
                ProgressUtil.closeProgressDialog();
            }
        }, json.toString());
    }
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent = new Intent(ContactsActivity.this, ContactsDataActivity.class);
        intent.setAction(sort_persons.get(position).getUsername());
        startActivity(intent);
    }
}
