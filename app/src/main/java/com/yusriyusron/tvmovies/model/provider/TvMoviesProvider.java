package com.yusriyusron.tvmovies.model.provider;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;

import com.yusriyusron.tvmovies.model.database.DbHelper;

import static com.yusriyusron.tvmovies.model.database.DbHelper.ALL_COLUMN;
import static com.yusriyusron.tvmovies.model.database.DbHelper.AUTHORITY;
import static com.yusriyusron.tvmovies.model.database.DbHelper.CONTENT_URI;
import static com.yusriyusron.tvmovies.model.database.DbHelper.TABLE_NAME;

public class TvMoviesProvider extends ContentProvider {

    private static final int TVMOVIES = 1;
    private static final int TVMOVIES_ID = 2;

    private SQLiteDatabase database;

    private static final UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

    static {
        uriMatcher.addURI(AUTHORITY, TABLE_NAME, TVMOVIES);
        uriMatcher.addURI(AUTHORITY, TABLE_NAME + "/#", TVMOVIES_ID);
    }

    public TvMoviesProvider() {
    }

    @Override
    public boolean onCreate() {
        DbHelper helper = new DbHelper(getContext());
        database = helper.getWritableDatabase();
        return false;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection,
                        String[] selectionArgs, String sortOrder) {
        Cursor cursor;
        switch (uriMatcher.match(uri)) {
            case TVMOVIES:
                cursor = database.query(TABLE_NAME, ALL_COLUMN, selection, null, null, null, DbHelper.COLUMN_TITLE + " ASC");
                break;
            default:
                throw new IllegalArgumentException("This is an Unknown URI " + uri);
        }
        cursor.setNotificationUri(getContext().getContentResolver(), uri);
        return cursor;
    }

    @Override
    public String getType(Uri uri) {
        return null;
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        long id = database.insert(TABLE_NAME, null, values);

        if (id > 0) {
            Uri _uri = ContentUris.withAppendedId(CONTENT_URI, id);
            getContext().getContentResolver().notifyChange(_uri, null);
            return _uri;
        }

        throw new SQLException("Insertion Failed for URI :" + uri);
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection,
                      String[] selectionArgs) {
        int updateCount = 0;
        switch (uriMatcher.match(uri)) {
            case TVMOVIES:
                updateCount = database.update(TABLE_NAME, values, selection, selectionArgs);
                break;
            default:
                throw new IllegalArgumentException("This is an Unknown URI " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return updateCount;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        int deleteCount = 0;
        switch (uriMatcher.match(uri)) {
            case TVMOVIES:
                deleteCount = database.delete(TABLE_NAME, selection, selectionArgs);
                break;
            default:
                throw new IllegalArgumentException("This is an Unknown URI " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return deleteCount;
    }
}
