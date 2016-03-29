package com.jlstudio.publish.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.jlstudio.R;
import com.jlstudio.main.application.Config;
import com.jlstudio.main.bean.CatchData;
import com.jlstudio.main.db.DBOption;
import com.jlstudio.main.net.DownLoadFile;
import com.jlstudio.main.util.ProgressUtil;
import com.jlstudio.publish.activity.ShowPersonAty;
import com.jlstudio.publish.adapter.SelectDepartmentadapter;
import com.jlstudio.publish.net.GetNotificationNet;
import com.jlstudio.publish.util.JsonToPubhlishData;
import com.jlstudio.publish.util.ShowToast;

import java.util.ArrayList;
import java.util.List;


/**询问下载对话框
 * Created by gzw on 2015/9/26.
 */
public class QueryDownLoadDialog extends Dialog implements View.OnClickListener{
    private Context context;
    private Button btn_submit,btn_cancle;
    private String fileName,filePath;
    public QueryDownLoadDialog(Context context,String fileName,String filePath) {
        super(context);
        this.context = context;
        this.fileName = fileName;
        this.filePath = filePath;
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_query_download);
        initView();
    }
    private void initView() {
        btn_submit = (Button) findViewById(R.id.btn_submit);
        btn_cancle = (Button) findViewById(R.id.btn_cancle);
        btn_submit.setOnClickListener(this);
        btn_cancle.setOnClickListener(this);
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_submit:
                ProgressUtil.showProgressDialog(context, "下载中");
                DownLoadFile.getFile(Config.URL + "download",filePath, fileName, new DownLoadFile.Success() {
                    @Override
                    public void onSuccess() {
                        ShowToast.show(context,"下载成功,文件保存在/storage/sdcard0/xiaoyuantongdownload");
                        ProgressUtil.closeProgressDialog();
                        dismiss();
                    }
                }, new DownLoadFile.Filure() {
                    @Override
                    public void onFilure() {
                        ShowToast.show(context,"下载失败");
                        ProgressUtil.closeProgressDialog();
                        dismiss();
                    }
                });
                break;
            case R.id.btn_cancle:
                dismiss();
                break;
        }
    }
}
