package com.jlstudio.group.activity;

import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.jlstudio.R;
import com.jlstudio.group.dialog.SelectGroup;
import com.jlstudio.main.activity.BaseActivity;
import com.jlstudio.main.application.Config;
import com.jlstudio.main.net.GetDataNet;
import com.jlstudio.publish.util.ShowToast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class NewFriendData extends BaseActivity implements View.OnClickListener {
    private TextView contacts_back;//返回
    private ImageView iface;//头像
    private TextView contacts_signature;//签名
    private TextView contacts_name;
    private TextView contacts_nickname;//昵称
    private TextView contacts_sex;//姓别
    private TextView contacts_phone;//手机号
    private TextView contacts_grade_name;//班级
    private TextView contacts_dept_name;//系别
    private TextView contacts_qq;//qq
    private TextView contacts_wechat;//微信
    private TextView title_name;
    private Button addfriend;
    private String searchdata;//搜索获取的结果
    private JSONObject json;//选中的人的信息
    private GetDataNet gn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_friend_data);
        gn = GetDataNet.getInstence(this);
        initData();
        initView();
    }

    private void initData() {
        String string = getIntent().getAction();
        int index = getIntent().getIntExtra("index", 0);

        try {
            if(index == -1){
                json = new JSONObject(string);
            }else{
                JSONObject o = new JSONObject(string);
                JSONArray array = o.getJSONArray("datas");
                json = array.getJSONObject(index);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void initView() {
        title_name = (TextView) findViewById(R.id.label_friend_name);
        iface = (ImageView) findViewById(R.id.iface);
        contacts_signature = (TextView) findViewById(R.id.contacts_signature);
        contacts_name = (TextView) findViewById(R.id.contacts_name);
        contacts_nickname = (TextView) findViewById(R.id.contacts_nickname);
        contacts_sex = (TextView) findViewById(R.id.contacts_sex);
        contacts_phone = (TextView) findViewById(R.id.contacts_phone);
        contacts_grade_name = (TextView) findViewById(R.id.contacts_grade_name);
        contacts_dept_name = (TextView) findViewById(R.id.contacts_dept_name);
        contacts_qq = (TextView) findViewById(R.id.contacts_qq);
        contacts_wechat = (TextView) findViewById(R.id.contacts_wechat);
        contacts_back = (TextView) findViewById(R.id.contacts_back);
        addfriend = (Button) findViewById(R.id.addfriend);
        Typeface iconfont = Typeface.createFromAsset(getAssets(), "fonts/iconfont.ttf");
        contacts_back.setTypeface(iconfont);
        contacts_back.setOnClickListener(this);
        addfriend.setOnClickListener(this);

        try {
            title_name.setText(json.getString("userrealname"));
            contacts_signature.setText(json.has("usersignature") ? json.getString("usersignature") : "");
            contacts_name.setText(json.has("userrealname") ? json.getString("userrealname") : "");
            contacts_nickname.setText(json.has("usernickname") ? json.getString("usernickname") : "");
            contacts_sex.setText(json.has("usergender") ? json.getString("usergender") : "");
            contacts_phone.setText(json.has("userphone") ? json.getString("userphone") : "");
            contacts_grade_name.setText(json.has("classname") ? json.getString("classname") : "");
            contacts_qq.setText(json.has("userqq") ? json.getString("userqq") : "");
            contacts_wechat.setText(json.has("userweixin") ? json.getString("userweixin") : "");
            contacts_dept_name.setText(json.has("departmentname") ? json.getString("departmentname") : "");
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.contacts_back:
                finish();
                break;
            case R.id.addfriend:
                new SelectGroup(this, new SelectGroup.CallBack() {
                    @Override
                    public void callback(String groupname) {
                        sendFriend(groupname);
                    }
                }).show();
        }
    }

    private void sendFriend(String group) {
        JSONObject json1 = new JSONObject();
        String username = Config.loadUser(this).getUsername();
        try {
            String receiver = json.getString("userid");
            json1.put("petitor", username);
            json1.put("receiver", receiver);
            json1.put("groupname", group);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        gn.getData(Config.URL, "addFriend", new Response.Listener<String>() {

            @Override
            public void onResponse(String s) {
                if(s.equals("0")){
                    ShowToast.show(NewFriendData.this, "好友请求发送成功");
                    finish();
                }else if(s.endsWith("4")){
                    ShowToast.show(NewFriendData.this, "已经发过好友请求了");
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError volleyError) {
                ShowToast.show(NewFriendData.this, "好友请求发送失败");
            }
        }, json1.toString());
    }
}
