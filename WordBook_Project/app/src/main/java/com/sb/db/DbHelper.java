package com.sb.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.sb.wordbook.Config;

import java.io.File;
import java.util.ArrayList;

public class DbHelper {

	private final Context ctx;
	private final String fileName;
	
	private SQLiteDatabase db;
	private final DbOpenHelper openHelper;

	private DbHelper(Context context, String name, int version) {
		
		this.ctx = context;
		this.fileName = name;
		openHelper = new DbOpenHelper(ctx, name, null, version);
		db = openHelper.getWritableDatabase();
	}
	
	public static DbHelper open(Context context, String name, int version) {
		
		DbHelper helper = new DbHelper(context, name, version);
		return helper;
	}
	
	public static DbHelper open(Context context, String name) {
		
		return open(context, name, 1);
	}
	
	public static DbHelper open(Context context) {
		
		return open(context, Config.DB_FILE, 1);
	}
	
	public void close(){
		
		db.close();
		openHelper.close();
	}
	
	public void deleteFile() {
		
		String file = ctx.getDatabasePath(fileName).getAbsolutePath();
		deleteFile(file);
	}

	public long insert(String wordText, String dt) {
		
		ContentValues values = new ContentValues();
		values.put("text", wordText);
		values.put("dt", dt);
		
		return db.insert(DbQuery.TABLE_NAME, null, values);
	}

	public Cursor getAllRecord(){
		
		return db.query(DbQuery.TABLE_NAME, null, null, null, null, null, null);
	}
	
	public static String getRootPath(Context ctx) {
		
		String path = ctx.getFilesDir().getAbsolutePath().replace("files", "databases");
		return path;
	}
	
	public static ArrayList<File> getDbFiles(Context ctx) {
		
		File files[] = new File(getRootPath(ctx)).listFiles();
		ArrayList<File> dbFiles = new ArrayList<File>();
		
		if( files != null ) {
			for( File f : files ) {
				if( !f.getAbsolutePath().contains("journal") ) {
					dbFiles.add(f);
				}
			}
		}
		
		return dbFiles;
	}
	
	public static void deleteFile(String path) {
		
		File f1 = new File(path);
		File f2 = new File(path.replace(".db", ".db-journal"));
		
		// delete .db 
		if( f1.exists() ) {
			f1.delete();
		}
		
		// delete .db-journal
		if( f2.exists() ) {
			f2.delete();
		}
	}
}
