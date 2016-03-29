package com.jlstudio.weather.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jlstudio.R;
import com.jlstudio.main.activity.BaseActivity;
import com.jlstudio.main.application.ActivityContror;
import com.jlstudio.publish.util.ShowToast;
import com.jlstudio.weather.model.WeatherDatas;
import com.jlstudio.weather.util.ActivityControl;
import com.jlstudio.weather.util.Config;
import com.jlstudio.weather.util.ConnectUtil;
import com.jlstudio.weather.util.FormatDatas;

public class ShowForecast extends BaseActivity implements View.OnClickListener {
    private TextView show_forecast,refresh,area,select_area;
    private TextView show_type,show_temp,show_wind,show_wendu,show_data,show_ganmao;
    private LinearLayout content;
    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            WeatherDatas datas = FormatDatas.getDatas(Config.loadDatas());
            if(datas != null){
                initBK(datas);
                setViewData(datas);
                ShowToast.show(ShowForecast.this, "天气信息更新成功!");
            }else{
                show_forecast.setText("获取天气信息失败!");
            }
            super.handleMessage(msg);
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_forecast);
        initView();
        String string = Config.loadDatas();
        if(string == null){
            startActivity(new Intent(this,ShowArea.class));
            ActivityContror.removeActivity(this);
        }else{
            WeatherDatas datas = FormatDatas.getDatas(string);
            initBK(datas);
            setViewData(datas);
        }
    }

    private void initBK(WeatherDatas data) {
        if(data.getType().equals("小雨")){
            content.setBackgroundResource(R.drawable.lrain);
        }else if(data.getType().equals("晴")){
            content.setBackgroundResource(R.drawable.sun);
        }else if(data.getType().indexOf("雷")!=-1){
            content.setBackgroundResource(R.drawable.ray);
        } else if(data.getType().indexOf("雨")!=-1){
            content.setBackgroundResource(R.drawable.lrain);
        }else if(data.getType().indexOf("雪")!=-1){
            content.setBackgroundResource(R.drawable.snow);
        }else if(data.getType().indexOf("多云")!=-1){
            content.setBackgroundResource(R.drawable.cloud);
        }else if(data.getType().equals("阴")){
            content.setBackgroundResource(R.drawable.yin);
        }

    }

    private void initView() {
        refresh = (TextView) findViewById(R.id.refresh);
        content = (LinearLayout) findViewById(R.id.content);
        select_area = (TextView) findViewById(R.id.select_area);
        area = (TextView) findViewById(R.id.area);
        show_type = (TextView) findViewById(R.id.show_type);
        show_temp = (TextView) findViewById(R.id.show_temp);
        show_wind = (TextView) findViewById(R.id.show_wind);
        show_wendu = (TextView) findViewById(R.id.show_wendu);
        show_ganmao = (TextView) findViewById(R.id.show_ganmao);
        show_data = (TextView) findViewById(R.id.show_data);
        refresh.setOnClickListener(this);
        select_area.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.select_area){
            startActivity(new Intent(this,ShowArea.class));
            ActivityControl.removeAty(this);
        }else if(v.getId() == R.id.refresh){
            ConnectUtil.connect(Config.loadArea(), new ConnectUtil.Listener() {
                @Override
                public void success(String result) {
                    Config.saveDatas(result);
                    handler.sendEmptyMessage(0);
                }

                @Override
                public void failer(int code) {
                }
            });
        }else{

        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        ActivityContror.removeActivity(this);
//        if(keyCode == KeyEvent.KEYCODE_BACK){
//            Intent i = new Intent();
//            i.putExtra("action","over");
//            this.sendBroadcast(i);
//            ActivityControl.destroryAllAty();
//        }
        return true;
    }
    private void setViewData(WeatherDatas datas){
        area.setText(datas.getName());
        show_type.setText(datas.getType());
        show_temp.setText(datas.getHeight()+"  "+datas.getLow());
        show_wind.setText(datas.getFengxiang()+"  "+datas.getFengli());
        show_data.setText(datas.getDate());
        show_ganmao.setText(datas.getGanmao());
        show_wendu.setText(datas.getWendu()+"℃");
    }
}
