package com.yusriyusron.tvmovies.model.database;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;
import android.util.Log;

import java.util.ArrayList;
import java.util.HashMap;

public class DbHelper extends SQLiteOpenHelper {

    //Content Provider
    public static final String AUTHORITY = "com.yusriyusron.tvmovies";
    private static final String SCHEME = "content";

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "favorite.db";
    public static final String TABLE_NAME = "favorite";

    public static final String COLUMN_ID = "id";
    public static final String COLUMN_POSTER = "poster";
    public static final String COLUMN_TITLE = "title";
    public static final String COLUMN_OVERVIEW = "overview";

    public static final String[] ALL_COLUMN = {COLUMN_ID,COLUMN_POSTER,COLUMN_TITLE,COLUMN_OVERVIEW};

    public DbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String SQL_CREATE_TABLE_FAVORITE = "CREATE TABLE "+TABLE_NAME+" (" +
                COLUMN_ID +" TEXT," +
                COLUMN_POSTER + " TEXT," +
                COLUMN_TITLE +" TEXT," +
                COLUMN_OVERVIEW +" TEXT)";
        sqLiteDatabase.execSQL(SQL_CREATE_TABLE_FAVORITE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS "+TABLE_NAME);
    }

    public void insert(String id, String poster, String title, String overview){
        SQLiteDatabase database = this.getWritableDatabase();
        String cleanTitle = title.replace("'","");
        String cleanOverview = overview.replace("'","");
        String query = "INSERT INTO "+TABLE_NAME+" (id,poster,title,overview) VALUES ('"+id+"','"+poster+"','"+cleanTitle+"','"+cleanOverview+"')";
        database.execSQL(query);
    }

    public boolean selectById(String id){
        SQLiteDatabase database = this.getReadableDatabase();
        String query = "SELECT * FROM "+TABLE_NAME+" WHERE "+COLUMN_ID+"='"+id+"'";
        Cursor cursor = database.rawQuery(query,null);
        if (cursor.moveToFirst()){
            Log.e("SELECT ID",id);
            return true;
        }
        return false;
    }

    public void delete(String id){
        SQLiteDatabase database = this.getWritableDatabase();
        String query = "DELETE FROM "+TABLE_NAME+" WHERE "+COLUMN_ID+"='"+id+"'";
        database.execSQL(query);
    }

    public ArrayList<HashMap<String, String>> getAllData() {
        ArrayList<HashMap<String, String>> list;
        list = new ArrayList<HashMap<String, String>>();
        String selectQuery = "SELECT * FROM " + TABLE_NAME;
        SQLiteDatabase database = this.getWritableDatabase();
        Cursor cursor = database.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                HashMap<String, String> map = new HashMap<String, String>();
                map.put(COLUMN_ID, cursor.getString(0));
                map.put(COLUMN_POSTER, cursor.getString(1));
                map.put(COLUMN_TITLE, cursor.getString(2));
                map.put(COLUMN_OVERVIEW, cursor.getString(3));
                list.add(map);
            } while (cursor.moveToNext());
        }

        database.close();
        return list;
    }

    //Content Provider
    public static final Uri CONTENT_URI = new Uri.Builder().scheme(SCHEME)
            .authority(AUTHORITY)
            .appendPath(TABLE_NAME)
            .build();
}
