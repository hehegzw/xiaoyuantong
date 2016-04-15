package com.jlstudio.publish.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
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
import com.jlstudio.publish.util.ShowToast;
import com.jlstudio.publish.util.StringUtil;
import com.qiniu.android.http.ResponseInfo;
import com.qiniu.android.storage.UpCompletionHandler;
import com.qiniu.android.storage.UpProgressHandler;
import com.qiniu.android.storage.UploadManager;
import com.qiniu.android.storage.UploadOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 高权限进入通讯录显示的内容
 */
public class FMyContact extends Fragment implements ExpandableListView.OnChildClickListener, View.OnClickListener {
    private View view;
    private Button publish;//发布按钮
    private ExpandableListView listView;//显示分组
    private List<Groups> listParent;//年级，自定义分组
    private List<List<Groups>> listChild;//班级，自定义分组
    private Map<String,List<MyContact> > class_person;//班级对应的人数
    private FMyContactAdapter adapter;
    private GetDataNet gn;//网络连接
    private DBOption db;//数据库连接
    private CatchData data;//数据库缓存数据
    private List<MyContact> childCount;//persondetailaty中选了多少人；
    private RegisterAndUnRegister ru;//获取已注册和未注册分离后的内容，和未注册的人数
    private boolean isSendAllSelect;//是否发送所有人
    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            final JSONObject json = (JSONObject) msg.obj;
            boolean ishasperson = false;
            int unregisterCount = 0;
            try {
                ishasperson = json.getBoolean("ishasperson");
                unregisterCount = json.getInt("unregister");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            if(!ishasperson){
                ShowToast.show(getContext(),"请选择发送对象");
                publish.setClickable(true);
                return;
            }
            if (Config.WP.getType().equals("短信")) {
                isSendAllSelect = true;
                try {
                    json.put("status", "sms");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                sendPublish(json);
            } else if (unregisterCount!=0) {
                //弹出对话框，询问未注册人的处理
                new UnRegisterQueryDialog2(getActivity(), new UnRegisterQueryDialog2.DialogListener() {
                    @Override
                    public void onResult(int resultCode) {
                        if (resultCode == 1) {
                            isSendAllSelect = true;
                            try {
                                json.put("status", "all");
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            sendPublish(json);//全部发送，未注册的用短信

                        } else {
                            isSendAllSelect = false;
                            try {
                                json.put("status", "all");
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            sendPublish(json);//只给注册的人发送，未注册的忽略
                        }
                    }
                }, unregisterCount).show();
            }else{
                try {
                    json.put("status", "all");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                sendPublish(json);
            }

        }
    };

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_mycontact, container, false);
        initView();
        gn = GetDataNet.getInstence(getActivity());
        db = new DBOption(getActivity());
        initData();
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    private void initData() {
        class_person = new HashMap<>();
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
        listView = (ExpandableListView) view.findViewById(R.id.listview);
        listParent = new ArrayList<>();
        childCount = new ArrayList<>();
        adapter = new FMyContactAdapter(getActivity(), listParent, listChild);
        listView.setAdapter(adapter);
        listView.setGroupIndicator(null);
        initEvent();
    }

    private void initEvent() {
        publish.setOnClickListener(this);
        listView.setOnChildClickListener(this);
        adapter.setListener(new FMyContactAdapter.ChildListener() {
            @Override
            public void click(int groupIndex, int childIndex) {
                Groups clazz = listChild.get(groupIndex).get(childIndex);
                List<MyContact> tempPersons = class_person.get(clazz.getGroup_name());
                if(tempPersons!=null){
                    if(clazz.isSelected()){
                        for(int i=0;i<tempPersons.size();i++){
                            tempPersons.get(i).setIsSelected(true);
                        }
                    }else{
                        for(int i=0;i<tempPersons.size();i++){
                            tempPersons.get(i).setIsSelected(false);
                        }
                    }
                }
            }
        });
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
                ProgressUtil.closeProgressDialog();
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Log.d("hehe", "获取数据失败");
                ProgressUtil.closeProgressDialog();
            }
        }, json.toString());

    }

    @Override
    public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
        Intent intent = new Intent(getActivity(), ShowPersonDetailAty.class);
        intent.setAction(listChild.get(groupPosition).get(childPosition).getGroup_name());
        intent.putExtra("isSelect", listChild.get(groupPosition).get(childPosition).isSelected());
        intent.putExtra("groupPosition",groupPosition);
        intent.putExtra("childPosition",childPosition);
        List<MyContact> tempPersons = class_person.get(listChild.get(groupPosition).get(childPosition).getGroup_name());
        if(tempPersons!=null){
            intent.putExtra("persons", (Serializable) tempPersons);
        }
        startActivityForResult(intent, Activity.RESULT_FIRST_USER);
        return true;
    }

    @Override
    public void onClick(View v) {
        ProgressUtil.showProgressDialog(getContext(),"发送中...");
        new Thread(){
            @Override
            public void run() {
                super.run();
                JSONObject json = getJsons();
                Message msg = new Message();
                msg.obj = json;
                handler.sendMessage(msg);
            }
        }.start();
    }

    private void refreshList(String content) {
        listParent = JsonToPubhlishData.getGroup(content,"高级");//按年级分组
        listChild = JsonToPubhlishData.getGroupSub(content, listParent);//年级中的班级
        adapter.setList(listParent,listChild);
    }

    /**
     * 发送通知
     *
     */
    private void sendPublish(final JSONObject json) {
        String filePath = Config.WP.getFilePath();
        if(!StringUtil.isEmpty(filePath)){
            UploadManager uploadManager = new UploadManager();
            final UploadOptions options = new UploadOptions(null, null, false, new UpProgressHandler() {
                @Override
                public void progress(String key, double percent) {
                    Log.d("qiniuinfo",percent+"");
                }
            },null);
            File file = new File(Config.WP.getFilePath());
            uploadManager.put(file, null, Config.FILETOKEN, new UpCompletionHandler() {
                @Override
                public void complete(String key, ResponseInfo info, JSONObject response) {
                    String infos = key+"\n"+info+"\n"+response;
                    try {
                        json.getJSONObject("publish").put("filePath",response.get("key")+"$"+Config.WP.getFileName());
                        sendToServer(json);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    Log.d("qiniuinfo",infos);
                }
            },options);
        }else{
            sendToServer(json);
        }

    }
    private void sendToServer(JSONObject json){
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
//                    Intent intent = new Intent(getActivity(), SendSMSService.class);
//                    intent.setAction(s);
//                    getActivity().startService(intent);
                }
                //一次发送结束，清楚全局变量
                Config.clearWP();
                getActivity().finish();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
            }
        }, json.toString());
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(data!=null){
            List<MyContact> temp = (List<MyContact>) data.getSerializableExtra("persons");
            class_person.put(data.getAction(),temp);
            int groupPosition = data.getIntExtra("groupPosition",0);
            int childPosition = data.getIntExtra("childPosition",0);
            listChild.get(groupPosition).get(childPosition).setIsSelected(data.getBooleanExtra("isGroupSelect",false));
            adapter.notifyDataSetChanged();
        }
    }
    private JSONObject getJsons(){
        JSONObject jsonObject = new JSONObject();
        JSONArray groupJson = new JSONArray();
        JSONArray classJson = new JSONArray();
        JSONArray persons = new JSONArray();
        List<MyContact> temp;
        int unRegisterCount = 0;
        for(int i=0;i<listChild.size();i++){
            Groups group = listParent.get(i);
            //我的分组，或者xx级被选中，则把其中的分组或者班级名放入json
            if(group.isSelected()){
                unRegisterCount+= group.getSubCounts()-group.getRegisterCount();
                for(int k=0;k<listChild.get(i).size();k++){
                    if(i==0){
                        groupJson.put(listChild.get(i).get(k).getGroup_name());
                    }else{
                        classJson.put(listChild.get(i).get(k).getGroup_name());
                    }
                }
            }else{
                for(int j=0;j<listChild.get(i).size();j++){
                    Groups groups = listChild.get(i).get(j);
                    //单个组或班级被选中
                    if(groups.isSelected()){
                        unRegisterCount+=groups.getSubCounts()-groups.getRegisterCount();
                        if(i==0){
                            groupJson.put(listChild.get(i).get(j).getGroup_name());
                        }else{
                            classJson.put(listChild.get(i).get(j).getGroup_name());
                        }
                    }else{//如果组或班级没有被选中，那看具体的人是不是被选中
                        temp = class_person.get(listChild.get(i).get(j).getGroup_name());
                        if(temp!=null){
                            for(int s=0;s<temp.size();s++){
                                if(temp.get(s).isSelected()){
                                    if(!temp.get(s).isRegister()){
                                        unRegisterCount++;
                                    }
                                    persons.put(temp.get(s).getUid());
                                }
                            }
                            temp = null;
                        }
                    }
                }
            }

        }
        try {
            jsonObject.put("groupName",groupJson);
            jsonObject.put("className",classJson);
            jsonObject.put("persons",persons);
            //jsonObject.put("status", status);
            jsonObject.put("username", Config.loadUser(getActivity()).getUsername());
            jsonObject.put("unregister",unRegisterCount);
            if(groupJson.length() == 0 && classJson.length() == 0&&persons.length()==0){
                jsonObject.put("ishasperson",false);
            }else{
                jsonObject.put("ishasperson",true);
            }
            //打包发送的内容
            JSONObject jsonPublish = new JSONObject();
            if (!StringUtil.isEmpty(Config.WP.getTitle())) {
                jsonPublish.put("title", Config.WP.getTitle());
            }
            if (!StringUtil.isEmpty(Config.WP.getContent())) {
                jsonPublish.put("content", Config.WP.getContent());
            }
            jsonObject.put("publish", jsonPublish);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject;
    }
}
