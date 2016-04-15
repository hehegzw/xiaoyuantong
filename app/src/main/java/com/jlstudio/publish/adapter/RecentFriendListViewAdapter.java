package com.jlstudio.publish.adapter;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.jlstudio.R;
import com.jlstudio.group.bean.Contacts;
import com.jlstudio.publish.bean.MyContact;

import java.util.List;

/**
 * Created by NewOr on 2015/11/21.
 */
public class RecentFriendListViewAdapter extends BaseAdapter {
    private Context context;
    private List<MyContact> contacst_list;

    public RecentFriendListViewAdapter(Context context, List<MyContact> contacst_list) {
        this.context = context;
        this.contacst_list = contacst_list;
    }


    public void setContacst_list(List<MyContact> contacst_list) {
        this.contacst_list = contacst_list;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return contacst_list.size();
    }

    @Override
    public Object getItem(int position) {
        return contacst_list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder view;
        if(convertView == null){
            convertView = LayoutInflater.from(context).inflate(R.layout.sub_frecentcontact, null);
            TextView tv = (TextView) convertView.findViewById(R.id.itemTv);
            TextView select = (TextView) convertView.findViewById(R.id.select);
            Typeface iconfont = Typeface.createFromAsset(context.getAssets(),"fonts/dot.ttf");
            select.setTypeface(iconfont);
            view = new ViewHolder(tv,select);
            convertView.setTag(view);
        }else{
            view = (ViewHolder) convertView.getTag();
        }
        MyContact contact = contacst_list.get(position);
        view.itemTv.setText(contact.getName());
        if(contact.isSelected()){
            view.select.setTextColor(context.getResources().getColor(R.color.blue));
        }else{
            view.select.setTextColor(context.getResources().getColor(R.color.fragment_back));
        }
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
