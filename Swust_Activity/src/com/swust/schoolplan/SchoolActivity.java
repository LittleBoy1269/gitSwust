package com.swust.schoolplan;

import com.swust.activity.R;
import com.swust.http.ImageLoader;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

public class SchoolActivity extends Activity {
	private ImageView title_img;
	private TextView title_text;
	private TextView person_text;
	private TextView time_text;
	private TextView place_text;
	private TextView detail_text;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.school_plan_detail);
		
		
		
		Intent intent = getIntent();
		String img_id=intent.getStringExtra("img_id");
		String title=intent.getStringExtra("title");
		String person=intent.getStringExtra("person");
		String time=intent.getStringExtra("time");
		String place=intent.getStringExtra("place");
		String detail=intent.getStringExtra("detail");
		String imgUrl=intent.getStringExtra("imgUrl");
		
		title_img=(ImageView) findViewById(R.id.school_activity_detail_img);
		title_text=(TextView) findViewById(R.id.detail_ac_title_text);
		person_text=(TextView) findViewById(R.id.detail_ac_person_text);
		time_text=(TextView) findViewById(R.id.detail_ac_time_text);
		place_text=(TextView) findViewById(R.id.detail_ac_place_text);
		detail_text=(TextView) findViewById(R.id.detail_text);
		
		title_img.setTag(imgUrl);
//		 new ImageLoader().showImageByThread(title_img, imgUrl);
		new ImageLoader().showImageByThread_(title_img, imgUrl);
		this.title_text.setText(title);
		this.person_text.setText(person);
		this.time_text.setText(time);
		this.place_text.setText(place);
		this.detail_text.setText(detail);
	}

}
