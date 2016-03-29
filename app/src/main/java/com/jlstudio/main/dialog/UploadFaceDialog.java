package com.jlstudio.main.dialog;

import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.jlstudio.R;
import com.jlstudio.group.activity.MyDataAty;
import com.jlstudio.main.activity.BaseActivity;
import com.jlstudio.main.application.ActivityContror;
import com.jlstudio.main.application.Config;
import com.jlstudio.main.net.UplaodFace;
import com.jlstudio.main.util.PhotoUtil;
import com.jlstudio.publish.util.ShowToast;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;

public class UploadFaceDialog extends BaseActivity implements View.OnClickListener {
    private TextView from_camera,from_album,cancle;
    private PhotoUtil pu;
    private String filePath;
    private String fileName;
    private String username;
    private Intent intent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_face_dialog);
        initDatas();
        initView();
    }

    private void initDatas() {
        intent = new Intent(this, MyDataAty.class);
        pu = PhotoUtil.getInstence(this);
        username = Config.loadUser(this).getUsername();
        fileName = username+".jpg";
        filePath = "/sdcard/" + fileName;
    }

    private void initView() {
        from_camera = (TextView) findViewById(R.id.camera);
        from_album = (TextView) findViewById(R.id.album);
        cancle = (TextView) findViewById(R.id.cancle);
        from_camera.setOnClickListener(this);
        from_album.setOnClickListener(this);
        cancle.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.camera:
                pu.openCamera(this, Config.REQUEST_CONDE_CAMERA);
                break;
            case R.id.album:
                pu.openLocalAlbum(this, Config.REQUEST_CONDE_ALBUM);
                break;
            case R.id.cancle:
               finish();
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == Config.REQUEST_CONDE_CAMERA || requestCode == Config.REQUEST_CONDE_ALBUM){
            pu.cope(this,data);
        }else{
            if(resultCode == RESULT_OK){
                // 拿到剪切数据
                Bitmap bmap = data.getParcelableExtra("data");

                // 显示剪切的图像
                // 图像保存到文件中
                Bitmap.CompressFormat format= Bitmap.CompressFormat.JPEG;
                int quality = 100;
                OutputStream stream = null;
                try {

                    stream = new FileOutputStream(filePath);
                } catch (FileNotFoundException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                bmap.compress(format, quality, stream);
                UplaodFace.sendPhoto(Config.URL, Config.UPLOADFACE, new UplaodFace.Success() {
                    @Override
                    public void onSuccess(String s) {
                        if (s.equals("1")){
                            ShowToast.show(UploadFaceDialog.this,"上传头像成功");
                            intent.setAction(filePath);
                            intent.putExtra("filepath", filePath);
                            setResult(6, intent);
                            ActivityContror.removeActivity(UploadFaceDialog.this);
                        }
                    }
                }, new UplaodFace.Failure() {
                    @Override
                    public void onFailure() {
                        ShowToast.show(UploadFaceDialog.this,"上传头像失败");
                        intent.setAction("failure");
                        setResult(0, intent);
                        ActivityContror.removeActivity(UploadFaceDialog.this);
                    }
                },username,filePath);
            }
        }
    }
}
