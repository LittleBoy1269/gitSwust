package com.swust.notepad;


import java.io.Console;

import android.app.Activity;


import com.swust.activity.MainActivity;
import com.swust.activity.R;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.text.Editable;
import android.text.Selection;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

public class NotepadEditActivity extends FragmentActivity {
	
	public static final int CHECK_STATE = 0;
	public static final int EDIT_STATE = 1;
	public static final int ALERT_STATE = 2;
	
	private int state = -1;
	
	private ImageView back;
	private Button complete;//完成
	private EditText title;
	private EditText content;
	private DatabaseManage dm = null;
	
	private String id = "";
	private String titleText = "";
	private String contentText = "";
	private String timeText = "";
	
	
	protected void onCreate(Bundle savedInstanceState){
		
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.edit_notepad);
		
		
		
		
		
		Intent intent = getIntent();
		state = intent.getIntExtra("state", EDIT_STATE);
		
		//赋值控件对象
		back=(ImageView) findViewById(R.id.back_logo);
		complete = (Button)findViewById(R.id.editComplete);
		title = (EditText)findViewById(R.id.editTitle);
		content = (EditText)findViewById(R.id.editContent);
		
		//设置监听
		
		if(state == ALERT_STATE){//修改状态,赋值控件
			id = intent.getStringExtra("_id");
			titleText = intent.getStringExtra("title");
			contentText = intent.getStringExtra("content");
			timeText = intent.getStringExtra("time");
			
			title.setText(titleText);
			content.setText(contentText);
		}
		
		dm = new DatabaseManage(this);
		back.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				finish();
				
			}
		});
		complete.setOnClickListener(new EditCompleteListener());
		content.setOnTouchListener(new OnTouchListener(){

			public boolean onTouch(View v, MotionEvent event) {
				content.setSelection(content.getText().toString().length());
				return false;
			}
			
		});
		
		
	}
	
	
	
	
	/**
	 * 监听完成按钮
	 */
	public class EditCompleteListener implements OnClickListener{

		public void onClick(View v) {
			
			titleText = title.getText().toString();
			contentText = content.getText().toString();
			try{
				dm.open();
				if(state == EDIT_STATE)//新增状态
					dm.insert(titleText, contentText);
				if(state == ALERT_STATE)//修改状态
					dm.update(Integer.parseInt(id), titleText, contentText);	
				dm.close();
				
			}catch(Exception ex){
				ex.printStackTrace();
			}
			Intent intent=new Intent();
			intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			intent.setClass(NotepadEditActivity.this,com.swust.activity.MainActivity.class);
			startActivity(intent);
			
			
			//保存完毕
		}
		
	}
	
	
}
