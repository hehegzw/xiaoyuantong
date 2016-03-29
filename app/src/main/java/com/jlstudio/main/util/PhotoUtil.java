package com.jlstudio.main.util;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Path;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by gzw on 2015/11/5.
 */
public class PhotoUtil {
    private Context context;
    private File imageFile;
    private Uri uri;
    private static PhotoUtil photoUtil;
    public static PhotoUtil getInstence(Context context){
        if(photoUtil == null){
            photoUtil = new PhotoUtil(context);
        }
        return photoUtil;
    }
    public PhotoUtil(Context context) {
        this.context = context;
    }
    //调用本地图库
    public void openLocalAlbum(Activity activity,int requestCode){
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        activity.startActivityForResult(intent,requestCode);
    }
    public void openCamera(Activity activity,int requestCode){
        String state = Environment.getExternalStorageState();
        if(state.equals(Environment.MEDIA_MOUNTED)){
            Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
            //自定义图片保存位置
            File rootFile = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM);
            imageFile = new File(rootFile+"/"+getImageName()+"jpg");
            if(!imageFile.exists()){
                try {
                    imageFile.createNewFile();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            uri = Uri.fromFile(imageFile);
            //intent.putExtra(MediaStore.EXTRA_OUTPUT,uri);//指明输出路径
            activity.startActivityForResult(intent,requestCode);
        }

    }

    public void cope(Activity activity,Intent data){
        Intent intent = new Intent();
        intent.setAction("com.android.camera.action.CROP");
        if(data == null) return;
        intent.setDataAndType(data.getData(), "image/*");// mUri是已经选择的图片Uri
        intent.putExtra("crop", "true");
        intent.putExtra("aspectX", 1);// 裁剪框比例
        intent.putExtra("aspectY", 1);
        intent.putExtra("outputX", 150);// 输出图片大小
        intent.putExtra("outputY", 150);
        intent.putExtra("return-data", true);
        activity.startActivityForResult(intent, 200);
    }

    public String getImageName() {
        return "自定义输出路径";
    }
}
