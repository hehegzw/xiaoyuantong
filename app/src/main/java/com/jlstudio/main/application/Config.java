package com.jlstudio.main.application;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Base64;
import android.util.Log;

import com.jlstudio.group.bean.Contacts;
import com.jlstudio.group.bean.Groups;
import com.jlstudio.main.activity.LoginAty;
import com.jlstudio.main.bean.User;
import com.jlstudio.publish.bean.MyContact;
import com.jlstudio.publish.bean.RegisterAndUnRegister;
import com.jlstudio.publish.bean.WritePublish;
import com.jlstudio.publish.util.StringUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import cn.jpush.android.api.JPushInterface;
import cn.jpush.android.api.TagAliasCallback;

/**
 * Created by gzw on 2015/10/20.
 */
public class Config {
    //网络相关4
    //public static final String URL = "http://192.168.0.100:8080/xyt/";//台式
    //public static final String URL = "http://192.168.0.120:8080/xyt/";//chengzi
    public static final String URL = "http://121.42.12.12:8080/xyt/";//ali
    public static final String QINIUURL = "http://7xslom.com2.z0.glb.clouddn.com/";
    public static final String QINIFILE = "http://7xslom.com2.z0.glb.clouddn.com/";
    public static final String RECMSG = "recmsgs";//我收到的通知列表
    public static final String GETSENDMESSAGEBYID = "sendmsg";//我发送的通知具体内容
    public static final String GETRECEIVEMESSAGEBYID = "recmsg";//我收到的通知具体内容
    public static final String SENDMSG = "sendmsgs";//我发送的通知列表
    public static final String GETDEPARTMENT = "GetDepartment";//获取系别
    public static final String GETTYPE = "GetType";//获取所有人的分组
    public static final String GETPERSON = "GetPerson";//获取具体组下的所有人
    public static final String SENDPUBLISH = "SendPublish";//发送通知
    public static final String GETUNREPLYPERSON = "unreplyphone";//获取未收到人的信息
    public static final String GETPHONE = "GetPhone";//获取手机号
    public static final String NOTICECONFIRM = "noticeconfirm";//回复通知
    public static final String DELETENOTICE = "deletenotice";//删除通知
    public static final String DELETEMYSENDNOTICE = "deletenotice";//删除发送的通知
    public static final String GROUPS = "groups";//删除发送的通知
    public static String SCHEDULE = "course";//获取课表信息
    public static String GETSCORE = "score";//获取成绩信息
    public static String CETSCORE = "cet";//查询46级
    public static String LOGIN = "login";//登录
    public static String UPLOADFACE = "uploadface";//上传头像
    public static String GETUSERINFO = "profile";//获取个人信息
    public static String SENDUSERINFO = "updateinfo";//修改个人信息
    public static String UPDATEPWD = "UpdatePwd";//修改密码
    public static final String GETLISTLOST = "GetListLost";//获取全部失物招领信息
    public static final String GETLISTCOMMONS = "ListContent";//获取该条目全部的评论信息
    public static final String UPLOADLOST = "UploadLost";//上传失物招领
    public static final String UPCOMMON = "PostContent";//上传评论信息
    public static final String GETCONTACT = "GetContact";//获取联系人
    public static final String GETCONTACTGROUP = "GetContactGroup";//获取联系人
    public static final String GETCLASSCONTACT = "GetClassContact";//获取分组的具体人员
    public static final String SEARCH_NET = "SearchNet";//随机&精确搜索好友

    //文件相关
    public static final String FILENAME = "share";//主要存放app内部各activity之间需要传送的数据
    public static final String USERFILE = "user";//存放登录用户的信息
    public static final String FILETOKEN = "U-xnzz4CMEqM8lSW1HisWIDflYDvIHlyWTCIigAm:0WxAWGK2dZ5FSqJ-67gIH21V6Is=:eyJzY29wZSI6ImNvbS1qbHN0dWRpby1maWxlIiwiZGVhZGxpbmUiOjMyMzgyMzQyMDB9";
    //缓存时间
    public static final int CATCHTIME = 1000*60 ;
    //意图返回码
    public static final int REQUEST_CONDE_CAMERA = 1;//从相机获取图片
    public static final int REQUEST_CONDE_ALBUM = 2;//从相册获取图片
    public static final int DELETE_RECEIVE_MSG =1;//删除收到的通知
    public static final int DELETE_SEND_MSG =1;//删除发送的通知

