package com.jlstudio.publish.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import com.jlstudio.R;
import com.jlstudio.group.bean.Contacts;
import com.jlstudio.group.bean.Groups;
import com.jlstudio.main.application.Config;
import com.jlstudio.publish.bean.MyContact;
import com.jlstudio.publish.dialog.UNRegisterQueryDialog;
import com.jlstudio.publish.dialog.UnRegisterQueryDialog2;

import java.util.List;

/**
 * Created by gzw on 2015/11/22.
 */
public class FMyContactAdapter extends BaseExpandableListAdapter {
    private List<Groups> listParent;
    private List<List<Groups>> listChild;
    private Context context;
    private ChildListener listener;

    public FMyContactAdapter(Context context, List<Groups> listParent, List<List<Groups>> listChild) {
        this.listParent = listParent;
        this.listChild = listChild;
        this.context = context;
    }

    public void setListener(ChildListener listener) {
        this.listener = listener;
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
            TextView count = (TextView) convertView.findViewById(R.id.count);
            Typeface iconfont = Typeface.createFromAsset(context.getAssets(), "fonts/dot.ttf");
            select.setTypeface(iconfont);
            view = new ViewHolder(tv,select,count);
            convertView.setTag(view);
        } else {
            view = (ViewHolder) convertView.getTag();
        }
        Groups contact = listParent.get(groupPosition);
        view.itemTv.setText(contact.getGroup_name());
        view.count.setText(contact.getRegisterCount()+"/"+contact.getSubCounts());
        view.select.setOnClickListener(new ParentClickListener(groupPosition));
        if (contact.isSelected()) {
            view.select.setTextColor(context.getResources().getColor(R.color.blue));
        } else {
            view.select.setTextColor(context.getResources().getColor(R.color.fragment_back));
        }

        return convertView;
    }

    @Override
    public View getChildView(final int groupPosition, final int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        ViewHolder view;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.sub_fmycontact_parent1, null);
            TextView tv = (TextView) convertView.findViewById(R.id.name);
            TextView select = (TextView) convertView.findViewById(R.id.select);
            TextView count = (TextView) convertView.findViewById(R.id.count);
            Typeface iconfont = Typeface.createFromAsset(context.getAssets(), "fonts/dot.ttf");
            select.setTypeface(iconfont);
            view = new ViewHolder(tv,select,count);
            convertView.setTag(view);
        } else {
            view = (ViewHolder) convertView.getTag();
        }
        Groups contact = listChild.get(groupPosition).get(childPosition);
        view.count.setText(contact.getRegisterCount()+"/"+contact.getSubCounts());
        view.itemTv.setText(contact.getGroup_name());
        view.select.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listChild.get(groupPosition).get(childPosition).isSelected()){
                    listParent.get(groupPosition).setIsSelected(false);
                    listChild.get(groupPosition).get(childPosition).setIsSelected(false);
                }else
                    listChild.get(groupPosition).get(childPosition).setIsSelected(true);

                notifyDataSetChanged();
                if(listener!=null){
                    listener.click(groupPosition,childPosition);
                }
            }
        });
        if (contact.isSelected()) {
            view.select.setTextColor(context.getResources().getColor(R.color.blue));
        } else {
            view.select.setTextColor(context.getResources().getColor(R.color.fragment_back));
        }
        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

    private class ViewHolder {
        public TextView itemTv;
        public TextView select;
        public TextView count;

        public ViewHolder(TextView itemTv,TextView select,TextView count) {
            this.itemTv = itemTv;
            this.select = select;
            this.count = count;
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
    public interface ChildListener{
        void click(int groupIndex,int childIndex);
    }
}
