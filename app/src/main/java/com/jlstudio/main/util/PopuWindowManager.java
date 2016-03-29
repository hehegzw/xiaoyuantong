package com.jlstudio.main.util;

import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.Toast;

import com.jlstudio.R;

/**
 * Created by gzw on 2016/1/4.
 */
public class PopuWindowManager {
    private PopupWindow popupWindow;
    private Context context;
    private boolean isFirst;//是否是第一次启动
    private String lastDir;//最后一次的箭头方向
    private LinearLayout linearLayout;
    private int position;//用户点击的哪个item;
    private DeleteListener deleteListener;

    public PopuWindowManager(Context context, DeleteListener deleteListener) {
        this.context = context;
        this.deleteListener = deleteListener;
        init();

    }

    public PopupWindow getPopupWindow() {
        return popupWindow;
    }

    private void init() {
        linearLayout = (LinearLayout) LayoutInflater.from(context).inflate(R.layout.popuwindow,null);
        popupWindow = new PopupWindow(linearLayout, 200, 200);
        popupWindow.setWidth(ViewGroup.LayoutParams.WRAP_CONTENT);
        popupWindow.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        popupWindow.setOutsideTouchable(true);
        popupWindow.setFocusable(true);
        popupWindow.setBackgroundDrawable(new BitmapDrawable());
        linearLayout.findViewById(R.id.listView).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (deleteListener!=null){
                    deleteListener.delete(position);
                }
            }
        });
        isFirst = true;
    }
    private ImageView getImageUp(){
        ImageView imageView = new ImageView(context);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        params.setMargins(0, 0, 0, -2);
        imageView.setLayoutParams(params);
        imageView.setImageResource(R.drawable.triangle);
        imageView.setScaleType(ImageView.ScaleType.FIT_XY);
        imageView.setScaleX((float) 0.5);
        //imageHeight = getViewHeight(imageView);
        return imageView;
    }
    private ImageView getImageDown(){
        ImageView imageView = new ImageView(context);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        params.setMargins(0, 0, 0, 2);
        imageView.setLayoutParams(params);
        imageView.setImageResource(R.drawable.triangle);
        imageView.setScaleType(ImageView.ScaleType.FIT_XY);
        imageView.setScaleX((float) 0.5);
        imageView.setRotation(180);
        //imageHeight = getViewHeight(imageView);
        return imageView;
    }
    private int getViewHeight(View view) {
        int owidth = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);

        int oheight = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);

        view.measure(owidth, oheight);

        int height = view.getMeasuredHeight();

        return height;
    }
    private boolean isUp(RecyclerView listView,View view){
        for(int i=0;i<listView.getChildCount();i++){
            if(view ==listView.getChildAt(i)){
                if(i <listView.getChildCount()/2){
                    return true;
                }
            }
        }
        return false;
    }
    public void showPopu(RecyclerView listView,View view,int positon){
        this.position = positon;
        int viewWidth = view.getWidth();
        int viewHeight = view.getHeight();
        ImageView imageView;
        if (!isFirst){
            if(lastDir.equals("up")){
                linearLayout.removeViewAt(0);
            }else{
                linearLayout.removeViewAt(1);
            }
        }else{
            isFirst = false;
        }
        if(isUp(listView,view)){
            imageView = getImageUp();
            linearLayout.addView(imageView,0);
            lastDir = "up";
            popupWindow.showAsDropDown(view, viewWidth / 2 - 80,0);
        }else{
            imageView = getImageDown();
            linearLayout.addView(imageView,1);
            lastDir = "down";
            popupWindow.showAsDropDown(view, viewWidth / 2 - 80, -(viewHeight + getViewHeight(linearLayout) +getViewHeight(imageView)));
        }
    }
    public interface DeleteListener{
        void delete(int position);
    }
}
