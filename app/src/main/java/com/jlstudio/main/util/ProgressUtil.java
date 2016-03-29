package com.jlstudio.main.util;

import android.app.ProgressDialog;
import android.content.Context;

/**
 * Created by gzw on 2015/11/3.
 */
public class ProgressUtil {
    private static ProgressDialog progressDialog;
    public static void showProgressDialog(Context context,String string){
        if(progressDialog == null){
            progressDialog = new ProgressDialog(context);
            progressDialog.setMessage(string+"...");
            progressDialog.setCanceledOnTouchOutside(false);
        }
        progressDialog.show();
    }
    public static void closeProgressDialog(){
        if(progressDialog!=null){
            progressDialog.dismiss();
            progressDialog = null;
        }
    }
}
