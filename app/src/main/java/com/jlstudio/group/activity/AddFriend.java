package com.jlstudio.group.activity;

import android.app.Activity;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.SoundPool;
import android.os.Vibrator;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.jlstudio.R;
import com.jlstudio.group.adapter.SearchAdapter;
import com.jlstudio.group.bean.Contacts;
import com.jlstudio.main.application.Config;
import com.jlstudio.main.net.DownFace;
import com.jlstudio.main.net.Downloadimgs;
import com.jlstudio.main.net.GetDataNet;
import com.jlstudio.main.util.ProgressUtil;
import com.jlstudio.publish.util.JsonToPubhlishData;
import com.jlstudio.publish.util.ShowToast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class AddFriend extends Activity implements View.OnClickListener, AdapterView.OnItemClickListener, SensorEventListener {
    private TextView back;
    private TextView tvshake;
    private TextView classname;
    private TextView name;
    private EditText search;
    private ListView listview;
    private ImageView delete;
    private TextView shake;
    private ImageView face;
    private LinearLayout textarea;
    private LinearLayout editarea;
    private LinearLayout personinfo;
    private List<Contacts> contactses;
    private SearchAdapter adapter;
    private GetDataNet gn;
    private String searchdata;//搜索获取的结果
    private SensorManager sensorManager = null;//摇一摇管理
    private Vibrator vibrator = null;//震动管理
    private String userid = null;
    private SoundPool soundPool;
    private int musicid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_friend);
        gn = GetDataNet.getInstence(this);
        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        vibrator = (Vibrator) getSystemService(Service.VIBRATOR_SERVICE);
        soundPool = new SoundPool(1,0,5);
        musicid = soundPool.load(this,R.raw.weixin,1);
        initView();
        initDate();
    }

    @Override
    protected void onResume() {
        super.onResume();
        sensorManager.registerListener(this, sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER), SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    protected void onPause() {
        super.onPause();
        sensorManager.unregisterListener(this);
    }

    private void initDate() {
        contactses = new ArrayList<>();
        adapter = new SearchAdapter(this, contactses);
        listview.setAdapter(adapter);
    }

    private void initView() {
        back = (TextView) findViewById(R.id.back);
        tvshake = (TextView) findViewById(R.id.tvshake);
        classname = (TextView) findViewById(R.id.classname);
        name = (TextView) findViewById(R.id.name);
        face = (ImageView) findViewById(R.id.image);
        search = (EditText) findViewById(R.id.search);
        listview = (ListView) findViewById(R.id.listview);
        delete = (ImageView) findViewById(R.id.delete);
        delete.setVisibility(View.INVISIBLE);
        shake = (TextView) findViewById(R.id.shake);
        textarea = (LinearLayout) findViewById(R.id.textarea);
        editarea = (LinearLayout) findViewById(R.id.editarea);
        personinfo = (LinearLayout) findViewById(R.id.personinfo);
        personinfo.setVisibility(View.GONE);
        editarea.setVisibility(View.GONE);
        delete.setOnClickListener(this);
        tvshake.setOnClickListener(this);
        listview.setOnItemClickListener(this);
        textarea.setOnClickListener(this);
        personinfo.setOnClickListener(this);
        Typeface iconfont = Typeface.createFromAsset(getAssets(), "fonts/iconfont.ttf");
        back.setTypeface(iconfont);
        iconfont= Typeface.createFromAsset(getAssets(), "fonts/shake.ttf");
        shake.setTypeface(iconfont);
        back.setOnClickListener(this);
        search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() != 0) {
                    Log.d("hehe", s.toString());
                    delete.setVisibility(View.VISIBLE);
                    getDataFromNet(s.toString(), "fuzzyFind");
                } else {
                    delete.setVisibility(View.INVISIBLE);
                    if (contactses != null) {
                        contactses.clear();
                        adapter.setP_list(contactses);
                    }
                }
            }
        });
    }

    /**
     * 从网络上获取数据
     */
    private void getDataFromNet(String name, String action) {
        JSONObject json = new JSONObject();
        try {
            json.put("username", name);
            json.put("userid", Config.loadUser(this).getUsername());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        gn.getData(Config.URL, action, new Response.Listener<String>() {

            @Override
            public void onResponse(String s) {
                try {
                    JSONObject json = new JSONObject(s);
                    if (json.getInt("type") == 0) {
                        searchFriend(s);
                    } else {
                        soundPool.setVolume(soundPool.play(musicid,1,1, 0, 0, 1),1.0f,1.0f);
                        shakeFriend(s);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }finally {
                    sensorManager.registerListener(AddFriend.this, sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER), SensorManager.SENSOR_DELAY_NORMAL);
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError volleyError) {
                ShowToast.show(AddFriend.this,"你的交际能力太强，已经找不到陌生人了");
                sensorManager.registerListener(AddFriend.this, sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER), SensorManager.SENSOR_DELAY_NORMAL);
            }
        }, json.toString());
        ProgressUtil.closeProgressDialog();
    }

    private void shakeFriend(String s) {
        try {
            searchdata = s;
            Log.d("AddFriend",s);
            JSONObject json = new JSONObject(s);
            userid = json.getString("userid");
            classname.setText(json.getString("classname"));
            name.setText(json.getString("userrealname"));
            personinfo.setVisibility(View.VISIBLE);
            String url = Config.URL+"faces/" + userid + ".jpg";
            Downloadimgs.initImageLoader(this).displayImage(url, face, Downloadimgs.getOption());
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void searchFriend(String s) {
        searchdata = s;
        contactses = JsonToPubhlishData.getSearchData(s);
        if (contactses != null)
            adapter.setP_list(contactses);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back:
                finish();
                overridePendingTransition(R.anim.activity_right_in, R.anim.activity_left_out);
                break;
            case R.id.delete:
                search.setText("");
                if (contactses != null) {
                    contactses.clear();
                    adapter.setP_list(contactses);
                }
                break;
            case R.id.textarea:
                textarea.setVisibility(View.GONE);
                editarea.setVisibility(View.VISIBLE);
                shake.setVisibility(View.GONE);
                personinfo.setVisibility(View.GONE);
                sensorManager.unregisterListener(this);
                break;
            case R.id.personinfo:
                Intent intent = new Intent(this, NewFriendData.class);
                intent.setAction(searchdata);
                intent.putExtra("index", -1);
                startActivity(intent);
                break;
            case R.id.tvshake:
                if(shake.getVisibility() == View.GONE){
                    shake.setVisibility(View.VISIBLE);
                    sensorManager.registerListener(this, sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER), SensorManager.SENSOR_DELAY_NORMAL);
                    editarea.setVisibility(View.GONE);
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(search.getWindowToken(), 0);//强制关闭软键盘
                    textarea.setVisibility(View.VISIBLE);
                    contactses.clear();
                    adapter.notifyDataSetChanged();
                }
                break;
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent = new Intent(this, NewFriendData.class);
        intent.setAction(searchdata);
        intent.putExtra("index", position);
        startActivity(intent);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            finish();
            overridePendingTransition(R.anim.activity_right_in, R.anim.activity_left_out);
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        int sensorType = event.sensor.getType();
        //values[0]:X轴，values[1]：Y轴，values[2]：Z轴
        float[] values = event.values;
        if (sensorType == Sensor.TYPE_ACCELEROMETER) {
            if ((Math.abs(values[0]) > 19 || Math.abs(values[1]) > 19 || Math.abs(values[2]) > 19)) {
                //摇动手机后，再伴随震动提示~~
                vibrator.vibrate(500);
                ProgressUtil.showProgressDialog(this, "数据加载中...");
                sensorManager.unregisterListener(this);
                getDataFromNet("", "shake");
            }
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

}
