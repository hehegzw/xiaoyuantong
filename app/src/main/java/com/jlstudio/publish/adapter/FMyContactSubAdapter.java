package com.jlstudio.publish.adapter;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import com.jlstudio.R;
import com.jlstudio.group.bean.Contacts;
import com.jlstudio.group.bean.Groups;
import com.jlstudio.publish.bean.MyContact;

import java.util.List;

/**
 * Created by gzw on 2015/11/22.
 */
public class FMyContactSubAdapter extends BaseExpandableListAdapter {
    private List<Groups> listParent;
    private List<List<MyContact>> listChild;
    private Context context;

    public FMyContactSubAdapter(Context context, List<Groups> listParent, List<List<MyContact>> listChild) {
        this.listParent = listParent;
        this.listChild = listChild;
        this.context = context;
    }

    public List<Groups> getListParent() {
        return listParent;
    }

    public void setList(List<Groups> listParent,List<List<MyContact>> listChild) {
        this.listParent = listParent;
        this.listChild = listChild;
        notifyDataSetChanged();
    }

    public void setListChild(List<List<MyContact>> listChild) {
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
            TextView select = (TextView) convertView.findViewById(R.id.select);
            Typeface iconfont = Typeface.createFromAsset(context.getAssets(), "fonts/dot.ttf");
            TextView personCount = (TextView) convertView.findViewById(R.id.peopleCount);
            select.setTypeface(iconfont);
            view = new ViewHolderParent(tv, select,personCount);
            convertView.setTag(view);
        } else {
            view = (ViewHolderParent) convertView.getTag();
        }
        Groups contact = listParent.get(groupPosition);
        view.itemTv.setText(contact.getGroup_name());
        view.personCount.setText(contact.getRegisterCount()+"/"+contact.getSubCounts());
        view.select.setOnClickListener(new ParentClickListener(groupPosition));
        if (contact.isSelected()) {
            view.select.setTextColor(context.getResources().getColor(R.color.blue));
        } else {
            view.select.setTextColor(context.getResources().getColor(R.color.fragment_back));
        }
        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        ViewHolder view;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.sub_fmycontactsub_child, null);
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
        MyContact contact = listChild.get(groupPosition).get(childPosition);
        view.itemTv.setText(contact.getName());
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

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }
    private class ViewHolderParent {
        public TextView itemTv;
        public TextView select;
        public TextView personCount;

        public ViewHolderParent(TextView itemTv, TextView select,TextView personCount) {
            this.itemTv = itemTv;
            this.select = select;
            this.personCount = personCount;
        }
        public ViewHolderParent(TextView itemTv, TextView select) {
            this.itemTv = itemTv;
            this.select = select;
        }
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
        public ViewHolder(TextView itemTv, TextView select) {
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
