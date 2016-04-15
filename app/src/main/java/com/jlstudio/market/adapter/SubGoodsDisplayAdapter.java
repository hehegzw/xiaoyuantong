package com.jlstudio.market.adapter;

import android.content.Context;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.facebook.drawee.view.SimpleDraweeView;
import com.jlstudio.R;
import com.jlstudio.main.application.Config;
import com.jlstudio.main.application.MyApplication;

import java.util.List;

/**
 * Created by gzw on 2016/3/24.
 */
public class SubGoodsDisplayAdapter extends RecyclerView.Adapter {
    private List<String> images;
    private Context context;
    private int imageWidth;
    private ClickListener click;
    private String suffix = null;

    public SubGoodsDisplayAdapter(List<String> iamges, Context context, int screenWidth) {
        this.images = iamges;
        this.context = context;
        this.imageWidth = screenWidth / 3;
        suffix = "?imageView2/0/w/100/h/100/format/jpg";
    }

    public void setClick(ClickListener click) {
        this.click = click;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.image, viewGroup, false);
        return new PicViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int i) {
        PicViewHolder picViewHolder = (PicViewHolder) viewHolder;
        ViewGroup.LayoutParams params = picViewHolder.image.getLayoutParams();
        params.width = imageWidth;
        picViewHolder.image.setLayoutParams(params);
        String url;
        url = Config.QINIUURL + images.get(i) + suffix;
        Uri uri = Uri.parse(url);
        picViewHolder.image.setImageURI(uri);
        //Downloadimgs.initImageLoader(context).displayImage(url,picViewHolder.image,Downloadimgs.getOption());
    }

    @Override
    public int getItemCount() {
        return images.size();
    }

    private class PicViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private SimpleDraweeView image;

        public PicViewHolder(View itemView) {
            super(itemView);
            image = (SimpleDraweeView) itemView.findViewById(R.id.image);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (click != null) {
                click.click(v, getPosition());
            }
        }
    }

    public interface ClickListener {
        void click(View v, int position);
    }
}
