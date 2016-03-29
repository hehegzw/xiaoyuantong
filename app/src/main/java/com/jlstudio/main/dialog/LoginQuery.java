package com.jlstudio.main.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;

import com.jlstudio.R;
import com.jlstudio.group.activity.ShowContactsActivity;
import com.jlstudio.main.activity.LoginAty;
import com.jlstudio.main.activity.MainActivity;
import com.jlstudio.main.application.Config;
import com.jlstudio.main.bean.CatchData;
import com.jlstudio.main.bean.User;
import com.jlstudio.main.db.DBOption;
import com.jlstudio.main.util.JsonToBean;
import com.jlstudio.publish.activity.PublishDatasAty;
import com.jlstudio.publish.net.GetNotificationNet;
import com.jlstudio.swzl.activity.LostPublishActivity;
import com.jlstudio.weather.util.ActivityControl;


/**
 * Created by gzw on 2015/9/26.
 */
public class LoginQuery extends Dialog implements View.OnClickListener {
    private Context context;
    private Button sure,cancle;
    private String user;
    private GetNotificationNet gn;//网络连接类
    private DBOption db;//数据库操作

    public LoginQuery(Context context,String user) {
        super(context);
        this.context = context;
        this.user = user;
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_login_query);
        gn = new GetNotificationNet(context);
        db = new DBOption(context);
        initView();
    }
    private void initView() {

        sure = (Button) findViewById(R.id.sure);
        cancle = (Button) findViewById(R.id.cancle);
        sure.setOnClickListener(this);
        cancle.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.sure:
                Config.saveBaseUser(context, JsonToBean.getUser(user));
                Config.saveUser(context, JsonToBean.getUser(user));
                context.startActivity(new Intent(context, ShowContactsActivity.class));
                break;
            case R.id.cancle:
                Config.saveUser(context, JsonToBean.getUser(user));
                context.startActivity(new Intent(context, ShowContactsActivity.class));
                break;
        }
        ((Activity)context).finish();
    }
}
