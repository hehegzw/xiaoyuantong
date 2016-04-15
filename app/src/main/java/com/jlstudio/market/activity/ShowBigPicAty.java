package com.jlstudio.market.activity;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.hp.hpl.sparta.Text;
import com.jlstudio.R;
import com.jlstudio.group.util.DpPxUtil;
import com.jlstudio.main.application.Config;
import com.jlstudio.market.adapter.PageAdapter;

import java.util.ArrayList;
import java.util.List;

public class ShowBigPicAty extends Activity implements ViewPager.OnPageChangeListener {
    private ViewPager viewPager;
    private List<SimpleDraweeView> list;
    private List<String> imagePath;
    private PageAdapter adapter;
    private int screenWidth;
    private int screenHeight;
    private String suffix;
    private TextView[] textViews;
    private LinearLayout dotArea;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_big_pic_aty);
        initData();
        initView();
    }

    private void initData() {
        dotArea = (LinearLayout) findViewById(R.id.dotarea);
        imagePath = getIntent().getStringArrayListExtra("imagePath");
        list = new ArrayList<>();
        textViews = new TextView[imagePath.size()];
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, 40);
        params.setMargins(0,0,10,0);
        for(int i=0;i<imagePath.size();i++){
            list.add(getImageView());
            TextView tx = getTextView();
            textViews[i] = tx;
            dotArea.addView(tx,params);
        }
        adapter = new PageAdapter(list);
        WindowManager wm = (WindowManager) getSystemService(WINDOW_SERVICE);
        screenWidth = wm.getDefaultDisplay().getWidth();
        screenHeight = wm.getDefaultDisplay().getHeight();
        suffix = "?imageView2/0/w/"+screenWidth+"/h/"+screenHeight+"/format/jpg";
        Uri uri = Uri.parse(Config.QINIUURL + imagePath.get(0) + suffix);
        list.get(0).setImageURI(uri);
        textViews[0].setTextColor(Color.BLUE);
    }

    private void initView() {
        viewPager = (ViewPager) findViewById(R.id.viewPager);
        viewPager.setAdapter(adapter);
        viewPager.setOnPageChangeListener(this);

    }

    private SimpleDraweeView getImageView(){
        SimpleDraweeView view = (SimpleDraweeView) LayoutInflater.from(this).inflate(R.layout.simpledraweeview,null);
        return view;
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        Uri uri = Uri.parse(Config.QINIUURL + imagePath.get(position) + suffix);
        list.get(position).setImageURI(uri);
        changeColor(position);
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }
    private TextView getTextView(){
        Typeface iconfont = Typeface.createFromAsset(getAssets(),"fonts/dot.ttf");
        TextView dot = new TextView(this);
        dot.setTypeface(iconfont);
        dot.setText(R.string.dot);
        dot.setTextSize(20);
        dot.setTextColor(Color.GRAY);
        return dot;
    }
    private void changeColor(int index){
        for(int i=0;i<textViews.length;i++){
            if(i == index) textViews[i].setTextColor(Color.BLUE);
            else textViews[i].setTextColor(Color.GRAY);
        }
    }
}
