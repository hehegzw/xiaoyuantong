package com.jlstudio.weather.util;

import android.util.Log;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by gzw on 2015/10/4.
 */
public class ConnectUtil {
    //http://wthrcdn.etouch.cn/weather_mini?citykey=101010100
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
                    Log.d("hehe",builder.toString());
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
