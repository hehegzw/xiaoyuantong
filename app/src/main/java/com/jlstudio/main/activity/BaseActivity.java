package com.jlstudio.main.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.Window;

import com.jlstudio.main.application.ActivityContror;

public class BaseActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        //ActivityContror.addActivity(this);
    }
}
