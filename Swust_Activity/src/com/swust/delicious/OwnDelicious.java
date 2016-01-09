package com.swust.delicious;

import org.json.JSONException;
import org.json.JSONObject;

import com.swust.activity.R;
import com.swust.http.ImageLoader;
import com.swust.http.SendMessage;
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
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class OwnDelicious extends Activity {

	private ImageView like_img;
	private ImageView dislike_img;
	private ImageView title_img;
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
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.own_delicious_detail);
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
		
		
		title_img=(ImageView) findViewById(R.id.delicious_detail_img);
		title_text=(TextView) findViewById(R.id.de_title_text);
		keyword_text=(TextView) findViewById(R.id.de_keyword_text);
		average_text=(TextView) findViewById(R.id.de_average_text);
		place_text=(TextView) findViewById(R.id.de_place_text);
		detail_text=(TextView) findViewById(R.id.de_detail_text);
		
		
		
		
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
		
		
		
	}
}
