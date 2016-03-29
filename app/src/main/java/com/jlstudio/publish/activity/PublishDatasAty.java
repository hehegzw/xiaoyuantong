package com.jlstudio.publish.activity;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.jlstudio.R;
import com.jlstudio.main.activity.BaseActivity;
import com.jlstudio.main.activity.LoginAty;
import com.jlstudio.main.activity.MainActivity;
import com.jlstudio.main.application.ActivityContror;
import com.jlstudio.main.application.Config;
import com.jlstudio.main.bean.User;
import com.jlstudio.main.db.DBOption;
import com.jlstudio.publish.dialog.SelectPublishType;
import com.jlstudio.publish.fragment.FMyPublish;
import com.jlstudio.publish.fragment.FMyReceive;
import com.jlstudio.publish.util.StringUtil;

import java.util.Set;

import cn.jpush.android.api.JPushInterface;
import cn.jpush.android.api.TagAliasCallback;

public class PublishDatasAty extends BaseActivity implements View.OnClickListener {
    private TextView myReceive, myPublish, back, title_name,btnPublish;
    private ImageView fragmentTitleImage1, fragmentTitleImage2;
    private Fragment currentFragment = null;
    private DBOption db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_publish_datas);

        db = new DBOption(this);
        Config.isLogin(this);
        initView();
        currentFragment = new FMyReceive();
        getFragmentManager().beginTransaction().add(R.id.fragment, currentFragment).commit();

    }

    private void initView() {
        myReceive = (TextView) findViewById(R.id.myreceive);
        myPublish = (TextView) findViewById(R.id.mypublish);
        btnPublish = (TextView) findViewById(R.id.right_icon);
        back = (TextView) findViewById(R.id.back);
        title_name = (TextView) findViewById(R.id.title_name);
        fragmentTitleImage1 = (ImageView) findViewById(R.id.fragment_title_image1);
        fragmentTitleImage2 = (ImageView) findViewById(R.id.fragment_title_image2);
        fragmentTitleImage2.setVisibility(View.INVISIBLE);
        myReceive.setOnClickListener(this);
        myPublish.setOnClickListener(this);
        btnPublish.setOnClickListener(this);
        Typeface iconfont = Typeface.createFromAsset(getAssets(), "fonts/iconfont.ttf");
        Typeface iconfont1 = Typeface.createFromAsset(getAssets(), "fonts/publish.ttf");
        back.setTypeface(iconfont);
        btnPublish.setTypeface(iconfont1);
        back.setOnClickListener(this);
        title_name.setText("消息");
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.myreceive:
                //转换fragment
                switchContent(new FMyReceive());
                //调整文字下方的横条
                myReceive.setTextColor(Color.BLUE);
                myPublish.setTextColor(Color.BLACK);
                fragmentTitleImage1.setVisibility(View.VISIBLE);
                fragmentTitleImage2.setVisibility(View.INVISIBLE);
                break;
            case R.id.mypublish:
                switchContent(new FMyPublish());
                myReceive.setTextColor(Color.BLACK);
                myPublish.setTextColor(Color.BLUE);
                fragmentTitleImage2.setVisibility(View.VISIBLE);
                fragmentTitleImage1.setVisibility(View.INVISIBLE);
                break;
            case R.id.right_icon:
                //选择发送类型
                new SelectPublishType(this).show();
                break;
            case R.id.back:
                finish();
                break;
        }
    }

    public void switchContent(Fragment to) {
        if (currentFragment != to) {
            FragmentTransaction transaction = getFragmentManager()
                    .beginTransaction();
            if (!to.isAdded()) { // 先判断是否被add过
                transaction.hide(currentFragment).add(R.id.fragment, to).commit(); // 隐藏当前的fragment，add下一个到Activity中
            } else {
                transaction.hide(currentFragment).show(to).commit(); // 隐藏当前的fragment，显示下一个
            }
            currentFragment = to;
        }
    }
}
