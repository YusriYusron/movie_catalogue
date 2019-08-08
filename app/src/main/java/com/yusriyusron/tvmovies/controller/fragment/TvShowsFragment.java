package com.yusriyusron.tvmovies.controller.fragment;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.yusriyusron.tvmovies.R;
import com.yusriyusron.tvmovies.controller.adapter.TvShowAdapter;
import com.yusriyusron.tvmovies.model.TvShow;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class TvShowsFragment extends Fragment{

    private RecyclerView recyclerView;
    private ArrayList<TvShow> listTvShow;
    private ProgressBar progressBar;
    private TextView textViewNotAvailable;

    public static String KEY_FRAGMENT = "fragment";

    public TvShowsFragment() {
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

        listTvShow = new ArrayList<>();
        getTvShows();

        progressBar = view.findViewById(R.id.progress_bar);
        textViewNotAvailable = view.findViewById(R.id.text_view_not_available);
        textViewNotAvailable.setVisibility(View.GONE);

        if (savedInstanceState == null){
            getTvShows();
        }else {
            if (getFragmentManager() != null){
                getFragmentManager().getFragment(savedInstanceState,KEY_FRAGMENT);
            }
        }
    }

    private void getTvShows(){
        String language = "";
        if (((AppCompatActivity)getActivity()).getSupportActionBar() != null){
            if (((AppCompatActivity)getActivity()).getSupportActionBar().getTitle().equals("TV Movies")){
                language = "en-US";
            }else if (((AppCompatActivity)getActivity()).getSupportActionBar().getTitle().equals("TV dan Film")){
                language = "id-ID";
            }
        }
        String url = "https://api.themoviedb.org/3/discover/tv?api_key=c1c287ec318c5d212f5d101e39ed3220&language="+language;

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONArray jsonArray = response.getJSONArray("results");

                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject results = jsonArray.getJSONObject(i);

                        String id = results.getString("id");
                        String title = results.getString("name");
                        String overview = results.getString("overview");
                        String image = results.getString("poster_path");

                        TvShow tvShow = new TvShow(id,"https://image.tmdb.org/t/p/w185"+image,title,overview);
                        listTvShow.add(tvShow);
                    }

                    progressBar.setVisibility(View.GONE);

                    TvShowAdapter adapter = new TvShowAdapter(getContext(),listTvShow);
                    recyclerView.setAdapter(adapter);

                    RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
                    recyclerView.setLayoutManager(layoutManager);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });

        if (getContext() != null){
            RequestQueue requestQueue = Volley.newRequestQueue(getContext());
            requestQueue.add(jsonObjectRequest);
        }
    }

}
