package com.swust.delicious;


import java.util.Comparator;
import java.util.List;

import org.apache.http.client.methods.AbortableHttpRequest;


import com.swust.activity.R;
import com.swust.http.ImageLoader;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

public class DListViewAdapter extends BaseAdapter implements AbsListView.OnScrollListener{
	
	private List<DeliciousEntity> delicious_list;
	private LayoutInflater mInflater;
	
	private ImageLoader mImageLoader;
	private int mStart,mEnd;
	public static String [] URLS;
	private boolean mFirstIn;
	private int flag=-1;//0表示通过handler，1表示通过AsyncTask
	
	public DListViewAdapter(Context context, List<DeliciousEntity>data){
		
		delicious_list = data;
		mInflater = LayoutInflater.from(context);
		flag=0;
	}
	public DListViewAdapter(Context context, List<DeliciousEntity>data,ListView listview){
		flag=1;
		mImageLoader = new ImageLoader(listview);
		delicious_list = data;
		mInflater = LayoutInflater.from(context);
		URLS = new String[data.size()];
		for( int i = 0; i < data.size(); i++)
		{
			URLS[i] = data.get(i).getUrl();
		}
		mFirstIn = true;
		listview.setOnScrollListener(this);
	}
	
	public void onDateChange(List<DeliciousEntity> data) { 
		this.delicious_list = data;
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

	public int getCount()
	{
		return delicious_list.size();
	}

	/**
	 * 根据索引获取列表对应索引的内容
	 */
	public Object getItem(int position) {
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
			holder = new ViewHolder();
			convertView = mInflater.inflate(R.layout.school_delicious_item,null);	
			holder.img_tv=(ImageView) convertView.findViewById(R.id.school_delicious_img);
			holder.comment_tv=(TextView) convertView.findViewById(R.id.comment_count);
			holder.title_tv = (TextView)convertView.findViewById(R.id.de_title_text);
			holder.keyword_tv=(TextView) convertView.findViewById(R.id.de_keyword_text);
			holder.average_tv=(TextView) convertView.findViewById(R.id.de_average_text);
			holder.place_tv = (TextView)convertView.findViewById(R.id.de_place_text);
			convertView.setTag(holder);

		}else{
			holder = (ViewHolder) convertView.getTag();
		}
		
		holder.img_tv.setImageResource(R.drawable.img_default);
		//String url="http://foodbucket.gz.bcebos.com/1.jpg";
		String url=delicious_list.get(position).imgUrl;
		if(url!=null && URLS.length==0){
			holder.img_tv.setTag(url);
			new ImageLoader().showImageByThread_(holder.img_tv, url);
			//mImageLoader.showImageByThread_(holder.img_tv, url);
		}
		if(url!=null && URLS.length!=0){
			holder.img_tv.setTag(url);
			//new ImageLoader().showImageByThread_(holder.img_tv, url);
			mImageLoader.showImageByAsyncTask(holder.img_tv, url);
		}
		//mImageLoader.showImageByAsyncTask(holder.img_tv, url);
		String str=entity.getcommentSum().toString();
		holder.comment_tv.setText(str);
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
		TextView comment_tv;
	}

	@Override
	public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
		// TODO Auto-generated method stub
		mStart = firstVisibleItem;
		mEnd = firstVisibleItem + visibleItemCount;
		//第一次加载的时候调用
		if (mFirstIn && visibleItemCount > 0)
		{
			mImageLoader.loadImages(mStart, mEnd);
			mFirstIn = false;
		}
	}

	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState) {
		if (scrollState == SCROLL_STATE_IDLE)
		{
			// 加载可见项
			mImageLoader.loadImages(mStart, mEnd);
		} else {
			// 停止任务
			mImageLoader.cancelAllTast();
		}
	}
	

	

}
