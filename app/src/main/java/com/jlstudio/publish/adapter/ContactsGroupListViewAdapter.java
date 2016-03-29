package com.jlstudio.publish.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.jlstudio.R;
import com.jlstudio.publish.bean.Contacts;

import java.util.List;

/**
 * Created by NewOr on 2015/11/20.
 */
public class ContactsGroupListViewAdapter extends BaseAdapter {

    private Context context;
    private List<Contacts> contactsList;

    public ContactsGroupListViewAdapter(Context context, List<Contacts> contactsList) {
        this.context = context;
        this.contactsList = contactsList;
    }

    public void onDataChanged(List<Contacts> contactsList) {
        this.contactsList = contactsList;
        this.notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return contactsList.size();
    }

    @Override
    public Object getItem(int position) {
        return contactsList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = new ViewHolder();
        convertView = LayoutInflater.from(context).inflate(R.layout.sub_item_contacts_groups, null);
        holder.tv_contacts_groupname = (TextView) convertView.findViewById(R.id.tv_contacts_groupname);
        holder.tv_contacts_groupname.setText(contactsList.get(position).getGroupname());
        return convertView;
    }

    class ViewHolder {
        TextView tv_contacts_groupname;
    }
}
