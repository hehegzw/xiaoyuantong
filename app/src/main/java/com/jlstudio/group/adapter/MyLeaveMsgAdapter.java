package com.jlstudio.group.adapter;

import android.content.Context;
import android.graphics.Color;
import android.text.SpannableString;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.jlstudio.R;
import com.jlstudio.group.bean.Contacts;
import com.jlstudio.group.bean.LeaveMsg;
import com.jlstudio.group.util.ExpressionUtil;
import com.jlstudio.group.util.ListViewUtil;
import com.jlstudio.main.application.Config;
import com.jlstudio.main.net.GetDataNet;
import com.jlstudio.publish.bean.PublishData;
import com.jlstudio.publish.util.ShowToast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.DataOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.SimpleFormatter;

/**
 * Created by NewOr on 2015/11/27.
 */
public class MyLeaveMsgAdapter extends BaseAdapter {
    private List<LeaveMsg> tempList;
    private LayoutInflater inflater;
    private Context context;
    private String username;
    public static final int TYPE_1 = 0;
    public static final int TYPE_2 = 1;
    public static final int TYPE_3 = 2;
    public static final int TYPE_4 = 3;

    public MyLeaveMsgAdapter(Context context, List<LeaveMsg> list,String username) {
        this.tempList = list;
        this.context = context;
        this.username = username;
        inflater = LayoutInflater.from(context);
    }

    public List<LeaveMsg> getTempList() {
        return tempList;
    }

    public void setTempList(List<LeaveMsg> tempList) {
        this.tempList = tempList;
        notifyDataSetChanged();
    }

    @Override
    public int getItemViewType(int position) {
        if (tempList.get(position).getType() == 1) {
            return TYPE_1;
        } else if (tempList.get(position).getType() == 2) {
            return TYPE_2;
        } else if (tempList.get(position).getType() == 3) {
            return TYPE_3;
        } else{
            return TYPE_4;
        }
    }

    @Override
    public int getViewTypeCount() {
        return 4;
    }

    @Override
    public int getCount() {
        return tempList.size();
    }

    @Override
    public Object getItem(int position) {
        return tempList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder1 holder1 = null;
        ViewHolder2 holder2 = null;
        ViewHolder3 holder3 = null;
        ViewHolder4 holder4 = null;
        int type = getItemViewType(position);
        if (convertView == null) {
            switch (type) {
                case TYPE_1:
                    convertView = inflater.inflate(R.layout.sub_list_item1, null);
                    holder1 = new ViewHolder1();
                    holder1.fromusername = (TextView) convertView.findViewById(R.id.fromusername);
                    holder1.tiem = (TextView) convertView.findViewById(R.id.msg_time);
                    holder1.text = (TextView) convertView.findViewById(R.id.msg_content);
                    convertView.setTag(holder1);
                    break;
                case TYPE_2:
                    convertView = inflater.inflate(R.layout.sub_list_item2, null);
                    holder2 = new ViewHolder2();
                    holder2.fromusername = (TextView) convertView.findViewById(R.id.fromname);
                    holder2.tousername = (TextView) convertView.findViewById(R.id.toname);
                    holder2.text = (TextView) convertView.findViewById(R.id.content);
                    holder2.textrep = (TextView) convertView.findViewById(R.id.text);
                    holder2.maohao = (TextView) convertView.findViewById(R.id.maohao);
                    convertView.setTag(holder2);
                    break;
                case TYPE_3:
                    convertView = inflater.inflate(R.layout.sub_list_item3, null);
                    holder3 = new ViewHolder3();
                    holder3.text = (TextView) convertView.findViewById(R.id.reply);
                    holder3.reply = (TextView) convertView.findViewById(R.id.submit);
                    convertView.setTag(holder3);
                    break;
                case TYPE_4:
                    convertView = inflater.inflate(R.layout.sub_list_item4, null);
                    holder4 = new ViewHolder4();
                    holder4.clicktext = (TextView) convertView.findViewById(R.id.clicktext);
                    convertView.setTag(holder4);
                    break;
                default:
                    break;
            }
        } else {
            switch (type) {
                case TYPE_1:
                    holder1 = (ViewHolder1) convertView.getTag();
                    break;
                case TYPE_2:
                    holder2 = (ViewHolder2) convertView.getTag();
                    break;
                case TYPE_3:
                    holder3 = (ViewHolder3) convertView.getTag();
                    break;
                case TYPE_4:
                    holder4 = (ViewHolder4) convertView.getTag();
                    break;
                default:
                    break;
            }
        }
        switch (type) {
            case TYPE_1:
                holder1.fromusername.setText(tempList.get(position).getFromname());
                holder1.text.setText(tempList.get(position).getContent());
                Date date = new Date(Long.valueOf(tempList.get(position).getTime()));
                SimpleDateFormat sdf = new SimpleDateFormat("yy/MM/dd HH:mm");
                String string = sdf.format(date);
                holder1.tiem.setText(string);
                break;
            case TYPE_2:
                holder2.fromusername.setText(tempList.get(position).getFromname());
                holder2.tousername.setText(tempList.get(position).getToname());
                //holder2.text.setText(tempList.get(position).getContent());
                String str = tempList.get(position).getContent();														//消息具体内容
                String zhengze = "f0[0-9]{2}|f10[0-7]";
                try {
                    SpannableString spannableString = ExpressionUtil.getExpressionString(context, str, zhengze);
                    holder2.text.setText(spannableString);
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                } catch (SecurityException e) {
                    e.printStackTrace();
                } catch (IllegalArgumentException e) {
                    e.printStackTrace();
                }
                break;
            case TYPE_3:
                break;
            case TYPE_4:
                holder4.clicktext.setText(tempList.get(position).getContent());
                break;
            default:
                break;
        }
        return convertView;
    }

    class ViewHolder1 {
        public TextView fromusername;
        public TextView tiem;
        public TextView text;
    }

    class ViewHolder2 {
        public TextView fromusername;
        public TextView tousername;
        public TextView text;
        public TextView textrep;
        public TextView maohao;
    }

    class ViewHolder3 {
        public TextView text;
        public TextView reply;
    }
    class ViewHolder4 {
        public TextView clicktext;
    }
}
