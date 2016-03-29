package com.jlstudio.publish.net;

import android.content.Context;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;


/**
 * Created by gzw on 2015/10/20.
 */
public class GetNotificationNet {
    private RequestQueue queue;
    private Context context;
    private static GetNotificationNet gn = null;

    public GetNotificationNet(Context context) {
        queue = Volley.newRequestQueue(context);
        this.context = context;
    }
    public static GetNotificationNet getInstence(Context context){
        if(gn == null){
            gn = new GetNotificationNet(context);
        }
        return gn;
    }
    public void getReceiveNotification(String url,String action, Response.Listener<JSONObject> listener, Response.ErrorListener errorListener,final String... params){
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST,url+action,null,listener,errorListener){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> map = new HashMap<>();
                map.put("action",params[0]);
                return map;
            }
        };
        queue.add(request);
    }
    public void getDepartment(String url, final String action,Response.Listener<String> listener, Response.ErrorListener errorListener,final String... params){
        StringRequest request = new StringRequest(Request.Method.POST,url+action,listener,errorListener){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = new HashMap<>();
                map.put("action", params[0]);
                return map;
            }
        };
        queue.add(request);
    }
    public void sendPublish(String url, String action,Response.Listener<String> listener, Response.ErrorListener errorListener,final String... params){
        StringRequest request = new StringRequest(Request.Method.POST,url+action,listener,errorListener){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = new HashMap<>();
                map.put("action", params[0]);
                return map;
            }
        };
        queue.add(request);
    }
}
