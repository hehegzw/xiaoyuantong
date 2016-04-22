package com.jlstudio.main.activity;

import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.ImageView;
import android.widget.TextView;

import com.jlstudio.R;
import com.jlstudio.group.activity.ShowContactsActivity;
import com.jlstudio.main.application.Config;
import com.jlstudio.main.bean.User;
import com.jlstudio.publish.util.StringUtil;

import cn.jpush.android.api.JPushInterface;

public class WelcomeAty extends BaseActivity {
    private TextView time;
    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
           time.setText(msg.what+"s");
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 取消状态栏
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_welcome_aty);
        ImageView image = (ImageView) findViewById(R.id.image);
        AlphaAnimation alphaAnimation = new AlphaAnimation(0.1f, 1.0f);
        alphaAnimation.setDuration(2000);
        image.setAnimation(alphaAnimation);
        alphaAnimation.setAnimationListener(new Animation.AnimationListener() {

            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                User user = Config.loadUser(WelcomeAty.this);
                User baseUser = Config.loadBaseUser(WelcomeAty.this);
                if (!StringUtil.isEmpty(user.getUsername()) || !StringUtil.isEmpty(baseUser.getUsername())) {
                    startActivity(new Intent(WelcomeAty.this,ShowContactsActivity.class));
                }else{
                    startActivity(new Intent(WelcomeAty.this,LoginAty.class));
                }
                finish();
            }
        });
        time = (TextView) findViewById(R.id.time);
        new Thread(){
            @Override
            public void run() {
                super.run();
                int i = 2;
                while(i>0){
                    i--;
                    try {
                        Thread.sleep(1000);
                        handler.sendEmptyMessage(i);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }.start();
    }
    @Override
    protected void onResume() {
        super.onResume();
        JPushInterface.onResume(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        JPushInterface.onPause(this);
    }
}
