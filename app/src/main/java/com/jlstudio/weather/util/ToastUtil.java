package com.jlstudio.weather.util;

import android.widget.Toast;

import com.jlstudio.main.application.MyApplication;


/**
 * Created by gzw on 2015/10/4.
 */
public class ToastUtil {
    public static void showToast(String content){
        Toast.makeText(MyApplication.getContext(),content,Toast.LENGTH_LONG).show();
    }
}
