package com.jlstudio.publish.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.TextView;

import com.jlstudio.R;
import com.jlstudio.main.application.Config;
import com.jlstudio.publish.fragment.FMyContact;
import com.jlstudio.publish.fragment.FMyContactSub;
import com.jlstudio.publish.fragment.FRecentContact;

public class ShowPersonAty extends FragmentActivity implements View.OnClickListener {
    private TextView tv_recent;
    private TextView tv_friend;
    private Fragment currentFragment;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_person);
        initView();
        initEvent();
        initFragment();
    }

    private void initFragment() {
        if(Config.loadUser(this).getRole().equals("普通用户")){
            currentFragment = new FMyContactSub();
            getSupportFragmentManager().beginTransaction().add(R.id.layout_fragment, currentFragment).commit();
        }else{
            currentFragment = new FMyContact();
            getSupportFragmentManager().beginTransaction().add(R.id.layout_fragment, currentFragment).commit();
        }
    }

    private void initEvent() {
//        tv_recent.setOnClickListener(this);
//        tv_friend.setOnClickListener(this);
    }

    private void initView() {
//        tv_recent = (TextView) findViewById(R.id.tv_recent);
//        tv_friend = (TextView) findViewById(R.id.tv_friend);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_recent: {
                switchContent(new FRecentContact());
                tv_recent.setBackgroundResource(R.drawable.shape_left_wite);
                tv_friend.setBackgroundResource(R.drawable.shape_right_blue);
                tv_friend.setTextColor(getResources().getColor(R.color.white));
                tv_recent.setTextColor(getResources().getColor(R.color.blue));
                break;
            }
            case R.id.tv_friend: {
                if(Config.loadUser(this).getRole().equals("普通用户")){
                    switchContent(new FMyContactSub());
                }else{
                    switchContent(new FMyContact());
                }
                tv_recent.setBackgroundResource(R.drawable.shape_left_blue);
                tv_friend.setBackgroundResource(R.drawable.shape_right_wite);
                tv_friend.setTextColor(getResources().getColor(R.color.blue));
                tv_recent.setTextColor(getResources().getColor(R.color.white));
                break;
            }
        }
    }
    public void switchContent(Fragment to) {
        if (currentFragment != to) {
            FragmentTransaction transaction = getSupportFragmentManager()
                    .beginTransaction();
            if (!to.isAdded()) { // 先判断是否被add过
                transaction.hide(currentFragment).add(R.id.layout_fragment, to).commit(); // 隐藏当前的fragment，add下一个到Activity中
            } else {
                transaction.hide(currentFragment).show(to).commit(); // 隐藏当前的fragment，显示下一个
            }
            currentFragment = to;
        }
    }
}
