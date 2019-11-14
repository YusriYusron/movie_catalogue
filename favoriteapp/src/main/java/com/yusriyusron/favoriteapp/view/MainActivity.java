package com.yusriyusron.favoriteapp.view;

import android.content.Context;
import android.content.Intent;
import android.database.ContentObserver;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.provider.Settings;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.yusriyusron.favoriteapp.R;
import com.yusriyusron.favoriteapp.controller.FavoriteAdapter;
import com.yusriyusron.favoriteapp.model.Movie;
import com.yusriyusron.favoriteapp.model.database.DbHelper;
import com.yusriyusron.favoriteapp.utils.MappingHelper;

import java.lang.ref.WeakReference;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity  implements LoadMovieCallback{

    private static final String EXTRA_INSTANCE_STATE = "EXTRA_INSTANCE_STATE";

    private RecyclerView recyclerView;
    private ProgressBar progressBar;
    private TextView textViewNotAvailable;

    private FavoriteAdapter adapter;

    private ArrayList<Movie> listMovie;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = findViewById(R.id.recycler_view);
        textViewNotAvailable = findViewById(R.id.text_view_not_available);
        progressBar = findViewById(R.id.progress_bar);
        listMovie = new ArrayList<>();
        adapter = new FavoriteAdapter(this);

        HandlerThread handlerThread = new HandlerThread("Data Observer");
        handlerThread.start();
        Handler handler = new Handler(handlerThread.getLooper());
        DataObserver dataObserver = new DataObserver(handler, this);
        getContentResolver().registerContentObserver(DbHelper.CONTENT_URI, true, dataObserver);

        if (savedInstanceState == null) {
            new LoadMovieAsync(this, this).execute();
        } else {
            progressBar.setVisibility(View.GONE);
            listMovie = savedInstanceState.getParcelableArrayList(EXTRA_INSTANCE_STATE);
            if (listMovie != null) {
                adapter.setListMovie(listMovie);
            }
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putParcelableArrayList(EXTRA_INSTANCE_STATE, adapter.getListMovie());
        super.onSaveInstanceState(outState);
    }

    @Override
    public void preExecute() {
        progressBar.setVisibility(View.VISIBLE);
        textViewNotAvailable.setVisibility(View.GONE);
    }

    @Override
    public void postExecute(Cursor cursor) {
        listMovie = MappingHelper.mapCursorMovieToArrayList(cursor);
        if (listMovie != null) {
            if (listMovie.size() > 0) {
                adapter.setListMovie(listMovie);
                textViewNotAvailable.setVisibility(View.GONE);
                progressBar.setVisibility(View.GONE);
                recyclerView.setLayoutManager(new LinearLayoutManager(this));
                recyclerView.setAdapter(adapter);
                for (int i = 0; i < adapter.getListMovie().size(); i++) {
                    Log.d("BAHASA",adapter.getListMovie().get(i).getImageMovie());
                }
            } else {
                textViewNotAvailable.setVisibility(View.VISIBLE);
                progressBar.setVisibility(View.GONE);
            }
        } else {
            textViewNotAvailable.setVisibility(View.VISIBLE);
            progressBar.setVisibility(View.GONE);
        }
    }

    private static class LoadMovieAsync extends AsyncTask<Void, Void, Cursor> {

        private final WeakReference<Context> weakContext;
        private final WeakReference<LoadMovieCallback> weakCallback;

        private LoadMovieAsync(Context context, LoadMovieCallback callback) {
            weakContext = new WeakReference<>(context);
            weakCallback = new WeakReference<>(callback);
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            weakCallback.get().preExecute();
        }

        @Override
        protected Cursor doInBackground(Void... voids) {
            Context context = weakContext.get();
            return context.getContentResolver().query(DbHelper.CONTENT_URI, null, null, null, null);
        }

        @Override
        protected void onPostExecute(Cursor cursor) {
            super.onPostExecute(cursor);
            weakCallback.get().postExecute(cursor);
        }
    }

    public static class DataObserver extends ContentObserver {
        final Context context;

        DataObserver(Handler handler, Context context) {
            super(handler);
            this.context = context;
        }

        @Override
        public void onChange(boolean selfChange) {
            super.onChange(selfChange);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.option_menu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.navigation_settings){
            Intent intent = new Intent(Settings.ACTION_LOCALE_SETTINGS);
            startActivity(intent);
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}

interface LoadMovieCallback {
    void preExecute();
    void postExecute(Cursor cursor);
}