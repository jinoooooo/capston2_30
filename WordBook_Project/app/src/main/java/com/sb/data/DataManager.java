package com.sb.data;

import android.content.Context;
import android.database.Cursor;

import com.sb.db.DbHelper;

import java.util.ArrayList;

public class DataManager {

    private static DataManager instance;
    public static DataManager getInstance() {

        if( instance == null ) {
            instance = new DataManager();
        }

        return instance;
    }

    public static void purgeManager() {

        if( instance != null ) {
            if( instance.datas != null ) {
                instance.datas.clear();
                instance.datas = null;
            }

            instance = null;
        }
    }

    private ArrayList<WordData> datas;

    private DataManager() {
        datas = new ArrayList<WordData>();
    }

    public void init(Context ctx) {

        DbHelper dbHelper = DbHelper.open(ctx);

        Cursor cursor = dbHelper.getAllRecord();

        while( cursor.moveToNext() ) {
            WordData data = new WordData();
            data.setSeq(cursor.getInt(cursor.getColumnIndex("seq")));
            data.setText(cursor.getString(cursor.getColumnIndex("text")));
            data.setDate(cursor.getString(cursor.getColumnIndex("dt")));
            datas.add(data);
        }

        cursor.close();
        dbHelper.close();
    }

    public void addWord(Context ctx, WordData data) {

        DbHelper dbHelper = DbHelper.open(ctx);
        dbHelper.insert(data.getText(), data.getDate());

        data.setSeq(datas.size()+1);
        datas.add(data);

        dbHelper.close();
    }

    

    public ArrayList<WordData> getDatas() {
        return datas;
    }
}
