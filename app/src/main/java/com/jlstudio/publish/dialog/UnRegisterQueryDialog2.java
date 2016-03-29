package com.jlstudio.publish.dialog;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;

import com.jlstudio.R;

/**
 * Created by gzw on 2015/11/25.
 */
public class UnRegisterQueryDialog2 extends Dialog implements View.OnClickListener {
    private Button btn_submit,btn_cancle;
    private TextView content;
    private CheckBox checkBox;
    private int count;//未注册人的数量
    private DialogListener listener;
    public UnRegisterQueryDialog2(Context context,DialogListener listener,int count) {
        super(context);
        this.listener = listener;
        this.count = count;
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_unregsterquery);
        btn_submit = (Button) findViewById(R.id.btn_submit);
        btn_cancle = (Button) findViewById(R.id.btn_cancle);
        checkBox = (CheckBox) findViewById(R.id.chack);
        content = (TextView) findViewById(R.id.content);
        content.setText("您选中的人中有"+count+"位未注册的用户，是否以手机短信的方式发送给他们？短信费用按运营商标准收取");
        btn_submit.setOnClickListener(this);
        btn_cancle.setOnClickListener(this);
    }

    public UnRegisterQueryDialog2(Context context, int themeResId) {
        super(context, themeResId);
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.btn_submit){
            if(listener!=null){
                listener.onResult(1);
                dismiss();
            }
        }else{
            listener.onResult(0);
            dismiss();
        }
    }
    public interface DialogListener{
        public void onResult(int resultCode);
    }
}
