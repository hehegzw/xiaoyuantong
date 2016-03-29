package com.jlstudio.iknow.activity;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.jlstudio.R;
import com.jlstudio.iknow.bean.Schedule;
import com.jlstudio.iknow.net.GetDataNet;
import com.jlstudio.iknow.util.JsonUtil;
import com.jlstudio.main.activity.BaseActivity;
import com.jlstudio.main.activity.MainActivity;
import com.jlstudio.main.application.ActivityContror;
import com.jlstudio.main.application.Config;
import com.jlstudio.main.bean.CatchData;
import com.jlstudio.main.db.DBOption;
import com.jlstudio.publish.util.ShowToast;
import com.jlstudio.publish.util.StringUtil;

import org.json.JSONException;
import org.json.JSONObject;


public class ScheduleAty extends BaseActivity implements View.OnClickListener {
	List<LinearLayout> layout;
	List<Schedule> tempschedule,list;
	Random rm;
	private TextView back,title;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.aty_schedule);
		initView();
		initLayout();
		initData();
	}

	private void initView() {
		back = (TextView) findViewById(R.id.back);
		title = (TextView) findViewById(R.id.title_name);
		title.setText("个人课表");
		Typeface iconfont = Typeface.createFromAsset(getAssets(),"fonts/iconfont.ttf");
		back.setTypeface(iconfont);
		back.setOnClickListener(this);
	}

	private void initData() {
		String data = getIntent().getStringExtra("data");
		tempschedule = new ArrayList<>();
		list = new ArrayList<>();
		list = JsonUtil.getSchedules(data);
		initView(list);
	}

	private void initLayout() {
		layout = new ArrayList<>();
		layout.add((LinearLayout) findViewById(R.id.mon));
		layout.add((LinearLayout) findViewById(R.id.tues));
		layout.add((LinearLayout) findViewById(R.id.wed));
		layout.add((LinearLayout) findViewById(R.id.thur));
		layout.add((LinearLayout) findViewById(R.id.fri));
		layout.add((LinearLayout) findViewById(R.id.sat));
		layout.add((LinearLayout) findViewById(R.id.sun));
	}

	private void initView(List<Schedule> schedules) {
		rm = new Random();
		for (int i = 0; i < schedules.size(); i++) {
			Schedule ss = schedules.get(i);
			if (i < 0) {
				setCourse1(i, ss.getOne(), ss.getThree());
				setCourse2(i, ss.getOne(), ss.getThree());
				setCourse1(i, ss.getFive(), ss.getSeven());
				setCourse2(i, ss.getFive(), ss.getSeven());
				setCourse3(i, ss.getNine());
			} else {
				setCourse3(i, ss.getOne());
				setCourse3(i, ss.getThree());
				setCourse3(i, ss.getFive());
				setCourse3(i, ss.getSeven());
				setCourse3(i, ss.getNine());
			}
		}
	}
