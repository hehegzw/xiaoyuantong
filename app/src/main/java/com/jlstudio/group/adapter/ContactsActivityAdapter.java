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
import com.jlstudio.main.application.Config;
import com.jlstudio.main.net.Downloadimgs;

import java.util.List;

/**
 * Created by NewOr on 2015/11/30.
 */
public class ContactsActivityAdapter extends BaseAdapter {
    private Context context;
    private List<Contacts> list;
    private LayoutInflater inflater;

    public ContactsActivityAdapter(Context context, List<Contacts> list) {
        this.context = context;
        this.list = list;
        inflater = LayoutInflater.from(context);
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
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView ==null) {
            convertView = inflater.inflate(R.layout.contacts_item, null);
            TextView name = (TextView) convertView.findViewById(R.id.name);
            TextView sign = (TextView) convertView.findViewById(R.id.sign);
            ImageView face = (ImageView) convertView.findViewById(R.id.face);
            viewHolder = new ViewHolder(name, sign, face);
            convertView.setTag(viewHolder);
        }else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.name.setText(list.get(position).getRealname());
        viewHolder.sign.setText(list.get(position).getSign());
        String url = Config.URL + "faces/" + list.get(position).getUsername() + ".jpg";
        Downloadimgs.initImageLoader(context).displayImage(url, viewHolder.face, Downloadimgs.getOption());
        return convertView;
    }

    private class ViewHolder {
        public TextView name;
        public TextView sign;
        public ImageView face;

        public ViewHolder(TextView name, TextView sign, ImageView face) {
            this.name = name;
            this.sign = sign;
            this.face = face;
        }

        public ViewHolder(TextView name) {
            this.name = name;
        }
    }

}


