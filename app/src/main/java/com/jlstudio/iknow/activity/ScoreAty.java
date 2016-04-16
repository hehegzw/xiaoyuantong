package com.jlstudio.iknow.activity;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.jlstudio.R;
import com.jlstudio.iknow.adapter.ListAdapter;
import com.jlstudio.iknow.bean.ScoreItem;
import com.jlstudio.iknow.util.JsonUtil;
import com.jlstudio.main.activity.BaseActivity;
import com.jlstudio.main.application.Config;
import com.jlstudio.main.bean.CatchData;
import com.jlstudio.main.db.DBOption;
import com.jlstudio.main.net.GetDataNet;
import com.jlstudio.publish.util.ShowToast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class ScoreAty extends BaseActivity implements View.OnClickListener {
    private TextView get_score,point;
    private TextView back, title_name;
    private SwipeRefreshLayout refresh;
    private ExpandableListView list;
    private List<ScoreItem> scoreItems;
    private ListAdapter adapter;
    private GetDataNet gn;//网络连接类
    private DBOption db;//数据库操作
    private CatchData contentData;//成绩
    private Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_score);
        gn = GetDataNet.getInstence(this);
        db = new DBOption(this);
        initView();
        initData();
        initListView();

    }

    private void initData() {
        intent = getIntent();
        String username = intent.getStringExtra("username");
        contentData = db.getCatch(Config.URL + Config.GETSCORE +username );
        String data = intent.getStringExtra("data");
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
        refresh = (SwipeRefreshLayout) findViewById(R.id.refresh);

        get_score = (TextView) findViewById(R.id.get_score);
        point = (TextView) findViewById(R.id.point);
        refresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refresh.setRefreshing(true);
                getDatasFromNet(intent.getStringExtra("username"), intent.getStringExtra("password"));
            }
        });
    }
    @Override
    public void onClick(View v) {
        finish();
    }
    private void getDatasFromNet(final String user, final String pwd) {
        JSONObject json = new JSONObject();
        try {
            json.put("username", user);
            json.put("password", pwd);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        gn.getData(Config.URL,Config.GETSCORE, new Response.Listener<String>() {

            @Override
            public void onResponse(String s) {
                try {
                    JSONObject json = new JSONObject(s);
                    String status = json.getString("status");
                    if (status.equals("success")) {
                        if (contentData == null) {
                            db.insertCatch(Config.URL + Config.GETSCORE + user, s, System.currentTimeMillis() + "");
                        } else {
                            db.updateCatch(Config.URL + Config.GETSCORE + user, s, System.currentTimeMillis() + "");
                        }
                        scoreItems = JsonUtil.getScores(s);
                        adapter.setList(scoreItems);
                    }
                    refresh.setRefreshing(false);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Log.d("hehe","获取数据失败");
                ShowToast.show(ScoreAty.this, "获取数据失败");
                refresh.setRefreshing(false);
            }
        }, json.toString());
    }
}
