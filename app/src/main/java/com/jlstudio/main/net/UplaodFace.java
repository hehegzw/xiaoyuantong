package com.jlstudio.main.net;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.apache.http.Header;
import org.json.JSONArray;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

/**
 * Created by gzw on 2015/11/8.
 */
public class UplaodFace {
    public static void sendPhoto(String url, String servelet, final Success success, final Failure failure, String... strings) {
        RequestParams params = new RequestParams();
        //上传头像
        if (strings.length == 1) {
            try {
                File file = new File(strings[1]);
                params.put("jpg", file);
                params.put("username", strings[0]);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            params.put("data", strings[0]);
            try {
                File file = new File(strings[1]);
                params.put("file", file);
                //params.add("uid",strings[0]);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        AsyncHttpClient client = new AsyncHttpClient();
        client.post(url + servelet, params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int i, org.apache.http.Header[] headers, byte[] bytes) {
                if (success != null) {
                    String string = new String(bytes);
                    success.onSuccess(string);
                }
            }

            @Override
            public void onFailure(int i, org.apache.http.Header[] headers, byte[] bytes, Throwable throwable) {
                if (failure != null) {
                    failure.onFailure();
                }
            }
        });
    }

    public static void uploadPic(String url, List<String> list, String data, final Success success, final Failure failure) {
        RequestParams params = new RequestParams();
            params.put("data", data);
        AsyncHttpClient client = new AsyncHttpClient();
        client.post(url, params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                if (success != null) {
                    success.onSuccess("ok");
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                if (failure != null) {
                    failure.onFailure();
                }
            }
        });
    }

    public static void getData(String url, String currentPage, final SuccessJson success, final Failure failure) {
        RequestParams params = new RequestParams();
        params.put("data", currentPage);
        AsyncHttpClient client = new AsyncHttpClient();
        client.post(url, params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                super.onSuccess(statusCode, headers, response);
                if (success != null) {
                    success.onSuccess(response);
                }
            }
            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
                if (failure != null) {
                    failure.onFailure();
                }
            }
        });
    }

    public static void deleteData(String url, String goodId, final Success success, final Failure failure) {
        RequestParams params = new RequestParams();
        params.put("data", goodId);
        AsyncHttpClient client = new AsyncHttpClient();
        client.post(url, params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int i, Header[] headers, byte[] bytes) {
                String string = new String(bytes);
                if (success != null) {
                    success.onSuccess(string);
                }
            }

            @Override
            public void onFailure(int i, Header[] headers, byte[] bytes, Throwable throwable) {
                if (failure != null) {
                    failure.onFailure();
                }
            }
        });
    }

    //
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
            bitmap.compress(Bitmap.CompressFormat.JPEG, 30, fileOut);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return file;
    }

    public static File getFilePress(String path) {
        int option = 90;
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
        bitmap.compress(Bitmap.CompressFormat.JPEG, 30, fileOut);
        if(file.length() > 50 * 1024){
            return getCompressFile(file);
        }
        bitmap.compress(Bitmap.CompressFormat.JPEG, option, fileOut);
        return file;
//        while (out.toByteArray().length > 50 * 1024) {
//            if (option > 30) {
//                out.reset();
//                option -= 10;
//                bitmap.compress(Bitmap.CompressFormat.JPEG, option, out);
//            } else {
//               return getCompressFile(out,file);
//            }
//        }
//        bitmap.compress(Bitmap.CompressFormat.JPEG, option, fileOut);
//        return file;
    }

    public interface SuccessJson {
        void onSuccess(JSONArray response);
    }

    public interface Success {
        void onSuccess(String s);
    }

    public interface Failure {
        void onFailure();
    }
}
