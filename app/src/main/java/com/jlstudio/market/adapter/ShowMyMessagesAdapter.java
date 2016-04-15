package com.jlstudio.market.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.jlstudio.R;
import com.jlstudio.main.application.Config;
import com.jlstudio.market.bean.Chat;
import com.jlstudio.market.bean.GoodsDetail;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by gzw on 2016/3/26.
 */
public class ShowMyMessagesAdapter extends RecyclerView.Adapter {
    private List<Chat> chats;
    private LayoutInflater inflater;
    private ClickListener listener;
    private LongClickListener longListener;
    private Context context;
    public ShowMyMessagesAdapter(List<Chat> chats, Context context) {
        this.chats = chats;
        this.inflater = LayoutInflater.from(context);
        this.context = context;
    }

    public void setListener(ClickListener listener) {
        this.listener = listener;
    }

    public void setLongListener(LongClickListener longListener) {
        this.longListener = longListener;
    }

    public void setGoods(List<Chat> goods) {
        this.chats = goods;
        notifyDataSetChanged();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = inflater.inflate(R.layout.sub_show_message,viewGroup,false);
        return new GoodViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int i) {
        GoodViewHolder goodViewHolder = (GoodViewHolder) viewHolder;
        Chat chat = chats.get(i);
        if(chat.getUserfrom().endsWith(Config.loadUser(context).getUsername())){
            goodViewHolder.userName.setText(chat.getToname());
        }else{
            goodViewHolder.userName.setText(chat.getFromname());
        }
        goodViewHolder.text.setText(chat.getText());
    }

    @Override
    public int getItemCount() {
        return chats.size();
    }
    private class GoodViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {
        private TextView userName;
        private TextView text;
        public GoodViewHolder(View itemView) {
            super(itemView);
            userName = (TextView) itemView.findViewById(R.id.userName);
            text = (TextView) itemView.findViewById(R.id.text);
            itemView.setOnClickListener(this);
            itemView.setOnLongClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if(listener!=null){
                listener.click(v,getPosition());
            }
        }

        @Override
        public boolean onLongClick(View v) {
            longListener.click(v,getPosition());
            return true;
        }
    }
    public interface ClickListener{
        void click(View v, int position);
    }
    public interface LongClickListener{
        void click(View v, int position);
    }
}
