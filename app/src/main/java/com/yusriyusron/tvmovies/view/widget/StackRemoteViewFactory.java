package com.yusriyusron.tvmovies.view.widget;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.yusriyusron.tvmovies.R;
import com.yusriyusron.tvmovies.model.Movie;
import com.yusriyusron.tvmovies.model.database.DbHelper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.ExecutionException;

import static com.yusriyusron.tvmovies.view.DetailActivity.TAG_ID;
import static com.yusriyusron.tvmovies.view.DetailActivity.TAG_OVERVIEW;
import static com.yusriyusron.tvmovies.view.DetailActivity.TAG_POSTER;
import static com.yusriyusron.tvmovies.view.DetailActivity.TAG_TITLE;

public class StackRemoteViewFactory implements RemoteViewsService.RemoteViewsFactory {

    private final ArrayList<Movie> mWidgetItems = new ArrayList<>();
    private final Context mContext;

    private DbHelper helper;


    StackRemoteViewFactory(Context context) {
        mContext = context;
        helper = new DbHelper(context);
    }

    @Override
    public void onCreate() {
    }

    @Override
    public void onDataSetChanged() {
        ArrayList<HashMap<String, String>> row = helper.getAllData();

        for (int i = 0; i < row.size() ; i++) {
            String id = row.get(i).get(TAG_ID);
            String poster = row.get(i).get(TAG_POSTER);
            String title = row.get(i).get(TAG_TITLE);
            String overview = row.get(i).get(TAG_OVERVIEW);

            Movie data = new Movie(id, poster, title, overview);
            mWidgetItems.add(data);
        }
    }

    @Override
    public void onDestroy() {

    }

    @Override
    public int getCount() {
        return mWidgetItems.size();
    }

    @Override
    public RemoteViews getViewAt(int position) {
        RemoteViews rv = new RemoteViews(mContext.getPackageName(), R.layout.widget_item);

        String poster = mWidgetItems.get(position).getImageMovie();

        try {
            Bitmap preview = Glide.with(mContext)
                    .asBitmap()
                    .load(poster)
                    .apply(new RequestOptions().fitCenter())
                    .submit()
                    .get();
            rv.setImageViewBitmap(R.id.imageView, preview);
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        Bundle extras = new Bundle();
        extras.putInt(ImageBannerWidget.EXTRA_ITEM, position);
        Intent fillInIntent = new Intent();
        fillInIntent.putExtras(extras);
        rv.setOnClickFillInIntent(R.id.imageView, fillInIntent);
        return rv;
    }

    @Override
    public RemoteViews getLoadingView() {
        return null;
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }
}
