package com.jlstudio.publish.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.jlstudio.R;
import com.jlstudio.main.application.Config;
import com.jlstudio.main.bean.CatchData;
import com.jlstudio.main.db.DBOption;
import com.jlstudio.publish.activity.ShowPersonAty;
import com.jlstudio.publish.adapter.SelectDepartmentadapter;
import com.jlstudio.publish.net.GetNotificationNet;
import com.jlstudio.publish.util.JsonToPubhlishData;
import com.jlstudio.publish.util.ShowToast;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by gzw on 2015/9/26.
 */
public class SelectDepartmentDialog extends Dialog implements View.OnClickListener, AdapterView.OnItemClickListener {
    private Context context;
    private Button btn_submit;
    private List<String> departments;
    private SelectDepartmentadapter adapter;
    private ListView listView;
    private GetNotificationNet gn;
    private DBOption db;
    private CatchData data;
    private String publishType;


    public SelectDepartmentDialog(Context context,String publishType) {
        super(context);
        this.context = context;
        this.publishType = publishType;
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_select_person);
        gn = GetNotificationNet.getInstence(context);
        initView();
        getDatas();
        initListView();
    }

    private void getDatas(){
        departments = new ArrayList<>();
        adapter = new SelectDepartmentadapter(context,departments);
        db = new DBOption(context);
        data = db.getCatch(Config.URL+Config.GETDEPARTMENT);
        if(data == null){
            getDatasFromNet();
        }else{
            Long time = System.currentTimeMillis();
            long catchtime = Long.parseLong(data.getTime());
            if((time - catchtime) > Config.CATCHTIME){
                getDatasFromNet();
            }else{
                departments = JsonToPubhlishData.getDepartment(data.getContent());
                adapter.setList(departments);
            }

        }
    }

    private void initListView() {
        listView = (ListView) findViewById(R.id.listview);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(this);
    }


    private void initView() {
        Window dialogwindow = getWindow();
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
                Intent intent = new Intent(context, ShowPersonAty.class);
                intent.putExtra("department",departments.get(position));
                intent.putExtra("publishType",publishType);
                context.startActivity(intent);
                dismiss();
                ((Activity)context).finish();
    }

    private void getDatasFromNet(){
        gn.getDepartment(Config.URL,Config.GETDEPARTMENT,new Response.Listener<String>(){

            @Override
            public void onResponse(String s) {
                if(s!=null){
                    if(data == null){
                        db.insertCatch(Config.URL+Config.GETDEPARTMENT,s,System.currentTimeMillis()+"");
                    }else{
                        db.updateCatch(Config.URL+Config.GETDEPARTMENT,s,System.currentTimeMillis()+"");
                    }
                    departments = JsonToPubhlishData.getDepartment(s);
                    adapter.setList(departments);
                }
            }
        },new Response.ErrorListener(){

            @Override
            public void onErrorResponse(VolleyError volleyError) {
                ShowToast.show(context, "获取数据失败");
            }
        },"department");
    }

}
