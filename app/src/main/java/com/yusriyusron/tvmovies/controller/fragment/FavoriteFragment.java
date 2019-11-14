package com.yusriyusron.tvmovies.controller.fragment;


import android.content.Context;
import android.database.ContentObserver;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.yusriyusron.tvmovies.R;
import com.yusriyusron.tvmovies.controller.adapter.FavoriteAdapter;
import com.yusriyusron.tvmovies.model.Movie;
import com.yusriyusron.tvmovies.model.database.DbHelper;
import com.yusriyusron.tvmovies.utils.MappingHelper;

import java.lang.ref.WeakReference;
import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class FavoriteFragment extends Fragment implements LoadMovieCallback{

    private RecyclerView recyclerView;
    ProgressBar progressBar;
    private ArrayList<Movie> listMovie;
    private TextView textViewNotAvailable;

    private DbHelper helper;

    private FavoriteAdapter adapter;

    public static String KEY_FRAGMENT = "fragment";

    private static final String EXTRA_STATE = "EXTRA_STATE";

    public FavoriteFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_movies_tv, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        EditText editTextSearch = view.findViewById(R.id.edit_text_search);
        editTextSearch.setVisibility(View.GONE);
        recyclerView = view.findViewById(R.id.recycler_view);

        progressBar = view.findViewById(R.id.progress_bar);
        textViewNotAvailable = view.findViewById(R.id.text_view_not_available);
        textViewNotAvailable.setVisibility(View.GONE);

        helper = new DbHelper(getContext());
        listMovie = new ArrayList<>();
        adapter = new FavoriteAdapter(getActivity());

        //Get Favorite Data
//        getAllDatas();

        HandlerThread handlerThread = new HandlerThread("Data Observer");
        handlerThread.start();
        Handler handler = new Handler(handlerThread.getLooper());
        DataObserver dataObserver = new DataObserver(handler, getContext());
        getActivity().getContentResolver()
                .registerContentObserver(DbHelper.CONTENT_URI, true, dataObserver);

        if (savedInstanceState == null) {
            new LoadMovieAsync(getContext(), this).execute();
        } else {
            progressBar.setVisibility(View.INVISIBLE);
            ArrayList<Movie> list = savedInstanceState.getParcelableArrayList(EXTRA_STATE);
            if (list != null) {
                adapter.setListMovie(list);
            }
        }
    }

//    private void getAllDatas() {
//        ArrayList<HashMap<String, String>> row = helper.getAllData();
//
//        if (row.size() <= 0) {
//            progressBar.setVisibility(View.GONE);
//            textViewNotAvailable.setVisibility(View.VISIBLE);
//        } else {
//            for (int i = 0; i < row.size(); i++) {
//                String id = row.get(i).get(TAG_ID);
//                String poster = row.get(i).get(TAG_POSTER);
//                String title = row.get(i).get(TAG_TITLE);
//                String overview = row.get(i).get(TAG_OVERVIEW);
//
//                Movie data = new Movie(id, poster, title, overview);
//                listMovie.add(data);
//            }
//            progressBar.setVisibility(View.GONE);
//            FavoriteAdapter adapter = new FavoriteAdapter(getContext(), listMovie);
//            adapter.notifyDataSetChanged();
//            recyclerView.setAdapter(adapter);
//
//            RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
//            recyclerView.setLayoutManager(layoutManager);
//        }
//    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        if (getFragmentManager() != null){
            getFragmentManager().getFragment(outState,KEY_FRAGMENT);
        }
        super.onSaveInstanceState(outState);
    }

    @Override
    public void preExecute() {
        progressBar.setVisibility(View.VISIBLE);
        textViewNotAvailable.setVisibility(View.GONE);
    }

    @Override
    public void postExecute(Cursor cursor) {
        progressBar.setVisibility(View.GONE);

        ArrayList<Movie> listMovie = MappingHelper.mapCursorMovieToArrayList(cursor);
        if (listMovie != null) {
            if (listMovie.size() > 0) {
                adapter.setListMovie(listMovie);
                textViewNotAvailable.setVisibility(View.GONE);
                progressBar.setVisibility(View.GONE);
                recyclerView.setAdapter(adapter);
                recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
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
}

interface LoadMovieCallback {
    void preExecute();
    void postExecute(Cursor cursor);
}
