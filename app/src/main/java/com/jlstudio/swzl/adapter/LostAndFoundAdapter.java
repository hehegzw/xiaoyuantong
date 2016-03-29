package com.jlstudio.swzl.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.jlstudio.R;
import com.jlstudio.swzl.bean.lostfound;

import java.util.ArrayList;

/**
 * Created by Joe on 2015/10/23.
 */
public class LostAndFoundAdapter extends BaseAdapter{
    private ArrayList<lostfound> allLostFound = new ArrayList<lostfound>();
    LayoutInflater inflater;
    private lostfound lf = new lostfound();
    Context context = null;
    View convertView;
    public LostAndFoundAdapter(Context context,ArrayList<lostfound> allLostFound) {
        this.inflater = inflater.from(context);
        this.allLostFound = allLostFound;
        this.context = context;
    }

    @Override
    public int getCount() {
        return allLostFound.size();
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
    public View getView(final int position, View convertView, ViewGroup parent) {
        //获取当前item对应的对象
        ViewHolder item;
        if(convertView == null){
            convertView = inflater.inflate(R.layout.lostandfound_item, null);
            TextView title = (TextView) convertView.findViewById(R.id.lost_found_title);
            TextView nickname = (TextView) convertView.findViewById(R.id.lost_found_nickname);
            TextView comment_count = (TextView) convertView.findViewById(R.id.lost_found_count);
            TextView time = (TextView) convertView.findViewById(R.id.lost_found_time);
            //获取失物招领的类别
            TextView flag = (TextView) convertView.findViewById(R.id.lost_found_flag);
            item = new ViewHolder(title,nickname,comment_count,time,flag);


            convertView.setTag(item);
        }else{
            item = (ViewHolder) convertView.getTag();
        }
        lf = allLostFound.get(position);
        item.title.setText(lf.getTitle());
        item.nickname.setText(lf.getNickname());
        item.comment_count.setText("1");
        item.time.setText(lf.getTime());
        if(lf.getFlag() == 0){
            item.flag.setText("失");
            item.flag.setBackgroundResource(R.drawable.lostandfoud_lost_shape);
        }
        else{
            item.flag.setText("领");
            item.flag.setBackgroundResource(R.drawable.lostandfoud_found_shape);
        }
        return convertView;
    }
    private class ViewHolder{
        public TextView title;
        public TextView nickname;
        public TextView comment_count;
        public TextView time;
        public TextView flag;

        public ViewHolder(TextView title, TextView nickname, TextView comment_count, TextView time,TextView flag) {
            this.title = title;
            this.nickname = nickname;
            this.comment_count = comment_count;
            this.time = time;
            this.flag = flag;
        }
    }
}
