package com.jlstudio.publish.server;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.jlstudio.main.application.Config;
import com.jlstudio.main.bean.CatchData;
import com.jlstudio.main.db.DBOption;
import com.jlstudio.main.net.GetDataNet;

import org.json.JSONException;
import org.json.JSONObject;

public class MyService extends Service {
    public MyService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        getDataFromNet();
        return super.onStartCommand(intent, flags, startId);
    }
    private void getDataFromNet() {
        final String username = Config.loadUser(getApplicationContext()).getUsername();
        GetDataNet gn = GetDataNet.getInstence(getApplicationContext());
        final DBOption db = new DBOption(getApplicationContext());
        final CatchData data = db.getCatch(Config.URL + Config.GETCONTACT + username);
        JSONObject json = new JSONObject();
        try {
            json.put("username", username);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        gn.getData(Config.URL, "groups", new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                if (data == null) {
                    db.insertCatch(Config.URL + Config.GETCONTACTGROUP +username, s, System.currentTimeMillis() + "");
                } else {
                    db.updateCatch(Config.URL + Config.GETCONTACTGROUP +username, s, System.currentTimeMillis() + "");
                }
                stopSelf();
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Log.d("hehe", "获取数据失败");
            }
        }, json.toString());
    }
}
