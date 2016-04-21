package com.jlstudio.iknow.dialog;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.text.InputType;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.jlstudio.R;
import com.jlstudio.iknow.activity.CETScoreAty;
import com.jlstudio.main.application.Config;
import com.jlstudio.main.bean.User;
import com.jlstudio.publish.net.GetNotificationNet;
import com.jlstudio.publish.util.ShowToast;
import com.jlstudio.publish.util.StringUtil;

import org.json.JSONException;
import org.json.JSONObject;


/**
 * Created by gzw on 2015/9/26.
 */
public class UpdatePwdDialog extends Dialog {
    private Context context;
    private EditText username, password;
    private TextView title_name,uid,pwd;
    private Button submit;
    private GetNotificationNet gn;

    public UpdatePwdDialog(Context context) {
        super(context);
        this.context = context;
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_querycet);
        gn = new GetNotificationNet(context);
        initView();
    }
    private void initView() {
        username = (EditText) findViewById(R.id.username);
        password = (EditText) findViewById(R.id.password);
        title_name = (TextView) findViewById(R.id.title_name);
        uid = (TextView) findViewById(R.id.uid);
        pwd = (TextView) findViewById(R.id.pwd);
        uid.setText("旧密码:");
        uid.setInputType(InputType.TYPE_NULL);
        pwd.setText("新密码:");
        pwd.setInputType(InputType.TYPE_NULL);
        title_name.setText("修改密码");
        submit = (Button) findViewById(R.id.submit);
        submit.setText("提交");
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String user = username.getText().toString();
                String pwd = password.getText().toString();
                if (StringUtil.isEmpty(user, pwd)) {
                    ShowToast.show(context, "内容不能为空");
                } else {
                    getDatasFromNet(user, pwd);
                }
            }
        });
    }


    private void getDatasFromNet(final String user, final String pwd) {
        JSONObject json = new JSONObject();
        String servlet = null;
        try {
            json.put("username",Config.loadUser(context).getUsername());
            json.put("oldpwd", user);
            json.put("newpwd", pwd);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        gn.getDepartment(Config.URL, Config.SENDUSERINFO, new Response.Listener<String>() {

            @Override
            public void onResponse(String s) {
                try {
                    JSONObject json = new JSONObject(s);
                    String status = json.getString("status");
                    if (status.equals("success")) {
                        User user = Config.loadUser(context);
                        user.setPassword(pwd);
                        Config.saveUser(context, user);
                        ShowToast.show(context, "修改成功");
                        dismiss();
                    } else {
                        ShowToast.show(context, "旧密码错误或新密码不合法");
                        username.setText("");
                        password.setText("");
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError volleyError) {
                ShowToast.show(context, "修改失败");
            }
        }, json.toString());
    }
}
