package com.jlstudio.publish.dialog;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.jlstudio.R;
import com.jlstudio.main.activity.BaseActivity;
import com.jlstudio.publish.activity.ShowPersonDetailAty;

public class UNRegisterQueryDialog extends BaseActivity implements View.OnClickListener{
    private Button btn_submit,btn_cancle;
    private CheckBox checkBox;
    private String index;//点击对象的位置
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_unregsterquery);
        index = getIntent().getAction();
        initView();
    }


    private void initView() {
        btn_submit = (Button) findViewById(R.id.btn_submit);
        btn_cancle = (Button) findViewById(R.id.btn_cancle);
        checkBox = (CheckBox) findViewById(R.id.chack);
        btn_submit.setOnClickListener(this);
        btn_cancle.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.btn_submit){
            Intent intent = new Intent();
            intent.setAction(index);
            setResult(1, intent);
            finish();
        }else{
            finish();
        }
    }

}
