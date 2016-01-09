package com.swust.schoolplan;


import java.util.List;


import com.swust.activity.R;
import com.swust.http.ImageLoader;
import com.swust.model.SchoolCalender;

import android.content.Context;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class SListViewAdapter extends BaseAdapter {
	
	List<ApkEntity> apk_list;
	
	private LayoutInflater mInflater;
	//private ImageLoader mImageLoader;
	public SListViewAdapter(Context context, List<ApkEntity> apk_list2 ){
		this.apk_list = apk_list2;
		this.mInflater = LayoutInflater.from(context);
		//mImageLoader=new ImageLoader();
		//inflater = (LayoutInflater) 
				//context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}
	
	public void onDateChange(List<ApkEntity> apk_list) {
		this.apk_list = apk_list;
		this.notifyDataSetChanged();
	}

	/**
	 * 往列表添加条目
	 * @param item
	 */
	public void addListItem(ApkEntity apk_list){
		this.apk_list.add(apk_list);
		
	}
	
	/**
	 * 删除指定位置的数据
	 * @param position
	 */
	public void removeListItem(int position){
		apk_list.remove(position);
	}

	
	/**
	 * 获取列表的数量
	 */
	public int getCount() {
		return apk_list.size();
	}

	/**
	 * 根据索引获取列表对应索引的内容
	 */
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return apk_list.get(position);
	}

	public long getItemId(int position) {
		return position;
	}

	/**
	 * 通过该函数显示数据
	 */
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		
		ApkEntity entity=apk_list.get(position);
		ViewHolder holder;
		
		if(convertView == null){
			convertView = mInflater.inflate(R.layout.school_plan_item,null);
			holder = new ViewHolder();
			holder.img_tv=(ImageView) convertView.findViewById(R.id.school_activity_img);
			holder.title_tv = (TextView)convertView.findViewById(R.id.ac_title_text);		
			holder.person_tv = (TextView)convertView.findViewById(R.id.ac_person_text);			
			holder.time_tv = (TextView)convertView.findViewById(R.id.ac_time_text);		
			holder.place_tv = (TextView)convertView.findViewById(R.id.ac_place_text);
			
			convertView.setTag(holder);

		}else{
			holder = (ViewHolder) convertView.getTag();
		}
		holder.img_tv.setImageResource(R.drawable.img_default);
		//String url="http://foodbucket.gz.bcebos.com/1.jpg";
		String url=entity.imgUrl;
		holder.img_tv.setTag(url);
		new ImageLoader().showImageByThread_(holder.img_tv, url);
		holder.title_tv.setText(entity.getTitle());	
		holder.person_tv.setText(entity.getPerson());
		holder.time_tv.setText(entity.getTime());
		holder.place_tv.setText(entity.getPlace());
		return convertView;
	}

	class ViewHolder {
		TextView title_tv;
		TextView person_tv;
		TextView time_tv;
		TextView place_tv;
		TextView detail_tv;
		ImageView img_tv;
	}

}
