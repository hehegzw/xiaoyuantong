package com.jlstudio.main.net;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Build;
import android.util.LruCache;
import android.widget.ImageView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.Volley;
import com.jlstudio.R;
import com.jlstudio.main.application.Config;

/**
 * Created by gzw on 2015/11/21.
 */
public class DownFace {
    private RequestQueue queue;
    private Context context;
    private static GetDataNet gn = null;

    public DownFace(Context context) {
        queue = Volley.newRequestQueue(context);
        this.context = context;
    }
    public static GetDataNet getInstence(Context context){
        if(gn == null){
            gn = new GetDataNet(context);
        }
        return gn;
    }
    public void loadImage(String url,String action,Response.Listener<Bitmap> listener,int maxWidth,int maxHeight,Response.ErrorListener errorListener){
        ImageRequest imageRequest = new ImageRequest(url+action,listener,maxWidth,maxHeight, Bitmap.Config.RGB_565,errorListener);
        queue.add(imageRequest);
    }
    public void loadImages(ImageView view,String uid){
        final String url= Config.URL+"faces/" + uid + ".jpg";
        int maxMemory = (int) Runtime.getRuntime().maxMemory();
        int cacheSize = maxMemory/8;
        final LruCache<String,Bitmap> lruCache = new LruCache<String,Bitmap>(cacheSize){
            @Override
            protected int sizeOf(String key, Bitmap value) {
                if(Build.VERSION.SDK_INT > 12){
                    return value.getByteCount();
                }else{
                    return value.getRowBytes()*value.getHeight();
                }
            }
        };
        ImageLoader.ImageCache imageCache = new ImageLoader.ImageCache() {
            @Override
            public Bitmap getBitmap(String s) {
                return lruCache.get(s);
            }

            @Override
            public void putBitmap(String s, Bitmap bitmap) {
                lruCache.put(s,bitmap);
            }
        };
        ImageLoader imageLoader = new ImageLoader(queue,imageCache);
        imageLoader.get(url,ImageLoader.getImageListener(view, R.drawable.logo,0));
    }
}
