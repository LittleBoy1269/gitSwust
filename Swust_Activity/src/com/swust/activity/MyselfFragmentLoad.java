package com.swust.activity;

import com.swust.load.LoadActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class MyselfFragmentLoad extends Fragment {

	private LinearLayout m_my_person;
	private RelativeLayout m_my_delicious;
	private RelativeLayout m_ilike_delicious;
	private RelativeLayout m_my_idea;
	private RelativeLayout m_my_logout;
	private View myselfView;
	private TextView load_text;
	private TextView register_text;
	
	
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState)
	{
		
		myselfView=inflater.inflate(R.layout.myself, container, false);
		initView();
		initEvents();
		return myselfView;
	}
	public void initView()
	{
		
		m_my_person=(LinearLayout) myselfView.findViewById(R.id.my_person);
	    m_my_idea=(RelativeLayout) myselfView.findViewById(R.id.my_idea);
		m_my_delicious=(RelativeLayout) myselfView.findViewById(R.id.my_share_delicious);
		m_ilike_delicious=(RelativeLayout) myselfView.findViewById(R.id.i_like_delicious);
		m_my_logout=(RelativeLayout) myselfView.findViewById(R.id.my_logout);
	}
	public void initEvents() 
	{
        m_my_idea.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View view) {
				startActivity(new Intent(getActivity(), com.swust.person.personAdviceActivity.class));
			}
		});
        m_my_delicious.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent=new Intent(getActivity(),com.swust.mydelicious.MyDeliciousActivity.class);
				startActivity(intent);
			}
		});
        m_ilike_delicious.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent=new Intent(getActivity(),com.swust.ilikedelicious.ILikeDeliciousActivity.class);
				startActivity(intent);
			}
		});
		m_my_person.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View view) {
				startActivity(new Intent(getActivity(), com.swust.person.personResActivity.class));
			}
		});
		m_my_logout.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				com.swust.load.LoadActivity.userid=null;
				
//				Intent intent=new Intent();
//				intent.setClass(getActivity(), com.swust.load.LoadActivity.class);
//				startActivity(intent);
			}
		});
	}
}