    //通知
    public static List<MyContact> persons = new ArrayList<>();//通知选择的个人对象
    public static List<Groups> groups = new ArrayList<>();//通知选择的组对象
    public static WritePublish WP = new WritePublish();//通知内容
    //是否支持webp
    public static int NOT_INIT = 0;//未初始化
    public static int SUPPORT = 1;//支持
    public static int NOT_SUPPORT = 2;//不支持


    /**
     * 保存已登录的账号信息
     * @param context
     * @param user
     */
    public static void saveUser(Context context,User user) {
        SharedPreferences share = context.getSharedPreferences(USERFILE, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = share.edit();
        if(user == null){
            editor.clear();
            return;
        }
        editor.putString("username",user.getUsername());
        editor.putString("password",user.getPassword());
        editor.putString("role",user.getRole());
        editor.putBoolean("isOnline", user.isOnline());
        editor.commit();
    }
    public static void saveBaseUser(Context context,User user) {
        SharedPreferences share = context.getSharedPreferences(USERFILE, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = share.edit();
        if(user == null){
            editor.clear();
            return;
        }
        editor.putString("baseusername",user.getUsername());
        editor.putString("basepassword",user.getPassword());
        editor.putString("baserole", user.getRole());
        editor.commit();
    }

    /**
     * 获取已登录的账号信息
     * @param context
     * @return
     */
    public static User loadUser(Context context) {
        SharedPreferences share = context.getSharedPreferences(USERFILE,Context.MODE_PRIVATE);
        return new User(share.getString("username",""),share.getString("password",""),share.getString("role",""));
    }
    public static User loadBaseUser(Context context) {
        SharedPreferences share = context.getSharedPreferences(USERFILE,Context.MODE_PRIVATE);
        return new User(share.getString("baseusername",""),share.getString("basepassword",""),share.getString("baserole",""));
    }

    /**
     * 判断是否是首次登录
     * @param context
     * @return
     */
    public static boolean isFirst(Context context) {
        SharedPreferences share = context.getSharedPreferences(FILENAME, Context.MODE_PRIVATE);
        boolean flag = share.getBoolean("isfirst", true);
        if(flag == true){
            SharedPreferences.Editor editor = share.edit();
            editor.putBoolean("isfirst", false);
            editor.commit();
            return true;
        }
        return false;
    }

    /**
     * 判断网络是否可用
     * @param activity
     * @return
     */
    public static boolean isNetworkAvailable(Activity activity){
        Context context = activity.getApplicationContext();
        // 获取手机所有连接管理对象（包括对wi-fi,net等连接的管理）
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        if (connectivityManager == null){
            return false;
        }else{
            // 获取NetworkInfo对象
            NetworkInfo[] networkInfo = connectivityManager.getAllNetworkInfo();

            if (networkInfo != null && networkInfo.length > 0){
                for (int i = 0; i < networkInfo.length; i++){
                    System.out.println(i + "===状态===" + networkInfo[i].getState());
                    System.out.println(i + "===类型===" + networkInfo[i].getTypeName());
                    // 判断当前网络状态是否为连接状态
                    if (networkInfo[i].getState() == NetworkInfo.State.CONNECTED){
                        return true;
                    }
                }
            }
        }
        return false;
    }

    /**
     * 判断是否登录
     * @param context
     * @return
     */
    public static boolean isLogin(Context context){
        User user = loadUser(context);
        User baseUser = loadBaseUser(context);
        if (StringUtil.isEmpty(user.getUsername())) {
            if(StringUtil.isEmpty(baseUser.getUsername())){
                return false;
            }else{
                saveUser(context,baseUser);
                return true;
            }
        }else{
            return true;
        }
    }

    /**
     * 向极光注册此用户
     * @param context
     * @param username 用户名
     */
    public static void registerJPUSH(Context context,String username){
        JPushInterface.setAlias(context,username, new TagAliasCallback() {
            @Override
            public void gotResult(int i, String s, Set<String> set) {
                Log.d("JPushInterface", i + "");
            }
        });
    }
    public static void clearWP(){
        WP.setFileName("");
        WP.setFilePath("");
        WP.setContent("");
        WP.setTitle("");
        WP.setType("");
    }
}
