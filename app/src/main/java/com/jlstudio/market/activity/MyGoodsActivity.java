package com.jlstudio.market.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.support.v4.widget.SwipeRefreshLayout;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jlstudio.R;
import com.jlstudio.main.application.Config;
import com.jlstudio.main.net.UplaodFace;
import com.jlstudio.main.util.ProgressUtil;
import com.jlstudio.market.adapter.ShowMyGoodsAdapter;
import com.jlstudio.market.bean.GoodsDetail;
import com.jlstudio.publish.util.RecycleViewDivider;
import com.jlstudio.publish.util.ShowToast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MyGoodsActivity extends Activity implements View.OnClickListener {
    private TextView back;
    private TextView titleName;
    private SwipeRefreshLayout refresh;
    private RecyclerView showMyGoods;
    private List<GoodsDetail> goods;
    private ShowMyGoodsAdapter adapter;
    private TextView publish;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_goods);
        initView();
    }

    @Override
    protected void onResume() {
        super.onResume();
        getDataNet();
    }

    private void initView() {
        back = (TextView) findViewById(R.id.back);
        Typeface iconfont = Typeface.createFromAsset(getAssets(), "fonts/iconfont.ttf");
        back.setTypeface(iconfont);
        publish = (TextView) findViewById(R.id.right_icon);
        iconfont = Typeface.createFromAsset(getAssets(), "fonts/publish.ttf");
        publish.setTypeface(iconfont);
        titleName = (TextView) findViewById(R.id.title_name);
        titleName.setText("我的宝贝");
        refresh = (SwipeRefreshLayout) findViewById(R.id.refresh);
        refresh.setDistanceToTriggerSync(100);
        refresh.setColorSchemeColors(R.color.fresh_one, R.color.fresh_two, R.color.fresh_three, R.color.fresh_four);
        goods = new ArrayList<>();
        adapter = new ShowMyGoodsAdapter(goods,this);
        showMyGoods = (RecyclerView) findViewById(R.id.showMyGoods);
        showMyGoods.setAdapter(adapter);
        showMyGoods.setHasFixedSize(true);
        showMyGoods.setLayoutManager(new LinearLayoutManager(this));
        showMyGoods.addItemDecoration(new RecycleViewDivider(this, LinearLayout.HORIZONTAL));
        initEvent();
    }

    private void initEvent() {
        back.setOnClickListener(this);
        publish.setOnClickListener(this);
        refresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getDataNet();
            }
        });
        adapter.setListener(new ShowMyGoodsAdapter.ClickListener() {
            @Override
            public void click(View v, int position) {
                Intent intent = new Intent(MyGoodsActivity.this, PublishGoodAty.class);
                intent.setAction("2");
                intent.putExtra("good", goods.get(position));
                startActivity(intent);
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.back:
                finish();
                break;
            case R.id.right_icon:
                Intent intent = new Intent(this,PublishGoodAty.class);
                intent.setAction("1");
                startActivity(intent);
                break;
        }
    }
    private void getDataNet(){
        goods.clear();
        JSONObject json = new JSONObject();
        try {
            json.put("userid",Config.loadUser(this).getUsername());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        UplaodFace.getData(Config.URL + "goods", json.toString(), new UplaodFace.SuccessJson() {
            @Override
            public void onSuccess(JSONArray response) {
                    jsonToData(response);
                    refresh.setRefreshing(false);
                    adapter.notifyDataSetChanged();
                ProgressUtil.closeProgressDialog();
            }

        }, new UplaodFace.Failure() {
            @Override
            public void onFailure() {
                refresh.setRefreshing(false);
                ShowToast.show(MyGoodsActivity.this, "获取数据失败，请检查网络");
                ProgressUtil.closeProgressDialog();
            }
        });
    }
    private void jsonToData(JSONArray response) {
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
        adapter.notifyDataSetChanged();
    }
}
