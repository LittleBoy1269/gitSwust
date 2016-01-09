package com.swust.load;

import org.json.JSONException;
import org.json.JSONObject;


import com.swust.activity.R;
import com.swust.http.SendMessage;
import com.swust.util.Code;
import com.swust.util.ConstantField;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class LoadActivity extends Activity implements OnClickListener{

	private EditText userEdit;
	private EditText passwordEdit;
	private String username;
	private String password;
	
	private Button login_button;
	private TextView load_register;
	public static Integer userid=null;
	private static String Username;
	//public static Integer userid=1;
	 public static SharedPreferences UserInfo;//保存用户信息
	public static Editor editor;
	public static Integer getUserid() {
		return userid;
	}

	public static void setUserid(Integer userid) {
		LoadActivity.userid = userid;
	}
	public static String getUsername() {
		return Username;
	}

	public static void setUsername(String username) {
		LoadActivity.Username = username;
	}
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.load);
		Intent intent=getIntent();
		username=intent.getStringExtra("username");
		password=intent.getStringExtra("password");
		initView();
		initEvents();
	}
	public void initView()
	{
		UserInfo=getSharedPreferences("UserInfo", MODE_PRIVATE);//用SharedPreferences记录用户登录的用户名
		editor=UserInfo.edit();
		String username=UserInfo.getString("username", null);
		String password=UserInfo.getString("password", null);
		userEdit=(EditText) findViewById(R.id.username_edit);
		passwordEdit=(EditText) findViewById(R.id.password_edit);
		userEdit.setText(username);
		passwordEdit.setText(password);
		login_button=(Button)findViewById(R.id.login_button);
		load_register=(TextView)findViewById(R.id.load_register);
		//String name=UserInfo.getString("username", "");
	}
	public void initEvents()
	{
		
		login_button.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View view) {
				// TODO Auto-generated method stub
				username = userEdit.getText().toString();
				password = passwordEdit.getText().toString();

				new Thread(new Runnable() {

					@Override
					public void run() {
						// TODO Auto-generated method stub
						// 登陆
						if (login(username, password)) {
							editor.putString("username", username);
							editor.putString("password", password);
							editor.commit();
							Log.d("username", username);
							Log.d("password", password);
							Intent intent=new Intent();
							intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
							intent.setClass(LoadActivity.this, com.swust.activity.MainActivity.class);
							startActivity(intent);
						} 
					}
				}).start();
			}
		});
		
//		login_button.setOnClickListener(new OnClickListener() {
//			
//			@Override
//			public void onClick(View v) {
//				String name=userEdit.getText().toString().trim();
//				String password=passwordEdit.getText().toString().trim();
//				if("小朋友".equals(name)&&"123456".equals(password)){
//					editor.putString("username", name);
//				    editor.commit();
//				    Toast.makeText(LoadActivity.this, "登录成功", Toast.LENGTH_LONG).show();
//				    Intent intent=new Intent();
//				    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//				    intent.putExtra("isLoad", 1);
//				    intent.setClass(LoadActivity.this, com.swust.activity.MainActivity.class);
//				    startActivity(intent);
//				}
//				else {
//					editor.remove("username");
//					editor.commit();
//					Toast.makeText(LoadActivity.this, "用户名或密码错误", Toast.LENGTH_LONG).show();
//				}
//			}
//		});
        load_register.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent=new Intent(LoadActivity.this,com.swust.load.RegisterActivity.class);
				startActivity(intent);
				
			}
		});
	}
	
	
	Handler mHandler = new Handler() {
		public void handleMessage(Message msg) {
			Toast.makeText(LoadActivity.this, "登陆成功", Toast.LENGTH_SHORT).show();
			super.handleMessage(msg);
		}

	};

	Handler mHandler_ = new Handler() {
		public void handleMessage(Message msg) {
			Toast.makeText(LoadActivity.this, "用户不存在，请注册", Toast.LENGTH_SHORT)
					.show();
			super.handleMessage(msg);
		}

	};
	
	Handler mHandler_2 = new Handler() {
		public void handleMessage(Message msg) {
			Toast.makeText(LoadActivity.this, "密码错误", Toast.LENGTH_SHORT)
					.show();
			super.handleMessage(msg);
		}

	};
	
	private boolean login(String user, String password) {
		JSONObject req = new JSONObject();
		JSONObject res;
		try {
			req.put(ConstantField.NUMBER, user);
			req.put(ConstantField.PASSWORD, password);
			res = SendMessage.sendMessage("LoginAction.action", req);

			if (res != null) {
				int code = res.getInt(ConstantField.STATUS);
				if (code == Code.SUCCESS) {
					mHandler.sendEmptyMessage(0);
					this.setUserid(res.getInt(ConstantField.USERID));
					this.setUsername(user);
					return true;
				} else if (code == Code.NO_USER) {
					mHandler_.sendEmptyMessage(0);
					Intent intent = new Intent(LoadActivity.this,
							RegisterActivity.class);
					startActivity(intent);
					return false;
				} else {
					mHandler_2.sendEmptyMessage(0);
					return false;
				}

			} else
				return false;
		} catch (JSONException e) {
			e.printStackTrace();
			return false;
		}

	}
	@Override
	public void onClick(View arg0) {
		
	}	
}
