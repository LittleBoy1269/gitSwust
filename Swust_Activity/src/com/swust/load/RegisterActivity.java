package com.swust.load;

import org.json.JSONException;
import org.json.JSONObject;


import com.swust.activity.R;
import com.swust.http.SendMessage;
import com.swust.util.Code;
import com.swust.util.ConstantField;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class RegisterActivity extends Activity {

	private Button register_button;
	private  EditText userEdit;
	private  EditText passwordEdit;
	private String username;
	private String password;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.register);
		register_button=(Button) findViewById(R.id.register_button);
		userEdit=(EditText)findViewById(R.id.register_username_edit);
		passwordEdit=(EditText)findViewById(R.id.register_password_edit);
        register_button.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View view) {
				username=userEdit.getText().toString();
				password=passwordEdit.getText().toString();
				
				
				new Thread(new Runnable() {

					@Override
					public void run() {
						//注册
						if(register(username,password)){
							Intent intent = new Intent();
			        		intent.putExtra("username",username );
			        		intent.putExtra("password",password );
			        		intent.setClass(RegisterActivity.this,com.swust.load.LoadActivity.class);
			    			startActivity(intent);
						}
						
					}
				}).start();
			}
		});
	}
	
	Handler mHandler = new Handler() {
		public void handleMessage(Message msg) {
			Toast.makeText(RegisterActivity.this, "注册成功", Toast.LENGTH_SHORT).show();
			super.handleMessage(msg);
		}
		

	};
	
	Handler mHandler2 = new Handler() {
		public void handleMessage(Message msg) {
			Toast.makeText(RegisterActivity.this, "用户名或者密码错误", Toast.LENGTH_SHORT).show();
			super.handleMessage(msg);
		}
		

	};
	
	Handler mHandler3 = new Handler() {
		public void handleMessage(Message msg) {
			Toast.makeText(RegisterActivity.this, "用户已存在", Toast.LENGTH_SHORT).show();
			super.handleMessage(msg);
		}
		

	};
	
	Handler mHandler4 = new Handler() {
		public void handleMessage(Message msg) {
			Toast.makeText(RegisterActivity.this, "注册失败", Toast.LENGTH_SHORT).show();
			super.handleMessage(msg);
		}
		

	};
	
	private boolean register(String username,String password){
		JSONObject req=new JSONObject();
		JSONObject res;
		try {
			req.put(ConstantField.NUMBER, username);
			req.put(ConstantField.PASSWORD, password);
			res=SendMessage.sendMessage("RegisterAction.action", req);
			if(res!=null){
				int code=res.getInt(ConstantField.STATUS);
				if(code==Code.SUCCESS){
					mHandler.sendEmptyMessage(0);
				return true;
				}else if(code==Code.EDU_FAIL){
					mHandler2.sendEmptyMessage(0);
					return false;
				}else { 
					mHandler3.sendEmptyMessage(0);
					Intent intent = new Intent();
	        		intent.putExtra("username",username );
	        		intent.putExtra("password",password );
	        		intent.setClass(RegisterActivity.this,com.swust.load.LoadActivity.class);
	    			startActivity(intent);
					return false;
					}
				
			}else{
				mHandler4.sendEmptyMessage(0);
				return false;
				}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			mHandler4.sendEmptyMessage(0);
			return false;
		}
	}
}