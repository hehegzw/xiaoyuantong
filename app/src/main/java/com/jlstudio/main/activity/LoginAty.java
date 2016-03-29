package com.jlstudio.main.activity;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.jlstudio.R;
import com.jlstudio.main.dialog.LoginQuery;
import com.jlstudio.main.application.Config;
import com.jlstudio.main.net.Downloadimgs;
import com.jlstudio.main.net.GetDataNet;
import com.jlstudio.main.util.JsonToBean;
import com.jlstudio.main.util.ProgressUtil;
import com.jlstudio.main.widget.CircleImageView;
import com.jlstudio.publish.util.ShowToast;
import com.jlstudio.publish.util.StringUtil;

import org.json.JSONException;
import org.json.JSONObject;

public class LoginAty extends BaseActivity implements View.OnClickListener {
    private EditText username, password;//用户名，密码
    private Button login;
    private CircleImageView face;
    private GetDataNet gn;//网络连接
    private TextView unloginfun;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        gn = GetDataNet.getInstence(this);
        initView();
    }

    private void initView() {
        username = (EditText) findViewById(R.id.username);
        password = (EditText) findViewById(R.id.password);
        face = (CircleImageView) findViewById(R.id.face);
        unloginfun = (TextView) findViewById(R.id.unloginfun);
        TextView text_user = (TextView) findViewById(R.id.text_user);
        TextView text_password = (TextView) findViewById(R.id.text_password);
        Typeface icon = Typeface.createFromAsset(getAssets(), "fonts/login.ttf");
        text_user.setTypeface(icon);
        text_password.setTypeface(icon);
        login = (Button) findViewById(R.id.login);
        login.setOnClickListener(this);
        unloginfun.setOnClickListener(this);
        password.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (password.hasFocus()) {
                    String string = username.getText().toString();
                    if (!TextUtils.isEmpty(string)) {
                        String url = Config.URL + "faces/" + string + ".jpg";
                        Downloadimgs.initImageLoader(LoginAty.this).displayImage(url, face, Downloadimgs.getOption());
                    }
                }
            }
        });
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.login){
            login();
        }else{
            startActivity(new Intent(this,MoreFunAty.class));
        }
    }
    private void login(){
        final String user = username.getText().toString();
        String pwd = password.getText().toString();
        if (StringUtil.isEmpty(user, pwd)) {
            ShowToast.show(this, "用户名或密码不能为空");
            return;
        }
        JSONObject json = new JSONObject();
        try {
            json.put("username", user);
            json.put("password", pwd);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        ProgressUtil.showProgressDialog(this, "登陆中");
        gn.getData(Config.URL, Config.LOGIN, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                try {
                    JSONObject json = new JSONObject(s);
                    if (json.getString("status").equals("1")) {
                        ProgressUtil.closeProgressDialog();
                        Config.registerJPUSH(LoginAty.this, JsonToBean.getUser(s).getUsername());
                        new LoginQuery(LoginAty.this, s).show();
                    } else {
                        ShowToast.show(LoginAty.this, "用户名密码错误");
                    }
                    ProgressUtil.closeProgressDialog();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                ShowToast.show(LoginAty.this, "网络连接失败");
                ProgressUtil.closeProgressDialog();
            }
        }, json.toString());
    }

}
