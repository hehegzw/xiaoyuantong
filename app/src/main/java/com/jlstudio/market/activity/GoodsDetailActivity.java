package com.jlstudio.market.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jlstudio.R;
import com.jlstudio.group.util.DpPxUtil;
import com.jlstudio.main.application.Config;
import com.jlstudio.main.net.Downloadimgs;
import com.jlstudio.market.bean.GoodsDetail;
import com.jlstudio.publish.util.ShowToast;

import java.util.List;

public class GoodsDetailActivity extends Activity implements View.OnClickListener {
    private TextView back;
    private TextView goodName;
    private TextView goodPrice;
    private TextView goodDescription;
    private TextView chat;
    private LinearLayout goodPic;
    private GoodsDetail good;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_goods_detail);
        initView();
        initData();
    }

    private void initData() {
        good = (GoodsDetail) getIntent().getSerializableExtra("good");
        goodName.setText(good.getDescription().split(":")[0]);
        goodPrice.setText(good.getPrice()+"");
        goodDescription.setText(good.getDescription().split(":")[1]);
        List<String> imagePath = good.getImages();
        for(String path:imagePath){
            ImageView iv = getIv();
            goodPic.addView(iv);
            Downloadimgs.initImageLoader(this).displayImage(Config.URL+path,iv);
        }
    }

    private void initView() {
        back = (TextView) findViewById(R.id.back);
        goodName = (TextView) findViewById(R.id.goodName);
        goodPrice = (TextView) findViewById(R.id.goodPrice);
        goodDescription = (TextView) findViewById(R.id.descriptiom);
        chat = (TextView) findViewById(R.id.chat);
        goodPic = (LinearLayout) findViewById(R.id.goodPic);
        Typeface icon = Typeface.createFromAsset(getAssets(),"fonts/iconfont.ttf");
        back.setTypeface(icon);
        icon = Typeface.createFromAsset(getAssets(), "fonts/chat.ttf");
        chat.setTypeface(icon);
        initEvent();
    }

    private void initEvent() {
        back.setOnClickListener(this);
        chat.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.back:
                finish();
                break;
            case R.id.chat:
                if(!good.getUserid().endsWith(Config.loadUser(this).getUsername())){
                    Intent intent = new Intent(this,ChatActivity.class);
                    intent.putExtra("good",good);
                    startActivity(intent);
                }else{
                    ShowToast.show(this,"跟自己也聊天啊");
                }
                break;
        }
    }
    private ImageView getIv(){
        ImageView iv = (ImageView) LayoutInflater.from(this).inflate(R.layout.image,goodPic,false);
        ViewGroup.LayoutParams lp = iv.getLayoutParams();
        lp.width = LinearLayout.LayoutParams.MATCH_PARENT;
        lp.height = DpPxUtil.getDp(this,2000);
        iv.setLayoutParams(lp);
        return iv;
    }
}
