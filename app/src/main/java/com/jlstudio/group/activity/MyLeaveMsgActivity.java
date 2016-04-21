package com.jlstudio.group.activity;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Debug;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.style.ImageSpan;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.jlstudio.R;
import com.jlstudio.group.adapter.ImageHeadAdapter;
import com.jlstudio.group.adapter.MyLeaveMsgAdapter;
import com.jlstudio.group.bean.LeaveMsg;
import com.jlstudio.group.util.SoftKeyBoardListener;
import com.jlstudio.main.activity.MainActivity;
import com.jlstudio.main.application.Config;
import com.jlstudio.main.net.GetDataNet;
import com.jlstudio.main.util.ProgressUtil;
import com.jlstudio.publish.bean.Type;
import com.jlstudio.publish.util.ShowToast;

import android.support.v4.widget.SwipeRefreshLayout;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MyLeaveMsgActivity extends Activity implements View.OnClickListener {

    private TextView contacts_back;
    private ListView my_leave_msg_listview;
    private List<List<LeaveMsg>> list;
    private List<LeaveMsg> tempList;
    private MyLeaveMsgAdapter adapter;
    private SwipeRefreshLayout layout_swipe;
    private LinearLayout editare;
    private EditText replytext;
    private Button submit;
    GetDataNet gn;
    private String fromusername;//要回复的人
    private String textid;//要回复的评论id
    private LeaveMsg msg;//临时存储要回复的消息
    //******************************************
    private RecyclerView recyclerView;
    private ImageHeadAdapter imageHeadAdapter;
    private int[] imageArrays = new int[107];
    private TextView headface, keybord;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_leave_msg);
        initView();
        initRecycleView();
        gn = GetDataNet.getInstence(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        getServiceData();
    }
    private void initEvent(){
        replytext.setClickable(false);
        replytext.setOnClickListener(this);
        contacts_back.setOnClickListener(this);
        headface.setOnClickListener(this);
        keybord.setOnClickListener(this);
        layout_swipe.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getServiceData();
            }
        });
        SoftKeyBoardListener.setListener(MyLeaveMsgActivity.this, new SoftKeyBoardListener.OnSoftKeyBoardChangeListener() {
            @Override
            public void keyBoardShow(int height) {
                replytext.requestFocus();
                replytext.setHint("回复:" + fromusername);
                editare.setVisibility(View.VISIBLE);
            }

            @Override
            public void keyBoardHide(int height) {
                if (recyclerView.getVisibility() == View.GONE)
                    editare.setVisibility(View.GONE);
            }
        });
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String string = replytext.getText().toString();
                if (TextUtils.isEmpty(string)) {
                    ShowToast.show(MyLeaveMsgActivity.this, "回复不能为空");
                    return;
                }
                JSONObject json = new JSONObject();
                try {
                    json.put("messageid", textid);
                    json.put("messagefrom", Config.loadUser(getApplicationContext()).getUsername());
                    json.put("messageto", msg.getTo());
                    json.put("text", string);
                    msg.setContent(string);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                gn.getData(Config.URL, "replymessage", new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        ShowToast.show(getApplicationContext(), "回复成功");
                        replytext.setText("");
                        editare.setVisibility(View.GONE);
                        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(replytext.getWindowToken(), 0);//强制关闭软键盘
                        if (recyclerView.getVisibility() == View.VISIBLE) {
                            recyclerView.setVisibility(View.GONE);
                        }
                        list.get(msg.getSquence()).add(msg);
                        if(list.get(msg.getSquence()).size()>6){
                            tempList = getTempListOpen(msg.getSquence());
                        }else{
                            tempList = getTempLIst();
                        }
                        adapter.setTempList(tempList);
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        ShowToast.show(getApplicationContext(), "回复失败");
                    }
                }, json.toString());
            }
        });
        my_leave_msg_listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (tempList.get(position).getType() == 4) {//加载评论
                    if (tempList.get(position).getContent().equals("点击加载全部评论...")) {
                        tempList = getTempListOpen(tempList.get(position).getSquence());
                        adapter.setTempList(tempList);
                    } else {
                        tempList = getTempLIst();
                        adapter.setTempList(tempList);
                    }
                } else if (tempList.get(position).getType() != 1) {
                    String fromuserid = null;
                    if (tempList.get(position).getType() == 3) {//我来说一句...
                        fromuserid = tempList.get(position).getOriFrom();
                        if (fromuserid.endsWith(Config.loadUser(MyLeaveMsgActivity.this).getUsername())) {
                        }
                        fromusername = tempList.get(0).getOriFromname();
                    } else {
                        if (recyclerView.getVisibility() == View.VISIBLE) {
                            recyclerView.setVisibility(View.GONE);
                            editare.setVisibility(View.GONE);
                            return;
                        }
                        fromuserid = tempList.get(position).getFrom();
                        fromusername = tempList.get(position).getFromname();
                    }
                    textid = tempList.get(position).getOriId();
                    msg = new LeaveMsg();
                    msg.setTo(fromuserid);
                    msg.setToname(fromusername);
                    msg.setFromname(tempList.get(0).getOriFromname());
                    msg.setFrom(Config.loadUser(getApplicationContext()).getUsername());
                    msg.setSquence(tempList.get(position).getSquence());
                    msg.setOriId(textid);
                    msg.setType(2);
                    Date date = new Date(System.currentTimeMillis());
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy年mm月dd日 hh:mm:ss");
                    String time = sdf.format(date);
                    msg.setTime(time);
                    editare.setVisibility(View.VISIBLE);
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
                }
            }
        });
    }
    private void initRecycleView() {
        recyclerView = (RecyclerView) findViewById(R.id.imagehead);
        initRecycleData();
        imageHeadAdapter = new ImageHeadAdapter(this, imageArrays);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new StaggeredGridLayoutManager(4, StaggeredGridLayoutManager.HORIZONTAL));
        recyclerView.setAdapter(imageHeadAdapter);
        imageHeadAdapter.setListenre(new ImageHeadAdapter.ImageHeadListener() {
            @Override
            public void listener(int position) {
                Bitmap bitmap = null;
                bitmap = BitmapFactory.decodeResource(getResources(), imageArrays[position]);
                ImageSpan imageSpan = new ImageSpan(MyLeaveMsgActivity.this, bitmap);
                String str = null;
                if (position < 10) {
                    str = "f00" + position;
                } else if (position < 100) {
                    str = "f0" + position;
                } else {
                    str = "f" + position;
                }
                SpannableString spannableString = new SpannableString(str);
                spannableString.setSpan(imageSpan, 0, 4, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                replytext.append(spannableString);
            }
        });
    }

    private void initRecycleData() {
        //生成107个表情的id，封装
        for (int i = 0; i < 107; i++) {
            try {
                if (i < 10) {
                    Field field = R.drawable.class.getDeclaredField("f00" + i);
                    int resourceId = Integer.parseInt(field.get(null).toString());
                    imageArrays[i] = resourceId;
                } else if (i < 100) {
                    Field field = R.drawable.class.getDeclaredField("f0" + i);
                    int resourceId = Integer.parseInt(field.get(null).toString());
                    imageArrays[i] = resourceId;
                } else {
                    Field field = R.drawable.class.getDeclaredField("f" + i);
                    int resourceId = Integer.parseInt(field.get(null).toString());
                    imageArrays[i] = resourceId;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void getServiceData() {
        list = new ArrayList<>();
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("userid", Config.loadUser(this).getUsername());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        ProgressUtil.showProgressDialog(this, "数据加载...");
        gn.getData(Config.URL, "getmessagestome", new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                Log.i("Test", "获取留言成功" + s);
                if (s.length() > 1) {
                    setData(s);
                    tempList = getTempLIst();
                    adapter.setTempList(tempList);
                }
                layout_swipe.setRefreshing(false);
                ProgressUtil.closeProgressDialog();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Log.i("Test", "获取留言失败");
                layout_swipe.setRefreshing(false);
                ProgressUtil.closeProgressDialog();
            }
        }, jsonObject.toString());
    }

    private void initView() {
        contacts_back = (TextView) findViewById(R.id.contacts_back);
        my_leave_msg_listview = (ListView) findViewById(R.id.my_leave_msg_listview);
        tempList = new ArrayList<>();
        adapter = new MyLeaveMsgAdapter(this, tempList, Config.loadUser(this).getUsername());
        my_leave_msg_listview.setAdapter(adapter);
        layout_swipe = (SwipeRefreshLayout) findViewById(R.id.layout_swipe);
        editare = (LinearLayout) findViewById(R.id.editare);
        replytext = (EditText) findViewById(R.id.replyedit);
        submit = (Button) findViewById(R.id.send);
        headface = (TextView) findViewById(R.id.headface);
        keybord = (TextView) findViewById(R.id.keybord);
        layout_swipe.setColorSchemeColors(R.color.fresh_one, R.color.fresh_two, R.color.fresh_three, R.color.fresh_four);
        Typeface iconfont = Typeface.createFromAsset(getAssets(), "fonts/iconfont.ttf");
        contacts_back.setTypeface(iconfont);
        iconfont = Typeface.createFromAsset(getAssets(), "fonts/leavemsg.ttf");
        headface.setTypeface(iconfont);
        keybord.setTypeface(iconfont);
        initEvent();

    }

    @Override
    public void onClick(View v) {
        InputMethodManager imm;
        switch (v.getId()) {
            case R.id.contacts_back:
                finish();
                overridePendingTransition(R.anim.activity_left_in, R.anim.activity_right_out);
                break;
            case R.id.headface:
                imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(replytext.getWindowToken(), 0);//强制关闭软键盘
                recyclerView.setVisibility(View.VISIBLE);
                editare.setVisibility(View.VISIBLE);
                break;
            case R.id.keybord:
                if (recyclerView.getVisibility() == View.GONE) return;
                recyclerView.setVisibility(View.GONE);
                imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
                break;
            case R.id.replyedit:
                recyclerView.setVisibility(View.GONE);
                break;
        }
    }

    private List<LeaveMsg> getTempLIst() {
        List<LeaveMsg> lists = new ArrayList<>();
        for (int i = 0; i < list.size(); i++) {
            List<LeaveMsg> temp = list.get(i);
            if (temp.size() > 6) {
                for (int j = 0; j < 6; j++) {
                    lists.add(temp.get(j));
                }
                lists.add(new LeaveMsg(4, "点击加载全部评论...", temp.get(0).getSquence()));
            } else {
                for (int j = 0; j < temp.size(); j++) {
                    lists.add(temp.get(j));
                }
            }
            lists.add(new LeaveMsg(3, temp.get(0).getSquence(), temp.get(0).getId(), temp.get(0).getFrom()));
        }
        for (int i = 0; i < lists.size(); i++) {
            Log.d("TTAAGG", lists.get(i).toString() + "\n");
        }
        return lists;
    }

    private List<LeaveMsg> getTempListOpen(int position) {
        List<LeaveMsg> lists = new ArrayList<>();
        for (int i = 0; i < list.size(); i++) {
            List<LeaveMsg> temp = list.get(i);
            if (temp.size() > 6 && position != i) {
                for (int j = 0; j < 6; j++) {
                    lists.add(temp.get(j));
                }
                lists.add(new LeaveMsg(4, "点击加载全部评论...", temp.get(0).getSquence()));
            } else {
                for (int j = 0; j < temp.size(); j++) {
                    lists.add(temp.get(j));
                }
                if (position == i) {
                    lists.add(new LeaveMsg(4, "收起", temp.get(0).getSquence()));
                }
            }
            lists.add(new LeaveMsg(3, temp.get(0).getSquence(), temp.get(0).getId(), temp.get(0).getFrom()));
        }
        return lists;
    }

    private void setData(String s) {
        list = new ArrayList<>();
        try {
            JSONObject jsonObject = new JSONObject(s);
            JSONArray array = jsonObject.getJSONArray("datas");
            for (int i = 0; i <array.length(); i++) {
                List<LeaveMsg> lists = new ArrayList<>();
                String from = null;//本条评论人
                String fromname = null;//本条评论人姓名
                String id = null;//本条评论id
                String oriToname = null;//本条评论id
                for (int j = 0; j < array.getJSONArray(i).length(); j++) {
                    JSONObject object = array.getJSONArray(i).getJSONObject(j);
                    LeaveMsg contacts = new LeaveMsg();
                    contacts.setId(object.getString("id"));
                    contacts.setTime(object.getLong("datetime") + "");
                    contacts.setContent(object.getString("text"));
                    contacts.setFrom(object.getString("messagefrom"));
                    contacts.setTo(object.getString("messageto"));
                    contacts.setToname(object.getString("toname"));
                    contacts.setFromname(object.getString("fromname"));
                    contacts.setSquence(i);
                    if (j == 0) {
                        from = object.getString("messagefrom");
                        contacts.setOriFrom(from);
                        fromname = object.getString("fromname");
                        contacts.setOriFromname(fromname);
                        oriToname = object.getString("toname");
                        contacts.setOriToname(object.getString("toname"));
                        id = object.getString("id");
                        contacts.setType(1);
                    } else {
                        contacts.setOriFrom(from);
                        contacts.setOriId(id);
                        contacts.setOriFromname(fromname);
                        contacts.setOriToname(oriToname);
                        contacts.setType(2);
                    }
                    lists.add(contacts);
                }
                list.add(lists);
            }
            Log.d("listContent",list.toString()+"\n"+list.size());
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (recyclerView.getVisibility() == View.VISIBLE) {
            recyclerView.setVisibility(View.GONE);
            editare.setVisibility(View.GONE);
            return true;
        } else if (keyCode == KeyEvent.KEYCODE_BACK) {
            finish();
            overridePendingTransition(R.anim.activity_left_in, R.anim.activity_right_out);
        }
        return super.onKeyDown(keyCode, event);
    }
}
