package com.jlstudio.group.adapter;

import android.content.Context;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.jlstudio.R;
import com.jlstudio.group.activity.FriendCircleActivity;
import com.jlstudio.group.bean.FriendCircleData;

import java.util.List;

/**
 * Created by NewOrin on 2015/10/23.
 */
public class FriendCircleAdapter extends BaseAdapter {
    private Context context;
    private List<FriendCircleData> friendCircleDataList;
    private int count_favour = 0;

    public FriendCircleAdapter(Context context, List<FriendCircleData> friendCircleDataList) {
        this.context = context;
        this.friendCircleDataList = friendCircleDataList;
    }

    @Override
    public int getCount() {
        return friendCircleDataList.size();
    }

    @Override
    public Object getItem(int position) {
        return friendCircleDataList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = new ViewHolder();
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.friendcirlce_item, null);
            holder.tv_name = (TextView) convertView.findViewById(R.id.tv_name);
            holder.tv_time = (TextView) convertView.findViewById(R.id.tv_time);
            holder.tv_comment = (TextView) convertView.findViewById(R.id.tv_content);
            holder.tv_favour = (TextView) convertView.findViewById(R.id.text_favour);
            holder.imb_favour = (ImageButton) convertView.findViewById(R.id.imagebutton_favour);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.tv_name.setText(friendCircleDataList.get(position).getName());
        holder.tv_time.setText(friendCircleDataList.get(position).getTime());
        holder.tv_comment.setText(friendCircleDataList.get(position).getComment());

        holder.imb_favour.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("Test","被点击了...");
                FriendCircleActivity f = new FriendCircleActivity();
                f.showToast("点击了");
            }
        });
//        initEvent(holder);
        return convertView;
    }

    public void initEvent(final ViewHolder holder) {
        count_favour++;
        holder.imb_favour.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FriendCircleActivity f = new FriendCircleActivity();
                Toast.makeText(context,"点个赞",Toast.LENGTH_LONG).show();
                count_favour++;
                final Handler handler = new Handler();
                new Thread() {
                    public void run() {
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                holder.tv_favour.setText(count_favour + "");
                            }
                        });
                    }
                }.start();
            }
        });
    }

    class ViewHolder {
        TextView tv_name;
        TextView tv_comment;
        TextView tv_favour;
        TextView tv_time;
        ImageButton imb_comment;
        ImageButton imb_favour;
    }
}
