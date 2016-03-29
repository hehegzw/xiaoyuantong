package com.jlstudio.market.activity;

import android.app.Activity;
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
        GoodsDetail good = (GoodsDetail) getIntent().getSerializableExtra("good");
        userId = good.getUserid();
        name.setText(good.getUsername());
        getDataFromNet();
        for (int i = 0; i < 20; i++) {
            if (i % 2 == 0)
                chats.add(new Chat(0 + "", "hehehedfsdfsd"));
            else
                chats.add(new Chat(1 + "", "hehehe"));
        }
        for (int i = 0; i < 20; i++) {
            Log.d("Chat", chats.get(i).getUserid());
        }
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back:
                finish();
                break;
        }
    }
    private void getDataFromNet(){
        JSONObject json = new JSONObject();
        String string = text.getText().toString();
        if(TextUtils.isEmpty(string)){
            ShowToast.show(this,"内容不能为空");
            return ;
        }
        try {
            json.put("text",text);
            json.put("fromUser",Config.loadUser(this).getUsername());
            json.put("toUser",userId);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        gn.getData(Config.URL, "chatData", new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {

            }
        },json.toString());
    }
}
