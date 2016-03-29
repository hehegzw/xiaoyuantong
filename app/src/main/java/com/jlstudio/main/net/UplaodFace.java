package com.jlstudio.main.net;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.apache.http.Header;
import org.json.JSONArray;

import java.io.File;
import java.io.FileNotFoundException;
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
                params.put("username",strings[0]);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }else{
            params.put("data",strings[0]);
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
    public static void uploadPic(String url, List<String> list,String data, final Success success, final Failure failure) {
        RequestParams params = new RequestParams();
        for(int i=0;i<list.size();i++){
            File file = new File(list.get(i));
            try {
                params.put("file"+i,file);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            params.put("data",data);
        }
        AsyncHttpClient client = new AsyncHttpClient();
        client.post(url,params,new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                if(success!=null){
                    success.onSuccess("ok");
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                if(failure!=null){
                    failure.onFailure();
                }
            }
        });
    }
    public static void getData(String url,String currentPage,final SuccessJson success, final Failure failure) {
        RequestParams params = new RequestParams();
        params.put("data",currentPage);
        AsyncHttpClient client = new AsyncHttpClient();
        client.post(url,params,new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                super.onSuccess(statusCode, headers, response);
                if(success!=null){
                    success.onSuccess(response);
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
                if(failure!=null){
                    failure.onFailure();
                }
            }
        });
    }
    public static void deleteData(String url,String goodId,final Success success, final Failure failure) {
        RequestParams params = new RequestParams();
        params.put("data",goodId);
        AsyncHttpClient client = new AsyncHttpClient();
        client.post(url, params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int i, Header[] headers, byte[] bytes) {
                String string = new String(bytes);
                if(success!=null){
                    success.onSuccess(string);
                }
            }

            @Override
            public void onFailure(int i, Header[] headers, byte[] bytes, Throwable throwable) {
                if(failure!=null){
                    failure.onFailure();
                }
            }
        });
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
