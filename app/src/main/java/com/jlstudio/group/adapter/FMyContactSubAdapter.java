package com.jlstudio.group.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.jlstudio.R;
import com.jlstudio.group.bean.Contacts;
import com.jlstudio.group.bean.Groups;
import com.jlstudio.main.application.Config;
import com.jlstudio.main.net.DownFace;
import com.jlstudio.main.net.Downloadimgs;
import com.jlstudio.publish.bean.MyContact;

import java.io.File;
import java.util.List;

/**
 * Created by gzw on 2015/11/22.
 */
public class FMyContactSubAdapter extends BaseExpandableListAdapter {
    private List<Groups> listParent;
    private List<List<MyContact>> listChild;
    private Context context;
    private DownFace df;

    public FMyContactSubAdapter(Context context, List<Groups> listParent, List<List<MyContact>> listChild) {
        this.listParent = listParent;
        this.listChild = listChild;
        this.context = context;
        df = new DownFace(context);
    }

    public List<Groups> getListParent() {
        return listParent;
    }

    public void setList(List<Groups> listParent,List<List<MyContact>> listChild) {
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
            convertView = LayoutInflater.from(context).inflate(R.layout.sub_fmycontact_parent1, null);
            TextView tv = (TextView) convertView.findViewById(R.id.name);
            TextView select = (TextView) convertView.findViewById(R.id.select);
            view = new ViewHolderParent(tv,select);
            convertView.setTag(view);
        } else {
            view = (ViewHolderParent) convertView.getTag();
        }
        Groups contact = listParent.get(groupPosition);
        view.name.setText(contact.getGroup_name());
        view.select.setVisibility(View.GONE);
        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.sub_fmycontact_child1, null);
            TextView name = (TextView) convertView.findViewById(R.id.name);
            TextView sign = (TextView) convertView.findViewById(R.id.sign);
            SimpleDraweeView image = (SimpleDraweeView) convertView.findViewById(R.id.image);
            viewHolder = new ViewHolder(name,sign,image);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        MyContact contact = listChild.get(groupPosition).get(childPosition);
        viewHolder.name.setText(contact.getName());
        viewHolder.sign.setText(contact.getSign());
        String url = Config.URL+"faces/" + contact.getUid() + ".jpg";
        Uri uri = Uri.parse(url);
        viewHolder.image.setImageURI(uri);
        //Downloadimgs.initImageLoader(context).displayImage(url, viewHolder.image, Downloadimgs.getOption());
        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }
    private class ViewHolderParent {
        public TextView name;
        public TextView select;

        public ViewHolderParent(TextView itemTv,TextView select) {
            this.name = itemTv;
            this.select = select;
        }
    }
    private class ViewHolder {
        public TextView name;
        public TextView sign;
        public SimpleDraweeView image;

        public ViewHolder(TextView name, TextView sign, SimpleDraweeView image) {
            this.name = name;
            this.sign = sign;
            this.image = image;
        }
    }
}
