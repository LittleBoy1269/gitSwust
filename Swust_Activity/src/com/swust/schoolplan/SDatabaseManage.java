//操作学校活动listview的数据库的manage

package com.swust.schoolplan;


import java.util.Date;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.util.Log;

public class SDatabaseManage {

	private Context mContext = null;
	
	private SQLiteDatabase mSQLiteDatabase = null;//用于操作数据库的对象
	private SDatabaseHelper dh = null;//用于创建数据库的对象
	
	private String dbName = "Sch_Activity.db";
	private int dbVersion = 1;

	public SDatabaseManage(Context context){
		mContext = context;
	}
	
	/**
	 * 打开数据库
	 */
	public void open(){
		
		try{
			dh = new SDatabaseHelper(mContext, dbName, null, dbVersion);
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
			String sql = "select * from Sch_Activity";
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
			String sql = "select * from Sch_Activity where _id='" + id +"'";
			cursor = mSQLiteDatabase.rawQuery(sql, null);
		}catch(Exception ex){
			ex.printStackTrace();
			cursor = null;
		}
		
		return cursor;
	}
	
	//插入数据
	public long insert(String title, String person, String time, String place,String detail){
		
		long l = -1;
		try{
			ContentValues cv = new ContentValues();
			cv.put("title", title);
			cv.put("person", person);
			cv.put("time", time);
			cv.put("place",place);
			cv.put("detail", detail);
			l = mSQLiteDatabase.insert("Sch_Activity", null, cv);
		
		}catch(Exception ex){
			ex.printStackTrace();
			l = -1;
		}
		return l;
		
	}
	/*
	//删除数据
	public int delete(long id){
		int affect = 0;
		try{
			affect = mSQLiteDatabase.delete("Sch_Activity", "_id=?", new String[]{id+""});
		}catch(Exception ex){
			ex.printStackTrace();
			affect = -1;
		}
		
		return affect;
	}
	
	//修改数据
	public int update(int id, String title, String person,String time,String place,String detail){
		int affect = 0;
		try{
			ContentValues cv = new ContentValues();
			
			cv.put("title", title);
			cv.put("person", person);
			cv.put("time", time);
			cv.put("place",place);
			cv.put("detail",detail);
			String w[] = {id+""};
			affect = mSQLiteDatabase.update("Sch_Activity", cv, "_id=?", w);
		}catch(Exception ex){
			ex.printStackTrace();
			affect = -1;
		}
		return affect;
	}
	*/
}
