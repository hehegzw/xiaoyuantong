package com.jlstudio.publish.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.jlstudio.R;
import com.jlstudio.publish.adapter.ContactsGroupListViewAdapter;
import com.jlstudio.publish.bean.Contacts;

import java.util.ArrayList;
import java.util.List;

public class MyContactsGroupAty extends Activity {

    private ListView contacts_listview;
    private List<Contacts> contactsList = new ArrayList<>();
    private LinearLayout layout_add_group;
    private LinearLayout layout_show_no_group;
    private ContactsGroupListViewAdapter contactsGroupListViewAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mycontacts_group);
        initView();
        initEvent();
        showList();
    }

    private void initEvent() {
        layout_add_group.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAddDialog();
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
                if (et_groupname.getText().toString().isEmpty()) {
                    Toast.makeText(MyContactsGroupAty.this, "请输入组名", Toast.LENGTH_SHORT).show();
                } else {
                    setData(et_groupname.getText().toString());
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
     * 给listview添加数据
     *
     * @param groupname
     */
    private void setData(String groupname) {
        Contacts contacts = new Contacts();
        contacts.setGroupname(groupname);
        contactsList.add(contacts);
        contactsGroupListViewAdapter.onDataChanged(contactsList);
        contacts_listview.setVisibility(View.VISIBLE);
        layout_show_no_group.setVisibility(View.GONE);
    }

    private void showList() {
        contactsGroupListViewAdapter = new ContactsGroupListViewAdapter(MyContactsGroupAty.this, contactsList);
        contacts_listview.setAdapter(contactsGroupListViewAdapter);
    }

    private void initView() {
        contacts_listview = (ListView) findViewById(R.id.contacts_listview);
        layout_add_group = (LinearLayout) findViewById(R.id.layout_add_group);
        layout_show_no_group = (LinearLayout) findViewById(R.id.layout_show_no_group);
        if (contactsList == null) {
            contacts_listview.setVisibility(View.GONE);
        }
    }

    public void doBack(View v) {
        finish();
    }
}
