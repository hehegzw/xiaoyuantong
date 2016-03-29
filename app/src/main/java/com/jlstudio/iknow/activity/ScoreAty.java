package com.jlstudio.iknow.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.jlstudio.R;
import com.jlstudio.iknow.adapter.ScoreAdapter;
import com.jlstudio.iknow.bean.ScoreItem;
import com.jlstudio.iknow.util.JsonUtil;
import com.jlstudio.main.activity.BaseActivity;
import com.jlstudio.main.activity.MainActivity;
import com.jlstudio.main.application.ActivityContror;
import com.jlstudio.main.application.Config;
import com.jlstudio.main.bean.CatchData;
import com.jlstudio.main.db.DBOption;
import com.jlstudio.publish.net.GetNotificationNet;
import com.jlstudio.publish.util.JsonToPubhlishData;
import com.jlstudio.publish.util.ShowToast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class ScoreAty extends BaseActivity implements View.OnClickListener {
    private TextView get_score,  point;
    private TextView back, title_name;
    private ListView list;
    private List<ScoreItem> scoreItems;
    private ScoreAdapter adapter;

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
        adapter = new ScoreAdapter(scoreItems, this);
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
        list = (ListView) findViewById(R.id.list);
        list.setAdapter(adapter);
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
