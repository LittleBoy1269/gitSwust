package com.swust.http;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashSet;
import java.util.Set;



import com.swust.activity.R;
import com.swust.delicious.DListViewAdapter;



import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.support.v4.util.LruCache;
import android.widget.ImageView;
import android.widget.ListView;


public class ImageLoader{
	private ImageView mImageView;
	private String mUrl;
	//创建Cache，本质是一个map
	private LruCache<String,Bitmap> mCache;
	private ListView mListView;
	private Set<MyAsyncTask> mTask;
	
	public ImageLoader(){
		
	}
	
	public ImageLoader(ListView listview) {
		mListView=listview;
		mTask = new HashSet<MyAsyncTask >();
		//获取最大可用内存
		int maxMemory = (int) Runtime.getRuntime().maxMemory();
		int cacheSize = maxMemory / 3;
		mCache = new LruCache<String, Bitmap>(cacheSize){
			protected int sizeOf(String key, Bitmap value) {
				//在每次存入缓存的时候调用
				return value.getByteCount();
			};
		};
	}
	// 增加到缓存
	public void addBitmapToCache(String url, Bitmap bitmap)
		{
		if (getBitmapFromCache(url) == null)
		{
			mCache.put(url, bitmap);
		}
	}
		
	//从缓存中获取数据
	public Bitmap getBitmapFromCache(String url)
	{
		return mCache.get(url);
	}
	
	
	private Handler mHandler=new Handler()
	{
		@Override
		public void handleMessage(Message msg)
		{
			super.handleMessage(msg);
			if(mImageView.getTag().equals(mUrl))
			{
				mImageView.setImageBitmap((Bitmap) msg.obj);
			}
			
		}
	};
	
//	public void showImageByThread(ImageView imageView,final String url)
//	{
//		mImageView=imageView;
//		mUrl=url;
//		new Thread(){
//			@Override
//			public void run() {
//				super.run();
//				try {
//					Bitmap bitmap=getBitmapFromURL(url);
//					Message message=Message.obtain();
//				    message.obj=bitmap;
//				    addBitmapToCache(url,bitmap);
//				    mHandler.sendMessage(message);
//				} catch (IOException e) {
//					e.printStackTrace();
//				}
//				
//			};
//		}.start();
//	}
	
	public void showImageByThread_(ImageView imageView,final String url)
	{
		mImageView=imageView;
		mUrl=url;
		new Thread(){
			@Override
			public void run() {
				super.run();
				try {
					
					Bitmap bitmap=getBitmapFromURL_(url);
					Message message=Message.obtain();
				    message.obj=bitmap;
				    mHandler.sendMessage(message);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
			};
		}.start();
	}
	
	public Bitmap getBitmapFromURL_(String urlString) throws IOException{
		Bitmap bitmap = null;
		InputStream is = null; 
		try{
			URL url=new URL(urlString);
			HttpURLConnection connection=(HttpURLConnection) url.openConnection();
			is = new BufferedInputStream(connection.getInputStream());
			bitmap= BitmapFactory.decodeStream(is);
			connection.disconnect();
			return bitmap;
		}catch(java.io.IOException e){
			e.printStackTrace();
		}finally{
			
			is.close();
		}
		return null;
	}
	
	public Bitmap getBitmapFromUrl(String urlString)
	{
		Bitmap bitmap = null;
		InputStream is = null;
		try {
			URL url = new URL(urlString);
			try {
				HttpURLConnection connection=  (HttpURLConnection) url.openConnection();
				is = new BufferedInputStream(connection.getInputStream());
				bitmap = BitmapFactory.decodeStream(is);
				connection.disconnect();
				
//				try {
//					Thread.sleep(1000);
//				} catch (InterruptedException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				}
				return bitmap;
			} catch (IOException e) {
				e.printStackTrace();
			}
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}finally {
			try {
				is.close();
				
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return null;
	}
	public void showImageByAsyncTask(ImageView imageView,String url)
	{
		//从缓存中取出对应的图片
		Bitmap bitmap = getBitmapFromCache(url);
		//如果缓存中没有，那么必须下载
		if (bitmap == null)
		{
			//new NewsAsyncTask(url).execute(url);
			imageView.setImageResource(R.drawable.img_default);
			
		}else
		{
			imageView.setImageBitmap(bitmap);
		}
	}
	
	public void cancelAllTast()
	{
		if (mTask != null)
		{
			for (MyAsyncTask task : mTask)
			{
				task.cancel(false);
				
			}
		}
	}
	
	
	//用来加载从start到end的所有图片
		public void loadImages(int start,int end)
		{
			for ( int i = start; i < end; i ++)
			{
				String url =DListViewAdapter.URLS[i];
				//从缓存中取出对应的图片
				Bitmap bitmap = getBitmapFromCache(url);
				//如果缓存中没有，那么必须下载
				if (bitmap == null)
				{
					MyAsyncTask task = new MyAsyncTask(url); 
					task.execute(url);
					mTask.add(task);
				}else
				{
					ImageView imageView = (ImageView) mListView.findViewWithTag(url);
					imageView.setImageBitmap(bitmap);
				}	
			}
		}
		
		private class MyAsyncTask extends AsyncTask<String, Void, Bitmap>
		{
//			private ImageView mImageView;
			private String mUrl;
			public MyAsyncTask(String url) {
//				mImageView = imageView;
				mUrl = url;
			}
			
			@Override
			protected Bitmap doInBackground(String... params) {
				String url = params[0];
				// 从网络获取图片
				Bitmap bitmap = getBitmapFromUrl(url);
				if (bitmap != null)
				{
					// 将不在缓存的图片加入缓存
					addBitmapToCache(url, bitmap);
				}
				return bitmap;
			}

			@Override
			protected void onPostExecute(Bitmap bitmap) {
				super.onPostExecute(bitmap);
				ImageView imageView = (ImageView) mListView.findViewWithTag(mUrl);
				if(imageView != null && bitmap != null)
				{
					imageView.setImageBitmap(bitmap);
				}	
				mTask.remove(this);
			}
			
		}
}