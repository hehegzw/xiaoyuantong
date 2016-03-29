package com.jlstudio.group.net;

import android.content.Context;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by NewOr on 2015/10/26.
 */
public class GetDataAction {
    RequestQueue mQueue;

    String responce_contacts;
    String friend_info;
    String group_contacts_data;
    private static GetDataAction getDataAction = null;
    private Context context;

    public GetDataAction(Context context) {
        mQueue = Volley.newRequestQueue(context);
        this.context = context;
    }

    public static GetDataAction getInstence(Context context) {
        if (getDataAction == null) {
            getDataAction = new GetDataAction(context);
        }
        return getDataAction;
    }

    /**
     * 获取通讯录信息
     *
     * @param url
     * @param action
     * @param listener
     * @param errorListener
     * @param params
     */
    public void getContactsData(String url, final String action, Response.Listener<String> listener, Response.ErrorListener errorListener, final String... params) {
        StringRequest request = new StringRequest(Request.Method.POST, url + action, listener, errorListener) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = new HashMap<>();
                map.put("userid", params[0]);
                return map;
            }
        };
        mQueue.add(request);
    }

    /**
     * 获取个人信息
     *
     * @param url
     * @param action
     * @param listener
     * @param errorListener
     * @param params
     */
    public void getContactsInfo(String url, final String action, Response.Listener<String> listener, Response.ErrorListener errorListener, final String... params) {
        StringRequest request = new StringRequest(Request.Method.POST, url + action, listener, errorListener) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = new HashMap<>();
                map.put("username", params[0]);
                return map;
            }
        };
        mQueue.add(request);
    }

    /**
     * 获取一个系的所有班级
     *
     * @param url
     * @param action
     * @param listener
     * @param errorListener
     * @param params
     */
    public void getGradesData(String url, final String action, Response.Listener<String> listener, Response.ErrorListener errorListener, final String... params) {
        StringRequest request = new StringRequest(Request.Method.POST, url + action, listener, errorListener) {
        };
        mQueue.add(request);
    }

    /**
     * 获取班级所有学生
     *
     * @param url
     * @param action
     * @param listener
     * @param errorListener
     * @param params
     */
    public void getGradeStudents(String url, final String action, Response.Listener<String> listener, Response.ErrorListener errorListener, final String... params) {
        StringRequest request = new StringRequest(Request.Method.POST, url + action, listener, errorListener) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = new HashMap<>();
                map.put("action", params[0]);
                return map;
            }
        };
        mQueue.add(request);
    }


    /**
     * 删除用户分组
     *
     * @param url
     * @param action
     * @param listener
     * @param errorListener
     * @param params
     */
    public void deleteContactsGroup(String url, final String action, Response.Listener<String> listener, Response.ErrorListener errorListener, final String... params) {
        StringRequest request = new StringRequest(Request.Method.POST, url + action, listener, errorListener) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = new HashMap<>();
                map.put("userid", params[0]);
                map.put("group_list", params[1]);
                return map;
            }
        };
    }

    public void addContactsGroup(String url, final String action, Response.Listener<String> listener, Response.ErrorListener errorListener, final String... params) {
        StringRequest request = new StringRequest(Request.Method.POST, url + action, listener, errorListener) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = new HashMap<>();
                map.put("userid", params[0]);
                map.put("groupname", params[1]);
                return map;
            }
        };
    }

}
