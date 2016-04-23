package com.jlstudio.publish.server;

import android.app.IntentService;
import android.content.Intent;
import android.content.Context;
import android.telephony.SmsManager;
import android.util.Log;

import com.jlstudio.main.application.Config;
import com.jlstudio.publish.bean.MyContact;
import com.jlstudio.publish.util.JsonToPubhlishData;
import com.jlstudio.publish.util.ShowToast;

import java.util.List;

/**
 * An {@link IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 * <p/>
 * TODO: Customize class - update intent actions, extra parameters and static
 * helper methods.
 */
public class SendSMSService extends IntentService {
    // TODO: Rename actions, choose action names that describe tasks that this
    // IntentService can perform, e.g. ACTION_FETCH_NEW_ITEMS
    private static final String ACTION_FOO = "com.jlstudio.publish.server.action.FOO";
    private static final String ACTION_BAZ = "com.jlstudio.publish.server.action.BAZ";

    // TODO: Rename parameters
    private static final String EXTRA_PARAM1 = "com.jlstudio.publish.server.extra.PARAM1";
    private static final String EXTRA_PARAM2 = "com.jlstudio.publish.server.extra.PARAM2";

    /**
     * Starts this service to perform action Foo with the given parameters. If
     * the service is already performing a task this action will be queued.
     *
     * @see IntentService
     */
    // TODO: Customize helper method
    public static void startActionFoo(Context context, String param1, String param2) {
        Intent intent = new Intent(context, SendSMSService.class);
        intent.setAction(ACTION_FOO);
        intent.putExtra(EXTRA_PARAM1, param1);
        intent.putExtra(EXTRA_PARAM2, param2);
        context.startService(intent);
    }

    /**
     * Starts this service to perform action Baz with the given parameters. If
     * the service is already performing a task this action will be queued.
     *
     * @see IntentService
     */
    // TODO: Customize helper method
    public static void startActionBaz(Context context, String param1, String param2) {
        Intent intent = new Intent(context, SendSMSService.class);
        intent.setAction(ACTION_BAZ);
        intent.putExtra(EXTRA_PARAM1, param1);
        intent.putExtra(EXTRA_PARAM2, param2);
        context.startService(intent);
    }

    public SendSMSService() {
        super("SendSMSService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        try{
            if (intent != null) {
                String[] list = JsonToPubhlishData.getResultPeople(intent.getAction());
                SmsManager sms = SmsManager.getDefault();
                String content = Config.WP.getContent();
                for (String p : list){
                    sms.sendTextMessage(p,null,content,null,null);
                }
                ShowToast.show(getApplicationContext(),"发送成功");
            }
        }catch (Exception e){
            System.out.print("手机号码有误");
            e.printStackTrace();
        }

    }

}
