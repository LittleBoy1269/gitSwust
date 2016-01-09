package com.swust.schoolplan;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Picture;

public class SDatabaseHelper extends SQLiteOpenHelper {
	
	private String StableName = "Sch_Activity";
	private Context mContext = null;
	private String sql = "create table if not exists " + StableName +
			"(_id integer primary key autoincrement, " +
			"title varchar," +
			"dimg BLOB," +
			"cloud_id int," +
			"person varchar," +
			"time varchar," +
			"place varchar," +
			"detail varchar)";
	
	public SDatabaseHelper(Context context, String name, CursorFactory factory,
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
		
		
	}

}
