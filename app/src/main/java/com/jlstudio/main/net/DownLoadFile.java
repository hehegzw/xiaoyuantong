package com.jlstudio.main.net;

import android.content.Context;
import android.os.Environment;
import android.util.Log;

import com.jlstudio.main.util.ProgressUtil;
import com.jlstudio.publish.util.ChineseUtil;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.BinaryHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.apache.http.Header;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLDecoder;
import java.net.URLEncoder;

/**
 * Created by gzw on 2015/11/30.
 */
public class DownLoadFile {
    static String paths = Environment.getExternalStorageDirectory() + "/xiaoyuantongdownload";
    public static void getFile(String path, String filename, Success success, Filure filure) {
        if (ChineseUtil.isChinese(path)) {
            String[] string = path.split("=");
            try {
                path = string[0] + "=" + URLEncoder.encode(string[1], "utf-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
        File file = new File(paths + "/" + filename);
        Log.d("DownLoadFile", file.getPath());
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        FileOutputStream out = null;
        InputStream in = null;
        try {
            out = new FileOutputStream(file);
            URL url = new URL(path);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setDoInput(true);
            conn.setDoOutput(true);
            conn.setUseCaches(false);
            conn.setReadTimeout(5000);
            conn.setRequestMethod("GET");
            Log.d("code", conn.getResponseCode() + "return");
            if (conn.getResponseCode() == 200 || conn.getResponseCode() == 201) {
                in = conn.getInputStream();
                byte[] bytes = new byte[1024];
                while (in.read(bytes) != -1) {
                    out.write(bytes);
                }
                out.flush();
                success.onSuccess();
            }
        } catch (Exception e) {
            e.printStackTrace();
            filure.onFilure();
        } finally {
            try {
                if (out != null)
                    out.close();
                if (in != null)
                    in.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void getFile(String url, String path, final String filename, final Success success, final Filure failure) {
        RequestParams params = new RequestParams();
        try {
            path = path.split("=")[1];
            JSONObject json = new JSONObject(path);
            params.put("data", json.get("filePath"));
        } catch (Exception e) {
            e.printStackTrace();
        }

        AsyncHttpClient client = new AsyncHttpClient();
        String[] allowedContentTypes = new String[] { ".*" };
        client.post(url, params, new BinaryHttpResponseHandler(allowedContentTypes) {
            @Override
            public void onSuccess(int i, Header[] headers, byte[] bytes) {
                createFile(filename,bytes,success,failure);
            }
            @Override
            public void onFailure(int i, Header[] headers, byte[] bytes, Throwable throwable) {
                if(failure!=null){
                    failure.onFilure();
                }
            }
        });
    }
    private static void createFile(String filename,byte[] bytes,Success success,Filure filure){
        File file = new File(paths + "/" + filename);
        if(!file.getParentFile().exists()){
            file.getParentFile().mkdirs();
        }
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        FileOutputStream out = null;
        try {
            out = new FileOutputStream(file);
            out.write(bytes);
            out.flush();
            out.close();
            if(success!=null)
                success.onSuccess();
        } catch (Exception e) {
            if (filure!=null){
                filure.onFilure();
            }
            e.printStackTrace();
        }
    }

    public interface Success {
        void onSuccess();
    }

    public interface Filure {
        void onFilure();
    }
}
