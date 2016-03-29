package com.jlstudio.publish.activity;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.jlstudio.R;
import com.jlstudio.main.activity.BaseActivity;
import com.jlstudio.main.application.Config;
import com.jlstudio.publish.util.ShowToast;
import com.jlstudio.publish.util.StringUtil;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

public class AddPublishAty extends BaseActivity implements View.OnClickListener {
    private static final int TITLE_COUNT = 20;
    private static final int CONTENT_COUNT = 210;//通知内容字数限制
    private static final int FILE_CODE = 1;//通知内容字数限制
    private EditText title, content;
    private String publishType;
    private LinearLayout upLoadArea;
    private String path = null;//文件路径
    private TextView back, title_name, title_count, content_count, titlename, filePath,publish,upload;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_add_publish);
        publishType = getIntent().getAction();
        initView();
    }

    private void initView() {
        //公共
        publish = (TextView) findViewById(R.id.right_icon);
        content_count = (TextView) findViewById(R.id.content_count);
        back = (TextView) findViewById(R.id.back);
        upload = (TextView) findViewById(R.id.upLoad);
        content = (EditText) findViewById(R.id.publish_content);
        title_name = (TextView) findViewById(R.id.title_name);//titlebar的标题
        title_count = (TextView) findViewById(R.id.title_count);//计算标题还能写几个字
        title = (EditText) findViewById(R.id.publish_title);//输入的消息的标题
        titlename = (TextView) findViewById(R.id.titlname);//显示标题两个字的textview
        publish.setOnClickListener(this);
        back.setOnClickListener(this);
        Typeface iconfont = Typeface.createFromAsset(getAssets(), "fonts/iconfont.ttf");
        Typeface send_publish = Typeface.createFromAsset(getAssets(), "fonts/publish.ttf");
        back.setTypeface(iconfont);
        upload.setTypeface(send_publish);
        publish.setText(R.string.send_publish);
        publish.setTypeface(send_publish);
        title_name.setText("添加消息");
        title.addTextChangedListener(new MyTextChangeListener(1));
        content.addTextChangedListener(new MyTextChangeListener(2));

        upLoadArea = (LinearLayout) findViewById(R.id.upLoadArea);
        filePath = (TextView) findViewById(R.id.filePath);
        upload.setOnClickListener(this);
        //区分
        if (publishType.equals("简单通知")) {
            upLoadArea.setVisibility(View.GONE);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back:
                startActivity(new Intent(this, PublishDatasAty.class));
                break;
            case R.id.right_icon://发送
                String str = title.getText().toString();
                String strcontent = content.getText().toString();
                if (str == null || str.equals("")) {
                    ShowToast.show(this, "标题不能为空");
                    return;
                } else {
                    if (str.length() >= 20) str.substring(0, 20);
                    Log.d("hehe", str);
                }
                //暂时保存要发送的通知
                Config.WP.setType("通知");
                Config.WP.setTitle(str);
                Config.WP.setContent(strcontent);
                if(path!=null){
                    Config.WP.setFilePath(path);
                }
                Intent intent = new Intent(this, ShowPersonAty.class);
                startActivity(intent);
                finish();
                break;
            case R.id.upLoad:
                showFileChooser();
                break;
        }
    }

    private class MyTextChangeListener implements TextWatcher {
        private int e;//1为title，2为content

        public MyTextChangeListener(int e) {
            this.e = e;
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
        }

        @Override
        public void afterTextChanged(Editable s) {
            if (e == 1) {
                int count = TITLE_COUNT - s.length();
                if (count == 0) {
                    title_count.setTextColor(Color.RED);
                    title_count.setText("您还剩" + count + "个汉字可以输入");
                } else if (count < 0) {
                    title_count.setTextColor(Color.RED);
                    title_count.setText("您输入的字数已经达到上限，超出部分将作废");
                } else {
                    title_count.setTextColor(Color.BLACK);
                    title_count.setText("您还剩" + count + "个汉字可以输入");
                }

            } else {
                int count = CONTENT_COUNT - s.length();
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
    //打开文件管理器
    private void showFileChooser() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("*/*");
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        try {
            startActivityForResult(Intent.createChooser(intent, "请选择一个要上传的文件"),
                    FILE_CODE);
        } catch (android.content.ActivityNotFoundException ex) {
            // Potentially direct the user to the Market with a Dialog
            Toast.makeText(this, "请安装文件管理器", Toast.LENGTH_SHORT)
                    .show();
        }
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub
        if(data == null) return;
        if (requestCode == FILE_CODE) {
            // Get the Uri of the selected file
            path = Uri.decode(data.getDataString());
            if(!path.toString().startsWith("file")){
                Uri url = Uri.parse(path);
                path = getAbsoluteFilePath(url);
            }else{
                path = path.toString().substring(7);
            }
            //截取文件名
            String[] filenames = path.split("/");
            String filename = filenames[filenames.length-1];
            filePath.setText(filename);
            Log.d("hehe", path);
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
    private String getAbsoluteFilePath(Uri uri){
        String[] proj = {MediaStore.Images.Media.DATA};
        Cursor cursor = getContentResolver().query(uri, proj, null, null, null);
        int index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        return cursor.getString(index);
    }
}
