package com.jlstudio.weather.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import com.jlstudio.R;
import com.jlstudio.main.activity.BaseActivity;
import com.jlstudio.weather.model.City;
import com.jlstudio.weather.model.County;
import com.jlstudio.weather.model.Province;
import com.jlstudio.weather.service.MyService;
import com.jlstudio.weather.util.ActivityControl;
import com.jlstudio.weather.util.Config;
import com.jlstudio.weather.util.XmlUtil;

import java.util.List;

/**
 * Created by gzw on 2015/10/4.
 */
public class InitDataActivity extends BaseActivity {
    private ProgressDialog progressDialog = null;
    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            startActivity(new Intent(InitDataActivity.this, ShowArea.class));
            closeProgressDialog();
            ActivityControl.removeAty(InitDataActivity.this);
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //startService(new Intent(this, MyService.class));自动更新天气
        showProgressDialog();
        if(Config.isFirstLogin()){
            new Thread(){
                @Override
                public void run() {
                    try {
                        XmlUtil.xmltodb("areaCode.xml", new XmlUtil.Success() {
                            @Override
                            public void onSuccess(List<Province> provinces, List<City> citys, List<County> countries) {

                            }
                        });
                        handler.sendEmptyMessage(0);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    super.run();
                }
            }.start();
        }else {
            startActivity(new Intent(InitDataActivity.this, ShowForecast.class));
            closeProgressDialog();
            ActivityControl.removeAty(this);
        }

    }
    private void showProgressDialog(){
        if(progressDialog == null){
            progressDialog = new ProgressDialog(this);
            progressDialog.setMessage("加载数据...");
            progressDialog.setCanceledOnTouchOutside(false);
        }
        progressDialog.show();
    }
    private void closeProgressDialog(){
        if(progressDialog!=null){
            progressDialog.dismiss();
        }
    }
}
