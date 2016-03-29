package com.jlstudio.publish.activity;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.jlstudio.R;
import com.jlstudio.main.activity.BaseActivity;
import com.jlstudio.main.application.Config;
import com.jlstudio.publish.util.ShowToast;

public class AddPublishBySMSAty extends BaseActivity implements View.OnClickListener {
    private static final int CONTENT_COUNT2 = 70;//短信内容字数限制
    private EditText content;
    private String publishType;
    private TextView back, title_name, title_count, content_count, titlename,publish;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_add_publish_by_sms);
        publishType = getIntent().getAction();
        initView();
    }

    private void initView() {
        publish = (TextView) findViewById(R.id.right_icon);
        content_count = (TextView) findViewById(R.id.content_count);
        back = (TextView) findViewById(R.id.back);
        content = (EditText) findViewById(R.id.publish_content);
        title_name = (TextView) findViewById(R.id.title_name);//titlebar的标题
        title_count = (TextView) findViewById(R.id.title_count);//计算标题还能写几个字
        titlename = (TextView) findViewById(R.id.titlname);//显示标题两个字的textview
        publish.setOnClickListener(this);
        back.setOnClickListener(this);
        Typeface iconfont = Typeface.createFromAsset(getAssets(), "fonts/iconfont.ttf");
        Typeface send_publish = Typeface.createFromAsset(getAssets(), "fonts/publish.ttf");
        publish.setText(R.string.send_publish);
        publish.setTypeface(send_publish);
        back.setTypeface(iconfont);
        title_name.setText("添加消息");
        content.addTextChangedListener(new MyTextChangeListener());
        //区分
        if (publishType.equals("短信")) {
            titlename.setVisibility(View.GONE);
            title_count.setVisibility(View.GONE);
            content_count.setText("您还剩下70个汉字可以输入");
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back:
                startActivity(new Intent(this, PublishDatasAty.class));
                break;
            case R.id.right_icon:
                String strcontent = content.getText().toString();
                if (strcontent == null || strcontent.equals("")) {
                    ShowToast.show(this, "内容不能为空");
                    return;
                } else {
                    Config.WP.setType("短信");
                    Config.WP.setContent(strcontent);
                    Intent intent = new Intent(this, ShowPersonAty.class);
                    startActivity(intent);
                }

                finish();
                break;
        }
    }

    private class MyTextChangeListener implements TextWatcher {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
        }

        @Override
        public void afterTextChanged(Editable s) {
            int count = CONTENT_COUNT2 - s.length();
            if (count == 0) {
                content_count.setTextColor(Color.RED);
                content_count.setText("您还剩" + count + "个汉字可以输入");
            } else if (count < 0) {
                content_count.setTextColor(Color.RED);
                content_count.setText("您输入的字数已经达到上限，超出部分将作废");
            } else {
                content_count.setTextColor(Color.BLACK);
                content_count.setText("您还剩" + count + "个汉字可以输入");
            }

        }
    }

}
