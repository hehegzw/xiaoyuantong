package com.jlstudio.group.dialog;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.jlstudio.R;
import com.jlstudio.iknow.activity.CETScoreAty;
import com.jlstudio.main.application.Config;
import com.jlstudio.main.net.GetDataNet;
import com.jlstudio.main.util.ProgressUtil;
import com.jlstudio.publish.util.ShowToast;
import com.jlstudio.publish.util.StringUtil;

import org.json.JSONException;
import org.json.JSONObject;


/**查CET
 * Created by gzw on 2015/9/26.
 */
public class CallDialog extends Dialog {
    private Context context;
    private Button submit;
    private Button cancle;
    private CallListener listener;

    public CallDialog(Context context,CallListener listener) {
        super(context);
        this.context = context;
        this.listener = listener;
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_call);
        initView();
    }

    private void initView() {
        submit = (Button) findViewById(R.id.submit);
        cancle = (Button) findViewById(R.id.cancle);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.isCall(true);
                dismiss();
            }

        });
        cancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.isCall(false);
                dismiss();
            }
        });
    }

    public interface CallListener{
        void isCall(boolean tag);
    }
}
