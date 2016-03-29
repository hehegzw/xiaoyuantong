package com.jlstudio.iknow.net;

import android.content.Context;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

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
    public static GetDataNet getInstence(Context context){
        if(gn == null){
            gn = new GetDataNet(context);
        }
        return gn;
    }
    public void getQueston(String url,String action, Response.Listener<JSONObject> listener, Response.ErrorListener errorListener,final String... params){
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
    public void getScore(String url,String action, Response.Listener<JSONObject> listener, Response.ErrorListener errorListener,final String... params){
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
    public void sendAnswer(String url, String action,Response.Listener<String> listener, Response.ErrorListener errorListener,final String... params){
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
    public void getSchedule(String url, String action,Response.Listener<String> listener, Response.ErrorListener errorListener,final String... params){
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
