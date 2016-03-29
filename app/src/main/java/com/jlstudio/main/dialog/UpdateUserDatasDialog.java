package com.jlstudio.main.dialog;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.jlstudio.R;
import com.jlstudio.group.activity.MyDataAty;
import com.jlstudio.main.activity.BaseActivity;

public class UpdateUserDatasDialog extends BaseActivity implements View.OnClickListener{
    private EditText edit_all,edit_number;
    private RadioGroup group;
    private RadioButton man,weman;
    private Button submit;
    private String action;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_updateuserdatas_dialog);
        initDatas();
        initView();
    }

    private void initDatas() {
        action = getIntent().getAction();
    }

    private void initView() {
        submit = (Button) findViewById(R.id.submit);
        edit_all = (EditText) findViewById(R.id.edit_all);
        edit_number = (EditText) findViewById(R.id.edit_number);
        group = (RadioGroup) findViewById(R.id.group);
        man = (RadioButton) findViewById(R.id.man);
        weman = (RadioButton) findViewById(R.id.weman);
        submit.setOnClickListener(this);
        if(action.equals("sex")){
            edit_number.setVisibility(View.GONE);
            edit_all.setVisibility(View.GONE);
        }else if(action.equals("qqNumber")||action.equals("phone")){
            group.setVisibility(View.GONE);
            edit_all.setVisibility(View.GONE);
        }else{
            group.setVisibility(View.GONE);
            edit_number.setVisibility(View.GONE);
        }
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent(this, MyDataAty.class);
        if(action.equals("sex")){
            if(man.isChecked()){
                intent.setAction("男");
                setResult(5, intent);
            }else{
                intent.setAction("女");
                setResult(5, intent);
            }
            finish();
        }else if(action.equals("qqNumber")||action.equals("phone")){
            if(action.equals("qqNumber")){
                intent.setAction(edit_number.getText().toString().trim());
                setResult(3, intent);
            }else{
                intent.setAction(edit_number.getText().toString().trim());
                setResult(2, intent);
            }
        }else{
            if(action.equals("sign")){
                intent.setAction(edit_all.getText().toString().trim());
                setResult(1, intent);
            }else if(action.equals("weixin")){
                intent.setAction(edit_all.getText().toString().trim());
                setResult(4, intent);
            }
        }
        finish();
    }

}
