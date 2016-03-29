package com.jlstudio.group.dialog;

import android.app.Dialog;
import android.content.Context;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.jlstudio.R;
import com.jlstudio.main.application.Config;
import com.jlstudio.main.bean.CatchData;
import com.jlstudio.main.db.DBOption;
import com.jlstudio.main.net.GetDataNet;
import com.jlstudio.publish.adapter.SelectDepartmentadapter;
import com.jlstudio.publish.util.ShowToast;
import com.jlstudio.publish.util.StringUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by gzw on 2015/9/26.
 */
public class EditGroup extends Dialog implements View.OnClickListener{
    private Context context;
    private Button submit,cancle;
    private EditText edit_group;
    private TextView title_name;
    private int index;
    private List<String> groups;
    private CallBack callBack;
    private String flag;//判断时添加还是修改

    public EditGroup(Context context,int index,List<String> groups,String flag,CallBack callBack) {
        super(context);
        this.context = context;
        this.index = index;
        this.groups = groups;
        this.flag = flag;
        this.callBack = callBack;
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.edit_group_dialog);
        initView();
        getDatas();
    }

    private void getDatas(){
    }

    private void initView() {
        edit_group = (EditText) findViewById(R.id.edit_group);
        submit = (Button) findViewById(R.id.submit);
        cancle = (Button) findViewById(R.id.cancle);
        title_name = (TextView) findViewById(R.id.title_name);
        if(flag.equals("add")){
            title_name.setText("输入分组名");
        }else{
            title_name.setText("修改分组名");
        }
        submit.setOnClickListener(this);
        cancle.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.submit:
                rename();
                break;
            case R.id.cancle:
                dismiss();
                break;
        }
    }

    private void rename() {
        String name = edit_group.getText().toString().trim();
        if(StringUtil.isEmpty(name)){
            Toast.makeText(context,"组名不能为空",Toast.LENGTH_SHORT).show();
            return;
        }
        for(int i=0;i<groups.size();i++){
            if (name.equals(groups.get(i))){
                Toast.makeText(context,"组名不能重复",Toast.LENGTH_SHORT).show();
                return;
            }
        }
        if(callBack!=null){
            callBack.callback(index,name);
        }
        dismiss();
    }

    public interface CallBack{
        void callback(int index,String name);
    }
}
