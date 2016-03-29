package com.jlstudio.main.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.jlstudio.R;
import com.jlstudio.main.bean.GridViewItem;

import java.util.List;

/**
 * Created by gzw on 2015/11/21.
 */
public class GridViewAdapter extends BaseAdapter {
    private Context context;
    private List<GridViewItem> list;
    private LayoutInflater inflater;

    public GridViewAdapter(Context context, List<GridViewItem> list) {
        this.context = context;
        this.list = list;
        inflater = LayoutInflater.from(context);
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
        if(convertView == null){
            convertView = inflater.inflate(R.layout.sub_gridview,null);
            convertView.setPadding(0, 10, 0, 0);
            //convertView.setBackgroundColor(Color.WHITE);
            ImageView iv = (ImageView) convertView.findViewById(R.id.image);
            TextView tv = (TextView) convertView.findViewById(R.id.text);
            tv.setPadding(0,20,10,20);
            view = new ViewHolder(iv,tv);
            convertView.setTag(view);
        }else{
            view = (ViewHolder) convertView.getTag();
        }
        view.iv.setImageResource(list.get(position).getImage());
        view.tv.setText(list.get(position).getName());
        return convertView;
    }
    private class ViewHolder{
        public ImageView iv;
        public TextView tv;

        public ViewHolder(ImageView iv, TextView tv) {
            this.iv = iv;
            this.tv = tv;
        }
    }
}
