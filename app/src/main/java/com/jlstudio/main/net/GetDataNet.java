package com.jlstudio.main.net;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Build;
import android.util.Log;
import android.util.LruCache;
import android.widget.ImageView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.jlstudio.R;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by gzw on 2015/10/30.
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
    public void getData(String url, final String action,Response.Listener<String> listener, Response.ErrorListener errorListener,final String params){
        StringRequest request = new StringRequest(Request.Method.POST,url+action,listener,errorListener){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = new HashMap<>();
                map.put("data", params);
                return map;
            }
        };
        queue.add(request);
    }
}
