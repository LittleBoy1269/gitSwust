package com.swust.mydelicious;

import java.io.ByteArrayOutputStream;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.graphics.Bitmap;
import android.util.Log;

import com.swust.delicious.DDatabaseHelper;

public class MyDeliciousDatabaseManage {
private Context mContext = null;
	
	private SQLiteDatabase mSQLiteDatabase = null;//用于操作数据库的对象
	private MyDeliciousDatabaseHelper dh = null;//用于创建数据库的对象
	
	private String dbName = "My_Delicious.db";
	private int dbVersion = 1;

	public MyDeliciousDatabaseManage(Context context){
		mContext = context;
	}
	
	/**
	 * 打开数据库
	 */
	public void open(){
		
		try{
			dh = new MyDeliciousDatabaseHelper(mContext, dbName, null, dbVersion);
			if(dh == null){
				Log.v("msg", "is null");
				return ;
			}
			mSQLiteDatabase = dh.getWritableDatabase();			
		}catch(SQLiteException se){
			se.printStackTrace();
		}
	}
	
	
	
	/**
	 * 关闭数据库
	 */
	public void close(){
		
		mSQLiteDatabase.close();
		dh.close();
		
	}
	
	
	
	//获取列表
	public Cursor selectAll(){
		Cursor cursor = null;
		try{
			String sql = "select * from My_Delicious";
			cursor = mSQLiteDatabase.rawQuery(sql, null);
		}catch(Exception ex){
			ex.printStackTrace();
			cursor = null;
		}
		return cursor;
	}
	
	public Cursor selectById(int id){
		
		//String result[] = {};
		Cursor cursor = null;
		try{
			String sql = "select * from My_Delicious where _id='" + id +"'";
			cursor = mSQLiteDatabase.rawQuery(sql, null);
		}catch(Exception ex){
			ex.printStackTrace();
			cursor = null;
		}
		
		return cursor;
	}
	
	//插入数据
	public long insert(String title, String keyword,String average,String place,String detail,Bitmap bmp){
		
		long l = -1;
		try{
			ContentValues cv = new ContentValues();
			final ByteArrayOutputStream os = new ByteArrayOutputStream();   
			bmp.compress(Bitmap.CompressFormat.PNG, 100, os);
			cv.put("title", title);
			cv.put("dimg",  os.toByteArray());
			cv.put("keyword", keyword);		
			cv.put("average",average);
			cv.put("place",place);
			cv.put("detail", detail);
			l = mSQLiteDatabase.insert("My_Delicious", null, cv);
		
		}catch(Exception ex){
			ex.printStackTrace();
			l = -1;
		}
		return l;
		
	}
	
	//删除数据
	public int delete(long id){
		int affect = 0;
		try{
			affect = mSQLiteDatabase.delete("My_Delicious", "_id=?", new String[]{id+""});
		}catch(Exception ex){
			ex.printStackTrace();
			affect = -1;
		}
		
		return affect;
	}
	/*
	//修改数据
	public int update(int id, String title, String keyword,String average,String place,String detail,Bitmap bmp){
		int affect = 0;
		try{
			ContentValues cv = new ContentValues();
			
			cv.put("title", title);	
			cv.put("keyword",keyword);
			cv.put("average",average);
			cv.put("place", place);
			cv.put("detail",detail);
			String w[] = {id+""};
			affect = mSQLiteDatabase.update("my_Delicious", cv, "_id=?", w);
		}catch(Exception ex){
			ex.printStackTrace();
			affect = -1;
		}
		return affect;
	}
	*/

}

