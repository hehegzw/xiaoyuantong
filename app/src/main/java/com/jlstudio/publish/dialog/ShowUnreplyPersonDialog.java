package com.jlstudio.publish.dialog;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.jlstudio.R;
import com.jlstudio.main.application.Config;
import com.jlstudio.main.bean.CatchData;
import com.jlstudio.main.db.DBOption;
import com.jlstudio.main.net.GetDataNet;
import com.jlstudio.publish.adapter.ShowPersonAdapter;
import com.jlstudio.publish.adapter.ShowUnReplyPersonAdapter;
import com.jlstudio.publish.bean.MyContact;
import com.jlstudio.publish.net.GetNotificationNet;
import com.jlstudio.publish.util.JsonToPubhlishData;
import com.jlstudio.publish.util.ShowToast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by gzw on 2015/9/26.
 */
public class ShowUnreplyPersonDialog extends Dialog implements View.OnClickListener, AdapterView.OnItemClickListener {
    private Context context;
    private TextView title;
    private List<MyContact> persons;
    private ShowUnReplyPersonAdapter adapter;
    private ListView listView;
    private String publishId;
    private GetDataNet gn;
    private DBOption db;
    private CatchData data;
    private String noticegroup;


    public ShowUnreplyPersonDialog(Context context, String publishId,String noticegroup) {
        super(context);
        this.context = context;
        this.publishId = publishId;
        this.noticegroup = noticegroup;
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_select_person);
        gn = GetDataNet.getInstence(context);
        initView();
        getDatas();
    }

    private void getDatas() {
        persons = new ArrayList<>();
        getDatasFromNet();
    }

    private void initListView() {
        listView = (ListView) findViewById(R.id.listview);
        adapter = new ShowUnReplyPersonAdapter(context, persons);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(this);
    }


    private void initView() {
        Window dialogwindow = getWindow();
        title = (TextView) findViewById(R.id.title);
        title.setText("未回执人数");
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
        ShowToast.show(context, "call hime");
        Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:"+persons.get(position).getPhone()));
        context.startActivity(intent);
        dismiss();
    }

    private void getDatasFromNet() {
        JSONObject json = new JSONObject();
        try {
            json.put("noticeid", publishId);
            json.put("noticegroup", noticegroup);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        gn.getData(Config.URL, Config.GETUNREPLYPERSON, new Response.Listener<String>() {

            @Override
            public void onResponse(String s) {
                if (s != null) {
                    persons = JsonToPubhlishData.getPerson(s);
                    initListView();
                }
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError volleyError) {
                ShowToast.show(context, "获取数据失败");
            }
        }, json.toString());
    }
}
