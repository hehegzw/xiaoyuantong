package com.jlstudio.group.activity;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.facebook.drawee.view.SimpleDraweeView;
import com.jlstudio.R;
import com.jlstudio.group.fragment.ContactsGroupFragment;
import com.jlstudio.group.fragment.FContact;
import com.jlstudio.group.fragment.RecentFragment;
import com.jlstudio.group.fragment.SearchLocationFragment;
import com.jlstudio.iknow.activity.FeedBack;
import com.jlstudio.main.activity.LoginAty;
import com.jlstudio.main.activity.MoreFunAty;
import com.jlstudio.main.application.ActivityContror;
import com.jlstudio.main.application.Config;
import com.jlstudio.main.bean.User;
import com.jlstudio.main.dialog.UploadFaceDialog;
import com.jlstudio.main.net.DownFace;
import com.jlstudio.main.net.Downloadimgs;
import com.jlstudio.main.util.JsonToBean;
import com.jlstudio.publish.activity.PublishDatasAty;
import com.jlstudio.publish.util.ShowToast;
import com.jlstudio.publish.util.StringUtil;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;
import java.util.List;

import cn.jpush.android.api.JPushInterface;

/**
 * 显示联系人
 */
public class ShowContactsActivity extends Activity implements View.OnClickListener, AdapterView.OnItemClickListener {
    private SimpleDraweeView face;//头像
    private SimpleDraweeView bigface;//头像
    private TextView tv_recent;//最近联系人按钮
    private TextView tv_friend;//好友按钮
    private Fragment currentFragment;//当前fragment
    private LinearLayout layout_add_friend;//bottom加好友区域
    private LinearLayout layout_search;//bottom搜索区域
    private LinearLayout layout_test;
    private TextView tv_icon_add;//bottom加好友图标
    private TextView tv_icon_search;//bottom搜索图标
    private TextView tv_icon_leave_msg;//bottom留言图标
    private Typeface iconfont;//字体文件加载
    public static String personalUrl;
    private TextView more;//更多
    private DrawerLayout drawerLayout;
    private long exitTime = 0;
    private ListView slidlist;
    private List<String> slidlistdata;
    private ImageLoader imageLoader;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.show_contacts);
        if(!Config.isLogin(this)){
            startActivity(new Intent(this,LoginAty.class));
            finish();
        }
        personalUrl = Config.URL + Config.loadUser(this).getUsername();
        imageLoader = Downloadimgs.initImageLoader(this);
        initView();
        initEvent();
        initFragment();
    }

    @Override
    protected void onResume() {
        super.onResume();
        User user = Config.loadUser(this);
        if (!StringUtil.isEmpty(user.getUsername())) {
            face.setVisibility(View.VISIBLE);
            String url = Config.URL + "faces/" + Config.loadUser(this).getUsername() + ".jpg";
            Uri uri = Uri.parse(url);
            face.setImageURI(uri);
            bigface.setImageURI(uri);
        }
        JPushInterface.onResume(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        JPushInterface.onPause(this);
    }

    private void initFragment() {
        currentFragment = new RecentFragment();
        getFragmentManager().beginTransaction().add(R.id.layout_fragment, currentFragment).commit();
    }

    private void swicthFragment(Fragment fragment) {
        if (currentFragment != fragment) {
            FragmentTransaction transaction = getFragmentManager().beginTransaction();
            if (!fragment.isAdded()) {
                transaction.hide(currentFragment).add(R.id.layout_fragment, fragment).commit();
            } else {
                transaction.hide(currentFragment).show(fragment).commit();
            }
            currentFragment = fragment;
        }
    }

    private void initEvent() {
        tv_recent.setOnClickListener(this);
        tv_friend.setOnClickListener(this);
        layout_add_friend.setOnClickListener(this);
        layout_search.setOnClickListener(this);
        layout_test.setOnClickListener(this);
        more.setOnClickListener(this);
        face.setOnClickListener(this);

    }

    /*********************************************************/

    private void initView() {
        face = (SimpleDraweeView) findViewById(R.id.face);
        face.setOnClickListener(this);
        tv_recent = (TextView) findViewById(R.id.tv_recent);
        tv_friend = (TextView) findViewById(R.id.tv_friend);
        layout_add_friend = (LinearLayout) findViewById(R.id.layout_add_friend);
        layout_search = (LinearLayout) findViewById(R.id.layout_search);
        layout_test = (LinearLayout) findViewById(R.id.layout_test);
        more = (TextView) findViewById(R.id.more);

        tv_icon_add = (TextView) findViewById(R.id.tv_icon_add);
        tv_icon_search = (TextView) findViewById(R.id.tv_icon_search);
        tv_icon_leave_msg = (TextView) findViewById(R.id.tv_icon_leave_msg);

        iconfont = Typeface.createFromAsset(getAssets(), "fonts/my_iconfont.ttf");
        tv_icon_add.setTypeface(iconfont);
        tv_icon_leave_msg.setTypeface(iconfont);
        iconfont = Typeface.createFromAsset(getAssets(), "fonts/phonemessage.ttf");
        tv_icon_search.setTypeface(iconfont);
        iconfont = Typeface.createFromAsset(getAssets(), "fonts/iconfont.ttf");
        more.setTypeface(iconfont);

        drawerLayout = (DrawerLayout) findViewById(R.id.drawlayout);
        bigface = (SimpleDraweeView) findViewById(R.id.bigface);
        slidlist = (ListView) findViewById(R.id.slidlist);
        slidlist.setOnItemClickListener(this);
        slidlistdata = new ArrayList<>();
        slidlistdata.add("查看通知");
        slidlistdata.add("上传头像");
        slidlistdata.add("个人资料");
        slidlistdata.add("切换账号");
        slidlistdata.add("意见反馈");
        slidlistdata.add("退出登录");
        slidlist.setAdapter(new ArrayAdapter<>(this, R.layout.sub_slid_listview, slidlistdata));
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_recent: {
                tv_recent.setBackgroundResource(R.drawable.shape_left_wite);
                tv_friend.setBackgroundResource(R.drawable.shape_right_blue);
                tv_friend.setTextColor(getResources().getColor(R.color.white));
                tv_recent.setTextColor(getResources().getColor(R.color.holo_blue_bright));
                RecentFragment recentFragment = new RecentFragment();
                swicthFragment(recentFragment);
                break;
            }
            case R.id.tv_friend: {
                User user = Config.loadUser(this);
                Log.i("Test", "用户身份" + user.getRole());
                tv_recent.setBackgroundResource(R.drawable.shape_left_blue);
                tv_friend.setBackgroundResource(R.drawable.shape_right_wite);
                tv_friend.setTextColor(getResources().getColor(R.color.holo_blue_bright));
                tv_recent.setTextColor(getResources().getColor(R.color.white));
                if (user.getRole().equals("普通用户")) {
                    swicthFragment(new FContact());
                } else {
                    swicthFragment(new ContactsGroupFragment());
                }
                break;
            }
            case R.id.layout_add_friend:
                startActivity(new Intent(this, AddFriend.class));
                overridePendingTransition(R.anim.activity_left_in, R.anim.activity_right_out);
                break;
            case R.id.layout_search:
                startActivity(new Intent(this, GroupManager.class));
                overridePendingTransition(R.anim.activity_right_in, R.anim.activity_left_out);
                break;
            case R.id.layout_test:
                startActivity(new Intent(this, MyLeaveMsgActivity.class));
                overridePendingTransition(R.anim.activity_right_in, R.anim.activity_left_out);
                break;
            case R.id.more:
                startActivity(new Intent(this, MoreFunAty.class));
                break;
            case R.id.face:
                drawerLayout.openDrawer(Gravity.LEFT);
                break;
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent = null;
        switch (position) {
            case 0:
                startActivity(new Intent(this, PublishDatasAty.class));
                break;
            case 1:
                intent = new Intent(this, UploadFaceDialog.class);
                startActivityForResult(intent, 0);
                break;
            case 2:
                startActivity(new Intent(this, MyDataAty.class));
                break;
            case 3:
                intent = new Intent(this, LoginAty.class);
                startActivity(intent);
                break;
            case 4:
                intent = new Intent(this, FeedBack.class);
                startActivity(intent);
                break;
            case 5:
                User baseUser = Config.loadBaseUser(this);
                User user = Config.loadUser(this);
                if(baseUser.getUsername().equals(user.getUsername())){
                    Config.registerJPUSH(this, "");
                    Config.saveUser(this, new User("", "", ""));
                    Config.saveBaseUser(this, new User("", "", ""));
                }else{
                    Config.registerJPUSH(this, baseUser.getUsername());
                    Config.saveUser(this, baseUser);
                }
                startActivity(new Intent(this,LoginAty.class));
                finish();
                break;
        }
        drawerLayout.closeDrawers();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data != null) {
            String filePath = data.getAction();
            Bitmap bitmap = BitmapFactory.decodeFile(filePath);
            bigface.setImageBitmap(bitmap);
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN){
            if((System.currentTimeMillis()-exitTime) > 2000){
                ShowToast.show(this, "再按一次退出程序");
                exitTime = System.currentTimeMillis();
            } else {
                Config.registerJPUSH(this, Config.loadBaseUser(this).getUsername());
                finish();
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        imageLoader.clearMemoryCache();
        imageLoader.clearDiskCache();
        System.exit(0);
    }
}
