package com.jlstudio.group.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.jlstudio.R;


/**
 * Created by gzw on 2016/2/28.
 */
public class ImageHeadAdapter extends RecyclerView.Adapter {
    private int[] imageArrays;
    private LayoutInflater inflater;
    private ImageHeadListener listenre;

    public ImageHeadAdapter(Context context, int[] imageArrays) {
        this.imageArrays = imageArrays;
        this.inflater = LayoutInflater.from(context);
    }

    public void setListenre(ImageHeadListener listenre) {
        this.listenre = listenre;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = inflater.inflate(R.layout.sub_imagehead_adapter,viewGroup,false);
        return new WordHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int i) {
        WordHolder holder = (WordHolder) viewHolder;
        holder.text.setImageResource(imageArrays[i]);
        holder.position = i;
    }

    @Override
    public int getItemCount() {
        return imageArrays.length;
    }
    private class WordHolder extends RecyclerView.ViewHolder{
        public ImageView text;
        public int position;
        public WordHolder(View itemView) {
            super(itemView);
            text = (ImageView) itemView.findViewById(R.id.text);
            text.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(listenre!=null){
                        listenre.listener(position);
                    }
                }
            });
        }
    }
    public interface ImageHeadListener{
        void listener(int position);
    }
}
