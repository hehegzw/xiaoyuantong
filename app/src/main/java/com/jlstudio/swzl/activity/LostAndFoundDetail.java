package com.jlstudio.swzl.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
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
import com.jlstudio.main.activity.BaseActivity;
import com.jlstudio.main.application.ActivityContror;
import com.jlstudio.main.application.Config;
import com.jlstudio.main.bean.CatchData;
import com.jlstudio.main.db.DBOption;
import com.jlstudio.swzl.adapter.LostAndFoundDetailAdapter;
import com.jlstudio.swzl.bean.commons;
import com.jlstudio.swzl.bean.lostfound;
import com.jlstudio.swzl.httpNet.ChangeJson;
import com.jlstudio.swzl.httpNet.util;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class LostAndFoundDetail extends BaseActivity implements View.OnClickListener{
    private TextView firstcontent;
    private TextView firstnickname;
    private TextView firsttime;
    private ListView common_lv;
    private TextView title_name;
    private Button button_sent;//发送评论按钮
    private EditText editText_common;//评论
    private TextView back;
    private LostAndFoundDetailAdapter adapter;
    private ArrayList<commons> commons_list = new ArrayList<commons>();//评论的集合
    private lostfound lf;
    private commons common = new commons();
    final Context context = this;
    private DBOption dbOption;
    private String GetListCommons = Config.URL + Config.GETLISTCOMMONS;//获取全部评论的URL
    private String UPCOMMON_URL = Config.URL + Config.UPCOMMON;//上传一条评论的URL
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lost_and_found_detail);
        //获取到上个页面传过来的参数
        Intent intent = this.getIntent();
        //获取该条目的对象
        lf=(lostfound)intent.getSerializableExtra("lostandfound");
        initData();
        initView();

    }

    private void initData() {
        //*****初始化评论列表数据****
        //申请异步线程，进行数据加载
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        dbOption = new DBOption(this);
        CatchData cd = dbOption.getCatch(GetListCommons);
            StringRequest stringRequest = new StringRequest(Request.Method.POST, GetListCommons,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String s) {
                            //将返回的json字符串添加到数据库中缓存
                            dbOption.updateCatch(GetListCommons, s, util.getcurTime());
                            //将Json字符串转化为集合
                            //将集合存储到工具类
                            commons_list = ChangeJson.C_changeJsonToArrayDate(s);
                            Log.d("LostAndFoundDetail", "访问数据库成功了！----->"+ s);
                            initView();
                            adapter.notifyDataSetChanged();

                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError volleyError) {
                    Log.d("LostAndFoundDetail", "respOutonse -> 访问失败了！！！！" + volleyError);
                }
            }

            ) {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> map = new HashMap<String, String>();
                    JSONObject json = new JSONObject();
                    //将对应的失物招领信息id传入
                    try {
                        json.put("lfid", String.valueOf(lf.getId_lostfound()));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    map.put("parameter",json.toString());
                    return map;
                }
            };

            requestQueue.add(stringRequest);
    }

    private void initView() {
        firstcontent = (TextView) findViewById(R.id.lost_found_first_detail_content);
        firstnickname = (TextView) findViewById(R.id.lost_found_first_detail_nickname);
        firsttime = (TextView) findViewById(R.id.lost_found_first_detail_time);
        common_lv = (ListView) findViewById(R.id.lost_found_detail_list);
        editText_common = (EditText) findViewById(R.id.lost_found_detail_edit);
        button_sent = (Button) findViewById(R.id.lost_found_detail_button);
        back = (TextView) findViewById(R.id.back);
        //设置本页一楼的信息
        firstcontent.setText(lf.getDescribe());
        firstnickname.setText(lf.getNickname());
        firsttime.setText(lf.getTime());
        //设置本页标题
        title_name = (TextView) findViewById(R.id.title_name);
        title_name.setText(lf.getTitle());
        //设置本页返回按钮
        Typeface iconfont = Typeface.createFromAsset(getAssets(),"fonts/iconfont.ttf");
        back.setTypeface(iconfont);
        //设置监听事件
        back.setOnClickListener(this);
        button_sent.setOnClickListener(this);
        //声明适配器对象
        adapter = new LostAndFoundDetailAdapter(commons_list,this);
        //设置适配器
        common_lv.setAdapter(adapter);
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back:
                ActivityContror.removeActivity(this);
                break;
            case R.id.lost_found_detail_button:
                if (!editText_common.getText().toString().equals(""))
                {
                    String s = editText_common.getText().toString();
                    editText_common.setText("");
                    //设置评论内容
                    common.setContent(s);
                    //设置当前用户的id
                    common.setUserid(Config.loadUser(this).getUsername());
                    //设置被评论的条目的id
                    common.setFor_which(String.valueOf(lf.getId_lostfound()));
                    //先让绑定的集合发生改变
                    commons_list.add(common);
                    adapter.notifyDataSetChanged();
                    //设置一个列表刷新
                    //执行一个发送操作
                    RequestQueue requestQueue = Volley.newRequestQueue(this);
                    StringRequest stringRequest = new StringRequest(Request.Method.POST, UPCOMMON_URL,
                            new Response.Listener<String>() {
                                @Override
                                public void onResponse(String s) {
                                    //将gson字符串转化为集合
                                    //将集合存储到工具类
                                    Log.d("CommonSend", "response -> 访问chengong了！！！！");
                                    Toast.makeText(context,"评论成功",Toast.LENGTH_SHORT).show();
                                    adapter.notifyDataSetChanged();
                                    //取回适配器对象,并且刷新列表
                                }
                            }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError volleyError) {
                            Log.d("CommonSend", "response -> 访问失败了！！！！" + volleyError);
                            Toast.makeText(context,"评论失败",Toast.LENGTH_SHORT).show();
                        }
                    }

                    ) {
                        @Override
                        protected Map<String, String> getParams() throws AuthFailureError {
                            Map<String,String> map1 = new HashMap<String,String>();
                            JSONObject map = new JSONObject();
                            try {
                                map.put("lfid",common.getFor_which());
                                map.put("lfctext",common.getContent());
                                map.put("userid",common.getUserid());
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            map1.put("parameter",map.toString());
                            return map1;
                        }
                    };
                    requestQueue.add(stringRequest);

                }
                else{
                    Toast.makeText(this,"评论不能为空",Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }


}
