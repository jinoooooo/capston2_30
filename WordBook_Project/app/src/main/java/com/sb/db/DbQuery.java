package com.sb.db;

public class DbQuery {

	public static String TABLE_NAME = "data";
	
	public static String CREATE_TABLE = 
			"CREATE TABLE " + TABLE_NAME + " (" +
			"'seq' INTEGER PRIMARY KEY AUTOINCREMENT, " +
			"'text' TEXT NOT NULL, " +
			"'dt' TEXT NOT NULL)";
	
	public static String DROP_TABLE = "drop table if exists " + TABLE_NAME;
}
