package com.jlstudio.group.activity;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.jlstudio.R;
import com.jlstudio.group.adapter.FriendCircleAdapter;
import com.jlstudio.group.bean.FriendCircleData;
import com.jlstudio.group.util.PullScrollView;

import java.util.ArrayList;
import java.util.List;

public class FriendCircleActivity extends Activity implements PullScrollView.OnTurnListener {

    private ScrollView mScrollView;
    private ImageView mHeadImg;
    private ListView listview_circle;
    private TextView tv_favour;

    private List<FriendCircleData> friendCircleDataList = null;
    private FriendCircleAdapter friendCircleAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friend_circle);
        initView();
        setData();
        showList();
    }

    private void showList() {
        friendCircleAdapter = new FriendCircleAdapter(FriendCircleActivity.this, friendCircleDataList);
        listview_circle.setAdapter(friendCircleAdapter);
    }

    private void setData() {
        friendCircleDataList = new ArrayList<FriendCircleData>();
        FriendCircleData friend1 = new FriendCircleData("Bingo Zhang", "2015-10-22", "周末，我在街上看见一个男人在和我心爱的女神吵架，吵着吵着男人居然动起了手，身为一个爷们我怎么能眼睁睁的看着心爱的女神受欺负，于是我用手捂住了双眼...");
        friendCircleDataList.add(friend1);
        FriendCircleData friend2 = new FriendCircleData("NewOrin", "2015-10-21", "今天感冒了，刚去药店买药，营业员是个美女，有个帅哥要买东西，对着美女营业员眨巴眼，只见营业员哦了一声，拿了一盒杜蕾斯给他.轮到我时，我也对美女眨眼，结果她给我拿了瓶眼药水!!!");
        friendCircleDataList.add(friend2);
        FriendCircleData friend3 = new FriendCircleData("Jack", "2015-10-20", "我妈妈喜欢打麻将，可是后来我出生了，妈妈为了我也为了整个家庭，渐渐的就不打麻将了，因为她觉得，好像打我比较有趣");
        friendCircleDataList.add(friend3);
    }

    protected void initView() {
        mScrollView = (ScrollView) findViewById(R.id.scroll_view);
        mHeadImg = (ImageView) findViewById(R.id.background_img);
        listview_circle = (ListView) findViewById(R.id.listview_circle);

        mScrollView.smoothScrollTo(0, 0);
//        mScrollView.setHeader(mHeadImg);
//        mScrollView.setOnTurnListener(this);
    }

    @Override
    public void onTurn() {
        Toast.makeText(FriendCircleActivity.this, "刷新了", Toast.LENGTH_SHORT).show();
    }
    public void showToast(String message){
        Toast.makeText(FriendCircleActivity.this,message,Toast.LENGTH_SHORT).show();
    }
}
