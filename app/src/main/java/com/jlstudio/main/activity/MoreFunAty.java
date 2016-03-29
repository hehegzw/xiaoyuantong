package com.jlstudio.main.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.TextView;

import com.jlstudio.R;
import com.jlstudio.group.activity.ShowContactsActivity;
import com.jlstudio.iknow.dialog.QueryDialog;
import com.jlstudio.iknow.dialog.QueryScoreDialog;
import com.jlstudio.main.adapter.GridViewAdapter;
import com.jlstudio.main.bean.GridViewItem;
import com.jlstudio.market.activity.ShowGoodsAty;
import com.jlstudio.publish.activity.PublishDatasAty;
import com.jlstudio.publish.activity.ShowPersonAty;
import com.jlstudio.publish.dialog.UNRegisterQueryDialog;
import com.jlstudio.swzl.activity.LostAndFound;
import com.jlstudio.weather.activity.InitDataActivity;

import java.util.ArrayList;
import java.util.List;

public class MoreFunAty extends Activity implements AdapterView.OnItemClickListener, View.OnClickListener {
    public static final int SCORE = 0;
    public static final int SCHEDULE = 1;
    private GridView gridView;
    private List<GridViewItem> itemList;
    private GridViewAdapter adapter;
    private TextView title_name,back;
    private int[] selector = {R.drawable.message_selector, R.drawable.lostandfound_selector, R.drawable.score_selector,
            R.drawable.schedule_selector, R.drawable.cet_selector, R.drawable.wether_selector};
    private String[] name = {"通知", "失物招领", "查成绩", "查课表", "查CET", "天气"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_morefun);
        initData();
        initView();
        initGridView();
    }

    private void initView() {
        title_name = (TextView) findViewById(R.id.title_name);
        back = (TextView) findViewById(R.id.back);
        title_name.setText("功能");
        Typeface iconfont = Typeface.createFromAsset(getAssets(),"fonts/iconfont.ttf");
        back.setTypeface(iconfont);
        back.setOnClickListener(this);
    }

    private void initData() {
        itemList = new ArrayList<>();
        for (int i = 0; i < 6; i++) {
            itemList.add(new GridViewItem(selector[i], name[i]));
        }
    }

    private void initGridView() {
        gridView = (GridView) findViewById(R.id.gridview);
        adapter = new GridViewAdapter(this, itemList);
        gridView.setAdapter(adapter);
        gridView.setOnItemClickListener(this);

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        switch (position) {
            case 0:
                startActivity(new Intent(this, PublishDatasAty.class));
                break;
            case 1:
                startActivity(new Intent(this, ShowGoodsAty.class));
                break;
            case 2:
                new QueryScoreDialog(this,SCORE).show();
                break;
            case 3:
                new QueryScoreDialog(this,SCHEDULE).show();
                break;
            case 4:
                new QueryDialog(this).show();
                break;
            case 5:
                startActivity(new Intent(this, InitDataActivity.class));
                break;
        }
    }

    @Override
    public void onClick(View v) {
        finish();
    }
}
