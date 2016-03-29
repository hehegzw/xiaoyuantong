package com.jlstudio.publish.adapter;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.jlstudio.R;
import com.jlstudio.publish.bean.MyContact;

import java.util.List;

/**
 * Created by gzw on 2015/10/20.
 */
public class ShowUnReplyPersonAdapter extends BaseAdapter {
    private List<MyContact> list;
    private Context context;

    public ShowUnReplyPersonAdapter(Context context, List<MyContact> list) {
        this.list = list;
        this.context = context;
    }

    public void setList(List<MyContact> list) {
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
        ViewHolder view;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.sub_show_unreply_person, null);
            TextView tv = (TextView) convertView.findViewById(R.id.itemTv);
            TextView select = (TextView) convertView.findViewById(R.id.select);
            view = new ViewHolder(tv, select);
            convertView.setTag(view);
        } else {
            view = (ViewHolder) convertView.getTag();
        }
        MyContact contact = list.get(position);
        view.itemTv.setText(contact.getName());
        return convertView;
    }

    private class ViewHolder {
        public TextView itemTv;
        public TextView select;

        public ViewHolder(TextView itemTv, TextView select) {
            this.itemTv = itemTv;
            this.select = select;
        }
    }
}
