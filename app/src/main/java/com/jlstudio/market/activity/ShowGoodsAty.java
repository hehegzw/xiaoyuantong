package com.jlstudio.market.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.jlstudio.R;
import com.jlstudio.main.activity.LoginAty;
import com.jlstudio.main.application.Config;
import com.jlstudio.main.net.GetDataNet;
import com.jlstudio.main.net.UplaodFace;
import com.jlstudio.main.util.ProgressUtil;
import com.jlstudio.market.adapter.GoodsDisplayAdapter;
import com.jlstudio.market.bean.GoodsDetail;
import com.jlstudio.publish.util.ShowToast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ShowGoodsAty extends Activity implements View.OnClickListener {
    private SwipeRefreshLayout refresh;
    private List<GoodsDetail> goods;
    private RecyclerView recyclerView;
    private GoodsDisplayAdapter adapter;
    private TextView back;
    private TextView publishGood;
    private int currentPage = 0;
    private GetDataNet gn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_goodsaty);
        ProgressUtil.showProgressDialog(this,"数据加载中...");
        initView();
        initRecycleView();
        initEvent();
        initData(true);
    }

    private void initView() {
        refresh = (SwipeRefreshLayout) findViewById(R.id.refresh);
        refresh.setDistanceToTriggerSync(100);
        refresh.setColorSchemeColors(R.color.fresh_one, R.color.fresh_two, R.color.fresh_three, R.color.fresh_four);
        back = (TextView) findViewById(R.id.back);
        publishGood = (TextView) findViewById(R.id.publishGood);
        Typeface iconfont = Typeface.createFromAsset(getAssets(), "fonts/iconfont.ttf");
        back.setTypeface(iconfont);
        iconfont = Typeface.createFromAsset(getAssets(), "fonts/chat.ttf");
        publishGood.setTypeface(iconfont);
    }

    private void initRecycleView() {
        recyclerView = (RecyclerView) findViewById(R.id.recycleView);
        goods = new ArrayList<>();
        adapter = new GoodsDisplayAdapter(goods,this);
        recyclerView.setAdapter(adapter);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }
    private void initEvent() {
        back.setOnClickListener(this);
        publishGood.setOnClickListener(this);
        refresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                initData(true);
            }
        });
        adapter.setClick(new GoodsDisplayAdapter.ClickListener() {
            @Override
            public void click(View v,View v2,int position) {
                if(position == goods.size()-1&&goods.size()>=20){
                    initData(false);
                    v.setVisibility(View.GONE);
                    v2.setVisibility(View.VISIBLE);
                }else {
                    Intent intent = new Intent(ShowGoodsAty.this,GoodsDetailActivity.class);
                    intent.putExtra("good",goods.get(position));
                    startActivity(intent);
                }
            }
        });
        adapter.setImageClick(new GoodsDisplayAdapter.ImageClickListener() {
            @Override
            public void click(View v1, int position) {
                Intent intent = new Intent(ShowGoodsAty.this,GoodsDetailActivity.class);
                intent.putExtra("good",goods.get(position));
                startActivity(intent);
            }
        });
    }
    private void initData(final boolean isRefresh) {
        if(isRefresh) currentPage = 0;
        JSONObject json = new JSONObject();
        try {
            json.put("currentPage",currentPage);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        UplaodFace.getData(Config.URL+"goods",json.toString(), new UplaodFace.SuccessJson() {
            @Override
            public void onSuccess(JSONArray response) {
                if(response.length() == 0){
                    ShowToast.show(ShowGoodsAty.this,"没有更多的数据了");
                }else{
                    jsonToData(response,isRefresh);
                    currentPage++;
                    refresh.setRefreshing(false);
                    adapter.notifyDataSetChanged();
                }
                ProgressUtil.closeProgressDialog();
            }

        }, new UplaodFace.Failure() {
            @Override
            public void onFailure() {
                refresh.setRefreshing(false);
                ShowToast.show(ShowGoodsAty.this, "获取数据失败，请检查网络");
                ProgressUtil.closeProgressDialog();
            }
        });
    }

    private void jsonToData(JSONArray response,boolean isRefresh) {
        if(isRefresh) goods.clear();
        else goods.remove(goods.size()-1);
        for(int i=0;i<response.length();i++){
            try {
                JSONObject json = response.getJSONObject(i);
                GoodsDetail good = new GoodsDetail();
                good.setId(Integer.valueOf(json.getString("id")));
                good.setFace(json.getString("face"));
                good.setUserid(json.getString("userid"));
                good.setUsername(json.getString("username"));
                good.setDescription(json.getString("description"));
                good.setPrice(json.getDouble("price"));
                good.setDatetime(json.getLong("datetime"));
                good.setCloseFlag(json.getInt("closeFlag"));
                good.setViews(json.getInt("views"));
                JSONArray array = json.getJSONArray("images");
                List<String> paths = new ArrayList<>();
                for(int j=0;j<array.length();j++){
                    paths.add((String) array.get(j));
                }
                good.setImages(paths);
                goods.add(good);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        if(goods.size()>=20)
            goods.add(new GoodsDetail());//加载更多
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.publishGood:
                if(!Config.isLogin(this)){
                    startActivity(new Intent(this,LoginAty.class));
                    finish();
                }else{
                    startActivity(new Intent(this,MyInfoActivity.class));
                }
                break;
            case R.id.back:
               finish();
                break;
        }
    }
}
