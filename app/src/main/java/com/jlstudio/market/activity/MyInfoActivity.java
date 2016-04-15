package com.jlstudio.market.activity;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jlstudio.R;
import com.jlstudio.market.fragment.MyCollecttFragment;
import com.jlstudio.market.fragment.MyGoodsListFragment;
import com.jlstudio.market.fragment.MyMessagesFragment;

public class MyInfoActivity extends Activity implements View.OnClickListener {
    private TextView back;
    private TextView titleName;
    private TextView publish;
    private LinearLayout layoutGoods;
    private LinearLayout layoutMessage;
    private LinearLayout layoutCollect;
    private TextView tvGoods;
    private TextView tvMessage;
    private TextView tvCollect;
    private TextView tv_goods;
    private TextView tv_message;
    private TextView tv_collect;
    private Fragment currentFrag;
    private int tag = 0;//当前在哪个选项卡
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_info);
        initView();
        initFragment();
    }

    private void initView() {
        back = (TextView) findViewById(R.id.back);
        titleName = (TextView) findViewById(R.id.title_name);
        publish = (TextView) findViewById(R.id.right_icon);
        titleName.setText("宝贝");
        layoutGoods = (LinearLayout) findViewById(R.id.layout_good);
        layoutMessage = (LinearLayout) findViewById(R.id.layout_message);
        layoutCollect = (LinearLayout) findViewById(R.id.layout_collect);
        tvGoods = (TextView) findViewById(R.id.tv_icon_good);
        tv_goods = (TextView) findViewById(R.id.tv_good);
        tvMessage = (TextView) findViewById(R.id.tv_icon_message);
        tv_message = (TextView) findViewById(R.id.tv_message);
        tvCollect = (TextView) findViewById(R.id.tv_icon_collect);
        tv_collect = (TextView) findViewById(R.id.tv_collect);

        Typeface icon = Typeface.createFromAsset(getAssets(),"fonts/goods.ttf");
        tvGoods.setTypeface(icon);
        tvMessage.setTypeface(icon);
        tvCollect.setTypeface(icon);
        icon = Typeface.createFromAsset(getAssets(),"fonts/iconfont.ttf");
        back.setTypeface(icon);
        icon = Typeface.createFromAsset(getAssets(), "fonts/publish.ttf");
        publish.setTypeface(icon);
        initEvent();
    }

    private void initEvent() {
        back.setOnClickListener(this);
        publish.setOnClickListener(this);
        layoutGoods.setOnClickListener(this);
        layoutMessage.setOnClickListener(this);
        layoutCollect.setOnClickListener(this);
    }
    private void initFragment() {
        currentFrag = new MyGoodsListFragment();
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.setCustomAnimations(R.animator.object_in_left,R.animator.object_out_right);
        transaction.add(R.id.fragment, currentFrag).commit();
        changeColor(0);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.back:
                finish();
                break;
            case R.id.right_icon:
                startActivity(new Intent(this,PublishGoodAty.class));
                break;
            case R.id.layout_good:
                if(tag == 0)return;
                tag = 0;
                changeColor(tag);
                titleName.setText("宝贝");
                swicthFragment(new MyGoodsListFragment());
                break;
            case R.id.layout_message:
                if(tag == 1)return;
                tag = 1;
                changeColor(tag);
                titleName.setText("消息");
                swicthFragment(new MyMessagesFragment());
                break;
            case R.id.layout_collect:
                if(tag == 2)return;
                tag = 2;
                changeColor(tag);
                titleName.setText("收藏");
                swicthFragment(new MyCollecttFragment());
                break;
        }
    }
    private void swicthFragment(Fragment fragment) {
        if (currentFrag != fragment) {
            FragmentTransaction transaction = getFragmentManager().beginTransaction();
            if(fragment instanceof MyGoodsListFragment){
                transaction.setCustomAnimations(R.animator.object_in_left,R.animator.object_out_right);
            }else if(fragment instanceof MyMessagesFragment){
                transaction.setCustomAnimations(R.animator.object_in_bottom,R.animator.object_out_bottom);
            }else{
                transaction.setCustomAnimations(R.animator.object_in_right,R.animator.object_out_left);
            }

            transaction.replace(R.id.fragment,fragment).commit();
//            if (!fragment.isAdded()) {
//                transaction.hide(currentFrag).add(R.id.fragment, fragment).commit();
//            } else {
//                transaction.hide(currentFrag).show(fragment).commit();
//            }
            currentFrag = fragment;
        }
    }
    private void changeColor(int tag){
        if(tag == 0){
            tvGoods.setTextColor(Color.BLUE);
            tv_goods.setTextColor(Color.BLUE);
            tv_message.setTextColor(Color.BLACK);
            tvMessage.setTextColor(Color.BLACK);
            tvCollect.setTextColor(Color.BLACK);
            tv_collect.setTextColor(Color.BLACK);
        }else if(tag == 1){
            tvGoods.setTextColor(Color.BLACK);
            tv_goods.setTextColor(Color.BLACK);
            tv_message.setTextColor(Color.BLUE);
            tvMessage.setTextColor(Color.BLUE);
            tvCollect.setTextColor(Color.BLACK);
            tv_collect.setTextColor(Color.BLACK);
        }else{
            tvGoods.setTextColor(Color.BLACK);
            tv_goods.setTextColor(Color.BLACK);
            tv_message.setTextColor(Color.BLACK);
            tvMessage.setTextColor(Color.BLACK);
            tvCollect.setTextColor(Color.BLUE);
            tv_collect.setTextColor(Color.BLUE);
        }
    }
}
