package com.swust.person;

import com.swust.activity.R;
import com.swust.delicious.DeliciousActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

public class personAdviceActivity extends Activity implements OnClickListener{

	public EditText adviceEdit;
	public String advice=null;
	public Button Complete;
	public ImageView Back;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.person_advice);
		adviceEdit=(EditText) findViewById(R.id.adviceContent);
		Complete=(Button) findViewById(R.id.adviceComplete);
		Back=(ImageView) findViewById(R.id.advice_back);
		Back.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				personAdviceActivity.this.finish();
			}
		});
		Complete.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				advice =adviceEdit.getText().toString().trim();
				if(advice.length()==0)
				{
					Toast.makeText(personAdviceActivity.this, "请输入您的建议", Toast.LENGTH_SHORT).show();
				}
				else{
					Toast.makeText(personAdviceActivity.this, "感谢你的反馈，我们会努力的", Toast.LENGTH_SHORT).show();
					Intent intent = new Intent(personAdviceActivity.this,com.swust.activity.MainActivity.class);
					intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
					startActivity(intent);
				}
				
			}
		});
	}
	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub
		
	}
}
