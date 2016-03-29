package com.jlstudio.group.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.jlstudio.R;
import com.jlstudio.main.application.ActivityContror;
import com.jlstudio.main.application.Config;
import com.jlstudio.main.net.GetDataNet;
import com.jlstudio.main.util.ProgressUtil;

import org.json.JSONException;
import org.json.JSONObject;

public class LeaveMsgActivity extends Activity implements View.OnClickListener {

    private TextView contacts_back;
    private TextView tv_msg_publish;
    private EditText et_leave_msg;
    private String leave_msg;
    private String contacts_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leave_msg);
        getData();
        initView();
        initEvent();
    }

    private void initEvent() {
        tv_msg_publish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                leave_msg = et_leave_msg.getText().toString();
                if (leave_msg.isEmpty()) {
                    Toast.makeText(LeaveMsgActivity.this, "请输入留言内容", Toast.LENGTH_SHORT).show();
                } else {
                    setSendJson(leave_msg);
                }
            }
        });
    }

    private void setSendJson(String leave_msg) {
        ProgressUtil.showProgressDialog(this, "请稍后...");
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("from", Config.loadUser(LeaveMsgActivity.this).getUsername());
            jsonObject.put("to", contacts_id);
            jsonObject.put("text", leave_msg);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        sendMsgToService(jsonObject.toString());
    }

    private void sendMsgToService(String sendJson) {
        GetDataNet getDateNet = GetDataNet.getInstence(LeaveMsgActivity.this);
        getDateNet.getData(Config.URL, "leavemessage", new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                if (s.equals("0")) {
                    Toast.makeText(LeaveMsgActivity.this, "留言成功", Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    Toast.makeText(LeaveMsgActivity.this, "留言失败", Toast.LENGTH_SHORT).show();
                }
                ProgressUtil.closeProgressDialog();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Log.i("Test", "网络错误!");
                ProgressUtil.closeProgressDialog();
            }
        }, sendJson);
    }

    private void initView() {
        contacts_back = (TextView) findViewById(R.id.contacts_back);
        tv_msg_publish = (TextView) findViewById(R.id.tv_msg_publish);
        et_leave_msg = (EditText) findViewById(R.id.et_leave_msg);
        Typeface iconfont = Typeface.createFromAsset(getAssets(), "fonts/iconfont.ttf");
        contacts_back.setTypeface(iconfont);
        contacts_back.setOnClickListener(this);
    }
    public void getData() {
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        contacts_id = bundle.getString("contacts_id");
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.contacts_back:
                finish();
                break;
        }
    }
}
