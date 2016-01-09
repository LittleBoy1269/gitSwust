package com.swust.delicious;

//操作学校美食listview的数据库的manage

import java.util.Date;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.util.Log;

public class DDatabaseManage {

	private Context mContext = null;
	
	private SQLiteDatabase mSQLiteDatabase = null;//用于操作数据库的对象
	private DDatabaseHelper dh = null;//用于创建数据库的对象
	
	private String dbName = "Sch_Delicious.db";
	private int dbVersion = 1;

	public DDatabaseManage(Context context){
		mContext = context;
	}
	
	/**
	 * 打开数据库
	 */
	public void open(){
		
		try{
			dh = new DDatabaseHelper(mContext, dbName, null, dbVersion);
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
			String sql = "select * from Sch_Delicious";
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
			String sql = "select * from Sch_Delicious where _id='" + id +"'";
			cursor = mSQLiteDatabase.rawQuery(sql, null);
		}catch(Exception ex){
			ex.printStackTrace();
			cursor = null;
		}
		
		return cursor;
	}
	
	//插入数据
	public long insert(String title, String keyword,String average,String place,String detail,String img_url){
		
		long l = -1;
		try{
			ContentValues cv = new ContentValues();
			cv.put("title", title);
			cv.put("keyword", keyword);		
			cv.put("average",average);
			cv.put("place",place);
			cv.put("detail", detail);
			l = mSQLiteDatabase.insert("Sch_Delicious", null, cv);
		
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
			affect = mSQLiteDatabase.delete("Sch_Delicious", "_id=?", new String[]{id+""});
		}catch(Exception ex){
			ex.printStackTrace();
			affect = -1;
		}
		
		return affect;
	}
	
	//修改数据
	public int update(int id, String title, String keyword,String average,String place,String detail){
		int affect = 0;
		try{
			ContentValues cv = new ContentValues();
			
			cv.put("title", title);	
			cv.put("keyword",keyword);
			cv.put("average",average);
			cv.put("place", place);
			cv.put("detail",detail);
			String w[] = {id+""};
			affect = mSQLiteDatabase.update("Sch_Delicious", cv, "_id=?", w);
		}catch(Exception ex){
			ex.printStackTrace();
			affect = -1;
		}
		return affect;
	}
	

}
