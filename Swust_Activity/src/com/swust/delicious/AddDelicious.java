package com.swust.delicious;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;

import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore.Images.ImageColumns;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.swust.activity.R;
import com.swust.http.SendMessage;
import com.swust.http.Server;
import com.swust.util.Code;
import com.swust.util.ConstantField;

public class AddDelicious extends Activity implements OnClickListener{

	private ImageView return_img;
	private TextView add_text;
	private TextView top_title;
	private EditText nameEdit;
	/**
	 * 美食类型
	 */
	private TextView mXiaochi;
	private TextView mZhongcan;
	private TextView mMaocai;
	private TextView mTianpin;
	private TextView mQita;
	/**
	 * 美食价格
	 */
	private TextView mWu;
	private TextView mBa;
	private TextView mShiwu;
	private TextView mErshiwu;
	
	private TextView mLaoqu;
	private TextView mXinqu;
	private TextView mXiaonei;
	
	private EditText placeEdit;
	private EditText desEdit;
	public Integer userid;
	public String name;
	public String foodType;
	public String personPrice;
	public String place;
	public String placeDetail;
	public String des;
	public String url;
	
	private ImageView addPic;
	private String RealFilePath;
	private String path;
	// 图片上传日期及时间
	String mDatetime = null;
	String mLabel = "";
	
	private static String QI_NIU="http://7xp289.com1.z0.glb.clouddn.com/";
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
		mXiaochi=(TextView) findViewById(R.id.delicious_keyword_xiaochi);
		mZhongcan= (TextView) findViewById(R.id.delicious_keyword_zhongcan);
		mMaocai=(TextView) findViewById(R.id.delicious_keyword_maocai);
		mTianpin=(TextView) findViewById(R.id.delicious_keyword_tianpin);
		mQita=(TextView) findViewById(R.id.delicious_keyword_qita);
		mWu=(TextView) findViewById(R.id.delicious_average_wu);
		mBa=(TextView) findViewById(R.id.delicious_average_ba);
		mShiwu=(TextView) findViewById(R.id.delicious_average_shiwu);
		mErshiwu=(TextView) findViewById(R.id.delicious_average_ershiwu);	
	    nameEdit=(EditText) findViewById(R.id.add_de_title_edittext);
	    placeEdit=(EditText) findViewById(R.id.add_de_place_edittext);
	    desEdit=(EditText) findViewById(R.id.add_de_detail_edittext);
	    
	    mLaoqu=(TextView) findViewById(R.id.delicious_place_laoqu);
	    mXinqu=(TextView) findViewById(R.id.delicious_place_xinqu);
	    mXiaonei=(TextView) findViewById(R.id.delicious_place_xiaonei);
	    addPic=(ImageView) findViewById(R.id.addpicture);
	}
	
	public void resetKeyword()
	{
		resetTextView(mXiaochi);
		resetTextView(mMaocai);
		resetTextView(mZhongcan);
		resetTextView(mTianpin);
		resetTextView(mQita);
	}
	public void resetAverage()
	{
		resetTextView(mWu);
		resetTextView(mBa);
		resetTextView(mShiwu);
		resetTextView(mErshiwu);
	}
	public void resetPlace()
	{
		resetTextView(mLaoqu);
		resetTextView(mXinqu);
		resetTextView(mXiaonei);
	}
	public void setTextView(TextView tv)
	{
		tv.setTextColor(getResources().getColor(R.color.mainBack));
		tv.setBackground(getResources().getDrawable(R.drawable.place_border));
		tv.setTextColor(getResources().getColor(R.color.mainBack));
		//tv.setBackgroundResource(R.color.mainBack);
		
	}
	public void resetTextView(TextView tv)
	{
		tv.setTextColor(getResources().getColor(R.color.gray));
		tv.setBackground(getResources().getDrawable(R.drawable.place_border));
		tv.setTextColor(getResources().getColor(R.color.gray));
		//tv.setBackgroundResource(R.color.gray);
	}
	
	public void initEvents()
	{
		top_title.setText("校园美食");	
		mXiaochi.setOnClickListener(this);
		mZhongcan.setOnClickListener(this);
		mMaocai.setOnClickListener(this);
		mTianpin.setOnClickListener(this);
		mQita.setOnClickListener(this);
		
		mWu.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				resetAverage();
				setTextView(mWu);
				personPrice="5-8元";
			}
		});
		mBa.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				resetAverage();
				setTextView(mBa);
				personPrice="9-15元";
			}
		});
		mShiwu.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				resetAverage();
				setTextView(mShiwu);
				personPrice="16-25元";
			}
		});
		mErshiwu.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				resetAverage();
				setTextView(mErshiwu);
				personPrice="25元以上";
			}
		});
        mLaoqu.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				resetPlace();
				setTextView(mLaoqu);
				place="老区";
			}
		});
        mXinqu.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				resetPlace();
				setTextView(mXinqu);
				place="新区";
			}
		});
        mXiaonei.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				resetPlace();
				setTextView(mXiaonei);
				place="校内";
			}
		});
		add_text.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// 上传图片
				userid=com.swust.load.LoadActivity.getUserid();
                name=nameEdit.getText().toString().trim();
