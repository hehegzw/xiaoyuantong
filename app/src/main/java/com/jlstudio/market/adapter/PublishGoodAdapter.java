package com.jlstudio.market.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;

import com.jlstudio.R;
import com.jlstudio.main.application.Config;
import com.jlstudio.main.net.Downloadimgs;

import java.util.List;

/**
 * Created by gzw on 2016/3/25.
 */
public class PublishGoodAdapter extends RecyclerView.Adapter {
    private List<String> goodPics;
    private Context context;
    private int imageWidth;
    private ClickListener listener;
    private LongClickListener longListener;

    public PublishGoodAdapter(List<String> goods, Context context) {
        this.goodPics = goods;
        this.context = context;
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        imageWidth = wm.getDefaultDisplay().getWidth() / 3;
    }

    public void setListener(ClickListener listener) {
        this.listener = listener;
    }

    public void setLongListener(LongClickListener longListener) {
        this.longListener = longListener;
    }

    public void setGoods(List<String> goods) {
        this.goodPics = goods;
        notifyDataSetChanged();
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.image, viewGroup, false);
        return new GoodViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int i) {
        GoodViewHolder goodViewHolder = (GoodViewHolder) viewHolder;
        goodViewHolder.position = i;
        ViewGroup.LayoutParams params = goodViewHolder.image.getLayoutParams();
        params.width = imageWidth;
        params.height = ViewGroup.LayoutParams.MATCH_PARENT;
        goodViewHolder.image.setScaleType(ImageView.ScaleType.FIT_XY);
        goodViewHolder.image.setLayoutParams(params);
        if (i == 0) {
            goodViewHolder.image.setImageResource(R.drawable.icon_addpic_focused);
        } else {
            if(goodPics.get(i).contains("/storage")){
                Bitmap bitmap = BitmapFactory.decodeFile(goodPics.get(i));
                goodViewHolder.image.setImageBitmap(bitmap);
            }else{
                Downloadimgs.initImageLoader(context).displayImage(Config.URL+goodPics.get(i),goodViewHolder.image,Downloadimgs.getOption());
            }

//            Bitmap bitmap = BitmapFactory.decodeFile(goodPics.get(i));
//            goodViewHolder.image.setImageBitmap(bitmap);
        }

    }

    @Override
    public int getItemCount() {
        return goodPics.size();
    }

    class GoodViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {
        public ImageView image;
        public int position;

        public GoodViewHolder(View itemView) {
            super(itemView);
            image = (ImageView) itemView.findViewById(R.id.image);
            image.setOnClickListener(this);
            image.setOnLongClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (listener != null) {
                listener.onClick(v, position);
            }
        }

        @Override
        public boolean onLongClick(View v) {
            if (longListener != null) {
                longListener.onLongClick(v,getPosition());
            }
            return true;
        }
    }

    public interface ClickListener {
        void onClick(View view, int position);
    }
    public interface LongClickListener {
        void onLongClick(View view, int position);
    }
}
