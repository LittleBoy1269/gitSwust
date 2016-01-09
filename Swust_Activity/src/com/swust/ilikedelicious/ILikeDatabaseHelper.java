package com.swust.ilikedelicious;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteDatabase.CursorFactory;

public class ILikeDatabaseHelper extends SQLiteOpenHelper {
	private String StableName = "Ilike_Delicious";
	private Context mContext = null;
	private String sql = "create table if not exists " + StableName +
			"(_id integer primary key autoincrement, " +
			"dimg BLOB," +
			"cloud_id int," +
			"like int," +
			"dislike int," +
			"title varchar," +	
			"keyword varchar," +
			"average varchar," +
			"place varchar," +
			"detail varchar)";
	
	public ILikeDatabaseHelper(Context context, String name, CursorFactory factory,
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
