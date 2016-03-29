package com.jlstudio.publish.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ExpandableListView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.jlstudio.R;
import com.jlstudio.group.bean.Contacts;
import com.jlstudio.group.bean.Groups;
import com.jlstudio.main.application.Config;
import com.jlstudio.main.bean.CatchData;
import com.jlstudio.main.db.DBOption;
import com.jlstudio.main.net.GetDataNet;
import com.jlstudio.main.net.UplaodFace;
import com.jlstudio.main.util.ProgressUtil;
import com.jlstudio.publish.activity.ShowPersonDetailAty;
import com.jlstudio.publish.adapter.FMyContactAdapter;
import com.jlstudio.publish.bean.MyContact;
import com.jlstudio.publish.bean.RegisterAndUnRegister;
import com.jlstudio.publish.dialog.UnRegisterQueryDialog2;
import com.jlstudio.publish.server.SendSMSService;
import com.jlstudio.publish.util.JsonToPubhlishData;
import com.jlstudio.publish.util.StringUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * 高权限进入通讯录显示的内容
 */
public class FMyContact extends Fragment implements ExpandableListView.OnChildClickListener, View.OnClickListener {
    private View view;
    private Button publish;//发布按钮
    private ExpandableListView listView;//显示分组
    private List<Groups> listParent;//年级，自定义分组
    private List<List<Groups>> listChild;//班级，自定义分组
    private FMyContactAdapter adapter;
    private GetDataNet gn;//网络连接
    private DBOption db;//数据库连接
    private CatchData data;//数据库缓存数据
    private List<MyContact> childCount;//persondetailaty中选了多少人；
    private RegisterAndUnRegister ru;//获取已注册和未注册分离后的内容，和未注册的人数
    private boolean isSendAllSelect;//是否发送所有人

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_mycontact, container, false);
        initView();
        gn = GetDataNet.getInstence(getActivity());
        db = new DBOption(getActivity());
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        initData();
    }

    private void initData() {
        ProgressUtil.showProgressDialog(getActivity(), "数据加载中...");
        data = db.getCatch(Config.URL + Config.GROUPS + Config.loadUser(getActivity()).getUsername());
        if (data == null) {
            getDataFromNet();
        } else {
            refreshList(data.getContent());

            ProgressUtil.closeProgressDialog();
        }
    }

    private void initView() {
        publish = (Button) view.findViewById(R.id.publish);
        publish.setOnClickListener(this);
        listView = (ExpandableListView) view.findViewById(R.id.listview);
        listParent = new ArrayList<>();
        childCount = new ArrayList<>();
        adapter = new FMyContactAdapter(getActivity(), listParent, listChild);
        listView.setAdapter(adapter);
        listView.setGroupIndicator(null);
        listView.setOnChildClickListener(this);
    }

    //从网络上获取数据
    private void getDataFromNet() {
        JSONObject json = new JSONObject();
        try {
            json.put("username", Config.loadUser(getActivity()).getUsername());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        gn.getData(Config.URL, Config.GROUPS, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                refreshList(s);
                if (data == null) {
                    db.insertCatch(Config.URL + Config.GROUPS + Config.loadUser(getActivity()).getUsername(), s, System.currentTimeMillis() + "");
                } else {
                    db.updateCatch(Config.URL + Config.GROUPS + Config.loadUser(getActivity()).getUsername(), s, System.currentTimeMillis() + "");
                }
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Log.d("hehe", "获取数据失败");
            }
        }, json.toString());
        ProgressUtil.closeProgressDialog();
    }

    @Override
    public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
        Intent intent = new Intent(getActivity(), ShowPersonDetailAty.class);
        intent.setAction(listChild.get(groupPosition).get(childPosition).getGroup_name());
        intent.putExtra("isSelect", listChild.get(groupPosition).get(childPosition).isSelected());
        startActivity(intent);
        return true;
    }

    @Override
    public void onClick(View v) {
        anaysis();
    }

    /**
     * 把已经选中的信息放入全局变量
     */
    private void anaysis() {
        for (int i = 0; i < listParent.size(); i++) {
            for (int j = 0; j < listChild.get(i).size(); j++) {
                //是否是被选中状态并且没有加入过
                if (listChild.get(i).get(j).isSelected() && !Config.groups.contains(listChild.get(i).get(j))) {
                    Config.groups.add(listChild.get(i).get(j));
                }
            }
        }
        ru = Config.separateRegUnReg();
        if (Config.WP.getType().equals("短信")) {
            isSendAllSelect = true;
            sendPublish("SMS");
        } else if (ru.UnRegCount != 0) {
            //弹出对话框，询问未注册人的处理
            new UnRegisterQueryDialog2(getActivity(), new UnRegisterQueryDialog2.DialogListener() {
                @Override
                public void onResult(int resultCode) {
                    if (resultCode == 1) {
                        isSendAllSelect = true;
                        if (StringUtil.isEmpty(Config.WP.getFilePath())) {
                            sendPublish("all");//全部发送，未注册的用短信
                        } else {
                            sendPublishWithFile("all");
                        }

                    } else {
                        isSendAllSelect = false;
                        if (StringUtil.isEmpty(Config.WP.getFilePath())) {
                            sendPublish("reg");//只给注册的人发送，未注册的忽略
                        } else {
                            sendPublishWithFile("reg");
                        }
                    }
                }
            }, ru.UnRegCount).show();
        }else{
            sendPublish("all");
        }
        for (int i = 0; i < Config.persons.size(); i++) {
            Log.d("haha", Config.persons.get(i).getUsername());//存放个人信息的全局变量
        }
        for (int i = 0; i < Config.groups.size(); i++) {
            Log.d("haha", Config.groups.get(i).getGroup_name());//存放个人信息的全局变量
        }
    }

    private void refreshList(String content) {
        listParent = JsonToPubhlishData.getGroup(content,"高级");//按年级分组
        listChild = JsonToPubhlishData.getGroupSub(content, listParent);//年级中的班级
        adapter.setList(listParent,listChild);
    }

    /**
     * 发送通知
     *
     * @param string 类型，全部还是忽略没有注册的
     */
    private void sendPublish(String string) {
        gn.getData(Config.URL, "publish", new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                if (isSendAllSelect) {
//                    if(ru.unRegister.size()>0){
//                        for(int i=0;i<ru.unRegister.size();i++){
//                            s+=","+ru.unRegister.get(i).getPhone();
//                        }
//                    }
                    //有短信，放在service里发送
                    Intent intent = new Intent(getActivity(), SendSMSService.class);
                    intent.setAction(s);
                    getActivity().startService(intent);
                }
                //一次发送结束，清楚全局变量
                Config.groups.clear();
                Config.persons.clear();
                getActivity().finish();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
            }
        }, getJson(string));
    }
    //带文件的消息
    private void sendPublishWithFile(String string) {
        UplaodFace.sendPhoto(Config.URL, "publish", new UplaodFace.Success() {
            @Override
            public void onSuccess(String s) {
                Log.d("success", "success");
                if (isSendAllSelect) {
//                    if(ru.unRegister.size()>0){
//                        for(int i=0;i<ru.unRegister.size();i++){
//                            s+=","+ru.unRegister.get(i).getPhone();
//                        }
//                    }
                    Intent intent = new Intent(getActivity(), SendSMSService.class);
                    intent.setAction(s);
                    getActivity().startService(intent);
                }
                Config.groups.clear();
                Config.persons.clear();
                Config.WP.setFilePath("");
                getActivity().finish();
            }
        }, new UplaodFace.Failure() {
            @Override
            public void onFailure() {
                Log.d("success", "Failure");
            }
        }, getJson(string), Config.WP.getFilePath());
    }

    private String getJson(String status) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("status", status);
            jsonObject.put("username", Config.loadUser(getActivity()).getUsername());
            //打包发送的内容
            JSONObject jsonPublish = new JSONObject();
            if (!StringUtil.isEmpty(Config.WP.getTitle())) {
                jsonPublish.put("title", Config.WP.getTitle());
            }
            if (!StringUtil.isEmpty(Config.WP.getContent())) {
                jsonPublish.put("content", Config.WP.getContent());
            }
            jsonObject.put("publish", jsonPublish);
            JSONArray array1 = new JSONArray();
            //对选择的组信息打包json
            for (int i = 0; i < Config.groups.size(); i++) {
                JSONObject o = new JSONObject();
                o.put("groupName", Config.groups.get(i).getGroup_name());
                array1.put(o);
            }
            //对选择的具体人信息打包json
            JSONArray array2 = new JSONArray();
            for (int i = 0; i < ru.register.size(); i++) {
                JSONObject o = new JSONObject();
                o.put("username", Config.groups.get(i).getGroup_name());
                array2.put(o);
            }
            jsonObject.put("group", array1);
            jsonObject.put("person", array2);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.d("FMyContact",jsonObject.toString());
        return jsonObject.toString();
    }
}
