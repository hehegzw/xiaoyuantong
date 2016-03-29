package com.jlstudio.publish.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.jlstudio.R;
import com.jlstudio.publish.bean.Person;
import com.jlstudio.publish.bean.PublishData;

import java.util.List;

/**
 * Created by gzw on 2015/10/16.
 */
public class SelectDepartmentadapter extends BaseAdapter {
    private List<String> list;
    private Context context;

    public SelectDepartmentadapter(Context context, List<String> list) {
        this.list = list;
        this.context = context;
    }

    public List<String> getList() {
        return list;
    }

    public void setList(List<String> list) {
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
        ViewHoder hoder;
        if(convertView == null){
            convertView = LayoutInflater.from(context).inflate(R.layout.dialog_list_item, null);
            TextView title = (TextView) convertView.findViewById(R.id.title);
            hoder = new ViewHoder(title);
            convertView.setTag(hoder);
        }else{
            hoder = (ViewHoder) convertView.getTag();
        }
        hoder.title.setText(list.get(position));
        return convertView;
    }

    private class ViewHoder {
        public TextView title;

        public ViewHoder(TextView title) {
            this.title = title;
        }
    }
}
