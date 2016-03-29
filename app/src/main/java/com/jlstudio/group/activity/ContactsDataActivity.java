package com.jlstudio.group.activity;

import android.app.AlertDialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.jlstudio.R;
import com.jlstudio.group.bean.Contacts;
import com.jlstudio.group.bean.Groups;
import com.jlstudio.group.dialog.SelectGroup;
import com.jlstudio.group.net.GetDataAction;
import com.jlstudio.main.activity.BaseActivity;
import com.jlstudio.main.application.Config;
import com.jlstudio.main.application.MyApplication;
import com.jlstudio.main.bean.CatchData;
import com.jlstudio.main.db.DBOption;
import com.jlstudio.main.net.DownFace;
import com.jlstudio.main.net.GetDataNet;
import com.jlstudio.main.util.ProgressUtil;
import com.jlstudio.publish.util.JsonToPubhlishData;
import com.jlstudio.publish.util.ShowToast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.net.URI;
import java.security.acl.Group;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 显示联系人详细信息
 */
public class ContactsDataActivity extends BaseActivity implements View.OnClickListener {

    private TextView contacts_back;//返回
    private ImageView iface;//头像
    private TextView contacts_signature;//签名
    private TextView contacts_name;
    private TextView contacts_nickname;//昵称
    private TextView contacts_sex;//姓别
    private TextView contacts_phone;//手机号
    private TextView contacts_grade_name;//班级
    private TextView contacts_dept_name;//系别
    private TextView contacts_qq;//qq
    private TextView contacts_wechat;//微信
    private TextView title_name;
    private TextView callphone;
    private TextView sendmessage;
    private TextView leavemsg;
    private Button moveto;
    private Button deletefriend;

    private String contacts_id;
    private String phoneNumber;
    private String userid;

    private List<String> addContacts = new ArrayList<>();

    private LinearLayout layout_callphone;
    private LinearLayout layout_sendmessage;
    private LinearLayout layout_leavemsg;
    private Typeface iconfont;
    private GetDataNet gn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contacts_data);
        gn = GetDataNet.getInstence(this);
        ProgressUtil.showProgressDialog(this, "正在加载...");
        getData();//从FContact中获取contacts_id
        userid = Config.loadUser(ContactsDataActivity.this).getUsername();
        initView();
        getDatasFromNet();
    }

    private void initEvent() {
        layout_callphone.setOnClickListener(this);
        contacts_back.setOnClickListener(this);
        layout_sendmessage.setOnClickListener(this);
        layout_leavemsg.setOnClickListener(this);
        moveto.setOnClickListener(this);
        deletefriend.setOnClickListener(this);
    }

    private void initView() {

        layout_callphone = (LinearLayout) findViewById(R.id.layout_callphone);
        layout_sendmessage = (LinearLayout) findViewById(R.id.layout_sendmessage);
        layout_leavemsg = (LinearLayout) findViewById(R.id.layout_leavemsg);

        title_name = (TextView) findViewById(R.id.label_friend_name);
        iface = (ImageView) findViewById(R.id.iface);
        contacts_signature = (TextView) findViewById(R.id.contacts_signature);
        contacts_name = (TextView) findViewById(R.id.contacts_name);
        contacts_nickname = (TextView) findViewById(R.id.contacts_nickname);
        contacts_sex = (TextView) findViewById(R.id.contacts_sex);
        contacts_phone = (TextView) findViewById(R.id.contacts_phone);
        contacts_grade_name = (TextView) findViewById(R.id.contacts_grade_name);
        contacts_dept_name = (TextView) findViewById(R.id.contacts_dept_name);
        contacts_qq = (TextView) findViewById(R.id.contacts_qq);
        contacts_wechat = (TextView) findViewById(R.id.contacts_wechat);

        contacts_back = (TextView) findViewById(R.id.contacts_back);
        callphone = (TextView) findViewById(R.id.callphone);
        sendmessage = (TextView) findViewById(R.id.sendmessage);
        leavemsg = (TextView) findViewById(R.id.leavemsg);

        moveto = (Button) findViewById(R.id.moveto);
        deletefriend = (Button) findViewById(R.id.deletefriend);

        iconfont = Typeface.createFromAsset(getAssets(), "fonts/iconfont.ttf");
        contacts_back.setTypeface(iconfont);
        iconfont = Typeface.createFromAsset(getAssets(), "fonts/my_iconfont.ttf");
        leavemsg.setTypeface(iconfont);
        iconfont = Typeface.createFromAsset(getAssets(), "fonts/phonemessage.ttf");
        callphone.setTypeface(iconfont);
        sendmessage.setTypeface(iconfont);
    }

    public void getData() {
        Intent intent = getIntent();
        contacts_id = intent.getAction();
        Log.i("Test", "获取联系人de id = " + contacts_id);
    }


    private void getDatasFromNet() {
        JSONObject json = new JSONObject();
        try {
            json.put("username", contacts_id);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        gn.getData(Config.URL, "profile", new Response.Listener<String>() {

            @Override
            public void onResponse(String s) {
                Log.i("Test", "获取联系人信息" + s);
                prepareData(s);
                initEvent();
                ProgressUtil.closeProgressDialog();
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError volleyError) {
                ProgressUtil.closeProgressDialog();
                ShowToast.show(MyApplication.getContext(), "获取数据失败");
            }
        }, json.toString());
        new DownFace(this).loadImage(Config.URL, "faces/" + contacts_id + ".jpg", new Response.Listener<Bitmap>() {
            @Override
            public void onResponse(Bitmap bitmap) {
                iface.setImageBitmap(bitmap);
            }
        }, 30, 30, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                ShowToast.show(ContactsDataActivity.this, "还没有上传头像奥!");
            }
        });
