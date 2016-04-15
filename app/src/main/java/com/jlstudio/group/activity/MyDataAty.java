package com.jlstudio.group.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.facebook.drawee.view.SimpleDraweeView;
import com.jlstudio.R;
import com.jlstudio.iknow.dialog.UpdatePwdDialog;
import com.jlstudio.main.activity.BaseActivity;
import com.jlstudio.main.activity.LoginAty;
import com.jlstudio.main.application.ActivityContror;
import com.jlstudio.main.application.Config;
import com.jlstudio.main.bean.CatchData;
import com.jlstudio.main.db.DBOption;
import com.jlstudio.main.dialog.UpdateUserDatasDialog;
import com.jlstudio.main.dialog.UploadFaceDialog;
import com.jlstudio.main.net.DownFace;
import com.jlstudio.main.net.Downloadimgs;
import com.jlstudio.main.net.GetDataNet;
import com.jlstudio.main.util.ProgressUtil;import com.jlstudio.publish.util.ShowToast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.Calendar;

public class MyDataAty extends BaseActivity implements View.OnClickListener {
    private TextView back,update_datas;//titlbar
    private Button  updatapwd,exit;//修改密码
    //显示详细信息的横条
    private RelativeLayout face,sign, name, phone, qqNumber, weixin, sex, school, department;
    private SimpleDraweeView iface;//头像
    //横条信息的文本
    private TextView tsign;//签名
    private TextView tname;//姓名
    private TextView tphone;//手机
    private TextView tqqNumber;//qq
    private TextView tweixin;//微信
    private TextView tsex;//姓别
    private TextView tclassName;//班级
    private TextView tdepartment;//系别

