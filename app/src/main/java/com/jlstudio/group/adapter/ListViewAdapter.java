package com.jlstudio.group.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.jlstudio.R;
import com.jlstudio.group.bean.Contacts;
import com.jlstudio.main.net.DownFace;

import java.util.List;

public class ListViewAdapter extends BaseAdapter {
    private Context context;
    private List<Contacts> list;
    private ViewHolder viewHolder;
    private DownFace df;//加载头像

    public ListViewAdapter(Context context, List<Contacts> list) {
        this.context = context;
        this.list = list;
        df = new DownFace(context);
        Log.i("Test", "获取数组..." + list.toString());
    }

    public void onDataChanged(List<Contacts> list) {
        this.list = list;
        this.notifyDataSetChanged();
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
    public boolean isEnabled(int position) {
        // TODO Auto-generated method stub
        if (list.get(position).getRealname().length() == 1)// 如果是字母索引
            return false;// 表示不能点击
        return super.isEnabled(position);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        String item = list.get(position).getRealname();
        viewHolder = new ViewHolder();
            if (position == 0 || position == 1) {
                convertView = LayoutInflater.from(context).inflate(R.layout.fcontact_list_item, null);
                viewHolder.show_item_name = (TextView) convertView.findViewById(R.id.show_item_name);
                viewHolder.show_item_name.setText(list.get(position).getRealname());
                if (position == 1 ) {
                    viewHolder.img_arrow_right = (ImageView) convertView.findViewById(R.id.img_arrow_right);
                    viewHolder.img_arrow_right.setVisibility(View.VISIBLE);
                }
        } else {
            if (item.length() == 1) {
                convertView = LayoutInflater.from(context).inflate(R.layout.contacts_index,
                        null);
                viewHolder.indexTv = (TextView) convertView
                        .findViewById(R.id.indexTv);
            } else {
                convertView = LayoutInflater.from(context).inflate(R.layout.contacts_item,
                        null);
                viewHolder.itemTv = (TextView) convertView
                        .findViewById(R.id.itemTv);
                viewHolder.face = (ImageView) convertView.findViewById(R.id.face);
            }
            if (item.length() == 1) {
                viewHolder.indexTv.setText(list.get(position).getRealname());
            } else {
                String[] showName = list.get(position).getRealname().split(",");
                viewHolder.itemTv.setText(showName[0]);
                df.loadImages(viewHolder.face,list.get(position).getUsername());
            }
        }
        return convertView;
    }

    private class ViewHolder {
        private TextView indexTv;
        private TextView itemTv;
        private TextView show_item_name;
        private ImageView img_arrow_right;//右箭头
        private ImageView face;//头像
    }
}