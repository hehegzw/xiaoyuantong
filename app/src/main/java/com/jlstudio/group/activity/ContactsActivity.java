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

    private HashMap<String, Integer> selector;// 存放含有索引字母的位置
    private LinearLayout layoutIndex;
    private LinearLayout layout_show_no_group;
    private ListView listView;
    private TextView tv_show;
    private TextView grd_contacts_title;
    private TextView grd_contacts_back;
    private DBOption db;
    private CatchData catchData;

    private ContactsActivityAdapter adapter;
    private String[] indexStr = {"#", "A", "B", "C", "D", "E", "F", "G", "H",
            "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U",
            "V", "W", "X", "Y", "Z"};
    private List<Contacts> persons = null;
    private List<Contacts> newPersons = new ArrayList<>();
    private int height;// 字体高度
    private boolean flag = false;
    private String titlename;
    private String choose;
    private List<Contacts> sort_persons;
    private GetDataNet gn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grade_contacts);
        ProgressUtil.showProgressDialog(ContactsActivity.this, "正在加载...");
        db = new DBOption(this);
        gn = GetDataNet.getInstence(this);
        getData();
        initView();
    }

    @Override
    protected void onResume() {
        super.onResume();
        //getData();
    }
    /**
     * 从上一个Fragment中获取班级名称
     */
    public void getData() {
        Intent intent = getIntent();
        titlename =intent.getAction();
        catchData = db.getCatch(Config.URL+"groupperson"+Config.loadUser(this).getUsername()+titlename);
        persons = new ArrayList<>();
        getPersonFromNet();
    }

    private void initEvent() {
        listView.setOnItemClickListener(this);
        grd_contacts_back.setOnClickListener(this);
    }
    private void initListView(){
        if (persons != null) {
            String[] allNames = SortListUtil.sortIndex(persons);
            newPersons = SortListUtil.sortList(allNames, persons, newPersons);
            selector = new HashMap<>();
            for (int j = 0; j < indexStr.length; j++) {// 循环字母表，找出newPersons中对应字母的位置
                for (int i = 0; i < newPersons.size(); i++) {
                    Contacts c = newPersons.get(i);
                    String name = c.getRealname();
                    if (newPersons.get(i).getRealname().equals(indexStr[j])) {
                        selector.put(indexStr[j], i);
                    }
                }
            }
            //监听焦点变化
            ViewTreeObserver observer = layoutIndex.getViewTreeObserver();
            observer.addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {

                public boolean onPreDraw() {
                    if (!flag) {
                        height = layoutIndex.getMeasuredHeight() / indexStr.length;
                        getIndexView();
                        flag = true;
                    }
                    return true;
                }
            });
            layout_show_no_group.setVisibility(View.GONE);
            adapter = new ContactsActivityAdapter(ContactsActivity.this, newPersons);
            listView.setAdapter(adapter);
        }
        ProgressUtil.closeProgressDialog();
    }


    private void initView() {
        grd_contacts_title = (TextView) findViewById(R.id.grd_contacts_title);
        grd_contacts_title.setText(titlename);
        layoutIndex = (LinearLayout) findViewById(R.id.grd_contacts_layout);
        layoutIndex.setBackgroundColor(Color.parseColor("#01000000"));
        layout_show_no_group = (LinearLayout) findViewById(R.id.layout_show_no_group);
        listView = (ListView) findViewById(R.id.grd_contacts_listView);
        tv_show = (TextView) findViewById(R.id.grd_contacts_tv);
        grd_contacts_back = (TextView) findViewById(R.id.grd_contacts_back);
        Typeface iconfont = Typeface.createFromAsset(getAssets(), "fonts/iconfont.ttf");
        grd_contacts_back.setTypeface(iconfont);
        tv_show.setVisibility(View.GONE);
        initEvent();
    }



    /**
     * 绘制索引列表
     */
    public void getIndexView() {
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT, height);
        for (int i = 0; i < indexStr.length; i++) {
            final TextView tv = new TextView(ContactsActivity.this);
            tv.setLayoutParams(params);
            tv.setText(indexStr[i]);
            tv.setPadding(10, 0, 10, 0);
            layoutIndex.addView(tv);
            layoutIndex.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    float y = event.getY();
                    int index = (int) (y / height);
                    if (index > -1 && index < indexStr.length) {// 防止越界
                        String key = indexStr[index];
                        if (selector.containsKey(key)) {
                            int pos = selector.get(key);
                            if (listView.getHeaderViewsCount() > 0) {// 防止ListView有标题栏，本例中没有。
                                listView.setSelectionFromTop(
                                        pos + listView.getHeaderViewsCount(), 0);
                            } else {
                                listView.setSelectionFromTop(pos, 0);// 滑动到第一项
                            }
                            tv_show.setVisibility(View.VISIBLE);
                            tv_show.setText(indexStr[index]);
                        }
                    }
                    switch (event.getAction()) {
                        case MotionEvent.ACTION_DOWN:
                            layoutIndex.setBackgroundColor(Color
                                    .parseColor("#bebaba"));
                            break;

                        case MotionEvent.ACTION_MOVE:

                            break;
                        case MotionEvent.ACTION_UP:
                            layoutIndex.setBackgroundColor(Color
                                    .parseColor("#01000000"));
                            tv_show.setVisibility(View.GONE);
                            break;
                    }
                    return true;
                }
            });
        }
    }


    @Override
    public void onClick(View v) {
        finish();
    }


    private List<Contacts> sortPersons(List<Contacts> sort_persons) {
        Contacts contacts;
        for (int i = 0; i < sort_persons.size(); i++) {
            contacts = new Contacts();
            contacts.setRealname(sort_persons.get(i).getRealname() + "," + sort_persons.get(i).getUsername());
            contacts.setSign(sort_persons.get(i).getSign());
            persons.add(contacts);
        }
        return persons;
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
                db.updateCatch(Config.URL+"groupperson"+Config.loadUser(ContactsActivity.this).getUsername()+titlename, s, "00");
                sort_persons = JsonToPubhlishData.getSingleContacts(s);
                persons = sortPersons(sort_persons);
                initListView();
                ProgressUtil.closeProgressDialog();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Log.i("Test", "网络错误!");
                Toast.makeText(ContactsActivity.this, "删除失败", Toast.LENGTH_SHORT).show();
                ProgressUtil.closeProgressDialog();
            }
        }, json.toString());
    }
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent = new Intent(ContactsActivity.this, ContactsDataActivity.class);
        String[] userid = newPersons.get(position).getRealname().split(",");
        intent.setAction(userid[1]);
        startActivity(intent);
    }
}
