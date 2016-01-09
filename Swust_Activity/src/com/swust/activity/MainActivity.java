package com.swust.activity;

import com.baidu.mobstat.SendStrategyEnum;
import com.baidu.mobstat.StatService;
import com.swust.delicious.AddDelicious;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.database.sqlite.SQLiteDatabase;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends FragmentActivity implements OnClickListener
{
	private LinearLayout mTabPlan;
	private LinearLayout mTabSchool;
	private LinearLayout mTabMyself;
	

	private TextView notepad_text;
	private TextView delicious_text;
	private TextView myself_text;
	
	private ImageButton mImgPlan;
	private ImageButton mImgSchool;
	private ImageButton mImgMyself;
	

	private Fragment mTab01;
	private Fragment mTab02;
	private Fragment mTab03;
	private Fragment mTab04;
	private SQLiteDatabase SQLiteDatabase = null;
	//private int isLoad=1;//0表示已经未登录，1表示已经登录
    private Integer Userid=null;
    
   
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_main);
		// 百度统计
	    BaiduStatistics();
	    initView();
		initEvent();
		
		setSelect(0);
//		SQLiteDatabase.execSQL("DROP TABLE reword");
//		SQLiteDatabase.execSQL("DROP TABLE SchoolDelicious");
	}
    
	
	private void initEvent()
	{
		mTabPlan.setOnClickListener(this);
		mTabSchool.setOnClickListener(this);
		mTabMyself.setOnClickListener(this);
		
	}

	private void initView()
	{
		mTabPlan = (LinearLayout) findViewById(R.id.id_tab_weixin);
		mTabSchool = (LinearLayout) findViewById(R.id.id_tab_frd);
		mTabMyself = (LinearLayout) findViewById(R.id.id_tab_address);
		

		mImgPlan = (ImageButton) findViewById(R.id.id_tab_weixin_img);
		mImgSchool = (ImageButton) findViewById(R.id.id_tab_frd_img);
		mImgMyself = (ImageButton) findViewById(R.id.id_tab_address_img);
		
		notepad_text=(TextView) findViewById(R.id.notepad_text);
		delicious_text=(TextView) findViewById(R.id.delicious_text);
		myself_text=(TextView) findViewById(R.id.myself_text);
		
		
		
	}

	private void setSelect(int i)
	{
		FragmentManager fm = getSupportFragmentManager();
		FragmentTransaction transaction = fm.beginTransaction();
		hideFragment(transaction);
		switch (i)
		{
		case 0:
			if (mTab01 == null)
			{
				mTab01 = new PlanFragment();
				transaction.add(R.id.id_content, mTab01);
			} else
			{
				transaction.show(mTab01);
			}
			mImgPlan.setImageResource(R.drawable.notepad_pressed);
			notepad_text.setTextColor(this.getResources().getColor(R.color.mainBack));
			break;
		case 1:
			if (mTab02 == null)
			{
				mTab02 = new SchoolFragment();
//				Bundle bundle=new Bundle();
//				bundle.putInt("isLoad", isLoad);
//				mTab02.setArguments(bundle);
				transaction.add(R.id.id_content, mTab02);
			} else
			{
				transaction.show(mTab02);
				
			}
			mImgSchool.setImageResource(R.drawable.delicious_pressed);
			delicious_text.setTextColor(this.getResources().getColor(R.color.mainBack));
			break;
		case 2://表示登录
			Userid=com.swust.load.LoadActivity.getUserid();
			//Userid=1;
			if(Userid==null)
			{
				if(mTab03==null)
				{
					mTab03=new MyselfFragment();
					transaction.add(R.id.id_content, mTab03);
				}
				else
				{
					transaction.show(mTab03);
				}
			}
			else
			{
				if(mTab04==null)
				{
					mTab04=new MyselfFragmentLoad();
					transaction.add(R.id.id_content, mTab04);
				}
				else
				{
					transaction.show(mTab04);
				}
			}
			mImgMyself.setImageResource(R.drawable.myself_pressed);
			myself_text.setTextColor(this.getResources().getColor(R.color.mainBack));
			break;

		default:
			break;
		}

		transaction.commit();
	}

	private void hideFragment(FragmentTransaction transaction)
	{
		if (mTab01 != null)
		{
			transaction.hide(mTab01);
		}
		if (mTab02 != null)
		{
			transaction.hide(mTab02);
		}
		if (mTab03 != null)
		{
			transaction.hide(mTab03);
		}
		if (mTab04 != null)
		{
			transaction.hide(mTab04);
		}
	}

	@Override
	public void onClick(View v)
	{
		resetImgs();
		switch (v.getId())
		{
		case R.id.id_tab_weixin:
			setSelect(0);
			break;
		case R.id.id_tab_frd:
			setSelect(1);
			break;
		case R.id.id_tab_address:
			setSelect(2);
			break;
		
		default:
			break;
		}
	}

	
	private void resetImgs()
	{
		mImgPlan.setImageResource(R.drawable.notepad_normal);
		mImgSchool.setImageResource(R.drawable.delicious_normal);
		mImgMyself.setImageResource(R.drawable.myself_normal);
		
		notepad_text.setTextColor(this.getResources().getColor(R.color.bottom_text_color));
		delicious_text.setTextColor(this.getResources().getColor(R.color.bottom_text_color));
		myself_text.setTextColor(this.getResources().getColor(R.color.bottom_text_color));
	}

	
	//在主activity中监听返回键退出程序
	private long exitTime = 0;

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
	    if(keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN){  
	        if((System.currentTimeMillis()-exitTime) > 2000){ 
	            Toast.makeText(getApplicationContext(), "再按一次退出程序", Toast.LENGTH_SHORT).show();                               
	            exitTime = System.currentTimeMillis();  
	        } else {
	            finish();
	            System.exit(0);
	        }
	        return true;  
	    }
	    return super.onKeyDown(keyCode, event);
	}
	private void BaiduStatistics() {
		
		// 设置AppKey
		StatService.setAppKey("xEX4OIkw9Xj6mRYTLMLF4oidisrjBWnW"); // appkey必须在mtj网站上注册生成，该设置建议在AndroidManifest.xml中填写，代码设置容易丢失

		/*
		 * 设置渠道的推荐方法。该方法同setAppChannel（String），
		 * 如果第三个参数设置为true（防止渠道代码设置会丢失的情况），将会保存该渠道，每次设置都会更新保存的渠道，
		 * 如果之前的版本使用了该函数设置渠道
		 * ，而后来的版本需要AndroidManifest.xml设置渠道，那么需要将第二个参数设置为空字符串,并且第三个参数设置为false即可。
		 * appChannel是应用的发布渠道，不需要在mtj网站上注册，直接填写就可以 该参数也可以设置在AndroidManifest.xml中
		 */
		StatService.setAppChannel(this, "RepleceWithYourChannel", true);
		// 测试时，可以使用1秒钟session过期，这样不断的间隔1S启动退出会产生大量日志。
		StatService.setSessionTimeOut(30);
		// setOn也可以在AndroidManifest.xml文件中填写，BaiduMobAd_EXCEPTION_LOG，打开崩溃错误收集，默认是关闭的
		StatService.setOn(this, StatService.EXCEPTION_LOG);
		/*
		 * 设置启动时日志发送延时的秒数<br/> 单位为秒，大小为0s到30s之间<br/>
		 * 注：请在StatService.setSendLogStrategy之前调用，否则设置不起作用
		 * 
		 * 如果设置的是发送策略是启动时发送，那么这个参数就会在发送前检查您设置的这个参数，表示延迟多少S发送。<br/>
		 * 这个参数的设置暂时只支持代码加入， 在您的首个启动的Activity中的onCreate函数中使用就可以。<br/>
		 */
		StatService.setLogSenderDelayed(10);
		/*
		 * 用于设置日志发送策略<br /> 嵌入位置：Activity的onCreate()函数中 <br />
		 * 
		 * 调用方式：StatService.setSendLogStrategy(this,SendStrategyEnum.
		 * SET_TIME_INTERVAL, 1, false); 第二个参数可选： SendStrategyEnum.APP_START
		 * SendStrategyEnum.ONCE_A_DAY SendStrategyEnum.SET_TIME_INTERVAL 第三个参数：
		 * 这个参数在第二个参数选择SendStrategyEnum.SET_TIME_INTERVAL时生效、
		 * 取值。为1-24之间的整数,即1<=rtime_interval<=24，以小时为单位 第四个参数：
		 * 表示是否仅支持wifi下日志发送，若为true，表示仅在wifi环境下发送日志；若为false，表示可以在任何联网环境下发送日志
		 */
		StatService.setSendLogStrategy(this, SendStrategyEnum.APP_START, 1,
				false);
		// 调试百度统计SDK的Log开关，可以在Eclipse中看到sdk打印的日志，发布时去除调用，或者设置为false
		StatService.setDebugOn(true);
	}
}
