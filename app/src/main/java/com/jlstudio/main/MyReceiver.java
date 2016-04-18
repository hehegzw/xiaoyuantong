package com.jlstudio.main;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import cn.jpush.android.service.PushService;

/**
 * Created by gzw on 2016/4/18.
 */
public class MyReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Intent intent1 = new Intent(context,PushService.class);
        if(Intent.ACTION_BOOT_COMPLETED.endsWith(intent.getAction())){
            context.startService(intent1);
        }else if(Intent.ACTION_USER_PRESENT.equals(intent.getAction())){
            context.startService(intent1);
        }else if(Intent.ACTION_PACKAGE_RESTARTED.endsWith(intent.getAction())){
            context.startService(intent1);
        }
    }
}
