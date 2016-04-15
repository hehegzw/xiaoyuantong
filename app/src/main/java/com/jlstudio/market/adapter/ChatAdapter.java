package com.jlstudio.market.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.jlstudio.R;
import com.jlstudio.main.application.Config;
import com.jlstudio.main.util.CircleImageView;
import com.jlstudio.market.bean.Chat;

import java.util.List;

/**
 * Created by gzw on 2016/3/24.
 */
public class ChatAdapter extends RecyclerView.Adapter {
    public static final int LEFT = 1;
    public static final int RIGHT = 2;
    private List<Chat> chats;
    private Context context;

    public ChatAdapter(List<Chat> chats, Context context) {
        this.chats = chats;
        this.context = context;
    }

    public void setChats(List<Chat> chats) {
        this.chats = chats;
        notifyDataSetChanged();
    }

    @Override
    public int getItemViewType(int position) {
        if(chats.get(position).getUserfrom().equals(Config.loadUser(context).getUsername()))
            return RIGHT;
        else
            return LEFT;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view;
        if(i == LEFT){
            Log.d("ChatAdapter","left"+i);
            view = LayoutInflater.from(context).inflate(R.layout.chat_left,viewGroup,false);
        }else{
            Log.d("ChatAdapter","right"+i);
            view = LayoutInflater.from(context).inflate(R.layout.chat_right,viewGroup,false);
        }
        return new ChatViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int i) {
        Log.d("ChatAdapter","onBindViewHolder"+i);
        ChatViewHolder cViewHolder = (ChatViewHolder) viewHolder;
        cViewHolder.text.setText(chats.get(i).getText());
    }

    @Override
    public int getItemCount() {
        return chats.size();
    }
    private class ChatViewHolder extends RecyclerView.ViewHolder{
        private CircleImageView image;
        private TextView text;
        public ChatViewHolder(View itemView) {
            super(itemView);
            image = (CircleImageView) itemView.findViewById(R.id.face);
            text = (TextView) itemView.findViewById(R.id.text);
        }
    }
}
