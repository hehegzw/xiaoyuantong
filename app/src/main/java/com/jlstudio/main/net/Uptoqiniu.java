package com.jlstudio.main.net;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.widget.ProgressBar;

import com.jlstudio.main.application.Config;
import com.jlstudio.main.application.MyApplication;
import com.jlstudio.publish.util.StringUtil;
import com.qiniu.android.http.ResponseInfo;
import com.qiniu.android.storage.UpCompletionHandler;
import com.qiniu.android.storage.UpProgressHandler;
import com.qiniu.android.storage.UploadManager;
import com.qiniu.android.storage.UploadOptions;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PipedReader;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by gzw on 2016/4/4.
 */
public class Uptoqiniu {
    private static final String tokan = "U-xnzz4CMEqM8lSW1HisWIDflYDvIHlyWTCIigAm:1Ci-tEc5q6zyN80Jlc9x2kY9R6Q=:eyJzY29wZSI6ImNvbS1qbHN0dWRpbyIsImRlYWRsaW5lIjozMjM3ODMxMTEyfQ==";
    private UploadManager uploadManager;
    private int fileCount;

    public Uptoqiniu() {
        uploadManager = MyApplication.getUploadManager();
    }

    public void UploadImages(final List<File> files, final SuccessListener successListener, final FaulerListener faulerListener, final ProgressListener progressListener) {
        ExecutorService exec = Executors.newSingleThreadExecutor();
        exec.execute(new CompressThread(files, new CompressListener() {
            @Override
            public void success(List<File> lists) {
                fileCount = 0;
                final List<String> list = new ArrayList<>();
                final UploadOptions options = new UploadOptions(null, null, false, new UpProgressHandler() {
                    @Override
                    public void progress(String key, double percent) {
                        Log.d("qiniuinfo", percent + "");
                        if(progressListener!=null) progressListener.progress(percent);
                    }
                }, null);
                for (int i = 0; i < files.size(); i++) {
                    uploadManager.put(files.get(i), null,tokan, new UpCompletionHandler() {
                        @Override
                        public void complete(String key, ResponseInfo info, JSONObject response) {
                            try {
                                if(StringUtil.isEmpty(response.getString("key"))){
                                    if(faulerListener!=null){
                                        faulerListener.fauler();
                                    }
                                }else{
                                    list.add(response.getString("key"));
                                    successListener.success(list,fileCount);
                                    fileCount++;
                                }

                            } catch (JSONException e) {
                                e.printStackTrace();
                                faulerListener.fauler();
                            }
                        }
                    }, options);
                }
            }
        }));
    }

    public interface SuccessListener {
        void success(List<String> list,int count);
    }

    public interface FaulerListener {
        void fauler();
    }
    public interface  ProgressListener{
        void progress(double percent);
    }
    public static File getCompressFile(File file) {
        String path = file.getAbsolutePath();
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        int scal = 1;
        BitmapFactory.decodeFile(path, options);
        int oriWidth = options.outWidth;
        int oriHeight = options.outHeight;
        if(oriWidth >768||oriHeight >1200){
            int scal1 = oriWidth/768;
            int scal2 = oriHeight/1200;
            int scal3 = scal1 > scal2?scal2:scal1;
            if (scal3 > 1){
                scal = scal3;
            }
        }
        options.inJustDecodeBounds = false;
        options.inSampleSize = scal;
        Bitmap bitmap = BitmapFactory.decodeFile(path,options);

        try {
            FileOutputStream fileOut = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 80, fileOut);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return file;
    }

    public static File getFilePress(String path) {
        File file = null;
        try {
            file = File.createTempFile("temp",".jpg");
        } catch (IOException e) {
            e.printStackTrace();
        }
        FileOutputStream fileOut = null;
        try {
            if (!file.exists())
                file.createNewFile();
            fileOut = new FileOutputStream(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
        Bitmap bitmap = BitmapFactory.decodeFile(path);
        bitmap.compress(Bitmap.CompressFormat.JPEG, 60, fileOut);
        if(file.length() > 300 * 1024){
            return getCompressFile(file);
        }
        return file;
    }
    class CompressThread implements Runnable{
        private List<File> lists;
        private CompressListener listener;
        public CompressThread(List<File> lists,CompressListener listener) {
            this.lists = lists;
            this.listener = listener;
        }
        @Override
        public void run() {
            for(int i=0;i<lists.size();i++){
                if(lists.get(i).length() > 1024*300){
                    File file = lists.remove(i);
                    file = getFilePress(file.getAbsolutePath());
                    lists.add(i,file);
                }
            }
            if(listener!=null) listener.success(lists);
        }
    }
    interface CompressListener{
        void success(List<File> lists);
    }
}
