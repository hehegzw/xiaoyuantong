package com.jlstudio.main.activity;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.jlstudio.R;
import com.jlstudio.main.adapter.PageAdapter;
import com.jlstudio.main.application.ActivityContror;
import com.jlstudio.main.application.Config;

import java.util.ArrayList;
import java.util.List;

public class WelcomeAty extends BaseActivity implements  View.OnClickListener, ViewPager.OnPageChangeListener {
    private ViewPager viewpager;
    private List<View> views;
    private PagerAdapter adapter;
    private TextView[] textViews;//小圆点
    private Button start;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome_aty);
        initView();
    }
    private void initView() {
        textViews = new TextView[4];
        viewpager = (ViewPager) findViewById(R.id.viewpager);
        views = new ArrayList<>();
        ImageView imageView1 = (ImageView) LayoutInflater.from(this).inflate(R.layout.viewpager_item,null);
        imageView1.setImageResource(R.drawable.lost);
        views.add(imageView1);
        ImageView imageView2 = (ImageView) LayoutInflater.from(this).inflate(R.layout.viewpager_item,null);
        imageView2.setImageResource(R.drawable.score);
        views.add(imageView2);
        ImageView imageView3 = (ImageView) LayoutInflater.from(this).inflate(R.layout.viewpager_item,null);
        imageView3.setImageResource(R.drawable.notifitation);
        views.add(imageView3);
        adapter = new PageAdapter(views);
        viewpager.setAdapter(adapter);
        viewpager.setOnPageChangeListener(this);

        start = (Button) findViewById(R.id.start);
        start.setOnClickListener(this);
        start.setVisibility(View.INVISIBLE);

        Typeface iconfont = Typeface.createFromAsset(getAssets(),"fonts/dot.ttf");
        textViews[0] = (TextView) findViewById(R.id.dot1);
        textViews[1] = (TextView) findViewById(R.id.dot2);
        textViews[2] = (TextView) findViewById(R.id.dot3);
        textViews[3] = (TextView) findViewById(R.id.dot4);
        for (int i = 0;i < 4; i++){
            textViews[i].setTypeface(iconfont);
            textViews[i].setOnClickListener(this);
        }
        updateColor(0);

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.dot1:
                start.setVisibility(View.INVISIBLE);
                viewpager.setCurrentItem(0);
                updateColor(0);
                break;
            case R.id.dot2:
                start.setVisibility(View.INVISIBLE);
                viewpager.setCurrentItem(1);
                updateColor(1);
                break;
            case R.id.dot3:
                start.setVisibility(View.VISIBLE);
                viewpager.setCurrentItem(2);
                updateColor(2);
                break;
            case R.id.dot4:
                viewpager.setCurrentItem(3);
                updateColor(3);
                break;
            case R.id.start:
                startActivity(new Intent(this, MainActivity.class));
                ActivityContror.removeActivity(this);
                break;
        }
    }
    private void updateColor(int index){
        for (int i=0; i< 4;i++){
            if(i == index){
                textViews[i].setTextColor(Color.BLUE);
            }else{
                textViews[i].setTextColor(Color.GRAY);
            }
        }
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        if(position == 2){
            start.setVisibility(View.VISIBLE);
        }else{
            start.setVisibility(View.INVISIBLE);
        }
        updateColor(position);
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }
}