//              foodType=foodTypeEdit.getText().toString();
//              personPrice=personPriceEdit.getText().toString();
                placeDetail=place + " " + placeEdit.getText().toString().trim();
                des=desEdit.getText().toString().trim();
                
                if(name==null)
                {
                	mHandler_1.sendEmptyMessage(0);
                	
                }
                else if(foodType==null)
                {
                	mHandler_2.sendEmptyMessage(0);
                	
                }
                else if(personPrice==null)
                {
                	mHandler_3.sendEmptyMessage(0);
                	
                }
                
                else if(place==null)
                {
                	mHandler_4.sendEmptyMessage(0);
                }
                else if(url==null)
                {
                	mHandler_5.sendEmptyMessage(0);
                 }
                else {
                	new Thread(new Runnable() {
    					@Override
    					public void run() {
    						if(putResource())
    						{    							
    							Intent intent=new Intent(AddDelicious.this,
    									com.swust.activity.MainActivity.class);
    							intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
    							startActivity(intent);
    						}
    					}
    				}).start();
                }
				
			}
		});
		return_img.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				AddDelicious.this.finish();
			}
		});
        
		addPic.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intentablum = new Intent(Intent.ACTION_PICK);
				intentablum.setType("image/*");
				startActivityForResult(intentablum, 5);
				
			}
		});
	}
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == 5 && data != null) {
			// uploadANDshowImage(data);
			ClipImage(data);
		}

		if (200 == requestCode) {
			if (resultCode == RESULT_OK) {
				showAndSaveImage(data);
				UpLoadImage(path);
			}
		}

	}
	@SuppressLint("SimpleDateFormat")
	private void UpLoadImage(final String PATH) {
		new Thread(new Runnable() {
			@Override
			public void run() {
				Log.d("图片路径", path);
				SimpleDateFormat sDateFormat = new SimpleDateFormat(
						"yyyy_MM_dd_HH_mm_ss_sss");
				mDatetime = sDateFormat.format(new java.util.Date());
				url=QI_NIU+mDatetime+".png";
				Log.d("url", url);
				Server sr=new Server();
				File file = new File(PATH);
				if (sr.upLoadFile("swustdelicious", file, mDatetime
						+ ".png")) {
					Log.d("图片", "上传成功" + mDatetime);
				} else {
					Log.e("图片", "上传失败");
				}
				file.delete();
			}
		}).start();

	}
	
	private void ClipImage(Intent data) {
		RealFilePath = getRealFilePath(getApplicationContext(), data.getData());
		if (null == RealFilePath)
			return;

		Intent intent = new Intent();

		intent.setAction("com.android.camera.action.CROP");
		intent.setDataAndType(data.getData(), "image/*");// data.getData()是已经选择的图片Uri
		intent.putExtra("crop", "true");
		intent.putExtra("aspectX", 4);// 裁剪框比例
		intent.putExtra("aspectY", 4);
		intent.putExtra("outputX", 350);// 输出图片大小
		intent.putExtra("outputY", 350);
		intent.putExtra("return-data", true);

		startActivityForResult(intent, 200);
	}
	
	@SuppressLint("SimpleDateFormat")
	private void showAndSaveImage(Intent data) {
		// 拿到剪切数据
		Bitmap bmap = data.getParcelableExtra("data");
		// 显示剪切的图像
		ImageView imageview = (ImageView) this
				.findViewById(R.id.addpicture);
		imageview.setImageBitmap(bmap);

		// 图像保存到文件中
		int index = RealFilePath.length() - 1;
		while (index >= 0 && '/' != RealFilePath.charAt(index)) {
			index--;
		}
		String s = RealFilePath.substring(0, index + 1);
		Log.d("文件", s);
		FileOutputStream foutput = null;
		try {
			SimpleDateFormat sDateFormat = new SimpleDateFormat(
					"yyyy_MM_dd_HH_mm_ss_sss");
			String date = sDateFormat.format(new java.util.Date());

			path = s + date + ".jpg";
			foutput = new FileOutputStream(path);
			bmap.compress(Bitmap.CompressFormat.PNG, 90, foutput);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			Log.e("保存", "e.printStackTrace()");
		} finally {
			if (null != foutput) {
				try {
					foutput.flush();
					foutput.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	public static String getRealFilePath(final Context context, final Uri uri) {
		if (null == uri)
			return null;
		final String scheme = uri.getScheme();
		String data = null;
		if (scheme == null)
			data = uri.getPath();
		else if (ContentResolver.SCHEME_FILE.equals(scheme)) {
			data = uri.getPath();
		} else if (ContentResolver.SCHEME_CONTENT.equals(scheme)) {
			Cursor cursor = context.getContentResolver().query(uri,
					new String[] { ImageColumns.DATA }, null, null, null);
			if (null != cursor) {
				if (cursor.moveToFirst()) {
					int index = cursor.getColumnIndex(ImageColumns.DATA);
					if (index > -1) {
						data = cursor.getString(index);
					}
				}
				cursor.close();
			}
		}
		return data;
	}
	
	
	Handler mHandler = new Handler() {
		public void handleMessage(Message msg) {
			Toast.makeText(AddDelicious.this, "分享成功", Toast.LENGTH_SHORT).show();
			super.handleMessage(msg);
		}

	};
	Handler mHandler_ = new Handler() {
		public void handleMessage(Message msg) {
			Toast.makeText(AddDelicious.this, "分享失败", Toast.LENGTH_SHORT).show();
			super.handleMessage(msg);
		}

	};
	Handler mHandler_1 = new Handler() {
		public void handleMessage(Message msg) {
			Toast.makeText(AddDelicious.this, "请输入美食主题", Toast.LENGTH_SHORT).show();
			super.handleMessage(msg);
		}

	};
	Handler mHandler_2 = new Handler() {
		public void handleMessage(Message msg) {
			Toast.makeText(AddDelicious.this, "请输入美食类型", Toast.LENGTH_SHORT).show();
			super.handleMessage(msg);
		}

	};
	Handler mHandler_3 = new Handler() {
		public void handleMessage(Message msg) {
			Toast.makeText(AddDelicious.this, "请选择价格区间", Toast.LENGTH_SHORT).show();
			super.handleMessage(msg);
		}

	};
	Handler mHandler_4 = new Handler() {
		public void handleMessage(Message msg) {
			Toast.makeText(AddDelicious.this, "请选择美食地点", Toast.LENGTH_SHORT).show();
			super.handleMessage(msg);
		}

	};
	Handler mHandler_5 = new Handler() {
		public void handleMessage(Message msg) {
			Toast.makeText(AddDelicious.this, "请选择图片", Toast.LENGTH_SHORT).show();
			super.handleMessage(msg);
		}

	};
	private boolean putResource()
	{
		
		JSONObject req = new JSONObject();
		JSONObject res;
		try {
			req.put(ConstantField.USERID, userid);
			req.put(ConstantField.FOOD_NAME, name);
			req.put(ConstantField.FOOD_TYPE, foodType);
			req.put(ConstantField.PERSONPRICE, personPrice);
			req.put(ConstantField.PLACE, placeDetail);
			req.put(ConstantField.DEC, des);
			req.put(ConstantField.URL, url);
			res = SendMessage.sendMessage("submitFoodAction.action", req);

			if (res != null) {
				int code = res.getInt(ConstantField.STATUS);
				if (code == Code.SUCCESS) {
					mHandler.sendEmptyMessage(0);
					return true;
				}  else {
					mHandler_.sendEmptyMessage(0);
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
	public void onClick(View v) {
		resetKeyword();
		switch (v.getId()) {
		case R.id.delicious_keyword_xiaochi:
			setTextView(mXiaochi);
			foodType="小吃";
			break;

       case R.id.delicious_keyword_zhongcan:
    	   setTextView(mZhongcan);
    	   foodType="中餐";
			break;
       case R.id.delicious_keyword_maocai:
    	   setTextView(mMaocai);
    	   foodType="冒菜";
	        break;
        case R.id.delicious_keyword_tianpin:
        	setTextView(mTianpin);
        	foodType="甜品";
	        break;
        case R.id.delicious_keyword_qita:
        	setTextView(mQita);
        	foodType="其他";
	        break;
		default:
			break;
		}
		
	}
}
