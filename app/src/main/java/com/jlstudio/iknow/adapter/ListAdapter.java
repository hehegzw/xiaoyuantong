package com.jlstudio.iknow.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import com.jlstudio.R;
import com.jlstudio.iknow.bean.ScoreItem;

import java.util.List;

/**
 * Created by gzw on 2016/4/14.
 */
public class ListAdapter extends BaseExpandableListAdapter {
    private List<ScoreItem> list;
    private LayoutInflater inflater;
    private Context context;

    public ListAdapter(Context context, List<ScoreItem> list) {
        this.list = list;
        this.inflater = LayoutInflater.from(context);
        this.context = context;
    }

    public void setList(List<ScoreItem> list) {
        this.list = list;
        notifyDataSetChanged();
    }

    @Override
    public int getGroupCount() {
        return list.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return 3;
    }

    @Override
    public Object getGroup(int groupPosition) {
        return list.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        if(childPosition ==0)return list.get(groupPosition).getCridit();
        else if(childPosition == 1)return list.get(groupPosition).getPoint();
        else return list.get(groupPosition).getScore();
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
        MyHolder holder;
        if(convertView == null){
            convertView = inflater.inflate(R.layout.sub_score_parent,parent,false);
            TextView tName = (TextView) convertView.findViewById(R.id.courseName);
            TextView tnumber = (TextView) convertView.findViewById(R.id.score);
            TextView arrow = (TextView) convertView.findViewById(R.id.arrow);
            holder = new MyHolder(tName,tnumber,arrow);
            convertView.setTag(holder);
        }else{
            holder = (MyHolder) convertView.getTag();
        }
        Typeface icon = Typeface.createFromAsset(context.getAssets(),"fonts/score.ttf");
        holder.arrow.setTypeface(icon);
        ScoreItem item = list.get(groupPosition);
        holder.tName.setText(item.getName());
        int score = Integer.valueOf(item.getScore());
        if(score<60){
            holder.tNumber.setTextColor(Color.RED);
        }else{
            holder.tNumber.setTextColor(Color.GREEN);
        }
        holder.tNumber.setText(item.getScore());
        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        MyHolder holder;
        if(convertView == null){
            convertView = inflater.inflate(R.layout.sub_score_child,parent,false);
            TextView tName = (TextView) convertView.findViewById(R.id.type);
            holder = new MyHolder(tName,null,null);
            convertView.setTag(holder);
        }else{
            holder = (MyHolder) convertView.getTag();
        }
        ScoreItem item = list.get(groupPosition);
        if(childPosition == 0){
            holder.tName.setText("学分:"+item.getCridit());
        }else if(childPosition == 1){
            holder.tName.setText("      绩点:"+item.getPoint());
        }else{
            holder.tName.setText("              成绩:"+item.getScore());
        }
        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return false;
    }
    private class MyHolder{
        public TextView tName;
        public TextView tNumber;
        public TextView arrow;

        public MyHolder(TextView tName, TextView tNumber,TextView arrow) {
            this.tName = tName;
            this.tNumber = tNumber;
            this.arrow = arrow;
        }
    }
}
