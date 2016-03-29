package com.jlstudio.group.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.jlstudio.R;
import com.jlstudio.group.bean.Contacts;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Joe on 2015/11/24.
 */
public class SearchAdapter extends BaseAdapter{
    private List<Contacts> p_list = new ArrayList<>();
    private Contacts p ;
    Context context = null;
    LayoutInflater inflater;
    public SearchAdapter(Context context, List<Contacts> p_list) {
        this.p_list = p_list;
        this.inflater = inflater.from(context);
        this.context = context;
    }

    public void setP_list(List<Contacts> p_list) {
        this.p_list = p_list;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return p_list.size();
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
        //获取当前item对应的对象
        Log.d("Adapter","进入adapter");
        ViewHolder item;
        if(convertView == null){
            convertView = inflater.inflate(R.layout.search_list_item, null);
            TextView name = (TextView) convertView.findViewById(R.id.realname);
            TextView grade = (TextView) convertView.findViewById(R.id.classname);
            TextView sign = (TextView) convertView.findViewById(R.id.sign);
            item = new ViewHolder(name,grade,sign);
            convertView.setTag(item);
        }else{
            item = (ViewHolder) convertView.getTag();
        }
        p = p_list.get(position);
        //为每一个item赋值
        Log.d("Adapter - -",p.getRealname()+"+++++"+p.getPinYinName());
        item.name.setText(p.getRealname());
        item.grade.setText(p.getClasses());
        item.sign.setText(p.getSign());

        return convertView;
    }
    private class ViewHolder{
        public TextView name;
        public TextView sign;
        public TextView grade;

        public ViewHolder(TextView name,TextView grade , TextView sign) {
            this.name = name;
            this.sign = sign;
            this.grade = grade;
        }
    }
}
