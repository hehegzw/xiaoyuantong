package com.jlstudio.market.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.facebook.drawee.generic.GenericDraweeHierarchy;
import com.facebook.drawee.generic.GenericDraweeHierarchyBuilder;
import com.facebook.drawee.view.SimpleDraweeView;
import com.jlstudio.R;
import com.jlstudio.group.util.DpPxUtil;
import com.jlstudio.main.activity.LoginAty;
import com.jlstudio.main.application.Config;
import com.jlstudio.main.application.MyApplication;
import com.jlstudio.main.net.Downloadimgs;
import com.jlstudio.main.net.GetDataNet;
import com.jlstudio.market.adapter.GoodDetailAdapter;
import com.jlstudio.market.adapter.SubGoodsDisplayAdapter;
import com.jlstudio.market.bean.GoodsDetail;
import com.jlstudio.publish.util.ShowToast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class GoodsDetailActivity extends Activity implements View.OnClickListener {
    private TextView back;
    private TextView titleName;
    private TextView goodName;
    private TextView goodPrice;
    private TextView goodDescription;
    private TextView chat;
    private TextView collect;
    private TextView moneyIcon;
    private TextView eyeIcon;
    private LinearLayout goodPic;
    private GoodsDetail good;
    private int screenWidth;
    private GetDataNet gn;
    private RecyclerView showPic;
    private GoodDetailAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_good_detail_aty);
        gn = GetDataNet.getInstence(this);
        initView();
        initData();
    }

    private void initData() {
        WindowManager wm = (WindowManager) getSystemService(WINDOW_SERVICE);
        screenWidth = wm.getDefaultDisplay().getWidth();
        good = (GoodsDetail) getIntent().getSerializableExtra("good");
        if (good.getUserid().endsWith(Config.loadUser(this).getUsername())){
            collect.setVisibility(View.INVISIBLE);
            chat.setVisibility(View.INVISIBLE);
        }
        addViewCount(good.getId() + "");
        goodName.setText(good.getDescription().split(":")[0]);
        goodPrice.setText(good.getPrice() + "");
        goodDescription.setText(good.getDescription().split(":")[1]);
        List<String> imagePath = good.getImages();
        adapter = new GoodDetailAdapter(imagePath,this,screenWidth);
        showPic.setAdapter(adapter);
        showPic.setHasFixedSize(true);
        LinearLayoutManager layout = new LinearLayoutManager(this);
        layout.setOrientation(LinearLayout.HORIZONTAL);
        showPic.setLayoutManager(layout);
        adapter.setClick(new GoodDetailAdapter.ClickListener() {
            @Override
            public void click(View v, int position) {
                Intent intent = new Intent(GoodsDetailActivity.this,ShowBigPicAty.class);
                ArrayList<String> list = (ArrayList<String>) good.getImages();
                intent.putStringArrayListExtra("imagePath",list);
                startActivity(intent);
            }
        });
    }

    private void initView() {
        back = (TextView) findViewById(R.id.back);
        titleName = (TextView) findViewById(R.id.title_name);
        titleName.setText("宝贝详情");
        goodName = (TextView) findViewById(R.id.goodName);
        goodPrice = (TextView) findViewById(R.id.goodPrice);
        goodDescription = (TextView) findViewById(R.id.description);
        chat = (TextView) findViewById(R.id.chat);
        collect = (TextView) findViewById(R.id.collection);
        goodPic = (LinearLayout) findViewById(R.id.goodPic);
        showPic = (RecyclerView) findViewById(R.id.showPic);
        moneyIcon = (TextView) findViewById(R.id.moneyicon);
        eyeIcon = (TextView) findViewById(R.id.eyeicon);
        Typeface icon = Typeface.createFromAsset(getAssets(), "fonts/iconfont.ttf");
        back.setTypeface(icon);
        icon = Typeface.createFromAsset(getAssets(), "fonts/goods.ttf");
        collect.setTypeface(icon);
        icon = Typeface.createFromAsset(getAssets(), "fonts/singlechat.ttf");
        chat.setTypeface(icon);
        icon = Typeface.createFromAsset(getAssets(), "fonts/moneyeye.ttf");
        moneyIcon.setTypeface(icon);
        eyeIcon.setTypeface(icon);
        initEvent();
    }

    private void initEvent() {
        back.setOnClickListener(this);
        chat.setOnClickListener(this);
        collect.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back:
                finish();
                break;
            case R.id.chat:
                if (!Config.isLogin(this)) {
                    startActivity(new Intent(this, LoginAty.class));
                    finish();
                } else {
                    Intent intent = new Intent(this, ChatActivity.class);
                    intent.putExtra("userId", good.getUserid());
                    intent.putExtra("name", good.getUsername());
                    startActivity(intent);
                }
                break;
            case R.id.collection:
                if (!Config.isLogin(this)) {
                    startActivity(new Intent(this, LoginAty.class));
                    finish();
                }else{
                    sendData();
                }
                break;
        }
    }

    private void addViewCount(String goodId) {
        JSONObject json = new JSONObject();
        try {
            json.put("goodsId", goodId);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        gn.getData(Config.URL, "addviewtime", new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {

            }
        }, json.toString());
    }
    private void sendData(){
        gn = GetDataNet.getInstence(this);
        JSONObject json = new JSONObject();
        try {
            json.put("userid",Config.loadUser(this).getUsername());
            json.put("goodsId",good.getId());

        } catch (JSONException e) {
            e.printStackTrace();
        }
        gn.getData(Config.URL, "collect", new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                if(s.endsWith("0")){
                    ShowToast.show(GoodsDetailActivity.this,"收藏成功");
                }else{
                    ShowToast.show(GoodsDetailActivity.this,"你已经收藏过了");
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                ShowToast.show(GoodsDetailActivity.this,"收藏失败");
            }
        },json.toString());
    }
}
