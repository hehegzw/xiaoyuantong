package com.jlstudio.group.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.jlstudio.R;
import com.jlstudio.group.bean.LeaveMsg;

import org.w3c.dom.Text;

import java.util.List;

/**
 * Created by gzw on 2016/3/9.
 */
public class LeaveMsgDetailAtyAdapter extends BaseAdapter {
    private List<LeaveMsg> list;
    private LayoutInflater inflater;
    private String username;
    private int showCount;
    public LeaveMsgDetailAtyAdapter(Context context,List<LeaveMsg> list,String username) {
        this.list = list;
        inflater = LayoutInflater.from(context);
        this.username = username;
    }

    public void setList(List<LeaveMsg> list) {
        this.list = list;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        MyHolder holder;
        if(convertView == null){
            convertView = inflater.inflate(R.layout.sub_leavemsgdetail,null);
            holder = new MyHolder();
            holder.fromname = (TextView) convertView.findViewById(R.id.fromname);
            holder.text = (TextView) convertView.findViewById(R.id.text);
            holder.toname = (TextView) convertView.findViewById(R.id.toname);
            holder.content = (TextView) convertView.findViewById(R.id.content);
            convertView.setTag(holder);
        }else{
            holder = (MyHolder) convertView.getTag();
        }
        if(list.get(position).getFrom().equals(username)){
            holder.fromname.setText(username);
            holder.toname.setText(list.get(position).getTo());
        }else{
            holder.fromname.setText(list.get(position).getFrom());
            holder.text.setVisibility(View.GONE);
        }
        holder.content.setText(list.get(position).getContent());
        return convertView;
    }
    private class MyHolder{
        public TextView fromname;
        public TextView text;
        public TextView toname;
        public TextView content;
    }

    @Override
    public boolean isEnabled(int position) {
        return false;
    }
}
