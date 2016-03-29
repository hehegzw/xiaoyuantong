package com.jlstudio.group.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.jlstudio.R;
import com.jlstudio.group.adapter.ContactsGroupListViewAdapter;
import com.jlstudio.group.bean.Contacts;
import com.jlstudio.group.bean.Groups;
import com.jlstudio.main.application.Config;
import com.jlstudio.main.bean.CatchData;
import com.jlstudio.main.db.DBOption;
import com.jlstudio.main.net.GetDataNet;
import com.jlstudio.main.util.ProgressUtil;
import com.jlstudio.publish.util.JsonToPubhlishData;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ContactsGroupActivity extends Activity {

    private ListView contacts_listview;
    private List<Groups> contactsList = new ArrayList<>();
    private LinearLayout layout_add_group;
    private LinearLayout layout_delete;
    private LinearLayout layout_show_no_group;
    private ContactsGroupListViewAdapter contactsGroupListViewAdapter;
    private TextView tv_contacts_title_bar;
    private TextView tv_icon_add;
    private TextView tv_icon_delete;
    private DBOption db;
    private CatchData catchData;
    private String dataStr;//从服务器接收的数据
    private String new_groupName;
    private GetDataNet gn;//网络
    private String[] choose_item = {"重命名", "删除"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contacts_group);
        db = new DBOption(ContactsGroupActivity.this);
        gn = GetDataNet.getInstence(ContactsGroupActivity.this);
        initView();
        //showList();
        initEvent();
    }

    /**
     * 初始化监听事件
     */
    private void initEvent() {
        layout_add_group.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAddDialog();
            }
        });
        contacts_listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.i("Test", "查看分组 " + contactsList.get(position).getGroup_name());
                Intent intent = new Intent(ContactsGroupActivity.this, ContactsActivity.class);
                intent.putExtra("flag", contactsList.get(position).getGroup_name());
                intent.putExtra("choose", "0");
                startActivity(intent);
            }
        });
        contacts_listview.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                showChooseDialog(position);
                return true;
            }
        });
        layout_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ContactsGroupActivity.this, DeleteGroupActivity.class);
                intent.putExtra("group", dataStr);
                startActivity(intent);
            }
        });
    }

    /**
     * 显示新建分组对话框
     */
    private void showAddDialog() {
        LayoutInflater inflater = LayoutInflater.from(this);
        View view = inflater.inflate(R.layout.dialog_send_msg, null);
        final EditText et_groupname = (EditText) view.findViewById(R.id.et_send_msg);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("新建分组");
        builder.setView(view);
        builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Groups contacts = new Groups();
                new_groupName = et_groupname.getText().toString();
                if (new_groupName.isEmpty()) {
                    Toast.makeText(ContactsGroupActivity.this, "请输入分组名!", Toast.LENGTH_SHORT).show();
                } else {
                    contacts.setGroup_name(new_groupName);
                    if (ifExist(contactsList, new_groupName)) {
                        Toast.makeText(ContactsGroupActivity.this, "分组名不能重复!", Toast.LENGTH_SHORT).show();
                    } else {
                        setSendJson(new_groupName);
                        setData(new_groupName);
                    }
                }
            }
        });
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    /**
     * 显示选择对话框（长按条目选择）
     *
     * @param position
     */
    private void showChooseDialog(final int position) {
        AlertDialog dialog = new AlertDialog.Builder(ContactsGroupActivity.this).setTitle("请选择").setItems(choose_item, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case 0:
                        showRenameDialog(position);
                        break;
                    case 1:
                        showConfirmDialog(position);
                        break;
                }
            }
        }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        }).show();
    }

    /**
     * 显示确认对话框
     *
     * @param position
     */
    private void showConfirmDialog(final int position) {
        AlertDialog dialog = new AlertDialog.Builder(ContactsGroupActivity.this).setTitle("确认删除?").setMessage("确认删除此分组?").setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                ProgressUtil.showProgressDialog(ContactsGroupActivity.this, "请稍后...");
                List<String> list = new ArrayList<>();
                list.add(contactsList.get(position).getGroup_name());
                contactsGroupListViewAdapter.notifyDataSetChanged();
                contactsList.remove(position);
                setSendDeleteJson(list.toString());
            }
        }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        }).show();
    }

    /**
     * 显示重命名对话框
     *
     * @param position
     */
    private void showRenameDialog(final int position) {
        View view = LayoutInflater.from(ContactsGroupActivity.this).inflate(R.layout.dialog_send_msg, null);
        final EditText et_groupname = (EditText) view.findViewById(R.id.et_send_msg);
        et_groupname.setText(contactsList.get(position).getGroup_name());

        InputMethodManager inputMethodManager = (InputMethodManager) et_groupname.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.toggleSoftInput(0, InputMethodManager.SHOW_FORCED);
        et_groupname.setSelectAllOnFocus(true);
        et_groupname.setHighlightColor(getResources().getColor(R.color.holo_blue_bright));

        AlertDialog dialog = new AlertDialog.Builder(ContactsGroupActivity.this).setTitle("重命名").setView(view).setPositiveButton("确认", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (ifExist(contactsList, et_groupname.getText().toString())) {
                    Toast.makeText(ContactsGroupActivity.this, "分组名不能重复!", Toast.LENGTH_SHORT).show();
                } else {
                    ProgressUtil.showProgressDialog(ContactsGroupActivity.this, "请稍后...");
                    setSendRenameJson(contactsList.get(position).getGroup_name(), et_groupname.getText().toString());
                }
            }
        }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        }).show();
    }

    /**
     * 给listview添加数据
     *
     * @param groupname
     */
    private void setData(String groupname) {
        Groups contacts = new Groups();
        contacts.setGroup_name(groupname);
        contactsList.add(contacts);
        contactsGroupListViewAdapter.onDataChanged(contactsList);
        contacts_listview.setVisibility(View.VISIBLE);
        layout_show_no_group.setVisibility(View.GONE);
    }

    /**
     * 显示ListView
     */
    private void showList() {
        dataStr = catchData.getContent();
        contactsList = JsonToPubhlishData.getGroup(dataStr);
        if (contactsList != null) {
            layout_show_no_group.setVisibility(View.GONE);
            contactsGroupListViewAdapter = new ContactsGroupListViewAdapter(ContactsGroupActivity.this, contactsList, contacts_listview);
            contacts_listview.setAdapter(contactsGroupListViewAdapter);
        }
    }

    /**
     * 初始化控件
     */
    private void initView() {
        contacts_listview = (ListView) findViewById(R.id.contacts_listview);
        layout_add_group = (LinearLayout) findViewById(R.id.layout_add_group);
        layout_delete = (LinearLayout) findViewById(R.id.layout_delete);
        layout_show_no_group = (LinearLayout) findViewById(R.id.layout_show_no_group);
        tv_icon_add = (TextView) findViewById(R.id.tv_icon_add);
        tv_icon_delete = (TextView) findViewById(R.id.tv_icon_delete);
        Typeface iconfont = Typeface.createFromAsset(getAssets(), "fonts/my_iconfont.ttf");
        tv_icon_add.setTypeface(iconfont);
        tv_icon_delete.setTypeface(iconfont);
        tv_contacts_title_bar = (TextView) findViewById(R.id.tv_contacts_title_bar);
        tv_contacts_title_bar.setText("我的分组");
    }

    /**
     * 返回
     *
     * @param v
     */
    public void doBack(View v) {
        finish();
    }

    /**
     * 向服务器发送添加分组的数据
     *
     * @param sendJson
     */
    public void addServiceGroup(String sendJson, String action) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        final String time = sdf.format(new Date());

        gn.getData(Config.URL, action, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                DBOption db = new DBOption(ContactsGroupActivity.this);
                db.updateCatch(Config.URL+"groupname"+Config.loadUser(ContactsGroupActivity.this).getUsername(), s, time);
                dataStr = s;
                contactsList = JsonToPubhlishData.getGroup(dataStr);
                contactsGroupListViewAdapter.notifyDataSetChanged();
                ProgressUtil.closeProgressDialog();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Log.i("Test", "网络错误!");
                ProgressUtil.closeProgressDialog();
            }
        }, sendJson);
    }

    /**
     * 将需要发送的信息打包成json字符串（添加分组）
     *
     * @param groupName
     */
    private void setSendJson(String groupName) {
        ProgressUtil.showProgressDialog(this, "请稍后...");
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("username", Config.loadUser(ContactsGroupActivity.this).getUsername());
            jsonObject.put("group", groupName);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        addServiceGroup(jsonObject.toString(), "addgroup");
    }

    private void setSendDeleteJson(String groupName) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("username", Config.loadUser(ContactsGroupActivity.this).getUsername());
            jsonObject.put("group", groupName);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        addServiceGroup(jsonObject.toString(), "deletegroup");
    }

    private void setSendRenameJson(String oldGroup, String newGroup) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("username", Config.loadUser(ContactsGroupActivity.this).getUsername());
            jsonObject.put("oldgroup", oldGroup);
            jsonObject.put("newgroup", newGroup);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.i("Test", "重命名" + jsonObject.toString());
        addServiceGroup(jsonObject.toString(), "renamegroup");
    }

    /**
     * 判断集合里是否有某个条目
     *
     * @param list
     * @param group
     * @return
     */
    public boolean ifExist(List<Groups> list, String group) {
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).getGroup_name().equals(group))
                return true;
        }
        return false;
    }

    @Override
    protected void onResume() {
        super.onResume();
        initData();
    }

    private void initData() {
        catchData = db.getCatch(Config.URL+"groupname"+Config.loadUser(this).getUsername());
        if(catchData!=null){
            showList();
        }else{
            ProgressUtil.showProgressDialog(this,"数据加载...");
            getGroupFromNet();
        }
    }

    private void getGroupFromNet(){
        JSONObject json = new JSONObject();
        try {
            json.put("username",Config.loadUser(this).getUsername());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        gn.getData(Config.URL,"groupname",new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                if(catchData == null){
                    db.insertCatch(Config.URL+"groupname"+Config.loadUser(ContactsGroupActivity.this).getUsername(),s,"00");
                }else{
                    db.updateCatch(Config.URL + "groupname" + Config.loadUser(ContactsGroupActivity.this).getUsername(), s, "00");
                }
                showList();
//                dataStr = s;
//                contactsList = JsonToPubhlishData.getGroup(dataStr);
//                contactsGroupListViewAdapter.notifyDataSetChanged();
                ProgressUtil.closeProgressDialog();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Log.i("Test", "网络错误!");
                ProgressUtil.closeProgressDialog();
            }
        }, json.toString());
    }
}
