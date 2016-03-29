package com.jlstudio.group.activity;

import android.app.Activity;
import android.os.Bundle;
import android.os.Debug;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.jlstudio.R;
import com.jlstudio.group.adapter.LeaveMsgDetailAtyAdapter;
import com.jlstudio.group.bean.LeaveMsg;
import com.jlstudio.main.application.Config;
import com.jlstudio.main.net.GetDataNet;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

public class LeaveMsgDetailAty extends Activity {
    private TextView fromusername,text,datetiem;
    private ListView list;
    private List<LeaveMsg> contents;
    private LeaveMsgDetailAtyAdapter adapter;
    private String msgId;
    private GetDataNet gn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leave_msg_detail_aty);
        initView();
        initData();
        gn = GetDataNet.getInstence(this);
    }



    private void initData() {
        msgId = getIntent().getAction();
        JSONObject o = new JSONObject();
        try {
            o.put("msgId",msgId);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        gn.getData(Config.URL, "getdata", new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {

            }
        },o.toString());
    }

    private void initView() {
        fromusername = (TextView) findViewById(R.id.fromusername);
        text = (TextView) findViewById(R.id.msg_content);
        datetiem = (TextView) findViewById(R.id.msg_time);
        list = (ListView) findViewById(R.id.list);
    }
    private void jsonToEntry(String s){

    }

}
