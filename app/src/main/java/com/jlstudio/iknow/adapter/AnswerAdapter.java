package com.jlstudio.iknow.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.jlstudio.R;
import com.jlstudio.iknow.bean.Answer;
import com.jlstudio.iknow.bean.Question;

import java.util.List;

/**
 * Created by gzw on 2015/10/21.
 */
public class AnswerAdapter extends BaseAdapter {
    private Context context;
    private List<Answer> list;

    public AnswerAdapter(Context context, List<Answer> list) {
        this.context = context;
        this.list = list;
    }

    public List<Answer> getList() {
        return list;
    }

    public void setList(List<Answer> list) {
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
        ViewHolder item;
        if(convertView == null){
            convertView = LayoutInflater.from(context).inflate(R.layout.answer_item,null);
            TextView answer = (TextView) convertView.findViewById(R.id.answer);
            TextView user = (TextView) convertView.findViewById(R.id.user);
            TextView time = (TextView) convertView.findViewById(R.id.time);
            item = new ViewHolder(answer,user,time);
            convertView.setTag(item);
        }else{
            item = (ViewHolder) convertView.getTag();
        }
        item.answer.setText(list.get(position).getAnswer());
        item.user.setText(list.get(position).getUser());
        item.time.setText(list.get(position).getTime());
        return convertView;
    }

    private class ViewHolder{
        public TextView answer;
        public TextView user;
        public TextView time;

        public ViewHolder(TextView title, TextView time,TextView count) {
            this.answer = title;
            this.user = time;
            this.time = count;
        }
    }
}
