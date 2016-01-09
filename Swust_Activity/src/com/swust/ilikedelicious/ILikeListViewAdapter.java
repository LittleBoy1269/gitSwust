package com.swust.ilikedelicious;


import java.util.List;


import com.swust.activity.R;
import com.swust.delicious.DeliciousEntity;
import com.swust.http.ImageLoader;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class ILikeListViewAdapter extends BaseAdapter {
	
	private List<DeliciousEntity> delicious_list;
	private LayoutInflater inflater;
	public ILikeListViewAdapter(Context context, List<DeliciousEntity> delicious_list2 ){
		delicious_list = delicious_list2;
		inflater = LayoutInflater.from(context);
		//inflater = (LayoutInflater) 
				//context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}
	
	public void onDateChange(List<DeliciousEntity> delicious_list) {
		this.delicious_list = delicious_list;
		this.notifyDataSetChanged();
	}

	/**
	 * 往列表添加条目
	 * @param item
	 */
	public void addListItem(DeliciousEntity delicious_list){
		this.delicious_list.add(delicious_list);
		
	}
	
	/**
	 * 删除指定位置的数据
	 * @param position
	 */
	public void removeListItem(int position){
		delicious_list.remove(position);
	}

	
	/**
	 * 获取列表的数量
	 */
	public int getCount() {
		return delicious_list.size();
	}

	/**
	 * 根据索引获取列表对应索引的内容
	 */
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return delicious_list.get(position);
	}

	public long getItemId(int position) {
		return position;
	}

	/**
	 * 通过该函数显示数据
	 */
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		
		DeliciousEntity entity=delicious_list.get(position);
		ViewHolder holder=null;
		
		if(convertView == null){
			convertView = inflater.inflate(R.layout.ilike_delicious_item,null);
			holder = new ViewHolder();
			holder.img_tv=(ImageView) convertView.findViewById(R.id.school_delicious_img);
			holder.title_tv = (TextView)convertView.findViewById(R.id.de_title_text);
			holder.keyword_tv=(TextView) convertView.findViewById(R.id.de_keyword_text);
			holder.average_tv=(TextView) convertView.findViewById(R.id.de_average_text);
			holder.place_tv = (TextView)convertView.findViewById(R.id.de_place_text);
			//holder.dimg_tv=convertView.findViewById(R.id.ilke_img);
			convertView.setTag(holder);

		}else{
			holder = (ViewHolder) convertView.getTag();
		}
		if(delicious_list.get(position).imgUrl==null)
			holder.img_tv.setImageResource(R.drawable.delicious);
		else
		    holder.img_tv.setImageResource(R.drawable.img_default);
		//String url="http://foodbucket.gz.bcebos.com/1.jpg";
		String url=delicious_list.get(position).imgUrl;
		if(url!=null){
			holder.img_tv.setTag(url);
			new ImageLoader().showImageByThread_(holder.img_tv, url);
		}
		holder.title_tv.setText(entity.getTitle());
		holder.keyword_tv.setText(entity.getKeyword());
		holder.average_tv.setText(entity.getAverage());
		holder.place_tv.setText(entity.getPlace());
		return convertView;
	}

	class ViewHolder {
		TextView title_tv;
		TextView average_tv;
		TextView place_tv;
		TextView keyword_tv;
		TextView detail_tv;
		ImageView img_tv;
	}

}
