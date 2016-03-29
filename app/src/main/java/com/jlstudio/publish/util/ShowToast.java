package com.jlstudio.publish.util;

import android.content.Context;
import android.widget.Toast;

/**
 * Created by gzw on 2015/10/20.
 */
public class ShowToast {
    public static void show(Context context,String string){
        Toast.makeText(context,string,Toast.LENGTH_LONG).show();
    }
}
