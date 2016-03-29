package com.jlstudio.publish.activity;

import android.content.Intent;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.jlstudio.R;
import com.jlstudio.main.activity.BaseActivity;
import com.jlstudio.main.application.ActivityContror;
import com.jlstudio.main.application.Config;
import com.jlstudio.main.bean.CatchData;
import com.jlstudio.main.db.DBOption;
import com.jlstudio.main.net.DownLoadFile;
import com.jlstudio.main.net.GetDataNet;
import com.jlstudio.publish.bean.PublishData;
import com.jlstudio.publish.bean.PublishListData;
import com.jlstudio.publish.dialog.QueryDownLoadDialog;
import com.jlstudio.publish.net.GetNotificationNet;
import com.jlstudio.publish.util.JsonToPubhlishData;
import com.jlstudio.publish.util.OpenFileUtil;
import com.jlstudio.publish.util.ShowToast;
import com.jlstudio.publish.util.StringUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;


public class ShowReceivePublishAty extends BaseActivity implements View.OnClickListener {
    private TextView title, time, content, back, title_name, content_name, publish_user;
    private TextView filename;
    private Button delete;
    private String flag;//0表示本条通知待回复，1表示本条消息可删除
    private DBOption db;
    private GetDataNet gn;
    private String noticeid;//本条消息id
    private String filePath;//附件地址
    private String fileName;//附件名称

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_msg);
        gn = GetDataNet.getInstence(this);
        db = new DBOption(this);
        noticeid = getIntent().getAction();
        initView();
        initData();
    }

    private void initData() {
        getDatasFromNet();
    }
    private void fillView(String string){
        PublishData datas = JsonToPubhlishData.getPublishDataItem(string, "receive");
        title.setText(datas.getTitle());
        time.setText(datas.getTime());
        publish_user.setText(datas.getNoticefromname());
        filename.setText(datas.getFilename());
        content_name.setText(datas.getContent());
        filePath = Config.URL+"download?data="+datas.getFilePath();
        Log.d("filepath",filePath);
        fileName = datas.getFilename();
        if(!StringUtil.isEmpty(fileName))
            filename.setOnClickListener(this);
        if (datas.getFlag().equals("0")) {
                delete.setText("收到");
                flag = datas.getFlag();
        } else {
            flag = datas.getFlag();
            delete.setText("删除");
        }
    }

    private void initView() {
        back = (TextView) findViewById(R.id.back);
        delete = (Button) findViewById(R.id.delete);
        publish_user = (TextView) findViewById(R.id.publish_user);
        title_name = (TextView) findViewById(R.id.title_name);
        title = (TextView) findViewById(R.id.publish_title);
        time = (TextView) findViewById(R.id.publish_time);
        content_name = (TextView) findViewById(R.id.publish_content);
        content = (TextView) findViewById(R.id.content);
        filename = (TextView) findViewById(R.id.filename);
        filename.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);
        title_name.setText("显示通知");
        Typeface iconfont = Typeface.createFromAsset(getAssets(), "fonts/iconfont.ttf");
        back.setTypeface(iconfont);
        back.setOnClickListener(this);
        delete.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.back) {
            ActivityContror.removeActivity(this);
        } else if(v.getId() == R.id.delete) {
            if (flag.equals("0")) {
                JSONObject json = new JSONObject();
                try {
                    json.put("noticeid", noticeid);
                    json.put("username", Config.loadUser(this).getUsername());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                //回复
                gn.getData(Config.URL, Config.NOTICECONFIRM, new Response.Listener<String>() {

                    @Override
                    public void onResponse(String s) {
                        if(s.equals("0")){
                            ShowToast.show(ShowReceivePublishAty.this, "回执成功");
                            refreshListView("refresh");
                        }else{
                            ShowToast.show(ShowReceivePublishAty.this, "回执失败");
                        }

                        finish();
                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        ShowToast.show(ShowReceivePublishAty.this, "回执失败");
                        finish();
                    }
                }, json.toString());
            } else {
                deletepublish();
            }
            //下载附件
        }else{
            String fileName = filename.getText().toString();
            String paths = Environment.getExternalStorageDirectory()+"/xiaoyuantongdownload/"+fileName;
            File file = new File(paths);
            if(!file.exists()){
                new QueryDownLoadDialog(this,fileName,filePath).show();
            }else{
                OpenFileUtil.openFile(this,file);
            }

        }
    }

    /**
     * 删除消息
     */
    private void deletepublish() {
        JSONObject json = new JSONObject();
        try {
            json.put("noticeid", noticeid);
            json.put("fromorto", "2");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        gn.getData(Config.URL, Config.DELETENOTICE, new Response.Listener<String>() {

            @Override
            public void onResponse(String s) {
                ShowToast.show(ShowReceivePublishAty.this, "删除成功");
                refreshListView("delete");
                finish();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                ShowToast.show(ShowReceivePublishAty.this, "删除失败");
                finish();
            }
        }, json.toString());
    }

    /**
     * type区分是回执更新 还是删除更新
     */
    private void refreshListView(String type) {
        String url = null;
        url = Config.URL + Config.RECMSG + Config.loadUser(this).getUsername();
        CatchData catchData = db.getCatch(url);
        String string = catchData.getContent();
        List<PublishListData> list = JsonToPubhlishData.getePublishListData(string, "receive");
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).getNoticeid().equals(noticeid)) {
                if(type.equals("delete")){
                    list.remove(list.get(i));
                }else{
                    list.get(i).setFlag("1");
                }

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

    /**
     * 获取本条通知详细信息
     */
    private void getDatasFromNet() {
        JSONObject json = new JSONObject();
        try {
            json.put("noticeid",noticeid);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        gn.getData(Config.URL, Config.GETRECEIVEMESSAGEBYID, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                if (s != null) {
                    fillView(s);
                } else {
                    ShowToast.show(ShowReceivePublishAty.this, "没有通知");
                }
                Log.d("hehe", s);
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError volleyError) {
                ShowToast.show(ShowReceivePublishAty.this, "获取数据失败");
            }
        }, json.toString());
    }
}
