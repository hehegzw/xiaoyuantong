package com.jlstudio.market.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.jlstudio.R;
import com.jlstudio.main.application.Config;
import com.jlstudio.main.net.UplaodFace;
import com.jlstudio.main.net.Uptoqiniu;
import com.jlstudio.main.util.ProgressUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.List;

/**
 * Created by gzw on 16-4-20.
 */
public class ProgressDialog extends Dialog {
    public static final int MODIFY = 2;
    private ProgressBar progressBar;
    private TextView count;
    private Uptoqiniu uptoqiniu;
    private JSONObject json;
    private Context context;
    private List<File> files;
    private String action;
    private int type;
    private int filecount;

    public ProgressDialog(Context context,List<File> files,JSONObject json,String action,int type) {
        super(context);
        LinearLayout layout = (LinearLayout) LayoutInflater.from(context).inflate(R.layout.progress_bar,null);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(layout);
        ViewGroup.LayoutParams params = layout.getLayoutParams();
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        params.width = wm.getDefaultDisplay().getWidth()-60;
        layout.setLayoutParams(params);
        this.context = context;
        this.files = files;
        this.json = json;
        this.action = action;
        this.type = type;
        setCanceledOnTouchOutside(false);
        initView();
        initData();
    }


    private void initView() {
        progressBar = (ProgressBar) findViewById(R.id.progressbar);
        count = (TextView) findViewById(R.id.count);
    }

    private void initData(){
        filecount = files.size();
        String temp = 0+"/"+filecount;
        count.setText(temp);
        sendToQiNiu();
    }
    private void sendToQiNiu(){
        uptoqiniu = new Uptoqiniu();
        uptoqiniu.UploadImages(files, new Uptoqiniu.SuccessListener() {
            @Override
            public void success(List<String> list,int fileCount) {
                if(fileCount == files.size()-1){
                    String temp = files.size()+"/"+filecount;
                    count.setText(temp);
                    JSONArray jsonArray = new JSONArray(list);
                    try {
                        json.put("images", jsonArray);
                        Uptoserver(action, json);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }else{
                    resetProgressBar(fileCount+1);
                }

            }
        }, new Uptoqiniu.FaulerListener() {
            @Override
            public void fauler() {
                if (type == MODIFY) {
                    Toast.makeText(context, "宝贝修改失败,请检查网络连接", Toast.LENGTH_SHORT).show();
                }
                Toast.makeText(context, "宝贝发布失败,请检查网络连接", Toast.LENGTH_SHORT).show();
            }
        }, new Uptoqiniu.ProgressListener() {
            @Override
            public void progress(double percent) {
                int progress = (int) (percent*100);
                progressBar.setProgress(progress);
            }
        });
    }
    private void Uptoserver(String action,JSONObject json){
        UplaodFace.uploadPic(Config.URL+action, null, json.toString(), new UplaodFace.Success() {
            @Override
            public void onSuccess(String s) {
                if(type == MODIFY){
                    Toast.makeText(context, "宝贝修改成功", Toast.LENGTH_SHORT).show();
                }
                Toast.makeText(context, "宝贝发布成功", Toast.LENGTH_SHORT).show();
                dismiss();
                ((Activity)context).finish();
            }
        }, new UplaodFace.Failure() {
            @Override
            public void onFailure() {
                if(type == MODIFY){
                    Toast.makeText(context, "宝贝修改失败", Toast.LENGTH_SHORT).show();
                }
                Toast.makeText(context, "宝贝发布失败", Toast.LENGTH_SHORT).show();
                dismiss();
            }
        });
    }
    private void resetProgressBar(int current){
        progressBar.setProgress(0);
        String temp = current+"/"+filecount;
        count.setText(temp);
    }
}
