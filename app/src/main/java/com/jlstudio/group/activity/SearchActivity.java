package com.jlstudio.group.activity;

import android.app.Activity;
import android.app.Service;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Vibrator;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.jlstudio.R;
import com.jlstudio.group.adapter.SearchAdapter;
import com.jlstudio.group.bean.Contacts;
import com.jlstudio.group.util.SearchUtil;
import com.jlstudio.main.application.Config;
import com.jlstudio.main.net.GetDataNet;
import com.jlstudio.main.util.ProgressUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * 搜索&添加好友
 */
public class SearchActivity extends Activity implements SensorEventListener{
    private SearchView sv;
    private ListView lv;//显示搜索结果的listview
    private SearchAdapter sa;//声明适配器对象
    private ImageView searchRandom;//随机按钮
    private TextView search_text;//随机提示语句
    private SensorManager sensorManager = null;//摇一摇管理
    private Vibrator vibrator = null;//震动管理

    private ArrayList<Contacts> p_list = new ArrayList<Contacts>();
    //    private final String SEARCH_NET_URL = Config.URL + Config.SEARCHNET;
    private final int SUCC_NUM = 1;//网络成功并且精确查找
    private final int SUCC_RAN = 2;//网络访问成功并且为随机查找
    private final int SEA_NULL = 3;//搜索为空,界面刷新使用
    private final String RANDOM = "random+&";//随机查找的标志
    private GetDataNet gn;//网络连接
    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            //在这里刷新UI
            switch (msg.what){
                case SUCC_NUM ://指代从网络获取的搜索内容(包括随机)
                    Toast.makeText(SearchActivity.this,"进入了消息解析",Toast.LENGTH_SHORT).show();
                    search_text.setVisibility(View.GONE);
                    searchRandom.setVisibility(View.GONE);
                    lv.setVisibility(View.VISIBLE);
                    sa = (SearchAdapter) lv.getAdapter();
                    sa.notifyDataSetChanged();
                    break;
                case SEA_NULL ://搜索框为空，刷新界面
                    Toast.makeText(SearchActivity.this,"刷新界面",Toast.LENGTH_SHORT).show();
                    lv.setVisibility(View.GONE);
                    search_text.setVisibility(View.VISIBLE);
                    searchRandom.setVisibility(View.VISIBLE);
                    break;
            }
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        gn = GetDataNet.getInstence(this);
        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        vibrator = (Vibrator) getSystemService(Service.VIBRATOR_SERVICE);
        init();
    }
    public void doSearch(final String query){
        JSONObject json = new JSONObject();
        try {
            //是否是随机查找
            if(!query.equals("random+&"))
                json.put("query", query);
            else
                json.put("query", "random+&");
            json.put("query","random+&");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        ProgressUtil.showProgressDialog(this, "数据加载...");
        gn.getData(Config.URL, Config.SEARCH_NET, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                ProgressUtil.closeProgressDialog();
                //发送消息至handler
                Message mes = new Message();
                mes.what = SUCC_NUM;
                //将json字符串转化为学生对象集合
                p_list = SearchUtil.getContactsFromJson(s, p_list);
                handler.sendMessage(mes);
                Toast.makeText(SearchActivity.this, "访问成功", Toast.LENGTH_SHORT).show();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                ProgressUtil.closeProgressDialog();
                Toast.makeText(SearchActivity.this, "连接服务器失败", Toast.LENGTH_SHORT).show();
            }
        }, json.toString());
    }
    //监听随机按钮
//    public void ran(View v){
//        doSearch(RANDOM);
//    }
    @Override
    protected void onResume()
    {
        super.onResume();
        sensorManager.registerListener(this,
                sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER), SensorManager.SENSOR_DELAY_NORMAL);
    }
    @Override
    protected void onPause()
    {
        super.onPause();
        sensorManager.unregisterListener(this);
    }
    @Override
    public void onSensorChanged(SensorEvent event) {
        int sensorType = event.sensor.getType();
        //values[0]:X轴，values[1]：Y轴，values[2]：Z轴
        float[] values = event.values;
        if (sensorType == Sensor.TYPE_ACCELEROMETER){
            if ((Math.abs(values[0]) > 19 || Math.abs(values[1]) > 19 || Math.abs(values[2]) > 19)){
                doSearch(RANDOM);
                //摇动手机后，再伴随震动提示~~
                vibrator.vibrate(500);
            }
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        //传感器精度改变回调
    }

    class SearchOnItemClickListener implements AdapterView.OnItemClickListener{
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        }
    }
    private void init() {
        sv = (SearchView) findViewById(R.id.search_sv);
        lv = (ListView) findViewById(R.id.search_lv);
        searchRandom = (ImageView) findViewById(R.id.search_randomImage);
        search_text = (TextView) findViewById(R.id.search_randomMen);
//        /* ---------------以下为测试------------------  */
//        p_list.add(new Contacts("张三111111111","zhangsan"));
//        p_list.add(new Contacts("张三22222222222222","zhangsan"));
//        p_list.add(new Contacts("张三33333","zhangsan"));
//        p_list.add(new Contacts("张三4444444","zhangsan"));
//        p_list.add(new Contacts("张三5","zhangsan"));
//        /* ---------------以上为测试------------------  */
        //初始化适配器，并且设置适配器
        sa = new SearchAdapter(SearchActivity.this,p_list);
        lv.setAdapter(sa);
        lv.setTextFilterEnabled(true);
        sv.setSubmitButtonEnabled(true);
        sv.isSubmitButtonEnabled();
        sv.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                /**
                 * 当点击搜索按钮,输入法搜索按钮,会触发这个方法.在这里做相应的搜索事件
                 * query为用户输入的值
                 * 当输入框为空或者""时,此方法没有被调用
                 * */
                Toast.makeText(SearchActivity.this, "你点击了搜索按钮" + query, Toast.LENGTH_SHORT).show();
                doSearch(query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                //用作实时搜索
                //当输入的文字发生变化的时候,会触发这个方法.在这里做匹配提示的操作等
                if(TextUtils.isEmpty(newText)){
                    Toast.makeText(SearchActivity.this, "现在空了", Toast.LENGTH_SHORT).show();
                    Message mes = new Message();
                    mes.what = SEA_NULL;
                    handler.sendMessage(mes);
                }
                return true;
            }
        });
    }
}
