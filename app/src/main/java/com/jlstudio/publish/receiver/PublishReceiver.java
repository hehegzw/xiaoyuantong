package com.jlstudio.publish.receiver;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import com.jlstudio.R;
import com.jlstudio.group.activity.ShowContactsActivity;
import com.jlstudio.market.activity.ChatActivity;
import com.jlstudio.publish.activity.PublishDatasAty;

import org.json.JSONException;
import org.json.JSONObject;

import cn.jpush.android.api.JPushInterface;

/**
 * Created by gzw on 2015/10/25.
 */
public class PublishReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        if(JPushInterface.ACTION_MESSAGE_RECEIVED.equals(intent.getAction())){
            Bundle bundle = intent.getExtras();
            String string = bundle.getString(JPushInterface.EXTRA_MESSAGE);
            try {
                JSONObject json = new JSONObject(string);
                Intent intenttar;
                if(json.has("noticetype") && json.getString("noticetype").equals("好友请求")){
                    intenttar = new Intent(context, ShowContactsActivity.class);
                    createNotification(context,string,intenttar,0);
                }else if(json.has("noticetype") && json.getString("noticetype").equals("chat")){
                    intenttar = new Intent(context, ChatActivity.class);
                    createNotification(context,string,intenttar,2);
                }else{
                    intenttar = new Intent(context, PublishDatasAty.class);
                    createNotification(context,string,intenttar,1);
                }


            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    }
    private void createNotification(Context context,String content,Intent intent,int flag){
        String title = null;
        String from = null;
        String noticeid = null;
        try {
            JSONObject json = new JSONObject(content);
            if(flag == 0){
                title = json.getString("noticetitile");
                from = json.getString("noticefromname");
            }else if(flag ==2){
                from = json.getString("noticefrom");
                title = json.getString("text");
            }else{
                title = json.getString("title");
                from = json.getString("noticefrom");
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
        intent.setAction(content);
        PendingIntent pendingIntent = PendingIntent.getActivity(context,0,intent,0);
        NotificationManager nm = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        Notification.Builder builder = new Notification.Builder(context);
        builder.setContentTitle(from)
                .setContentText(title)
                .setTicker("您有新消息")
                .setSmallIcon(R.drawable.ic_launcher)
                .setWhen(System.currentTimeMillis())
                .setDefaults(Notification.DEFAULT_VIBRATE)
                .setContentIntent(pendingIntent);

        Notification nf = builder.build();
        nf.flags = Notification.FLAG_AUTO_CANCEL;//点击自动取消
        //nf.defaults = Notification.DEFAULT_SOUND;
        long[] vibrate = {0,100,200,300,400};
        nf.vibrate = vibrate;
        nf.sound = Uri.parse("android.resource://"+context.getPackageName()+"/"+R.raw.msgsound);
        nm.notify(0,nf);
    }
}
