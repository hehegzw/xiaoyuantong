package com.jlstudio.publish.activity;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.jlstudio.R;
import com.jlstudio.main.activity.BaseActivity;
import com.jlstudio.main.application.ActivityContror;
import com.jlstudio.main.application.Config;
import com.jlstudio.main.db.DBOption;
import com.jlstudio.main.net.GetDataNet;
import com.jlstudio.publish.bean.PublishData;
import com.jlstudio.publish.bean.PublishListData;
import com.jlstudio.publish.dialog.QueryDownLoadDialog;
import com.jlstudio.publish.dialog.ShowUnreplyPersonDialog;
import com.jlstudio.publish.util.JsonToPubhlishData;
import com.jlstudio.publish.util.ShowToast;
import com.jlstudio.publish.util.StringUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

public class ShowSendPublishAty extends BaseActivity implements View.OnClickListener {
    private TextView title, time, content, back, title_name;
    private TextView replyCount, unreplyCount, text1, text2;
    private TextView filename;
    private LinearLayout unReply;
    private Button delete;
    private String datetime;
    private PublishData data;
    private DBOption db;
    private String noticeid;
    private String noticegroup;
    private GetDataNet gn;
    private String filePath;//附件地址
    private String fileName;//附件名称

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_publish_send);
        gn = GetDataNet.getInstence(this);
        noticeid = getIntent().getAction();
        noticegroup = getIntent().getStringExtra("noticegroup");
        initView();
        initData();
    }

    private void initData() {
        getDatasFromNetafter();
    }
    private void fillView(String string){
        data = JsonToPubhlishData.getPublishDataItem(string, "send");
        title.setText(data.getTitle());
        time.setText(data.getTime());
        content.setText(data.getContent());
        replyCount.setText(data.getReplyedCount());
        unreplyCount.setText(data.getUnReplyCount());
        filePath = data.getFilePath();
        fileName = data.getFilename();
        filename.setText(fileName);
        if(!StringUtil.isEmpty(fileName))
            filename.setOnClickListener(this);
    }

    private void initView() {
        back = (TextView) findViewById(R.id.back);
        delete = (Button) findViewById(R.id.delete);
        title_name = (TextView) findViewById(R.id.title_name);
        title = (TextView) findViewById(R.id.publish_title);
        time = (TextView) findViewById(R.id.publish_time);
        content = (TextView) findViewById(R.id.publish_content);
        unReply = (LinearLayout) findViewById(R.id.unReply);
        unreplyCount = (TextView) findViewById(R.id.unreplyCount);
        replyCount = (TextView) findViewById(R.id.replyedCount);
        content = (TextView) findViewById(R.id.publish_content);
        filename = (TextView) findViewById(R.id.filename);
        delete.setText("删除通知");
        text1 = (TextView) findViewById(R.id.text1);
        text2 = (TextView) findViewById(R.id.text2);
        title_name.setText("显示通知");
        Typeface iconfont = Typeface.createFromAsset(getAssets(), "fonts/iconfont.ttf");
        back.setTypeface(iconfont);
        back.setOnClickListener(this);
        delete.setOnClickListener(this);
        unReply.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back:
                startActivity(new Intent(this, PublishDatasAty.class));
                ActivityContror.removeActivity(this);
                break;
            case R.id.unReply:
                new ShowUnreplyPersonDialog(this, noticeid,noticegroup).show();
                break;
            case R.id.delete:
                deletepublish();
                break;
//            case R.id.filename:
//                new QueryDownLoadDialog(this,fileName,filePath).show();
//                break;
        }
    }

    private void deletepublish() {
        JSONObject json = new JSONObject();
        try {
            json.put("noticeid", noticeid);
            json.put("fromorto", "1");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        gn.getData(Config.URL, Config.DELETEMYSENDNOTICE, new Response.Listener<String>() {

            @Override
            public void onResponse(String s) {
                ShowToast.show(ShowSendPublishAty.this, "删除成功");
                refreshListView();
                Intent intent = new Intent(ShowSendPublishAty.this, PublishDatasAty.class);
                startActivity(intent);
                ActivityContror.removeActivity(ShowSendPublishAty.this);
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError volleyError) {
                ShowToast.show(ShowSendPublishAty.this, "删除失败");
            }
        }, json.toString());
    }

    private void refreshListView() {
        String url = null;
        url = Config.URL + Config.SENDMSG + Config.loadUser(this).getUsername();
        List<PublishListData> list = JsonToPubhlishData.getePublishListData(db.getCatch(url).getContent(),"send");
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).getNoticeid().equals(noticeid)) {
                list.remove(i);
                break;
            }
        }
        Gson array = new Gson();
        String sarray = array.toJson(list);
        JSONObject object = new org.json.JSONObject();
        try {
            JSONArray jsonArray = new JSONArray(sarray);
            object.put("datas", jsonArray);
            db.updateCatch(url, object.toString(), System.currentTimeMillis() + "");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void getDatasFromNetafter() {
        JSONObject json = new JSONObject();
        try {
            json.put("noticeid",noticeid);
            json.put("noticegroup", noticegroup);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        gn.getData(Config.URL, Config.GETSENDMESSAGEBYID, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                db = new DBOption(ShowSendPublishAty.this);
                if (s != null) {
                    fillView(s);
                } else {
                    ShowToast.show(ShowSendPublishAty.this, "没有通知");
                }
                Log.d("hehe", s);
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError volleyError) {
                ShowToast.show(ShowSendPublishAty.this, "获取数据失败");
                Log.d("hehe", "error");
            }
        }, json.toString());

    }
}
