package com.jlstudio.swzl.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.jlstudio.R;
import com.jlstudio.swzl.bean.commons;

import java.util.ArrayList;

/**
 * Created by Joe on 2015/10/27.
 */
public class LostAndFoundDetailAdapter extends BaseAdapter {
    private ArrayList<commons> list_com = new ArrayList<commons>();
    private Context context;
    private LayoutInflater layoutInflater ;
    private commons com;
    public LostAndFoundDetailAdapter(ArrayList<commons> list_com,Context context) {
        this.layoutInflater = layoutInflater.from(context);
        this.list_com = list_com;
        this.context = context;
    }

    public ArrayList<commons> getList_com() {
        return list_com;
    }

    public void setList_com(ArrayList<commons> list_com) {
        this.list_com = list_com;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return list_com.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Log.d("AAA","add----?>?>>>>");
        com = list_com.get(position);
        ViewHolder holder = null;
        if(convertView == null){
            convertView = layoutInflater.inflate(R.layout.lostandfound_detail_item, null);
            holder = new ViewHolder();
            holder.content = (TextView) convertView.findViewById(R.id.lost_found_detail_content);
            holder.nickname = (TextView) convertView.findViewById(R.id.lost_found_detail_nickname);
            holder.time = (TextView) convertView.findViewById(R.id.lost_found_detail_time);
            convertView.setTag(holder);
        }
        else{
            holder = (ViewHolder)convertView.getTag();
        }
        holder.content.setText(com.getContent());
        holder.nickname.setText(com.getUserNickname());
        holder.time.setText(com.getTime());
        return convertView;
    }
    public static class ViewHolder {
        public TextView content;
        public TextView nickname;
        public TextView time;

    }
}
