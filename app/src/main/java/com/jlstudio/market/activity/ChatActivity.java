package com.jlstudio.market.activity;

import android.app.Activity;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.jlstudio.R;
import com.jlstudio.main.application.Config;
import com.jlstudio.main.net.GetDataNet;
import com.jlstudio.market.adapter.ChatAdapter;
import com.jlstudio.market.bean.Chat;
import com.jlstudio.market.bean.GoodsDetail;
import com.jlstudio.publish.util.ShowToast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ChatActivity extends Activity implements View.OnClickListener {
    private RecyclerView recyclerView;
    private TextView back;
    private TextView name;
    private List<Chat> chats;
    private ChatAdapter adapter;
    private EditText text;
    private Button submit;
    private String userId;
    private GetDataNet gn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        gn = GetDataNet.getInstence(this);
        initView();
        initRecycleView();
    }

    @Override
    protected void onResume() {
        super.onResume();
        initData();
    }

    private void initView() {
        text = (EditText) findViewById(R.id.text);
        submit = (Button) findViewById(R.id.submit);
        back = (TextView) findViewById(R.id.back);
        name = (TextView) findViewById(R.id.title_name);
        Typeface iconfont = Typeface.createFromAsset(getAssets(), "fonts/iconfont.ttf");
        back.setTypeface(iconfont);
        initEvent();
    }

    private void initRecycleView() {
        recyclerView = (RecyclerView) findViewById(R.id.recycleView);
        chats = new ArrayList<>();
        adapter = new ChatAdapter(chats, this);
        recyclerView.setAdapter(adapter);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    private void initEvent() {
        back.setOnClickListener(this);
        submit.setOnClickListener(this);
        text.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() != 0) {
                    submit.setBackgroundResource(R.drawable.chat_submit);
                } else {
                    submit.setBackgroundResource(R.drawable.chat_unsubmit);
                }
            }
        });
    }

    private void initData() {
        String string = getIntent().getAction();
        if(string!=null){
            try {
                JSONObject json = new JSONObject(string);
                userId = json.getString("noticefromuserid");
                name.setText(json.getString("noticefrom"));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }else{
            userId = getIntent().getStringExtra("userId");
            name.setText(getIntent().getStringExtra("name"));
        }
        getDataFromNet();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back:
                finish();
                break;
            case R.id.submit:
                sendData();
                break;
        }
    }
    private void getDataFromNet(){
        JSONObject json = new JSONObject();
        try {
            json.put("userfrom",Config.loadUser(this).getUsername());
            json.put("userto",userId);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        gn.getData(Config.URL, "getchathistorys", new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                getChats(s);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                ShowToast.show(ChatActivity.this,"获取聊天内容失败");
            }
        },json.toString());
    }
    private void sendData(){
        JSONObject json = new JSONObject();
        String string = text.getText().toString();
        if(TextUtils.isEmpty(string)){
            ShowToast.show(this,"内容不能为空");
            return ;
        }
        try {
            json.put("text",string);
            json.put("userfrom",Config.loadUser(this).getUsername());
            json.put("userto",userId);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        gn.getData(Config.URL, "chat", new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
               ShowToast.show(ChatActivity.this,"发送成功");
                refershChats();
                text.setText("");
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                ShowToast.show(ChatActivity.this,"发送失败");
            }
        },json.toString());
    }

    private void refershChats() {
        Chat chat = new Chat();
        chat.setText(text.getText().toString());
        chat.setUserfrom(Config.loadUser(this).getUsername());
        chat.setUserto(userId);
        chats.add(chat);
        adapter.notifyDataSetChanged();
    }

    private void getChats(String string){
        chats.clear();
        try {
            JSONArray array = new JSONArray(string);

            JSONObject json ;
            for(int i=0;i<array.length();i++){
                Chat chat = new Chat();
                json = (JSONObject) array.get(i);
                chat.setUserfrom(json.getString("userfrom"));
                chat.setUserto(json.getString("userto"));
                chat.setText(json.getString("text"));
                chats.add(chat);
            }
            adapter.notifyDataSetChanged();
            recyclerView.scrollToPosition(chats.size()-1);
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }
}
