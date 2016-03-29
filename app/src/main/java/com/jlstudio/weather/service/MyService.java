package com.jlstudio.weather.service;

import android.annotation.TargetApi;
import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;
import android.os.SystemClock;

import com.jlstudio.R;
import com.jlstudio.main.application.MyApplication;
import com.jlstudio.weather.receiver.MyReceiver;
import com.jlstudio.weather.util.Config;
import com.jlstudio.weather.util.ConnectUtil;

public class MyService extends Service {
    public MyService() {
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (intent.getStringExtra("action")!=null && intent.getStringExtra("action").equals("over")) {
            this.stopSelf();
        } else {
            new Thread() {
                @Override
                public void run() {
                    updateWeather();
                    super.run();
                }
            }.start();
//            AlarmManager manager = (AlarmManager) getSystemService(ALARM_SERVICE);
//            int anHour = 8 *60*60*1000;
//            long time = SystemClock.elapsedRealtime()+anHour;
//            Intent i = new Intent(this, MyReceiver.class);
//            PendingIntent pi = PendingIntent.getBroadcast(this, 0, i, 0);
//            manager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP, time, pi);
        }

        return super.onStartCommand(intent, flags, startId);
    }

    private void updateWeather() {
        ConnectUtil.connect(Config.loadArea(), new ConnectUtil.Listener() {
            @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
            @Override
            public void success(String result) {
                Config.saveDatas(result);
                String text = null;
                if (result.indexOf("雨") != -1) {
                    text = "未来8小时可能有降雨，出门带把伞奥!";
                } else if (result.indexOf("雪") != -1) {
                    text = "未来8小时可能有降雪，出门带把伞奥!";
                }
                if (text != null) {
                    NotificationManager nm = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                    Notification.Builder builder = new Notification.Builder(MyApplication.getContext()).setTicker("hehe").
                            setSmallIcon(R.drawable.ic_launcher);
                    Notification nf = builder.setContentTitle("温馨提示").setContentText(text).build();
                    nm.notify(0, nf);
                }

            }

            @Override
            public void failer(int code) {
            }
        });
    }
}
