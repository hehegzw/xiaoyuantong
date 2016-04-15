package com.jlstudio.main.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jlstudio.R;
import com.jlstudio.iknow.dialog.QueryDialog;
import com.jlstudio.iknow.dialog.QueryScoreDialog;
import com.jlstudio.market.activity.ShowGoodsAty;
import com.jlstudio.weather.activity.InitDataActivity;

public class MoreFunAty extends Activity implements View.OnClickListener {
    public static final int SCORE = 0;
    public static final int SCHEDULE = 1;
    private TextView back;
    private TextView titleName;
    private LinearLayout layoutSchedule;
    private LinearLayout layoutScore;
    private LinearLayout layoutMacket;
    private LinearLayout layoutCET;
    private LinearLayout layoutWeather;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_functions);
        initView();
    }

    private void initView() {
        back = (TextView) findViewById(R.id.back);
        Typeface iconfont = Typeface.createFromAsset(getAssets(),"fonts/iconfont.ttf");
        back.setTypeface(iconfont);
        titleName = (TextView) findViewById(R.id.title_name);
        titleName.setText("常用功能");
        layoutSchedule = (LinearLayout) findViewById(R.id.layout_schedule);
        layoutScore = (LinearLayout) findViewById(R.id.layout_score);
        layoutMacket = (LinearLayout) findViewById(R.id.layout_macket);
        layoutCET = (LinearLayout) findViewById(R.id.layout_cet);
        layoutWeather = (LinearLayout) findViewById(R.id.layout_weather);
        initEvent();

    }

    private void initEvent() {
        back.setOnClickListener(this);
        layoutSchedule.setOnClickListener(this);
        layoutScore.setOnClickListener(this);
        layoutMacket.setOnClickListener(this);
        layoutCET.setOnClickListener(this);
        layoutWeather.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.back:
                finish();
                break;
            case R.id.layout_schedule:
                new QueryScoreDialog(this,SCHEDULE).show();
                break;
            case R.id.layout_score:
                new QueryScoreDialog(this,SCORE).show();
                break;
            case R.id.layout_macket:
                startActivity(new Intent(this, ShowGoodsAty.class));
                break;
            case R.id.layout_cet:
                new QueryDialog(this).show();
                break;
            case R.id.layout_weather:
                startActivity(new Intent(this, InitDataActivity.class));
                break;
        }
    }
}
