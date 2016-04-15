package com.jlstudio.group.adapter;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import com.jlstudio.R;
import com.jlstudio.group.bean.Groups;

import java.util.List;

/**
 * Created by gzw on 2015/11/22.
 */
public class FMyContactGroupAdapter extends BaseExpandableListAdapter {
    private List<Groups> listParent;
    private List<List<Groups>> listChild;
    private Context context;

    public FMyContactGroupAdapter(Context context, List<Groups> listParent, List<List<Groups>> listChild) {
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
    public View getGroupView(final int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        ViewHolder view;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.sub_fmycontact_parent1, null);
            TextView tv = (TextView) convertView.findViewById(R.id.name);
            TextView select = (TextView) convertView.findViewById(R.id.select);
            Typeface iconfont = Typeface.createFromAsset(context.getAssets(), "fonts/dot.ttf");
            select.setTypeface(iconfont);
            view = new ViewHolder(tv,select);
            convertView.setTag(view);
        } else {
            view = (ViewHolder) convertView.getTag();
        }
        Groups contact = listParent.get(groupPosition);
        view.itemTv.setText(contact.getGroup_name());
        view.select.setVisibility(View.GONE);
        return convertView;
    }

    @Override
    public View getChildView(final int groupPosition, final int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        ViewHolder view;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.sub_fmycontact_parent1, null);
            TextView tv = (TextView) convertView.findViewById(R.id.name);
            TextView select = (TextView) convertView.findViewById(R.id.select);
            Typeface iconfont = Typeface.createFromAsset(context.getAssets(), "fonts/dot.ttf");
            select.setTypeface(iconfont);
            view = new ViewHolder(tv,select);
            convertView.setTag(view);
        } else {
            view = (ViewHolder) convertView.getTag();
        }
        Groups contact = listChild.get(groupPosition).get(childPosition);
        view.itemTv.setText(contact.getGroup_name());
        view.select.setVisibility(View.GONE);
        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

    private class ViewHolder {
        public TextView itemTv;
        public TextView select;

        public ViewHolder(TextView itemTv,TextView select) {
            this.itemTv = itemTv;
            this.select = select;
        }
    }
    private class ParentClickListener implements View.OnClickListener{
        int groupPosition;

        public ParentClickListener(int groupPosition) {
            this.groupPosition = groupPosition;
        }

        @Override
        public void onClick(View v) {
            if (listParent.get(groupPosition).isSelected()) {
                listParent.get(groupPosition).setIsSelected(false);
                for (int i = 0; i < listChild.get(groupPosition).size(); i++) {
                    listChild.get(groupPosition).get(i).setIsSelected(false);
                }
            } else {
                listParent.get(groupPosition).setIsSelected(true);
                for (int i = 0; i < listChild.get(groupPosition).size(); i++) {
                    listChild.get(groupPosition).get(i).setIsSelected(true);
                }
            }
            notifyDataSetChanged();
        }
    }
}
