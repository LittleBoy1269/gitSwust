package com.swust.notepad;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {
	
	private String tableName = "Notepad";
	private Context mContext = null;
	private String sql = "create table if not exists " + tableName +
			"(_id integer primary key autoincrement, " +
			"cloud_id int," +
			"title varchar," +
			"content text," +
			"time varchar)";
	
	public DatabaseHelper(Context context, String name, CursorFactory factory,
			int version) {
		super(context, name, factory, version);
		
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		
		
		//创建表
	
		db.execSQL(sql);
		
		
		
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub
		
	}

}
