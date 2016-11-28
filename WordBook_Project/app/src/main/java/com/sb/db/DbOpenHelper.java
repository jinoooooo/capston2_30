package com.sb.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class DbOpenHelper extends SQLiteOpenHelper {

	public DbOpenHelper(Context context, String name, CursorFactory factory, int version) {
		
		super(context, name, factory, version);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		
		// SQLiteOpenHelper 가 최초 실행 되었을 때
		db.execSQL(DbQuery.CREATE_TABLE);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

		db.execSQL(DbQuery.DROP_TABLE);

		onCreate(db); // 테이블을 지웠으므로 다시 테이블을 만들어주는 과정
	}
}