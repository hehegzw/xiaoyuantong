package com.jlstudio.publish.fragment;

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
import com.qiniu.android.http.ResponseInfo;
import com.qiniu.android.storage.UpCompletionHandler;
import com.qiniu.android.storage.UpProgressHandler;
import com.qiniu.android.storage.UploadManager;
import com.qiniu.android.storage.UploadOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
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
    private List<List<MyContact>> listChild;
    private FMyContactSubAdapter adapter;
    private GetDataNet gn;
    private DBOption db;//数据库连接
    private CatchData data;//数据库缓存数据
    private int parentIndex;//当前点击的人的父位置
    private int index;//当前点击的人的位置
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
        ProgressUtil.showProgressDialog(getContext(), "数据加载中...");
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
        ProgressUtil.showProgressDialog(getContext(),"发送中...");
        publish.setClickable(false);
        new Thread(){
            @Override
            public void run() {
                super.run();
                JSONObject json = getJson();
                Message msg = new Message();
                msg.obj = json;
                handler.sendMessage(msg);
            }
        }.start();
    }


    private void refreshList(String content) {
        listParent = JsonToPubhlishData.getGroup(content, "普通");
        listChild = JsonToPubhlishData.getContacts(content, listParent);
        //adapter.setList(listParent, listChild);
    }

    /**
     * 发送通知
     *
     */
    private void sendPublish(final JSONObject json) {
        String filePath = Config.WP.getFilePath();
        String fileName = Config.WP.getFileName();
        if(!StringUtil.isEmpty(filePath)){
            UploadManager uploadManager = new UploadManager();
            final UploadOptions options = new UploadOptions(null, null, false, new UpProgressHandler() {
                @Override
                public void progress(String key, double percent) {
                    Log.d("qiniuinfo",percent+"");
                }
            },null);
            File file = new File(Config.WP.getFilePath());
            if(!file.exists()) try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
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
    private void sendToServer(JSONObject json) {
        gn.getData(Config.URL, "publish", new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                if (isSendAllSelect) {
                    if(ru.unRegister.size()>0){
                        for(int i=0;i<ru.unRegister.size();i++){
                            s+=","+ru.unRegister.get(i).getPhone();
                        }
                    }
                    Intent intent = new Intent(getActivity(), SendSMSService.class);
                    intent.setAction(s);
                    getActivity().startService(intent);
                    ShowToast.show(getContext(), "发送成功");
                }
                ProgressUtil.closeProgressDialog();
                Config.clearWP();
                getActivity().finish();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                ShowToast.show(getContext(), "发送失败");
                ProgressUtil.closeProgressDialog();
            }
        }, json.toString());
    }

    private JSONObject getJson() {
        JSONObject jsonObject = new JSONObject();
        JSONArray persons = new JSONArray();
        int unRegisterCount = 0;
        for(int i=0;i<listChild.size();i++){
            for(int j=0;j<listChild.get(i).size();j++){
                MyContact mc = listChild.get(i).get(j);
                if(mc.isSelected()){
                    if(!mc.isRegister()){
                        unRegisterCount++;
                    }
                    persons.put(mc.getUid());
                }
            }
        }
        try {
            if(persons.length()==0){
                jsonObject.put("ishasperson",false);
            }else{
                jsonObject.put("ishasperson",true);
            }
            jsonObject.put("persons",persons);
            jsonObject.put("unregister",unRegisterCount);
            //jsonObject.put("status", status);
            jsonObject.put("username", Config.loadUser(getContext()).getUsername());
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

    private void sendByMsg(String string) {

    }
}
