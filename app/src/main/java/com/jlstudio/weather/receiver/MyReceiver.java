package com.jlstudio.weather.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.jlstudio.weather.service.MyService;


public class MyReceiver extends BroadcastReceiver {
    public static String ACTION = "over";
    public MyReceiver() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {
//       String action = intent.getStringExtra("action");
//        Intent i = new Intent(context, MyService.class);
//        if(action!=null && action.equals(ACTION)){
//            i.putExtra("action","over");
//            context.startActivity(i);
//        }else{
//            context.startService(i);
//        }
    }
}
