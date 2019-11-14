package com.yusriyusron.tvmovies.controller.fragment;


import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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
public class TvShowsFragment extends Fragment {

    private RecyclerView recyclerView;
    private ArrayList<TvShow> listTvShow;
    private ArrayList<TvShow> listSearchTvShows;
    private ProgressBar progressBar;

    private TvShowAdapter adapter = null;

    private RequestQueue requestQueue;

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
        EditText editTextSearch = view.findViewById(R.id.edit_text_search);
        recyclerView = view.findViewById(R.id.recycler_view);

        listTvShow = new ArrayList<>();

        progressBar = view.findViewById(R.id.progress_bar);
        TextView textViewNotAvailable = view.findViewById(R.id.text_view_not_available);
        textViewNotAvailable.setVisibility(View.GONE);

        //Get TV Show
        getTvShows();

        //Search
        searchFilter(editTextSearch);
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

                    adapter = new TvShowAdapter(getContext(),listTvShow);
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
            requestQueue = Volley.newRequestQueue(getContext());
            requestQueue.add(jsonObjectRequest);
        }
    }

    private void searchFilter(EditText editTextSearch){
        TextWatcher textWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.toString().equals("")){
                    getTvShows();
                }else {
                    searchTvShows(charSequence.toString());
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        };

        editTextSearch.addTextChangedListener(textWatcher);
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        if (getFragmentManager() != null){
            getFragmentManager().getFragment(outState,KEY_FRAGMENT);
        }
        super.onSaveInstanceState(outState);
    }

    private void searchTvShows(String tvShows){
        listSearchTvShows = new ArrayList<>();
        String language = "";
        if (((AppCompatActivity)getActivity()).getSupportActionBar() != null){
            if (((AppCompatActivity)getActivity()).getSupportActionBar().getTitle().equals("TV Movies")){
                language = "en-US";
            }else if (((AppCompatActivity)getActivity()).getSupportActionBar().getTitle().equals("TV dan Film")){
                language = "id-ID";
            }
        }
        String url = "https://api.themoviedb.org/3/search/tv?api_key=c1c287ec318c5d212f5d101e39ed3220&language="+language+"&query="+tvShows;

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
                        listSearchTvShows.add(tvShow);
                    }

                    progressBar.setVisibility(View.GONE);

                    adapter = new TvShowAdapter(getContext(),listSearchTvShows);
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
            requestQueue = Volley.newRequestQueue(getContext());
            requestQueue.add(jsonObjectRequest);
        }
    }


}
