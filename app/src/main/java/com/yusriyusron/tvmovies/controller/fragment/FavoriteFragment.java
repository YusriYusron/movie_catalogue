package com.yusriyusron.tvmovies.controller.fragment;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.yusriyusron.tvmovies.R;
import com.yusriyusron.tvmovies.controller.adapter.FavoriteAdapter;
import com.yusriyusron.tvmovies.model.Movie;
import com.yusriyusron.tvmovies.model.database.DbHelper;

import java.util.ArrayList;
import java.util.HashMap;

import static com.yusriyusron.tvmovies.view.DetailActivity.TAG_ID;
import static com.yusriyusron.tvmovies.view.DetailActivity.TAG_OVERVIEW;
import static com.yusriyusron.tvmovies.view.DetailActivity.TAG_POSTER;
import static com.yusriyusron.tvmovies.view.DetailActivity.TAG_TITLE;

/**
 * A simple {@link Fragment} subclass.
 */
public class FavoriteFragment extends Fragment {

    private RecyclerView recyclerView;
    ProgressBar progressBar;
    private ArrayList<Movie> listMovie;
    private TextView textViewNotAvailable;

    private DbHelper helper;

    public static String KEY_FRAGMENT = "fragment";

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
        recyclerView = view.findViewById(R.id.recycler_view);

        progressBar = view.findViewById(R.id.progress_bar);
        textViewNotAvailable = view.findViewById(R.id.text_view_not_available);
        textViewNotAvailable.setVisibility(View.GONE);

        helper = new DbHelper(getContext());
        listMovie = new ArrayList<>();

        if (savedInstanceState == null) {
            getAllDatas();
        } else {
            if (getFragmentManager() != null) {
                getFragmentManager().getFragment(savedInstanceState, KEY_FRAGMENT);
            }
        }
    }

    private void getAllDatas() {
        ArrayList<HashMap<String, String>> row = helper.getAllData();

        if (row.size() <= 0) {
            progressBar.setVisibility(View.GONE);
            textViewNotAvailable.setVisibility(View.VISIBLE);
        } else {
            for (int i = 0; i < row.size(); i++) {
                String id = row.get(i).get(TAG_ID);
                String poster = row.get(i).get(TAG_POSTER);
                String title = row.get(i).get(TAG_TITLE);
                String overview = row.get(i).get(TAG_OVERVIEW);

                Movie data = new Movie(id, poster, title, overview);
                listMovie.add(data);
            }
            progressBar.setVisibility(View.GONE);
            FavoriteAdapter adapter = new FavoriteAdapter(getContext(), listMovie);
            adapter.notifyDataSetChanged();
            recyclerView.setAdapter(adapter);

            RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
            recyclerView.setLayoutManager(layoutManager);
        }

    }
}
