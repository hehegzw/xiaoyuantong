package com.jlstudio.main.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.jlstudio.main.application.Config;
import com.jlstudio.main.bean.User;
import com.jlstudio.main.db.DBOption;
import com.jlstudio.main.util.JsonToBean;

import java.util.Set;

import cn.jpush.android.api.JPushInterface;
import cn.jpush.android.api.TagAliasCallback;

public class MainActivity extends BaseActivity {
    private DBOption db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_main);
        db = new DBOption(this);
        //是否是首次登录
        if(Config.isFirst(this)){
            startActivity(new Intent(this,WelcomeAty.class));
            finish();
        }
//        User user = Config.loadUser(this);
//        if(user != null && user.isOnline() == true){
//        }
        //尝试获取默认账号
//        User user = JsonToBean.getUser(db.getCatch("defualtUser").getContent());
//        if(user != null){
//            Config.saveUser(this,user);
//            Config.registerJPUSH(this, user.getUsername());
//        }
        startActivity(new Intent(this,MoreFunAty.class));
        finish();
    }

}
