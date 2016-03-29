package com.jlstudio.group.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.View;
import android.widget.AdapterView;
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

/**
 * 删除分组
 */
public class DeleteGroupActivity extends Activity {

    private String groupStr;
    private ListView contacts_listview;
    private TextView tv_icon_delete;
    private List<Groups> contactsList = new ArrayList<>();
    private LinearLayout layout_delete;
    private ContactsGroupListViewAdapter contactsGroupListViewAdapter;
    private TextView mSelectedCount;
    private List<String> deleteGroups = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delete_group);
        getData();
        initView();
        showList();
    }

    private void initEvent() {
        contacts_listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                contactsGroupListViewAdapter.notifyDataSetChanged();
                updateSeletedCount();
            }
        });
        layout_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                doDeleteGroup();
                if (deleteGroups.isEmpty()) {
                    Toast.makeText(DeleteGroupActivity.this, "请选择分组", Toast.LENGTH_SHORT).show();
                } else {
                    showDialog();
                }
            }
        });
    }

    private void showDialog() {
        AlertDialog dialog = new AlertDialog.Builder(DeleteGroupActivity.this).setTitle("确认删除所选分组?").setPositiveButton("确认", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                setSendJson(deleteGroups.toString());
            }
        }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        }).show();
    }

    private void showList() {
        contactsList = JsonToPubhlishData.getGroup(groupStr);
        if (contactsList != null) {
            contactsGroupListViewAdapter = new ContactsGroupListViewAdapter(DeleteGroupActivity.this, contactsList, contacts_listview);
            contacts_listview.setAdapter(contactsGroupListViewAdapter);
            initEvent();
            contacts_listview.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
        }
    }

    private void initView() {
        contacts_listview = (ListView) findViewById(R.id.contacts_listview);
        layout_delete = (LinearLayout) findViewById(R.id.layout_delete);
        tv_icon_delete = (TextView) findViewById(R.id.tv_icon_delete);
        mSelectedCount = (TextView) findViewById(R.id.tv_show_delete_num);
        Typeface iconfont = Typeface.createFromAsset(getAssets(), "fonts/my_iconfont.ttf");
        tv_icon_delete.setTypeface(iconfont);
    }

    public void getData() {
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        groupStr = bundle.getString("group");
    }

    public void updateSeletedCount() {
        if (contacts_listview.getCheckedItemCount() == 0) {
            mSelectedCount.setVisibility(View.GONE);
            layout_delete.setClickable(false);
            layout_delete.setBackgroundColor(getResources().getColor(R.color.darker_gray));
        } else {
            mSelectedCount.setVisibility(View.VISIBLE);
            layout_delete.setClickable(true);
            layout_delete.setBackgroundColor(getResources().getColor(R.color.holo_blue_bright));
        }
        mSelectedCount.setText("(" + Integer.toString(contacts_listview.getCheckedItemCount()) + ")");
    }

    public void doBack(View v) {
        DeleteGroupActivity.this.finish();
    }

    private void doDeleteGroup() {
        SparseBooleanArray sbl = contacts_listview.getCheckedItemPositions();
        for (int i = 0; i < contactsList.size(); i++) {
            if (sbl.get(i)) {
                deleteGroups.add(contactsList.get(i).getGroup_name());
            }
        }
        Log.i("Test", deleteGroups.toString());
    }

    public void setSendJson(String groupName) {
        ProgressUtil.showProgressDialog(this, "请稍后...");
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("username", Config.loadUser(DeleteGroupActivity.this).getUsername());
            jsonObject.put("groups", groupName);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.i("Test", "删除分组..." + jsonObject.toString());
        addServiceGroup(jsonObject.toString());
    }

    public void addServiceGroup(String sendJson) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        final String time = sdf.format(new Date());
        GetDataNet getDateNet = GetDataNet.getInstence(DeleteGroupActivity.this);
        getDateNet.getData(Config.URL, "deletegroup", new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                    Log.i("Test", "删除分组成功!-->" + s);
                    DBOption db = new DBOption(DeleteGroupActivity.this);
                    db.updateCatch(Config.URL+"groupname"+Config.loadUser(DeleteGroupActivity.this).getUsername(), s, time);
                    groupStr = s;
                    showList();
                    ProgressUtil.closeProgressDialog();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Log.i("Test", "网络错误!");
                Toast.makeText(DeleteGroupActivity.this, "删除失败", Toast.LENGTH_SHORT).show();
                ProgressUtil.closeProgressDialog();
            }
        }, sendJson);
    }
}
