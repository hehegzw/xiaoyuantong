package com.jlstudio.iknow.activity;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.ListView;
import android.widget.TextView;

import com.jlstudio.R;
import com.jlstudio.iknow.adapter.ListAdapter;
import com.jlstudio.iknow.adapter.ScoreAdapter;
import com.jlstudio.iknow.bean.ScoreItem;
import com.jlstudio.iknow.util.JsonUtil;
import com.jlstudio.main.activity.BaseActivity;
import com.jlstudio.main.activity.MainActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class ScoreAty extends BaseActivity implements View.OnClickListener {
    private TextView get_score,point;
    private TextView back, title_name;
    private ExpandableListView list;
    private List<ScoreItem> scoreItems;
    private ListAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_score);
        initView();
        initData();
        initListView();

    }

    private void initData() {
        String data = getIntent().getStringExtra("data");
        scoreItems = new ArrayList<>();
        adapter = new ListAdapter(this,scoreItems);
        try {
            JSONObject json = new JSONObject(data);
            title_name.setText(json.getString("name"));
            point.setText(json.getString("gpa"));
            get_score.setText(json.getString("sumCredit"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        scoreItems = JsonUtil.getScores(data);
        adapter.setList(scoreItems);

    }

    private void initListView() {
        list = (ExpandableListView) findViewById(R.id.list);
        list.setAdapter(adapter);
        list.setGroupIndicator(null);
        list.setChildIndicator(null);
        list.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
            @Override
            public boolean onGroupClick(ExpandableListView parent, View view, int groupPosition, long id) {
                Log.d("helf","setOnItemClickListener");
                if(scoreItems.get(groupPosition).isSpread()){
                    ((TextView)view.findViewById(R.id.arrow)).setText(R.string.arrright);
                    scoreItems.get(groupPosition).setSpread(false);
                }else{
                    ((TextView)view.findViewById(R.id.arrow)).setText(R.string.arrbottom);
                    scoreItems.get(groupPosition).setSpread(true);
                }
                return false;
            }
        });
    }

    private void initView() {

        back = (TextView) findViewById(R.id.back);
        Typeface iconfont = Typeface.createFromAsset(getAssets(), "fonts/iconfont.ttf");
        back.setTypeface(iconfont);
        back.setOnClickListener(this);
        title_name = (TextView) findViewById(R.id.title_name);
        title_name.setText("个人成绩");

        get_score = (TextView) findViewById(R.id.get_score);
        point = (TextView) findViewById(R.id.point);
    }
    @Override
    public void onClick(View v) {
        startActivity(new Intent(this, MainActivity.class));
        finish();
    }
}
