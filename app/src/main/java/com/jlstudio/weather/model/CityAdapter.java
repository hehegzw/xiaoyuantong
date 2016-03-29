package com.jlstudio.weather.model;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;


import com.jlstudio.R;
import com.jlstudio.main.application.MyApplication;

import java.util.List;

/**
 * Created by gzw on 2015/10/5.
 */
public class CityAdapter extends BaseAdapter {
    private List<City>list = null;
    public CityAdapter(List<City> list) {
        this.list = list;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    public List<City> getList() {
        return list;
    }

    public void setList(List<City> list) {
        this.list = list;
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
        ViewHoder item;
        if(convertView == null){
            convertView = LayoutInflater.from(MyApplication.getContext()).inflate(R.layout.weather_list_view,null);
            TextView tv = (TextView) convertView.findViewById(R.id.show);
            item = new ViewHoder(tv);
            convertView.setTag(item);
        }else{
            item = (ViewHoder) convertView.getTag();
        }
        item.tv.setText(list.get(position).getName());
        return convertView;
    }
    class ViewHoder{
        public TextView tv;

        public ViewHoder(TextView tv) {
            this.tv = tv;
        }
    }
}
