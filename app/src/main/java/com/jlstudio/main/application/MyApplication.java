package com.jlstudio.main.application;

import android.app.Application;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.util.Log;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.imagepipeline.core.ImagePipelineConfig;
import com.jlstudio.R;
import com.jlstudio.main.util.CrashHandler;
import com.jlstudio.market.util.XUtilsImageLoader;
import com.qiniu.android.storage.UploadManager;

import cn.finalteam.galleryfinal.CoreConfig;
import cn.finalteam.galleryfinal.FunctionConfig;
import cn.finalteam.galleryfinal.GalleryFinal;
import cn.finalteam.galleryfinal.ImageLoader;
import cn.finalteam.galleryfinal.ThemeConfig;
import cn.jpush.android.api.JPushInterface;

/**
 * Created by gzw on 2015/10/14.
 */
public class MyApplication extends Application {
    private static Context context;
    private static UploadManager uploadManager;
    private static int isSupportWebP = Config.NOT_INIT;
    @Override
    public void onCreate() {
        super.onCreate();
        JPushInterface.setDebugMode(true);
        JPushInterface.init(this);
//        CrashHandler crashHandler = CrashHandler.getInstance();
//        crashHandler.init(getApplicationContext());
        Log.d("hehe","初始化成功");
        context = this;
        //facebook
        Fresco.initialize(this);

        ThemeConfig theme = new ThemeConfig.Builder().build();
        FunctionConfig functionConfig = new FunctionConfig.Builder()
                .setEnableCamera(true)
                .setEnableEdit(true)
                .setEnableCrop(true)
                .setEnableRotate(true)
                .setCropSquare(true)
                .setEnablePreview(true)
                .build();
        ImageLoader imageloader = new XUtilsImageLoader(this);
        CoreConfig coreConfig = new CoreConfig.Builder(this, imageloader, theme)
                .setFunctionConfig(functionConfig)
                .build();
        GalleryFinal.init(coreConfig);
    }
    public static Context getContext(){
        return context;
    }
    public static UploadManager getUploadManager(){
        if(uploadManager == null){
            uploadManager = new UploadManager();
        }
        return uploadManager;
    }
    public static int isSupportWebP(Context context){
        if(isSupportWebP == Config.NOT_INIT){
            Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.analysis);
            if(bitmap == null) isSupportWebP = Config.NOT_SUPPORT;
            else isSupportWebP = Config.SUPPORT;
        }
        return isSupportWebP;
    }
}
