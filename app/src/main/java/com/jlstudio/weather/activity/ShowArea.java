package com.jlstudio.weather.activity;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.jlstudio.R;
import com.jlstudio.main.activity.MainActivity;
import com.jlstudio.publish.util.ShowToast;
import com.jlstudio.weather.net.GetDataNet;
import com.jlstudio.main.activity.BaseActivity;
import com.jlstudio.main.db.DBOption;
import com.jlstudio.weather.model.City;
import com.jlstudio.weather.model.CityAdapter;
import com.jlstudio.weather.model.County;
import com.jlstudio.weather.model.CountyAdapter;
import com.jlstudio.weather.model.Province;
import com.jlstudio.weather.model.ProvinceAdapter;
import com.jlstudio.weather.util.ActivityControl;
import com.jlstudio.weather.util.Config;
import com.jlstudio.weather.util.ConnectUtil;

import org.json.JSONObject;

import java.util.List;


public class ShowArea extends BaseActivity implements AdapterView.OnItemClickListener, View.OnClickListener {
    public static final String URL = "http://wthrcdn.etouch.cn/weather_mini?citykey=";
    private ListView show_area = null;
    private ProvinceAdapter padapter = null;
    private CityAdapter cadapter = null;
    private CountyAdapter ccadapter = null;
    private List<Province> provinces = null;
    private  List<City> citys = null;
    private  List<County> counties = null;
    private DBOption db;
    private GetDataNet gn;
    private int currentAdapter = 1;
    private TextView back,title_name;
    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            ShowToast.show(ShowArea.this, "获取数据失败,请检查网络");
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_area);
        db = new DBOption(this);
        gn = new GetDataNet(this);
        initView();
        //initData();
    }
    private void initData() {
        Intent i = getIntent();
        Bundle bundle = i.getBundleExtra("datas");
        provinces = (List<Province>) bundle.getSerializable("provinces");
        citys = (List<City>) bundle.getSerializable("citys");
        counties = (List<County>) bundle.getSerializable("counties");
    }

    private void initView() {
        show_area = (ListView) findViewById(R.id.show_area);
        List<Province> list = db.getProvinces();
        padapter = new ProvinceAdapter(list);
        show_area.setAdapter(padapter);
        show_area.setOnItemClickListener(this);
        back = (TextView) findViewById(R.id.back);
        title_name = (TextView) findViewById(R.id.title_name);
        Typeface iconfont = Typeface.createFromAsset(getAssets(),"fonts/iconfont.ttf");
        back.setTypeface(iconfont);
        title_name.setText("选择地区");
        back.setOnClickListener(this);
    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if (currentAdapter == 1) {
            List<City> cities = db.getCitys(padapter.getList().get(position).getCode());
            cadapter = new CityAdapter(cities);
            show_area.setAdapter(cadapter);
            currentAdapter = 2;
        } else if (currentAdapter == 2) {
            List<County> counties = db.getCountrys(cadapter.getList().get(position).getCode());
            ccadapter = new CountyAdapter(counties);
            show_area.setAdapter(ccadapter);
            currentAdapter = 3;
        } else if (currentAdapter == 3) {
            Config.saveArea(ccadapter.getList().get(position).getCode());
            String code = ccadapter.getList().get(position).getCode();
//            gn.getData(URL + ccadapter.getList().get(position).getCode(),new Response.Listener<JSONObject>(){
//                @Override
//                public void onResponse(JSONObject jsonObject) {
//                    Config.saveDatas(jsonObject.toString());
//                    Intent intent = new Intent();
//                    intent.setClass(ShowArea.this, ShowForecast.class);
//                    startActivity(intent);
//                    ActivityControl.removeAty(ShowArea.this);
//                }
//            },new Response.ErrorListener(){
//                @Override
//                public void onErrorResponse(VolleyError volleyError) {
//                    ShowToast.show(ShowArea.this,"获取数据失败");
//                }
//            });
//            gn.getDataString(URL + ccadapter.getList().get(position).getCode(), new Response.Listener<String>() {
//
//                @Override
//                public void onResponse(String s) {
//                    Config.saveDatas(s);
//                    Intent intent = new Intent();
//                    intent.setClass(ShowArea.this, ShowForecast.class);
//                    startActivity(intent);
//                    ActivityControl.removeAty(ShowArea.this);
//                }
//            }, new Response.ErrorListener() {
//
//                @Override
//                public void onErrorResponse(VolleyError volleyError) {
//                    ShowToast.show(ShowArea.this,"获取数据失败");
//                }
//            });
            ConnectUtil.connect(ccadapter.getList().get(position).getCode(), new ConnectUtil.Listener() {
                @Override
                public void success(String result) {
                    Config.saveDatas(result);
                    Intent intent = new Intent();
                    intent.setClass(ShowArea.this, ShowForecast.class);
                    startActivity(intent);
                    ActivityControl.removeAty(ShowArea.this);
                }

                @Override
                public void failer(int code) {
                    handler.sendEmptyMessage(1);
                }
            });
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (currentAdapter == 3) {
                show_area.setAdapter(cadapter);
                currentAdapter = 2;
            } else if (currentAdapter == 2) {
                show_area.setAdapter(padapter);
                currentAdapter = 1;
            } else if (currentAdapter == 1) {
                Intent intent = new Intent();
                intent.setClass(ShowArea.this, MainActivity.class);
                startActivity(intent);
                ActivityControl.removeAty(ShowArea.this);
            }
        }
        return true;
    }

    @Override
    public void onClick(View v) {
        ActivityControl.removeAty(this);
    }
}
