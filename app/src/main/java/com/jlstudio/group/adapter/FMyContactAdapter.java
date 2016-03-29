package com.jlstudio.group.adapter;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.jlstudio.R;
import com.jlstudio.group.bean.Groups;

import java.util.List;

/**
 * Created by gzw on 2015/11/22.
 */
public class FMyContactAdapter extends BaseExpandableListAdapter {
    private List<Groups> listParent;
    private List<List<Groups>> listChild;
    private Context context;

    public FMyContactAdapter(Context context, List<Groups> listParent, List<List<Groups>> listChild) {
        this.listParent = listParent;
        this.listChild = listChild;
        this.context = context;
    }

    public List<Groups> getListParent() {
        return listParent;
    }

    public void setList(List<Groups> listParent, List<List<Groups>> listChild) {
        this.listParent = listParent;
        this.listChild = listChild;
        notifyDataSetChanged();
    }

    @Override
    public int getGroupCount() {
        return listParent.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return listChild.get(groupPosition).size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return listParent.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return listChild.get(childPosition).get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        ViewHolderParent view;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.sub_fmycontact_parent, null);
            TextView tv = (TextView) convertView.findViewById(R.id.itemTv);
            view = new ViewHolderParent(tv);
            convertView.setTag(view);
        } else {
            view = (ViewHolderParent) convertView.getTag();
        }
        Groups contact = listParent.get(groupPosition);
        view.name.setText(contact.getGroup_name());
        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        ViewHolderItem view;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.sub_fmycontact_child, null);
            TextView name = (TextView) convertView.findViewById(R.id.name);
            TextView sign = (TextView) convertView.findViewById(R.id.sign);
            ImageView image = (ImageView) convertView.findViewById(R.id.image);
            view = new ViewHolderItem(name,sign,image);
            convertView.setTag(view);
        } else {
            view = (ViewHolderItem) convertView.getTag();
        }
        Groups contact = listChild.get(groupPosition).get(childPosition);
        view.name.setText(contact.getGroup_name());
        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

    private class ViewHolderParent{
        public TextView name;

        public ViewHolderParent(TextView name) {
            this.name = name;
        }
    }
    private class ViewHolderItem {
        public TextView name;
        public TextView sign;
        public ImageView image;

        public ViewHolderItem(TextView name, TextView sign, ImageView image) {
            this.name = name;
            this.sign = sign;
            this.image = image;
        }
    }
}
