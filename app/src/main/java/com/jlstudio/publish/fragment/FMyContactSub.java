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
import com.jlstudio.main.net.DownLoadFile;
import com.jlstudio.main.net.GetDataNet;
import com.jlstudio.main.net.UplaodFace;
import com.jlstudio.main.util.ProgressUtil;
import com.jlstudio.publish.adapter.FMyContactAdapter;
import com.jlstudio.publish.adapter.FMyContactSubAdapter;
import com.jlstudio.publish.bean.MyContact;
import com.jlstudio.publish.bean.RegisterAndUnRegister;
import com.jlstudio.publish.dialog.UNRegisterQueryDialog;
import com.jlstudio.publish.dialog.UnRegisterQueryDialog2;
import com.jlstudio.publish.server.SendSMSService;
import com.jlstudio.publish.util.JsonToPubhlishData;
import com.jlstudio.publish.util.ShowToast;
import com.jlstudio.publish.util.StringUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.security.Policy;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by gzw on 2015/11/22.
 */
public class FMyContactSub extends Fragment implements ExpandableListView.OnChildClickListener, View.OnClickListener {
    private View view;
    private Button publish;
    private ExpandableListView listView;
    private List<Groups> listParent;
    private List<List<Contacts>> listChild;
    private FMyContactSubAdapter adapter;
    private GetDataNet gn;
    private DBOption db;//数据库连接
    private CatchData data;//数据库缓存数据
    private int parentIndex;//当前点击的人的父位置
    private int index;//当前点击的人的位置
    private RegisterAndUnRegister ru;//获取已注册和未注册分离后的内容，和未注册的人数
    private boolean isSendAllSelect;//是否发送所有人

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_mycontact, container, false);
        listView = (ExpandableListView) view.findViewById(R.id.listview);
        publish = (Button) view.findViewById(R.id.publish);
        publish.setOnClickListener(this);
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
        ProgressUtil.showProgressDialog(getContext(),"数据加载中...");
        CatchData data = db.getCatch(Config.URL + Config.GROUPS + Config.loadUser(getActivity()).getUsername());
        if (data == null) {
            getDataFromNet();
        } else {
            refreshList(data.getContent());
            initView();
            ProgressUtil.closeProgressDialog();
        }
    }

    private void initView() {
        listView = (ExpandableListView) view.findViewById(R.id.listview);
        adapter = new FMyContactSubAdapter(getActivity(), listParent, listChild);
        listView.setAdapter(adapter);
        listView.setGroupIndicator(null);
        listView.setOnChildClickListener(this);
    }

    @Override
    public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
        if (listChild.get(groupPosition).get(childPosition).isSelected()) {
            listChild.get(groupPosition).get(childPosition).setIsSelected(false);
        } else if (!listChild.get(groupPosition).get(childPosition).isSelected()) {
            listChild.get(groupPosition).get(childPosition).setIsSelected(true);
        }
        adapter.setListChild(listChild);
        return true;
    }

    /**
     * 从网络上获取数据
     */
    private void getDataFromNet() {
        JSONObject json = new JSONObject();
        try {
            json.put("username", Config.loadUser(getContext()).getUsername());
            json.put("role", Config.loadUser(getActivity()).getRole());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        gn.getData(Config.URL, Config.GROUPS, new Response.Listener<String>() {

            @Override
            public void onResponse(String s) {
                refreshList(s);
                initView();
                if (data == null) {
                    db.insertCatch(Config.URL + Config.GROUPS + Config.loadUser(getActivity()).getUsername(), s, System.currentTimeMillis() + "");
                } else {
                    db.updateCatch(Config.URL + Config.GROUPS + Config.loadUser(getActivity()).getUsername(), s, System.currentTimeMillis() + "");
                }
                ProgressUtil.closeProgressDialog();
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError volleyError) {
                publish.setClickable(true);
                Log.d("hehe", "发布失败");
                ProgressUtil.closeProgressDialog();
            }
        }, json.toString());

    }

    @Override
    public void onClick(View v) {
        publish.setClickable(false);
        anaysis();
    }

    private void anaysis() {

        for (int i = 0; i < listParent.size(); i++) {
            for (int j = 0; j < listChild.get(i).size(); j++) {
                if (listChild.get(i).get(j).isSelected() && !Config.persons.contains(listChild.get(i).get(j))) {
                    Config.persons.add(listChild.get(i).get(j));
                }
            }
        }
        if(Config.persons.size() == 0 && Config.groups.size() == 0){
            ShowToast.show(getContext(),"请选择发送对象");
            publish.setClickable(true);
            return;
        }
        //分离注册和未注册的人
        ru = Config.separateRegUnReg();
        if(ru.UnRegCount!=0) {
            //弹出对话框，询问未注册人的处理
            new UnRegisterQueryDialog2(getActivity(), new UnRegisterQueryDialog2.DialogListener() {
                @Override
                public void onResult(int resultCode) {
                    ProgressUtil.showProgressDialog(getContext(),"发送中...");
                    if(resultCode == 1){
                        isSendAllSelect = true;
                        if(StringUtil.isEmpty(Config.WP.getFilePath())){
                            sendPublish("all");
                        }else{
                            sendPublishWithFile("all");
                        }

                    }else{
                        isSendAllSelect = false;
                        if(StringUtil.isEmpty(Config.WP.getFilePath())){
                            sendPublish("reg");
                        }else{
                            sendPublishWithFile("reg");
                        }
                    }
                }
            }, ru.UnRegCount).show();
        }else{
            ProgressUtil.showProgressDialog(getContext(),"发送中...");
            if(StringUtil.isEmpty(Config.WP.getFilePath())){
                sendPublish("all");
            }else{
                sendPublishWithFile("all");
            }
        }

//        for (int i = 0; i < Config.persons.size(); i++) {
//            Log.d("haha", Config.persons.get(i).getUsername());//存放个人信息的全局变量
//        }
//        for (int i = 0; i < Config.groups.size(); i++) {
//            Log.d("haha", Config.groups.get(i).getGroup_name());//存放个人信息的全局变量
//        }
    }

    private void refreshList(String content) {
        listParent = JsonToPubhlishData.getGroup(content,"普通");
        listChild = JsonToPubhlishData.getContacts(content, listParent);
        //adapter.setList(listParent, listChild);
    }
    /**
     * 发送通知
     * @param string 类型，全部还是忽略没有注册的
     */
    private void sendPublish(String string){
        gn.getData(Config.URL, "publish", new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                if (isSendAllSelect) {
//                    if(ru.unRegister.size()>0){
//                        for(int i=0;i<ru.unRegister.size();i++){
//                            s+=","+ru.unRegister.get(i).getPhone();
//                        }
//                    }
                    Intent intent = new Intent(getActivity(), SendSMSService.class);
                    intent.setAction(s);
                    getActivity().startService(intent);
                    ShowToast.show(getContext(), "发送成功");
                }
                Config.groups.clear();
                Config.persons.clear();
                ProgressUtil.closeProgressDialog();
                getActivity().finish();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                ShowToast.show(getContext(), "发送失败");
                ProgressUtil.closeProgressDialog();
            }
        }, getJson(string));
    }
    private void sendPublishWithFile(String string){
        UplaodFace.sendPhoto(Config.URL, "publish", new UplaodFace.Success() {
            @Override
            public void onSuccess(String s) {
                JSONObject json;
                String[] phones;
                try {
                    json = new JSONObject(s);
                    if (json.getString("status").equals("1")) {
                        phones = json.getString("phoneList").split(",");
                        for (String phone : phones) {
                            Log.d("hehe", phone + "\n");
                        }
                    }
                    ShowToast.show(getContext(), "发送成功");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
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
                ProgressUtil.closeProgressDialog();
                getActivity().finish();
            }
        }, new UplaodFace.Failure() {
            @Override
            public void onFailure() {
                ProgressUtil.closeProgressDialog();
                ShowToast.show(getContext(), "发送失败");
            }
        }, getJson(string), Config.WP.getFilePath());
    }
    private String getJson(String status){
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("status",status);
            jsonObject.put("username",Config.loadUser(getContext()).getUsername());
            //打包发送的内容
            JSONObject jsonPublish = new JSONObject();
            if(!StringUtil.isEmpty(Config.WP.getTitle())){
                jsonPublish.put("title",Config.WP.getTitle());
            }
            if(!StringUtil.isEmpty(Config.WP.getContent())){
                jsonPublish.put("content",Config.WP.getContent());
            }
            jsonObject.put("publish",jsonPublish);
            JSONArray array1 = new JSONArray();
            //对选择的组信息打包json
            for(int i=0;i<Config.groups.size();i++){
                JSONObject o = new JSONObject();
                o.put("groupName",Config.groups.get(i).getGroup_name());
                array1.put(o);
            }
            //对选择的具体人信息打包json
            JSONArray array2 = new JSONArray();
            for(int i=0;i<ru.register.size();i++){
                JSONObject o = new JSONObject();
                o.put("username",ru.register.get(i).getUsername());
                array2.put(o);
            }
            jsonObject.put("group",array1);
            jsonObject.put("person",array2);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject.toString();
    }
    private void sendByMsg(String string){

    }
}
