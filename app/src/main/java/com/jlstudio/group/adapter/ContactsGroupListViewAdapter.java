package com.jlstudio.group.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.jlstudio.R;
import com.jlstudio.group.bean.Contacts;
import com.jlstudio.group.bean.Groups;

import java.util.List;

/**
 * Created by NewOr on 2015/11/20.
 */
public class ContactsGroupListViewAdapter extends BaseAdapter {

    private Context context;
    private List<Groups> contactsList;
    private ListView listView;

    public ContactsGroupListViewAdapter(Context context, List<Groups> contactsList, ListView listView) {
        this.listView = listView;
        this.context = context;
        this.contactsList = contactsList;
    }

    public void onDataChanged(List<Groups> contactsList) {
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
        convertView = LayoutInflater.from(context).inflate(R.layout.item_contacts_groups, null);
        holder.tv_contacts_groupname = (TextView) convertView.findViewById(R.id.tv_contacts_groupname);
        holder.tv_contacts_groupname.setText(contactsList.get(position).getGroup_name());
        updateBackground(position, convertView);
        return convertView;
    }

    class ViewHolder {
        TextView tv_contacts_groupname;
    }

    public void updateBackground(int position, View view) {

        int backgroundId;
        if (listView.isItemChecked(position)) {
            backgroundId = R.drawable.list_selected_holo_light;
        } else {
            backgroundId = R.drawable.conversation_item_background_read;
        }
        Drawable background = context.getResources().getDrawable(backgroundId);
        view.setBackground(background);
    }
}
