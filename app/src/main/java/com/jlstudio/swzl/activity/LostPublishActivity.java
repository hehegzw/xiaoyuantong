package com.jlstudio.swzl.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
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
import com.jlstudio.swzl.bean.lostfound;
import com.jlstudio.weather.util.ActivityControl;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;


public class LostPublishActivity extends BaseActivity implements View.OnClickListener{
    private Spinner spinner_academy;
    private Spinner spinner_type;
    private TextView title_name,back;//title 标题
    private RelativeLayout rela_xuehao;
    private RelativeLayout rela_name;
    private RelativeLayout rela_phone;
    private RelativeLayout line_diy;
    private RelativeLayout line_diy_content;
    private ImageView imageView;
    private CheckBox checkBoxfound;
    private CheckBox checkBoxlost;
    private Button submit;//提交按钮
    private Button cancle;//取消按钮
    private EditText editText_xuehao;//填写的一卡通通号
    private EditText editText_name;//填写的姓名
    private EditText editText_phone;//填写的手机号
    private EditText editText_content;//填写的自定义具体内容
    private EditText editText_title;//填写的标题

    private lostfound lf  = new lostfound();//对提交该失物招领信息的定义
    private String content  = "未填写";//发布的详细信息
    private String title = "未填写";//发布的标题
    private String academy;//系别
    private View phone;//联系电话
    private String name;//真实姓名
    private String number;//一卡通号
    private int flag = 0;//0代表选择一卡通，1代表其他
    private int flag2;//0代表失物，1代表招领
    private String UPLOAD_URL = Config.URL + Config.UPLOADLOST;//上传失物招领的URL
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lost_publish);
        initView();
        event();

        //系别选择的事件监听
        spinner_academy.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {
                //获取点击的系别
                academy = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // TODO Auto-generated method stub
            }
        });
        //类型的项的监听事件
        spinner_type.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {
                String str = parent.getItemAtPosition(position).toString();
                if (position == 0) {
                    //选择了一卡通
                    rela_xuehao.setVisibility(View.VISIBLE);
                    rela_name.setVisibility(View.VISIBLE);
                    rela_phone.setVisibility(View.VISIBLE);
                    line_diy.setVisibility(View.GONE);
                    imageView.setVisibility(View.INVISIBLE);
                    line_diy_content.setVisibility(View.GONE);
                    flag = 0;
                } else {
                    //选择了其他
                    title = "";
                    content = "";
                    rela_xuehao.setVisibility(View.GONE);
                    rela_name.setVisibility(View.GONE);
                    rela_phone.setVisibility(View.GONE);
                    line_diy.setVisibility(View.VISIBLE);
                    imageView.setVisibility(View.VISIBLE);
                    line_diy_content.setVisibility(View.VISIBLE);
                    flag = 1;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // TODO Auto-generated method stub
            }
        });

    }
    private void event() {
        submit.setOnClickListener(this);
        cancle.setOnClickListener(this);
    }

    private void initView() {
        spinner_academy = (Spinner) findViewById(R.id.lost_found_publish_academyspinner);
        spinner_type = (Spinner) findViewById(R.id.lost_found_publish_typespinner);
        rela_phone = (RelativeLayout) findViewById(R.id.lost_found_phone);
        rela_name = (RelativeLayout) findViewById(R.id.lost_found_name);
        rela_xuehao = (RelativeLayout) findViewById(R.id.lost_found_xuehao);
        line_diy = (RelativeLayout) findViewById(R.id.lost_found_publish_diy_title);
        imageView = (ImageView) findViewById(R.id.lost_found_publish_pic);
        line_diy_content = (RelativeLayout) findViewById(R.id.lost_found_publish_diy_content);
        checkBoxfound = (CheckBox) findViewById(R.id.lost_found_publish_checkbox_found);
        checkBoxlost = (CheckBox) findViewById(R.id.lost_found_publish_checkbox_lost);
        editText_xuehao = (EditText) findViewById(R.id.lost_found_publish_xuehao);
        editText_name = (EditText) findViewById(R.id.lost_found_publish_name);
        editText_phone = (EditText) findViewById(R.id.lost_found_publish_phone);
        editText_content = (EditText) findViewById(R.id.lost_found_publish_content);
        editText_title = (EditText) findViewById(R.id.lost_found_publish_title);
        back = (TextView) findViewById(R.id.back);
        title_name = (TextView) findViewById(R.id.title_name);
        Typeface iconfont = Typeface.createFromAsset(getAssets(),"fonts/iconfont.ttf");
        back.setTypeface(iconfont);
        title_name.setText("发布");
        back.setOnClickListener(this);
        checkBoxlost.setOnClickListener(this);
        checkBoxfound.setOnClickListener(this);
        submit = (Button) findViewById(R.id.lost_found_publish_submit);
        cancle = (Button) findViewById(R.id.lost_found_publish_cancel);
    }
    @Override
    public void onClick(View v) {
            switch (v.getId()) {
                case R.id.lost_found_publish_submit:
                    //初始化失物招领对象内容
                    if(!judge()){
                        lf = creatLostfound();
                        //执行发送操作
                        LostAndFound_publish(this, UPLOAD_URL, lf);
                    }
                    break;
                case R.id.lost_found_publish_cancel:
                    //如果点击取消直接清除当前activity
                    ActivityControl.removeAty(this);
                    break;
                case R.id.lost_found_publish_pic:
                    break;
                case R.id.back:
                    //如果点击取消直接清除当前activity
                    ActivityControl.removeAty(this);
                    break;
                case R.id.lost_found_publish_checkbox_found:
                    if (checkBoxlost.isChecked()) {
                        checkBoxlost.setChecked(false);
                    }
                    break;
                case R.id.lost_found_publish_checkbox_lost:
                    if (checkBoxfound.isChecked()) {
                        checkBoxfound.setChecked(false);
                    }
                    break;
            }
    }

    private boolean judge() {
        boolean a = false;
        if (checkBoxlost.isChecked() == checkBoxfound.isChecked()) {
            Toast.makeText(this, "必须选择失物或招领其中一个！", Toast.LENGTH_SHORT).show();

            a = true;
        }
        if(flag == 1){
            if (editText_title.getText().toString().equals("") || editText_content.getText().toString().equals("")) {
                Toast.makeText(this, "必须填写标题和内容！", Toast.LENGTH_SHORT).show();
                a = true;
            }
        }
        if(flag == 0){
            if (editText_phone.getText().toString().equals("") || editText_name.getText().toString().equals("")) {
                Toast.makeText(this, "手机姓名必须填写", Toast.LENGTH_SHORT).show();
                a = true;
            }
        }
        return a;
    }

    //初始化失物招领对象
    private lostfound creatLostfound() {
        lostfound l = new lostfound();
        if(flag == 0) {
            //如果是一卡通，将一卡通号，系别，手机号和姓名拼成一个内容
            //将失物招领标题就设置为一卡通
            title = "一卡通";
            content = "";
            content += editText_xuehao.getText().toString();
            content += editText_name.getText().toString();
            content += editText_phone.getText().toString();
        }
        else{
            //选择其他的时候
            content  = editText_content.getText().toString();
            title = editText_title.getText().toString();
        }
        l.setTitle(title);//设置失物招领标题
        l.setDescribe(content);//设置失物招领内容
        //*******************************************************
        //*******************************************************
        l.setOwnerId(Integer.parseInt(Config.loadUser(this).getUsername()));//设置当前用户的id
        l.setIntegral(0);//设置悬赏积分
        //0代表失物，1代表招领
        if(checkBoxlost.isChecked())
        {
            flag2 = 0;
        }
        else
            flag2 = 1;
        l.setFlag(flag2);
        Log.d("test2","么么哒——————>"+flag);
        return l;
    }
    /**
     * 用作失物招领的发布
     * @param context
     * @param httpurl
     * @param lostf
     */
    private boolean LostAndFound_publish(final Context context, String httpurl, lostfound lostf) {

        RequestQueue requestQueue = Volley.newRequestQueue(context);
        final lostfound lf = lostf;

        StringRequest stringRequest = new StringRequest(Request.Method.POST, httpurl,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        //将Json字符串转化为集合
                        //将集合存储到工具类
                        Log.d("LostPublish", "response -> 访问成功了"+s);
                        Toast.makeText(context,"发布成功",Toast.LENGTH_SHORT).show();
                        //关闭当前界面
                        ActivityContror.removeActivity((Activity)context);
                        //启动一个新界面
                        startActivity(new Intent(context, LostAndFound.class));
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Log.d("LostPublish", "response -> 访问失败了！！！！" + volleyError);
                Toast.makeText(context,"连接服务器失败",Toast.LENGTH_SHORT).show();
            }
        }

        ) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> map1 = new HashMap<String,String>();
                JSONObject map = new JSONObject();
                try {
                    map.put("title",lf.getTitle());
                    map.put("describe",lf.getDescribe());
                    map.put("userid",lf.getOwnerId());
                    map.put("flag",lf.getFlag());
                    map.put("integral",lf.getIntegral());
                    //上传图片
//                   map.put("pic","lf.getPic()");
                }catch (Exception e){

                }
                map1.put("parameter",map.toString());
                return map1;
            }
        };
        requestQueue.add(stringRequest);
        return true;
    }
}
