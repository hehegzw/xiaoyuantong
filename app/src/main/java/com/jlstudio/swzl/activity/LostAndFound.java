package com.jlstudio.swzl.activity;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.jlstudio.R;
import com.jlstudio.main.activity.BaseActivity;
import com.jlstudio.main.activity.MainActivity;
import com.jlstudio.main.application.ActivityContror;
import com.jlstudio.main.application.Config;
import com.jlstudio.main.bean.CatchData;
import com.jlstudio.main.db.DBOption;
import com.jlstudio.main.util.ProgressUtil;
import com.jlstudio.swzl.adapter.LostAndFoundAdapter;
import com.jlstudio.swzl.bean.lostfound;
import com.jlstudio.swzl.httpNet.ChangeJson;
import com.jlstudio.swzl.httpNet.util;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class LostAndFound extends BaseActivity implements OnClickListener, AdapterView.OnItemClickListener {
    private ArrayList<lostfound> lostfounds = new ArrayList<>();
    private ListView listView;
    private DBOption dbOption;
    private TextView back,title_name,write_data;
    private String GetListLost_URL = Config.URL + Config.GETLISTLOST;//获取全部失物招领信息的URL

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lost_and_found);
        initData();
        initView();
    }

    private void initData() {
        //申请异步线程，进行数据加载
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        dbOption = new DBOption(this);
        CatchData cd = dbOption.getCatch(GetListLost_URL);
        //判断数据库是否数据过期
        if (util.IsOutTime(dbOption, cd, GetListLost_URL)) {
            ProgressUtil.showProgressDialog(this, "数据加载...");
            StringRequest stringRequest = new StringRequest(Request.Method.POST, GetListLost_URL,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String s) {
                            //将返回的json字符串添加到数据库中缓存
                            dbOption.updateCatch(GetListLost_URL, s, util.getcurTime());
                            //将Json字符串转化为集合
                            //将集合初始化
                            lostfounds = ChangeJson.L_changeJsonToArrayDate(s);
                            //初始化界面
                            initView();
                            Log.d("LostAndFound", "访问数据库成功了！----->"+s);
                            ProgressUtil.closeProgressDialog();
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError volleyError) {
                    Log.d("LostAndFound", "respOutonse -> 访问失败了！！！！" + volleyError);
                    ProgressUtil.closeProgressDialog();
                }
            }
            ) {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> map = new HashMap<String, String>();
                    map.put("found", "getallLostAndFound");
                    return map;
                }
            };

            requestQueue.add(stringRequest);
        } else {
            //当数据没有过期时候，进行一个取数据库并转化为集合
            lostfounds = ChangeJson.L_changeJsonToArrayDate(cd.getContent());
            //初始化界面
        }
    }

    private void initView() {
        listView = (ListView) findViewById(R.id.lost_found_all_list);
        back = (TextView) findViewById(R.id.back);
        write_data = (TextView) findViewById(R.id.right_icon);
        title_name = (TextView) findViewById(R.id.title_name);
        Typeface iconfont = Typeface.createFromAsset(getAssets(), "fonts/iconfont.ttf");
        Typeface iconfont1 = Typeface.createFromAsset(getAssets(), "fonts/publish.ttf");
        back.setTypeface(iconfont);
        write_data.setTypeface(iconfont1);
        title_name.setText("失物招领");
        //声明适配器对象
        LostAndFoundAdapter lostAndFoundAdapter = new LostAndFoundAdapter(this, lostfounds);
        listView.setAdapter(lostAndFoundAdapter);
        write_data.setOnClickListener(this);
        back.setOnClickListener(this);
        listView.setOnItemClickListener(this);
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back:
                finish();
                break;
            case R.id.right_icon:
                //不删除掉当前的界面，然后跳转
                startActivity(new Intent(this, LostPublishActivity.class));
                break;
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent = new Intent();
        intent.setClass(this, LostAndFoundDetail.class);
        //将点击的对象传到下一个activity
        Bundle bundle = new Bundle();
        bundle.putSerializable("lostandfound", lostfounds.get(position));
        intent.putExtras(bundle);
        startActivity(intent);
    }
}