//        String fileName = "/sdcard/"+contacts_id+".jpg";
//        File file = new File(fileName);
//        if(file.exists()){
//            Bitmap bitmap = BitmapFactory.decodeFile(fileName);
//            iface.setImageBitmap(bitmap);
//        }else{
//            new DownFace(this).loadImage(Config.URL, "faces/" + contacts_id + ".jpg", new Response.Listener<Bitmap>() {
//                @Override
//                public void onResponse(Bitmap bitmap) {
//                    iface.setImageBitmap(bitmap);
//                }
//            }, 30, 30, new Response.ErrorListener() {
//                @Override
//                public void onErrorResponse(VolleyError volleyError) {
//                    ShowToast.show(ContactsDataActivity.this, "还没有上传头像奥!");
//                }
//            });
//        }
    }

    private void prepareData(String s) {
        Log.i("Test", ">" + s);
        try {
            JSONObject jsonObject = new JSONObject(s);
            title_name.setText(jsonObject.getString("userrealname"));
            phoneNumber = jsonObject.getString("userphone");

            contacts_signature.setText(jsonObject.getString("usersignature"));
            contacts_name.setText(jsonObject.getString("userrealname"));
            contacts_nickname.setText(jsonObject.getString("usernickname"));
            contacts_sex.setText(jsonObject.getString("usergender"));
            contacts_phone.setText(jsonObject.getString("userphone"));
            contacts_grade_name.setText(jsonObject.getString("class"));
            contacts_dept_name.setText(jsonObject.getString("department"));
            contacts_qq.setText(jsonObject.getString("userqq"));
            contacts_wechat.setText(jsonObject.getString("userweixin"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * 拨打电话
     *
     */
    public void doPhoneCall() {
        AlertDialog dialog = new AlertDialog.Builder(ContactsDataActivity.this).setTitle("确认").setMessage("确认拨打电话?").setPositiveButton("是", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //获取编辑框内输入的目标电话号码
                Intent intent = new Intent();
                intent.setAction("android.intent.action.CALL");
                intent.addCategory("android.intent.category.DEFAULT");
                //指定要拨打的电话号码
                intent.setData(Uri.parse("tel:" + phoneNumber));
                doDataBase();
                startActivity(intent);
            }
        }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        }).show();

    }

    /**
     * 发送短信
     *
     */
    public void doSendMsg() {
        Uri sms = Uri.parse("smsto:"+phoneNumber);
        Intent intent = new Intent(android.content.Intent.ACTION_SENDTO,sms);
        startActivity(intent);
    }

    private void doDataBase() {
        DBOption db = new DBOption(ContactsDataActivity.this);
        List<Contacts> getContacts_list = db.getRecentContacts(userid);
        Contacts contacts = new Contacts();
        contacts.setContacts_id(contacts_id);
       // contacts.setContacts_name(contacts_name);
        contacts.setUsername(Config.loadUser(ContactsDataActivity.this).getUsername());
        if (getContacts_list != null && ifExist(getContacts_list)) {
            Log.i("Test", "->>>>>>>>>>更新了...");
            db.updateRecentContacts(contacts);
        } else {
            Log.i("Test", "->>>>>>>>>>插入了...");
            db.insertRecentContacts(contacts);
        }
    }

    public boolean ifExist(List<Contacts> list) {
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).getContacts_id().equals(contacts_id))
                return true;
        }
        return false;
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.contacts_back:
                finish();
                break;
            case R.id.layout_leavemsg:
                Intent intent = new Intent(ContactsDataActivity.this, LeaveMsgActivity.class);
                intent.putExtra("contacts_id", contacts_id);
                startActivity(intent);
                break;
            case R.id.layout_callphone:
                doPhoneCall();
                break;
            case R.id.layout_sendmessage:
                doSendMsg();
                break;
            case R.id.moveto:
                new SelectGroup(this, new SelectGroup.CallBack() {
                    @Override
                    public void callback(String groupname) {
                        moveFriend(groupname);
                    }
                }).show();
                break;
            case R.id.deletefriend:
                deleteFriend();
                break;
        }
    }

    /**
     * 移动好友到分组
     * @param group
     */
    private void moveFriend(String group) {
        JSONObject json = new JSONObject();
        String username = Config.loadUser(this).getUsername();
        try {
            json.put("username", username);
            json.put("friend", contacts_id);
            json.put("groupname", group);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        gn.getData(Config.URL, "movefriend", new Response.Listener<String>() {

            @Override
            public void onResponse(String s) {
                ShowToast.show(ContactsDataActivity.this, "移动成功");
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError volleyError) {
                ShowToast.show(ContactsDataActivity.this, "移动失败");
            }
        }, json.toString());
    }
    private void deleteFriend() {
        JSONObject json = new JSONObject();
        String username = Config.loadUser(this).getUsername();
        try {
            json.put("username", username);
            json.put("friend", contacts_id);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        gn.getData(Config.URL, "deletefriend", new Response.Listener<String>() {

            @Override
            public void onResponse(String s) {
                ShowToast.show(ContactsDataActivity.this, "删除成功");
                finish();
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError volleyError) {
                ShowToast.show(ContactsDataActivity.this, "删除失败");
            }
        }, json.toString());
    }
}