//以下内容除了setCourse3 暂时没用，逻辑有点问题
	private void setCourse1(int i, String one, String two) {
		if (one.equals("")) {
			TextView tv = new TextView(this);
			LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
					LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT, 1);
			lp.setMargins(0, 0, 3, 3);
			tv.setBackgroundColor(Color.TRANSPARENT);
			tv.setLayoutParams(lp);
			layout.get(i).addView(tv);
		} else {
			if (two.equals("") || !two.equals(one)) {
				TextView tv = new TextView(this);
				LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
						LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT, 1);
				lp.setMargins(0, 0, 3, 3);
				tv.setLayoutParams(lp);
				tv.setText(one);
				int witch_bk = rm.nextInt(5);
				switch (witch_bk) {
				case 0:
					tv.setBackgroundResource(R.drawable.schedule_shape_1);
					break;
				case 1:
					tv.setBackgroundResource(R.drawable.schedule_shape_2);
					break;
				case 2:
					tv.setBackgroundResource(R.drawable.schedule_shape_3);
					break;
				case 3:
					tv.setBackgroundResource(R.drawable.schedule_shape_4);
					break;
				case 4:
					tv.setBackgroundResource(R.drawable.schedule_shape_5);
					break;
				}
				layout.get(i).addView(tv);
			} else if (!two.equals("") && two.equals(one)) {
				TextView tv = new TextView(this);
				LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
						LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT, 2);
				lp.setMargins(0, 0, 3, 3);
				tv.setLayoutParams(lp);
				tv.setText(one);
				int witch_bk = rm.nextInt(5);
				switch (witch_bk) {
				case 0:
					tv.setBackgroundResource(R.drawable.schedule_shape_1);
					break;
				case 1:
					tv.setBackgroundResource(R.drawable.schedule_shape_2);
					break;
				case 2:
					tv.setBackgroundResource(R.drawable.schedule_shape_3);
					break;
				case 3:
					tv.setBackgroundResource(R.drawable.schedule_shape_4);
					break;
				case 4:
					tv.setBackgroundResource(R.drawable.schedule_shape_5);
					break;
				}
				layout.get(i).addView(tv);
			}
		}
	}

	private void setCourse2(int i, String one, String two) {
		if (one.equals(two)) {
			return;
		} else {
			if (two.equals("")) {
				TextView tv = new TextView(this);
				LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
						LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT, 1);
				lp.setMargins(0, 0, 3, 3);
				tv.setBackgroundColor(Color.TRANSPARENT);
				tv.setLayoutParams(lp);
				layout.get(i).addView(tv);
			} else {
				TextView tv = new TextView(this);
				LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
						LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT, 1);
				lp.setMargins(0, 0, 3, 3);
				tv.setLayoutParams(lp);
				tv.setText(two);
				int witch_bk = rm.nextInt(5);
				switch (witch_bk) {
				case 0:
					tv.setBackgroundResource(R.drawable.schedule_shape_1);
					break;
				case 1:
					tv.setBackgroundResource(R.drawable.schedule_shape_2);
					break;
				case 2:
					tv.setBackgroundResource(R.drawable.schedule_shape_3);
					break;
				case 3:
					tv.setBackgroundResource(R.drawable.schedule_shape_4);
					break;
				case 4:
					tv.setBackgroundResource(R.drawable.schedule_shape_5);
					break;
				}
				layout.get(i).addView(tv);
			}
		}
	}

	private void setCourse3(int i, String one) {
		if (one.equals("")) {
			TextView tv = new TextView(this);
			LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
					LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT, 1);
			lp.setMargins(0, 0, 3, 3);
			tv.setBackgroundColor(Color.TRANSPARENT);
			tv.setLayoutParams(lp);
			tv.setTextSize(12);
			layout.get(i).addView(tv);
		} else {
			TextView tv = new TextView(this);
			LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
					LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT, 1);
			lp.setMargins(0, 0, 3, 3);
			tv.setLayoutParams(lp);
			tv.setText(one);
			tv.setTextSize(12);
			int witch_bk = rm.nextInt(5);
			switch (witch_bk) {
			case 0:
				tv.setBackgroundResource(R.drawable.schedule_shape_1);
				break;
			case 1:
				tv.setBackgroundResource(R.drawable.schedule_shape_2);
				break;
			case 2:
				tv.setBackgroundResource(R.drawable.schedule_shape_3);
				break;
			case 3:
				tv.setBackgroundResource(R.drawable.schedule_shape_4);
				break;
			case 4:
				tv.setBackgroundResource(R.drawable.schedule_shape_5);
				break;
			}
			layout.get(i).addView(tv);
		}
	}
	@Override
	public void onClick(View v) {
		switch (v.getId()){
			case R.id.back:
				startActivity(new Intent(this,MainActivity.class));
				ActivityContror.removeActivity(this);
		}
	}
}
