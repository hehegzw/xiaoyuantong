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
import com.jlstudio.group.dialog.EditGroup;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Joe on 2015/11/24.
 */
public class GroupManagerAdapter extends BaseAdapter{
    private List<String> p_list = new ArrayList<>();
    private Context context = null;
    private LayoutInflater inflater;
    private EditGroup.CallBack callBack;
    public GroupManagerAdapter(Context context, List<String> p_list) {
        this.p_list = p_list;
        this.inflater = inflater.from(context);
        this.context = context;
    }

    public void setCallBack(EditGroup.CallBack callBack) {
        this.callBack = callBack;
    }

    public void setP_list(List<String> p_list) {
        this.p_list = p_list;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return p_list.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        //获取当前item对应的对象
        Log.d("Adapter","进入adapter");
        ViewHolder item;
        if(convertView == null){
            convertView = inflater.inflate(R.layout.sub_group_manager, null);
            TextView name = (TextView) convertView.findViewById(R.id.realname);
            ImageView image = (ImageView) convertView.findViewById(R.id.image);
            item = new ViewHolder(name,image);
            convertView.setTag(item);
        }else{
            item = (ViewHolder) convertView.getTag();
        }
        item.name.setText(p_list.get(position));
        item.image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(callBack!=null){
                    callBack.callback(position,p_list.get(position));
                }
            }
        });
        return convertView;
    }
    private class ViewHolder{
        public TextView name;
        public ImageView image;

        public ViewHolder(TextView name,ImageView image) {
            this.name = name;
            this.image = image;
        }
    }
    public interface CallBack{
        void callback(int index,String name);
    }
}
