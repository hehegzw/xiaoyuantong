package com.jlstudio.market.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.jlstudio.R;
import com.jlstudio.main.net.Downloadimgs;

import java.util.List;

/**
 * Created by gzw on 2016/3/24.
 */
public class SubGoodsDisplayAdapter extends RecyclerView.Adapter {
    public static final String SUBURL = "http://192.168.0.120:8080/xyt/";
    private List<String> images;
    private Context context;
    private int imageWidth;
    public SubGoodsDisplayAdapter(List<String> iamges, Context context, int screenWidth) {
        this.images = iamges;
        this.context = context;
        this.imageWidth = screenWidth/3;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.image,viewGroup,false);
        return new PicViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int i) {
        PicViewHolder picViewHolder = (PicViewHolder) viewHolder;
        ViewGroup.LayoutParams params = picViewHolder.image.getLayoutParams();
        params.width = imageWidth;
        params.height = ViewGroup.LayoutParams.MATCH_PARENT;
        picViewHolder.image.setLayoutParams(params);
        String url = SUBURL+images.get(i);
        Downloadimgs.initImageLoader(context).displayImage(url,picViewHolder.image,Downloadimgs.getOption());
    }

    @Override
    public int getItemCount() {
        return images.size();
    }
    private class PicViewHolder extends RecyclerView.ViewHolder{
        private ImageView image;
        public PicViewHolder(View itemView) {
            super(itemView);
            image = (ImageView) itemView.findViewById(R.id.image);
        }
    }
}
