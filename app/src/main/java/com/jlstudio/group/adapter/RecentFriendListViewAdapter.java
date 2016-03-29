package com.jlstudio.group.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.jlstudio.R;
import com.jlstudio.group.bean.Contacts;
import com.jlstudio.main.application.Config;
import com.jlstudio.main.net.Downloadimgs;

import java.util.List;

/**
 * Created by NewOr on 2015/11/21.
 */
public class RecentFriendListViewAdapter extends BaseAdapter {
    private Context context;
    private List<Contacts> contacst_list;
    private LayoutInflater inflater;

    public RecentFriendListViewAdapter(Context context, List<Contacts> contacst_list) {
        this.context = context;
        this.contacst_list = contacst_list;
        inflater = LayoutInflater.from(context);
    }

    public void onDataChanged(List<Contacts> contacst_list){
        this.contacst_list = contacst_list;
        this.notifyDataSetChanged();
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
        ViewHolder viewHolder;
        if(convertView == null){
            convertView = inflater.inflate(R.layout.contacts_item, null);
            ImageView face = (ImageView) convertView.findViewById(R.id.face);
            TextView name = (TextView) convertView.findViewById(R.id.name);
            TextView sign = (TextView) convertView.findViewById(R.id.sign);
            viewHolder = new ViewHolder(name,sign,face);
            convertView.setTag(viewHolder);

        }else{
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.name.setText(contacst_list.get(position).getRealname());
        viewHolder.time.setText(contacst_list.get(position).getTime());
        String url = Config.URL+"faces/" +contacst_list.get(position).getUsername() + ".jpg";
        Downloadimgs.initImageLoader(context).displayImage(url, viewHolder.face, Downloadimgs.getOption());
        return convertView;
    }

    private class ViewHolder {
        public TextView name;
        public TextView time;
        public ImageView face;

        public ViewHolder(TextView name, TextView time, ImageView face) {
            this.name = name;
            this.time = time;
            this.face = face;
        }
    }
}
