package com.adapter;

import java.util.ArrayList;

import android.content.Context;
import android.media.Image;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.entity.MyEntity;
import com.test.MainActivity;
import com.test.R;

public class MyAdapter extends BaseAdapter{

	ArrayList<MyEntity>de_list;
	LayoutInflater inflater;
	Context context;
	public MyAdapter(Context context,ArrayList<MyEntity>data)
	{
		this.de_list=data;
		this.inflater=LayoutInflater.from(context);
		this.context=context;
	}
	
	
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return de_list.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return de_list.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}
    /**
     * 获取listview每个item内的元素
     * @kai
     */
	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		MyEntity entity=de_list.get(position);
	    ViewHolder holder;
		if(convertView==null)
		{
			holder=new ViewHolder();
			convertView=inflater.inflate(R.layout.listview_item, null);
			holder.title_img_hv=(ImageView) convertView.findViewById(R.id.title_img);
			holder.title_hv=(TextView) convertView.findViewById(R.id.title);
			holder.content_hv=(TextView) convertView.findViewById(R.id.content);
			//holder.like_img_hv=(ImageView) convertView.findViewById(R.id.like_img);
			convertView.setTag(holder);
		}
		else
		{
			holder=(ViewHolder) convertView.getTag();
		}
		holder.title_hv.setText(entity.getTitle());
		holder.content_hv.setText(entity.getContent());
		/*
		holder.like_img_hv.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				holder.like_img_hv.setImageResource(R.drawable.like_pressed);
			}
		});
		*/
		return convertView;
	}

	class ViewHolder{
		ImageView title_img_hv;
		TextView title_hv;
		TextView content_hv;
		//ImageView like_img_hv;
	}

	public void onDataChange(ArrayList<MyEntity>data) {
		this.de_list=data;
		this.notifyDataSetChanged();
	}
}
