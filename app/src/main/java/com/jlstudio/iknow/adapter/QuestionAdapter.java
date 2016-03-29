package com.jlstudio.iknow.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.jlstudio.R;
import com.jlstudio.iknow.bean.Question;

import java.util.List;

/**
 * Created by gzw on 2015/10/21.
 */
public class QuestionAdapter extends BaseAdapter {
    private Context context;
    private List<Question> list;

    public QuestionAdapter(Context context, List<Question> list) {
        this.context = context;
        this.list = list;
    }

    public List<Question> getList() {
        return list;
    }

    public void setList(List<Question> list) {
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
            convertView = LayoutInflater.from(context).inflate(R.layout.question_item,null);
            TextView title = (TextView) convertView.findViewById(R.id.title);
            TextView count = (TextView) convertView.findViewById(R.id.count);
            TextView time = (TextView) convertView.findViewById(R.id.time);
            item = new ViewHolder(title,time,count);
            convertView.setTag(item);
        }else{
            item = (ViewHolder) convertView.getTag();
        }
        item.title.setText(list.get(position).getTitile());
        item.count.setText(list.get(position).getCount());
        item.time.setText(list.get(position).getTime());
        return convertView;
    }

    private class ViewHolder{
        public TextView title;
        public TextView time;
        public TextView count;

        public ViewHolder(TextView title, TextView time,TextView count) {
            this.title = title;
            this.time = time;
            this.count = count;
        }
    }
}