    //网络连接类
    private GetDataNet gn;
    //数据库连接
    private DBOption db;
    //缓存类
    private CatchData data;
    private JSONObject jsonObject;//修改的内容

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_data);
        gn = GetDataNet.getInstence(this);
        jsonObject = new JSONObject();
        initView();
        initData();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    private void initData() {
        final String uid = Config.loadUser(this).getUsername();
        JSONObject json = new JSONObject();
        try {
            json.put("username",uid);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        db = new DBOption(this);
        data = db.getCatch(Config.URL+Config.GETUSERINFO+uid);
        data = null;
        if(data == null){
            ProgressUtil.showProgressDialog(this, "数据加载...");
            gn.getData(Config.URL, Config.GETUSERINFO, new Response.Listener<String>() {

                @Override
                public void onResponse(String s) {
                    Log.d("hehe", s);
                    initView(s);
                    ProgressUtil.closeProgressDialog();
                    if (data == null) {
                        db.insertCatch(Config.URL + Config.GETUSERINFO + uid, s, System.currentTimeMillis() + "");
                    } else {
                        db.updateCatch(Config.URL + Config.GETUSERINFO + uid, s, System.currentTimeMillis() + "");
                    }
                }
            }, new Response.ErrorListener() {

                @Override
                public void onErrorResponse(VolleyError volleyError) {
                    ShowToast.show(MyDataAty.this, "获取数据失败");
                    ProgressUtil.closeProgressDialog();
                }
            }, json.toString());
        }else{
        }
        String url = Config.URL+"faces/" + Config.loadUser(this).getUsername() + ".jpg";
        Uri uri = Uri.parse(url);
        iface.setImageURI(uri);
        //Downloadimgs.initImageLoader(this).displayImage(url,iface,Downloadimgs.getOption());
    }
    private void initView(){

        back = (TextView) findViewById(R.id.back);
        update_datas = (TextView) findViewById(R.id.update_datas);
        back.setOnClickListener(this);
        update_datas.setOnClickListener(this);
        Typeface iconfont = Typeface.createFromAsset(getAssets(), "fonts/iconfont.ttf");
        back.setTypeface(iconfont);
        iface = (SimpleDraweeView) findViewById(R.id.iface);
        face = (RelativeLayout) findViewById(R.id.face);
        sign = (RelativeLayout) findViewById(R.id.sign);
        name = (RelativeLayout) findViewById(R.id.name);
        phone = (RelativeLayout) findViewById(R.id.phone);
        qqNumber = (RelativeLayout) findViewById(R.id.qqNumber);
        weixin = (RelativeLayout) findViewById(R.id.weixin);
        sex = (RelativeLayout) findViewById(R.id.sex);
        department = (RelativeLayout) findViewById(R.id.department);

        tsign = (TextView) findViewById(R.id.tsign);
        tname = (TextView) findViewById(R.id.tname);
        tphone = (TextView) findViewById(R.id.tphone);
        tqqNumber = (TextView) findViewById(R.id.tqqNumber);
        tweixin = (TextView) findViewById(R.id.tweixin);
        tsex = (TextView) findViewById(R.id.tsex);
        tclassName = (TextView) findViewById(R.id.tclassName);
        tdepartment = (TextView) findViewById(R.id.tdepartment);


        updatapwd = (Button) findViewById(R.id.updatapwd);
        face.setOnClickListener(this);
        sign.setOnClickListener(this);
        name.setOnClickListener(this);
        phone.setOnClickListener(this);
        qqNumber.setOnClickListener(this);
        weixin.setOnClickListener(this);
        sex.setOnClickListener(this);
        tclassName.setOnClickListener(this);
        department.setOnClickListener(this);
        updatapwd.setOnClickListener(this);
    }
    private void initView(String s) {
        JSONObject jsonObject = null;
        try {
            jsonObject = new JSONObject(s);
            if(jsonObject.has("usersignature"))
                tsign.setText(jsonObject.getString("usersignature"));
            if(jsonObject.has("userrealname"))
                tname.setText(jsonObject.getString("userrealname"));
            if(jsonObject.has("userphone"))
                tphone.setText(jsonObject.getString("userphone"));
            if(jsonObject.has("userqq"))
                tqqNumber.setText(jsonObject.getString("userqq"));
            if(jsonObject.has("userweixin"))
                tweixin.setText(jsonObject.getString("userweixin"));
            if(jsonObject.has("usergender"))
                tsex.setText(jsonObject.getString("usergender"));
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onClick(View v) {
        Intent intent;
        switch (v.getId()) {
            case R.id.back:
                ActivityContror.removeActivity(this);
                break;
            case R.id.update_datas:
                sendDatas();
                break;
            case R.id.face:
                intent = new Intent(this, UploadFaceDialog.class);
                intent.setAction("face");
                startActivityForResult(intent, 0);
                break;
            case R.id.sign:
                intent = new Intent(this, UpdateUserDatasDialog.class);
                intent.setAction("sign");
                startActivityForResult(intent, 1);
                break;
            case R.id.name:
                //ActivityContror.removeActivity(this);
                break;
            case R.id.phone:
                intent = new Intent(this, UpdateUserDatasDialog.class);
                intent.setAction("phone");
                startActivityForResult(intent, 2);
                break;
            case R.id.qqNumber:
                intent = new Intent(this, UpdateUserDatasDialog.class);
                intent.setAction("qqNumber");
                startActivityForResult(intent, 3);
                break;
            case R.id.weixin:
                intent = new Intent(this, UpdateUserDatasDialog.class);
                intent.setAction("weixin");
                startActivityForResult(intent, 4);
                break;
            case R.id.sex:
                intent = new Intent(this, UpdateUserDatasDialog.class);
                intent.setAction("sex");
                startActivityForResult(intent, 5);
                break;
            case R.id.school:
                break;
            case R.id.department:
                break;
            case R.id.updatapwd:
                new UpdatePwdDialog(this).show();
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try {
            switch (resultCode) {
                case 1:
                    tsign.setText(data.getAction());
                    jsonObject.put("signature", data.getAction());
                    break;
                case 2:
                    tphone.setText(data.getAction());
                    jsonObject.put("phone", data.getAction());
                    break;
                case 3:
                    tqqNumber.setText(data.getAction());
                    jsonObject.put("qq", data.getAction());
                    break;
                case 4:
                    tweixin.setText(data.getAction());
                    jsonObject.put("weixin", data.getAction());
                    break;
                case 5:
                    tsex.setText(data.getAction());
                    jsonObject.put("gender", data.getAction());
                    break;
                case 6:
                    tsex.setText(data.getAction());
                    String filePath =  data.getAction();
                    Bitmap bitmap = BitmapFactory.decodeFile(filePath);
                    iface.setImageBitmap(bitmap);
                    break;
            }
        } catch (Exception e) {

        }
    }
    private void sendDatas(){
        if(jsonObject.length() == 0){
            ShowToast.show(this,"无修改内容");
            return;
        }
        try {
            jsonObject.put("username",Config.loadUser(this).getUsername());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        ProgressUtil.showProgressDialog(this,"数据提交...");
        gn.getData(Config.URL, Config.SENDUSERINFO, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
               if(s.equals("0")){
                   ShowToast.show(MyDataAty.this,"修改成功");
                   updateCache();
               }else{
                   ShowToast.show(MyDataAty.this,"修改失败");
               }
                ProgressUtil.closeProgressDialog();
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError volleyError) {
                ShowToast.show(MyDataAty.this, "修改失败");
                ProgressUtil.closeProgressDialog();
            }
        }, jsonObject.toString());
    }
    private void updateCache(){
        JSONObject json = new JSONObject();
        try {
            json.put("signature",tsign.getText().toString().trim());
            json.put("realname",tname.getText().toString().trim());
            json.put("phone",tphone.getText().toString().trim());
            json.put("qq",tqqNumber.getText().toString().trim());
            json.put("weixin",tweixin.getText().toString().trim());
            json.put("gender",tsex.getText().toString().trim());
            db.updateCatch(Config.URL+Config.GETUSERINFO + Config.loadUser(this).getUsername(), json.toString(), System.currentTimeMillis() + "");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
