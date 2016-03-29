package com.jlstudio.publish.adapter;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.jlstudio.R;
import com.jlstudio.group.bean.Contacts;
import com.jlstudio.publish.bean.MyContact;
import com.jlstudio.publish.bean.Person;

import java.util.List;

/**
 * Created by gzw on 2015/10/20.
 */
public class ShowPersonAdapter extends BaseAdapter {
    private List<Contacts> list;
    private Context context;

    public ShowPersonAdapter(Context context, List<Contacts> list) {
        this.list = list;
        this.context = context;
    }

    public void setList(List<Contacts> list) {
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
            convertView = LayoutInflater.from(context).inflate(R.layout.sub_showpersondetail, null);
            TextView tv = (TextView) convertView.findViewById(R.id.itemTv);
            TextView select = (TextView) convertView.findViewById(R.id.select);
            Typeface iconfont = Typeface.createFromAsset(context.getAssets(), "fonts/dot.ttf");
            select.setTypeface(iconfont);
            TextView isreg = (TextView) convertView.findViewById(R.id.isReg);
            view = new ViewHolder(tv, select,isreg);
            convertView.setTag(view);
        } else {
            view = (ViewHolder) convertView.getTag();
        }
        Contacts contact = list.get(position);
        view.itemTv.setText(contact.getUsername());
        //判断是否被选中，改变小圆点的颜色
        if (contact.isSelected()) {
            view.select.setTextColor(context.getResources().getColor(R.color.blue));
        } else {
            view.select.setTextColor(context.getResources().getColor(R.color.fragment_back));
        }
        //判断此用户时候注册，改变本条目的背景颜色
        if(contact.isRegister()){
            convertView.setBackgroundColor(context.getResources().getColor(R.color.white));
        }else{
            view.isreg.setText("未注册");
            convertView.setBackgroundColor(context.getResources().getColor(R.color.fragment_back));
        }
        return convertView;
    }

    private class ViewHolder {
        public TextView itemTv;
        public TextView select;
        public TextView isreg;

        public ViewHolder(TextView itemTv, TextView select,TextView isreg) {
            this.itemTv = itemTv;
            this.select = select;
            this.isreg = isreg;
        }
    }
}
