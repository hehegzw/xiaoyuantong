package com.jlstudio.weather.net;

import android.content.Context;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;


/**
 * Created by gzw on 2015/10/20.
 */
public class GetDataNet {
    private RequestQueue queue;
    private Context context;
    private static GetDataNet gn = null;

    public GetDataNet(Context context) {
        queue = Volley.newRequestQueue(context);
        this.context = context;
    }

    public static GetDataNet getInstence(Context context) {
        if (gn == null) {
            gn = new GetDataNet(context);
        }
        return gn;
    }

    public void getData(String url, Response.Listener<JSONObject> listener, Response.ErrorListener errorListener, final String... params) {
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null, listener, errorListener) {
            //解决中文乱码
//            @Override
//            protected Response<JSONObject> parseNetworkResponse(NetworkResponse response) {
//                JSONObject jsonObject;
//                try {
//                    jsonObject = new JSONObject(new String(response.data, "UTF-8"));
//                    return Response.success(jsonObject, HttpHeaderParser.parseCacheHeaders(response));
//                } catch (UnsupportedEncodingException e) {
//                    e.printStackTrace();
//                    return Response.error(new ParseError(e));
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                    return Response.error(new ParseError(e));
//                }
//            }
        };
        queue.add(request);
    }

    public void getDataString(String url, Response.Listener<String> listener, Response.ErrorListener errorListener, final String... params) {
        StringRequest request = new StringRequest(Request.Method.GET, url, listener, errorListener) {
//            @Override
//            protected Response<String> parseNetworkResponse(NetworkResponse response) {
//                String string;
//                try {
//                    string = new String(response.data,"UTF-8");
//                    return Response.success(string, HttpHeaderParser.parseCacheHeaders(response));
//                } catch (UnsupportedEncodingException e) {
//                    e.printStackTrace();
//                    return Response.error(new ParseError(e));
//                }
//            }
        };
        queue.add(request);
    }
    public static void connect(final String path,final Listener listener){
        new Thread(new Runnable() {
            @Override
            public void run() {
                HttpURLConnection conn = null;
                try{
                    URL url = new URL("http://wthrcdn.etouch.cn/weather_mini?citykey="+path);
                    conn = (HttpURLConnection) url.openConnection();
                    conn.setRequestMethod("GET");
                    conn.setConnectTimeout(5000);
                    InputStream in = conn.getInputStream();
                    byte[] bytes = new byte[1024];
                    int len = 0;
                    BufferedReader reader = new BufferedReader(new InputStreamReader(in));
                    StringBuilder builder = new StringBuilder();
                    String lenth = null;
                    while((lenth = reader.readLine())!=null){
                        builder.append(lenth);
                    }
                    if(listener!=null){
                        listener.success(builder.toString());
                    }
                    Log.d("hehe", builder.toString());
                }catch(Exception e){
                    if(listener!=null){
                        listener.failer(0);
                    }
                }
            }
        }).start();
    }
    public interface Listener{
        public void success(String result);
        public void failer(int code);
    }
}
