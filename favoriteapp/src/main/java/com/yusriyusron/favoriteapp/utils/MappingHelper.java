package com.yusriyusron.favoriteapp.utils;

import android.database.Cursor;

import com.yusriyusron.favoriteapp.model.Movie;
import com.yusriyusron.favoriteapp.model.TvShow;
import com.yusriyusron.favoriteapp.model.database.DbHelper;

import java.util.ArrayList;

public class MappingHelper {
    public static ArrayList<Movie> mapCursorMovieToArrayList(Cursor cursor) {
        ArrayList<Movie> movies = new ArrayList<>();
        while (cursor.moveToNext()) {
            String id = cursor.getString(cursor.getColumnIndexOrThrow(DbHelper.COLUMN_ID));
            String title = cursor.getString(cursor.getColumnIndexOrThrow(DbHelper.COLUMN_TITLE));
            String poster = cursor.getString(cursor.getColumnIndexOrThrow(DbHelper.COLUMN_POSTER));
            String overview = cursor.getString(cursor.getColumnIndexOrThrow(DbHelper.COLUMN_OVERVIEW));
            movies.add(new Movie(id,poster,title,overview));
        }
        return movies;
    }

    public static ArrayList<TvShow> mapCursorTvShowToArrayList(Cursor cursor) {
        ArrayList<TvShow> tvShows = new ArrayList<>();
        while (cursor.moveToNext()) {
            String id = cursor.getString(cursor.getColumnIndexOrThrow(DbHelper.COLUMN_ID));
            String title = cursor.getString(cursor.getColumnIndexOrThrow(DbHelper.COLUMN_TITLE));
            String poster = cursor.getString(cursor.getColumnIndexOrThrow(DbHelper.COLUMN_POSTER));
            String overview = cursor.getString(cursor.getColumnIndexOrThrow(DbHelper.COLUMN_OVERVIEW));
            tvShows.add(new TvShow(id,poster,title,overview));
        }
        return tvShows;
    }
}
