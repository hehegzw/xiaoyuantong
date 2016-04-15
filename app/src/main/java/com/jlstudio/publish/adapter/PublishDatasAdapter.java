package com.jlstudio.publish.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.jlstudio.R;
import com.jlstudio.publish.bean.PublishListData;

import java.util.List;

/**
 * Created by gzw on 2015/10/16.
 */
public class PublishDatasAdapter extends RecyclerView.Adapter {
    private OnRecyclerViewListener onRecyclerViewListener;
    private List<PublishListData> list;
    private Context context;
    private String type;

    public PublishDatasAdapter(Context context, List<PublishListData> list,String type) {
        this.list = list;
        this.context = context;
        this.type = type;
    }

    public void setList(List<PublishListData> list) {
        this.list = list;
        //notifyDataSetChanged();
    }

    public void setOnRecyclerViewListener(OnRecyclerViewListener onRecyclerViewListener) {
        this.onRecyclerViewListener = onRecyclerViewListener;
    }
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.publish_datas_item,viewGroup,false);
        return new PersonViewHolder(view);
    }
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int i) {
        PersonViewHolder holder = (PersonViewHolder) viewHolder;
        holder.position = i;
        holder.title.setText(list.get(i).getTitle());
        holder.time.setText(list.get(i).getTime());
        if(type.equals("receive")&&list.get(i).getFlag().equals("0")){
            holder.iscallback.setVisibility(View.VISIBLE);
        }else{
            holder.iscallback.setVisibility(View.GONE);
        }
    }
    @Override
    public int getItemCount() {
        return list.size();
    }

    private class PersonViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {
        public TextView title;
        public TextView time;
        public TextView iscallback;
        public int position;
        public PersonViewHolder(View itemView) {
            super(itemView);
            title = (TextView) itemView.findViewById(R.id.title);
            time = (TextView) itemView.findViewById(R.id.time);
            iscallback = (TextView) itemView.findViewById(R.id.iscallback);
            itemView.setOnClickListener(this);
            itemView.setOnLongClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if(onRecyclerViewListener!=null){
                onRecyclerViewListener.onItemClick(position);
            }
        }

        @Override
        public boolean onLongClick(View v) {
            if(onRecyclerViewListener!=null){
                onRecyclerViewListener.onItemLongClick(v,position);
            }
            return false;
        }
    }
    public static interface OnRecyclerViewListener{
        void onItemClick(int position);
        boolean onItemLongClick(View view,int position);
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }
}
