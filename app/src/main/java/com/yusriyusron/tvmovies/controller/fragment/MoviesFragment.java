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
import com.yusriyusron.tvmovies.controller.adapter.MoviesAdapter;
import com.yusriyusron.tvmovies.model.Movie;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class MoviesFragment extends Fragment {

    private RecyclerView recyclerView;
    private ArrayList<Movie> listMovie;
    private ArrayList<Movie> listSearchMovies;
    private ProgressBar progressBar;

    private MoviesAdapter adapter = null;

    private RequestQueue requestQueue;

    public static String KEY_FRAGMENT = "fragment";

    public MoviesFragment() {
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

        listMovie = new ArrayList<>();

        progressBar = view.findViewById(R.id.progress_bar);
        TextView textViewNotAvailable = view.findViewById(R.id.text_view_not_available);
        textViewNotAvailable.setVisibility(View.GONE);

        //Get Movies
        getMovies();

        //Search
        searchFilter(editTextSearch);
    }

    private void getMovies(){
        String language = "";
        if (((AppCompatActivity)getActivity()).getSupportActionBar() != null){
            if (((AppCompatActivity)getActivity()).getSupportActionBar().getTitle().equals("TV Movies")){
                language = "en-US";
            }else if (((AppCompatActivity)getActivity()).getSupportActionBar().getTitle().equals("TV dan Film")){
                language = "id-ID";
            }
        }

        String url = "https://api.themoviedb.org/3/discover/movie?api_key=c1c287ec318c5d212f5d101e39ed3220&language="+language;

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONArray jsonArray = response.getJSONArray("results");

                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject results = jsonArray.getJSONObject(i);

                        String id = results.getString("id");
                        String title = results.getString("title");
                        String overview = results.getString("overview");
                        String image = results.getString("poster_path");

                        Movie movie = new Movie(id,"https://image.tmdb.org/t/p/w185"+image,title,overview);
                        listMovie.add(movie);
                    }

                    progressBar.setVisibility(View.GONE);

                    adapter = new MoviesAdapter(getContext(),listMovie);
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
                    getMovies();
                }else {
                    searchMovies(charSequence.toString());
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

    private void searchMovies(final String movieName){
        listSearchMovies = new ArrayList<>();
        String language = "";
        if (((AppCompatActivity)getActivity()).getSupportActionBar() != null){
            if (((AppCompatActivity)getActivity()).getSupportActionBar().getTitle().equals("TV Movies")){
                language = "en-US";
            }else if (((AppCompatActivity)getActivity()).getSupportActionBar().getTitle().equals("TV dan Film")){
                language = "id-ID";
            }
        }

        String url = "https://api.themoviedb.org/3/search/movie?api_key=c1c287ec318c5d212f5d101e39ed3220&language="+language+"&query="+movieName;

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONArray jsonArray = response.getJSONArray("results");

                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject results = jsonArray.getJSONObject(i);

                        String id = results.getString("id");
                        String title = results.getString("title");
                        String overview = results.getString("overview");
                        String image = results.getString("poster_path");

                        Movie movie = new Movie(id,"https://image.tmdb.org/t/p/w185"+image,title,overview);
                        listSearchMovies.add(movie);
                    }

                    progressBar.setVisibility(View.GONE);

                    adapter = new MoviesAdapter(getContext(),listSearchMovies);
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
