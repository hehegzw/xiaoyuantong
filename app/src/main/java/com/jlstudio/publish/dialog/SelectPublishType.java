package com.jlstudio.publish.dialog;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.jlstudio.R;
import com.jlstudio.publish.activity.AddPublishAty;
import com.jlstudio.publish.activity.AddPublishBySMSAty;
import com.jlstudio.publish.adapter.SelectDepartmentadapter;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by gzw on 2015/9/26.
 */
public class SelectPublishType extends Dialog implements View.OnClickListener, AdapterView.OnItemClickListener {
    private Context context;
    private List<String> type;
    private SelectDepartmentadapter adapter;
    private ListView listView;


    public SelectPublishType(Context context) {
        super(context);
        this.context = context;
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_select_person);
        initView();
        getDatas();
        initListView();
    }

    private void getDatas(){
        type = new ArrayList<>();
        type.add("简单通知");
        type.add("会议&公告");
        type.add("短信");
        adapter = new SelectDepartmentadapter(context,type);
    }

    private void initListView() {
        listView = (ListView) findViewById(R.id.listview);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(this);
    }


    private void initView() {
        Window dialogwindow = getWindow();
        TextView title = (TextView) findViewById(R.id.title);
        title.setText("选择消息类型");
        WindowManager.LayoutParams lp = dialogwindow.getAttributes();
        DisplayMetrics dm = context.getResources().getDisplayMetrics();
        lp.width = (int) (dm.widthPixels * 0.8);
        lp.height = (int) (dm.heightPixels * 0.6);
        dialogwindow.setAttributes(lp);
    }

    @Override
    public void onClick(View v) {
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
        switch (position){
            case 0://简单通知
                Intent intent = new Intent(context, AddPublishAty.class);
                intent.setAction("简单通知");
                context.startActivity(intent);
                break;
            case 1://会议通知(可添加附件)
                Intent intent1 = new Intent(context, AddPublishAty.class);
                intent1.setAction("会议&公告");
                context.startActivity(intent1);
                break;
            case 2://短信通知
                Intent intent2 = new Intent(context, AddPublishBySMSAty.class);
                intent2.setAction("短信通知");
                context.startActivity(intent2);
                break;
            default:
        }
        dismiss();
    }
}
