package com.jlstudio.iknow.adapter;


import com.jlstudio.R;
import com.jlstudio.iknow.bean.ScoreItem;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

public class ScoreAdapter extends BaseAdapter {
	private List<ScoreItem> list;
	private Context context;
	
	public ScoreAdapter(List<ScoreItem> list, Context context) {
		super();
		this.list = list;
		this.context = context;
	}
	public void setList(List<ScoreItem> list) {
		this.list = list;
		this.notifyDataSetChanged();
	}
	
	@Override
	public int getCount() {
		return list.size();
	}

	@Override
	public Object getItem(int arg0) {
		return list.get(arg0);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		Item item;
		if(convertView == null){
			convertView = LayoutInflater.from(context).inflate(R.layout.lv_score_item, null);
			TextView name = (TextView) convertView.findViewById(R.id.name);
			TextView cridit = (TextView) convertView.findViewById(R.id.cridit);
			TextView point = (TextView) convertView.findViewById(R.id.point);
			TextView score = (TextView) convertView.findViewById(R.id.score);
			item = new Item(name, cridit, point, score);
			convertView.setTag(item);
		}else{
			item = (Item) convertView.getTag();
		}
		int alpha = Integer.parseInt(list.get(position).getScore());
		if(alpha < 60){
			convertView.setBackgroundColor(Color.rgb(48,126,254));
		}else{
			convertView.setBackgroundColor(Color.argb((100-alpha)*2+15,48,126,254));
		}

//		if(position%4 == 0){
//			convertView.setBackgroundColor(context.getResources().getColor(R.color.td_one));
//		}else if(position%4 == 1){
//			convertView.setBackgroundColor(context.getResources().getColor(R.color.td_two));
//		}else if(position%4 == 2){
//			convertView.setBackgroundColor(context.getResources().getColor(R.color.td_three));
//		}else if(position%4 == 3){
//			convertView.setBackgroundColor(context.getResources().getColor(R.color.td_four));
//		}
		
		item.name.setText(list.get(position).getName());
		item.cridit.setText(list.get(position).getCridit());
		item.point.setText(list.get(position).getPoint());
		item.score.setText(list.get(position).getScore());
		return convertView;
	}
	
	private class Item{
		public TextView name;
		public TextView cridit;
		public TextView point;
		public TextView score;
		public Item(TextView name, TextView cridit, TextView point,
				TextView score) {
			super();
			this.name = name;
			this.cridit = cridit;
			this.point = point;
			this.score = score;
		}
		
	}

}
