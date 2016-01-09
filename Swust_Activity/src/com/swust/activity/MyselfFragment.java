package com.swust.activity;


import com.swust.load.LoadActivity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.webkit.WebView.FindListener;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class MyselfFragment extends Fragment implements OnClickListener
{
	
	private int isLoad=0; //判断是否登录
	private View myselfView;
	private TextView load_text;
	private TextView register_text;
	private LinearLayout mNotload;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState)
	{
		    
		    
			myselfView=inflater.inflate(R.layout.myselfnotload, container, false);
			load_text=(TextView) myselfView.findViewById(R.id.load_text);
			register_text=(TextView) myselfView.findViewById(R.id.register_text);
			mNotload=(LinearLayout) myselfView.findViewById(R.id.notload_layout);
			load_text.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View arg0) {
					Intent intent = new Intent();
					intent.setClass(getActivity(), com.swust.load.LoadActivity.class);
	    			getActivity().startActivity(intent);
				}
			});
	        register_text.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View arg0) {
					Intent intent = new Intent();
					intent.setClass(getActivity(), com.swust.load.RegisterActivity.class);
	    			getActivity().startActivity(intent);
				}
			});
		    mNotload.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					Toast.makeText(getActivity(), "请登录", Toast.LENGTH_SHORT).show();
					Intent intent = new Intent();
					intent.setClass(getActivity(), com.swust.load.LoadActivity.class);
	    			getActivity().startActivity(intent);
				}
			});
		
		return myselfView;
	}

	
 
	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub
		
	}

	
}

