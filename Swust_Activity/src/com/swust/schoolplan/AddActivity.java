package com.swust.schoolplan;

import com.swust.activity.R;
import com.swust.delicious.AddDelicious;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

public class AddActivity extends Activity {

	private TextView add_text;
	private ImageView return_img;
	private TextView top_title;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.add_delicious);
		initView();
		initEvents();
	}
	public void initView()
	{
		return_img=(ImageView) findViewById(R.id.top_return_img);
		add_text=(TextView) findViewById(R.id.top_add_text);
		top_title=(TextView) findViewById(R.id.top_text);
	}
	public void initEvents()
	{
		top_title.setText("校园活动");
		
		return_img.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				AddActivity.this.finish();
			}
		});
        
	}
}
