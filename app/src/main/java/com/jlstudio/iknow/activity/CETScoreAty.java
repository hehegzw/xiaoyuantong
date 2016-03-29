package com.jlstudio.iknow.activity;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.jlstudio.R;
import com.jlstudio.main.activity.BaseActivity;
import com.jlstudio.main.activity.MainActivity;
import com.jlstudio.main.application.ActivityContror;
import com.jlstudio.publish.net.GetNotificationNet;

import org.json.JSONException;
import org.json.JSONObject;

public class CETScoreAty extends BaseActivity implements View.OnClickListener {
	private TextView back,title_name;
	private TextView name,school,test_type,id,time,totle_score,listen,read,write;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_cetscore);
		initView();
		initData();
	}

	private void initView() {
		back = (TextView) findViewById(R.id.back);
		Typeface iconfont = Typeface.createFromAsset(getAssets(),"fonts/iconfont.ttf");
		back.setTypeface(iconfont);
		back.setOnClickListener(this);
		title_name = (TextView) findViewById(R.id.title_name);
		title_name.setText("CET成绩");
		name = (TextView) findViewById(R.id.name);
		school = (TextView) findViewById(R.id.school);
		test_type = (TextView) findViewById(R.id.test_type);
		id = (TextView) findViewById(R.id.id);
		time = (TextView) findViewById(R.id.time);
		totle_score = (TextView) findViewById(R.id.totle_score);
		listen = (TextView) findViewById(R.id.listen);
		read = (TextView) findViewById(R.id.read);
		write = (TextView) findViewById(R.id.write);
	}

	private void initData() {
		String data = getIntent().getStringExtra("data");
		try {
			JSONObject json = new JSONObject(data);
			name.setText(json.getString("姓名"));
			school.setText(json.getString("学校"));
			test_type.setText(json.getString("考试类别"));
			id.setText(json.getString("准考证号"));
			time.setText(json.getString("考试时间"));
			totle_score.setText(json.getString("总分"));
			listen.setText(json.getString("听力"));
			read.setText(json.getString("阅读"));
			write.setText(json.getString("写作与翻译"));
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()){
			case R.id.back:
				startActivity(new Intent(CETScoreAty.this, MainActivity.class));
				finish();
				break;
		}
	}
}
