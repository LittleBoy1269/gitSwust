package com.swust.delicious;

import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import com.swust.activity.R;
import com.swust.http.ImageLoader;
import com.swust.http.SendMessage;
import com.swust.load.LoadActivity;
import com.swust.util.Code;
import com.swust.util.ConstantField;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class DeliciousActivity extends Activity implements OnClickListener{
	private RelativeLayout like_button;
	private RelativeLayout dislike_button;
    private LinearLayout judge_button;
    
	private ImageView like_img;
	private ImageView dislike_img;
	private ImageView title_img;
	
	private TextView like_text;
	private TextView dislike_text;
	private TextView title_text;
	private TextView keyword_text;
	private TextView average_text;
	private TextView place_text;
	private TextView detail_text;
	public String imgUrl;
	public String title;
	public String keyword;
	public String average;
	public String place;
	public String detail;
	public Integer userid;
	public int foodid=0;
	public final int NOJUDGE=0;
	public final int JUDGE=1;
	public final int NOLOGIN=2;
	public int onClick=0;//0表示未点赞  1表示已点
	private Handler handler = new Handler()
    {
    	@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			switch (msg.what) {
			case NOJUDGE:
				like_button.setOnClickListener(new OnClickListener() {
	
					@Override
					public void onClick(View arg0) {
						if(onClick==0)
						{
							onClick=1;
							like_img.setImageResource(R.drawable.like_pressed);
							like_text.setTextColor(getResources().getColor(R.color.mainBack));
								new Thread(new Runnable() {

									@Override
									public void run() {
										sendMind(0);
									}
								}).start();	
						}
					}
				});
				dislike_button.setOnClickListener(new OnClickListener() {
	
					@Override
					public void onClick(View arg0) {
						if(onClick==0){
							onClick=1;
							dislike_img.setImageResource(R.drawable.dislike_pressed);
							dislike_text.setTextColor(getResources().getColor(R.color.mainBack));
								new Thread(new Runnable() {

									@Override
									public void run() {
										sendMind(1);
									}
								}).start();	
						}
						
					}
				});
				break;
				
			case JUDGE:
                 judge_button.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View arg0) {
						Toast.makeText(DeliciousActivity.this, "你已经评价过该美食", Toast.LENGTH_SHORT).show();
						
					}
				});
				break;
				   
		    case NOLOGIN:
                 judge_button.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View arg0) {
						
							Toast.makeText(DeliciousActivity.this, "请登录", Toast.LENGTH_SHORT).show();
							Intent intent = new Intent(DeliciousActivity.this,com.swust.load.LoadActivity.class);
							startActivity(intent);
					
						
					}
				});
				break;
			}
		}
    };
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.school_delicious_detail);
		Intent intent = getIntent();
		
		imgUrl=intent.getStringExtra("imgUrl");
		title=intent.getStringExtra("title");
		keyword=intent.getStringExtra("keyword");
		average=intent.getStringExtra("average");
		place=intent.getStringExtra("place");
		detail=intent.getStringExtra("detail");	
		foodid=intent.getIntExtra("foodid", foodid);
		userid=com.swust.load.LoadActivity.getUserid();
		//注FOOD_ID未获取
		
		
		like_text=(TextView) findViewById(R.id.like_text);
		dislike_text=(TextView) findViewById(R.id.dislike_text);
		title_img=(ImageView) findViewById(R.id.delicious_detail_img);
		title_text=(TextView) findViewById(R.id.de_title_text);
		keyword_text=(TextView) findViewById(R.id.de_keyword_text);
		average_text=(TextView) findViewById(R.id.de_average_text);
		place_text=(TextView) findViewById(R.id.de_place_text);
		detail_text=(TextView) findViewById(R.id.de_detail_text);
		
		like_img=(ImageView) findViewById(R.id.like_img);
		dislike_img=(ImageView) findViewById(R.id.dislike_img);
		
		judge_button=(LinearLayout) findViewById(R.id.judge_button);
		like_button=(RelativeLayout) findViewById(R.id.button_like);
		dislike_button=(RelativeLayout) findViewById(R.id.button_dislike);
		if(imgUrl!=null)
		{
			title_img.setTag(imgUrl);
			new ImageLoader().showImageByThread_(title_img, imgUrl);
		}
		this.title_text.setText(title);
		this.keyword_text.setText(keyword);
		this.average_text.setText(average);
		this.place_text.setText(place);
		this.detail_text.setText(detail);
		
		
		initEvents();
	}

//	private int ifComment(Integer foodid,Integer userid){
//		JSONObject req=new JSONObject();
//		JSONObject res;
//		try {
//			req.put(ConstantField.FOOD_ID, foodid);
//			req.put(ConstantField.USERID, userid);
//			res=SendMessage.sendMessage("ifcommentAction.action", req);
//			if(res!=null){
//				int code= res.getInt(ConstantField.IF_COMMENT);
//				return code;
//			}
//		} catch (JSONException e) {
//			e.printStackTrace();
//		}
//		return 0;
//	}
	
	public void initEvents()
	{
		new Thread(new Runnable() {

			@Override
			public void run() {
				JSONObject req=new JSONObject();
				JSONObject res;
				Message msg=new Message();//0表示未评论, 1表示已评论;2表示未登录
				try {
					if(userid==null)
					    msg.what=2;
					else 
					{
						req.put(ConstantField.FOOD_ID, foodid);
						req.put(ConstantField.USERID, userid);
						res=SendMessage.sendMessage("ifcommentAction.action", req);
						if(res!=null){
							int code= res.getInt(ConstantField.IF_COMMENT);
							msg.what=code;
						}
					}
					handler.sendMessage(msg);
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
		}).start();
		
		
	}
	Handler mHandler = new Handler() {
		public void handleMessage(Message msg) {
			Toast.makeText(DeliciousActivity.this, "点赞成功", Toast.LENGTH_SHORT).show();
			super.handleMessage(msg);
		}

	};
	Handler mHandler_ = new Handler() {
		public void handleMessage(Message msg) {
			Toast.makeText(DeliciousActivity.this, "坑爹成功", Toast.LENGTH_SHORT).show();
			super.handleMessage(msg);
		}

	};
	private void sendMind(Integer comment)
	{
		JSONObject req = new JSONObject();
		JSONObject res;
		try {
			req.put(ConstantField.USERID, userid);
			req.put(ConstantField.FOOD_ID, foodid);
			req.put(ConstantField.COMMENT,comment);
			res = SendMessage.sendMessage("commentFoodAction.action", req);
		} catch (JSONException e) {
			e.printStackTrace();
			
		}
	}

	@Override
	public void onClick(View v) {
	}	
	
}
