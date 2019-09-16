package com.alonso.popularmovies;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.alonso.popularmovies.adapters.MovieAdapter;
import com.alonso.popularmovies.model.Movie;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.alonso.popularmovies.utils.NetworkUtils;
import com.squareup.okhttp.Response;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    public static final String TAG = MainActivity.class.getSimpleName();
    private Response response;
    private NetworkUtils mNetworkUtils;
    private Gson mGson;
    private GridLayoutManager gridLayoutManager;
    private Context mContext;
    private RecyclerView mRecyclerView;
    private MovieAdapter movieAdapter;
    private ArrayList<Movie> movieListFromBackground;
    private Toolbar myToolbar;
    private String topRated = "Top rated movies";
    private String popular = "Popular movies";
    private String favorites = "Favorites movies";
    private Activity activity;
    private TextView mErrorMessage;
    private TextView mErrorIntenetMessage;
    private ProgressBar mProgressBar;
    private FrameLayout mFrameLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mErrorMessage = findViewById(R.id.tv_error_message_display);
        mErrorIntenetMessage = findViewById(R.id.tv_error_internet_message_display);
        mProgressBar = findViewById(R.id.pb_loading_indicator);
        mFrameLayout = findViewById(R.id.frame_layout);

        myToolbar = findViewById(R.id.my_toolbar);
        myToolbar.setTitle(topRated);
        setSupportActionBar(myToolbar);

        mContext = getApplicationContext();
        mNetworkUtils = new NetworkUtils();
        mGson = new Gson();
        movieListFromBackground = new ArrayList<>();
        activity = MainActivity.this;

        String typeSort = "top_rated";
        gridLayoutManager = new GridLayoutManager(mContext, 2, 1, false);

        mRecyclerView = findViewById(R.id.popular_movies_gv);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(gridLayoutManager);

        if (savedInstanceState == null) {
            loadData(typeSort);
        }

    }

    public boolean isOnline() {
        ConnectivityManager cm =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }

    public void loadData(String param) {
        new RetrieveMoviesAsync().execute(param);
    }

    class RetrieveMoviesAsync extends AsyncTask<String, Void, ArrayList<Movie>> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mRecyclerView.setVisibility(View.GONE);
            mFrameLayout.setVisibility(View.VISIBLE);
            mProgressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected ArrayList<Movie> doInBackground(String... strings) {


            String url = NetworkUtils.buildUrl(strings[0]);
            Log.d(TAG, url);

            if (isOnline()) {

                response = mNetworkUtils.getResponseFromUrl(url);
                Log.d(TAG + " Result 1:", response.toString());

                if (response.isSuccessful()) {
                    try {

                        String jsonData = response.body().string();
                        JSONObject mObject = new JSONObject(jsonData);

                        JSONArray movies = mObject.getJSONArray("results");
                        Log.d(TAG + " Result 2:", movies.toString());

                        Type listType = new TypeToken<List<Movie>>() {
                        }.getType();
                        movieListFromBackground = mGson.fromJson(movies.toString(), listType);

                        return movieListFromBackground;

                    } catch (IOException e) {
                        Log.d(TAG + " Failure 1", e.getMessage());
                        return null;
                    } catch (JSONException e) {
                        Log.d(TAG + " Failure 2", e.getMessage());
                        return null;
                    }
                } else {
                    showErrorMessage();
                }
            } else {
                showErrorInternetConnectionMessage();
            }
            return null;
        }

        @Override
        protected void onPostExecute(ArrayList<Movie> movies) {
            super.onPostExecute(movies);
            mProgressBar.setVisibility(View.INVISIBLE);
            if (movieListFromBackground.size() != 0) {
                showMoviesData();
                movieAdapter = new MovieAdapter(mContext, movies, activity);
                mRecyclerView.setAdapter(movieAdapter);
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_top_rated) {
            myToolbar.setTitle(topRated);
            loadData("top_rated");

            return true;
        } else if (id == R.id.action_popular) {
            myToolbar.setTitle(popular);
            loadData("popular");

            return true;
        } else if (id == R.id.action_favorites) {
            myToolbar.setTitle(favorites);
            //TODO cambiar parametro por favorito 
            loadData("popular");

            return true;
        } else {
            if (myToolbar.getTitle() == topRated) {
                loadData("top_rated");
            } else if (myToolbar.getTitle() == popular) {
                loadData("popular");
            } else {
                loadData("top_rated");
            }
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putParcelableArrayList("Movies", movieListFromBackground);
        outState.putString("Title", myToolbar.getTitle().toString());
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        if (savedInstanceState != null) {

            movieListFromBackground = savedInstanceState.getParcelableArrayList("Movies");
            myToolbar.setTitle(savedInstanceState.getString("Title"));

            movieAdapter = new MovieAdapter(mContext, movieListFromBackground, activity);
            mRecyclerView.setAdapter(movieAdapter);
        }
    }

    private void showMoviesData() {
        mFrameLayout.setVisibility(View.INVISIBLE);
        mErrorMessage.setVisibility(View.INVISIBLE);
        mErrorIntenetMessage.setVisibility(View.INVISIBLE);
        mRecyclerView.setVisibility(View.VISIBLE);
    }

    private void showErrorMessage() {
        mFrameLayout.setVisibility(View.VISIBLE);
        mRecyclerView.setVisibility(View.GONE);
        mErrorMessage.setVisibility(View.VISIBLE);
        mErrorIntenetMessage.setVisibility(View.INVISIBLE);

    }

    private void showErrorInternetConnectionMessage() {
        mFrameLayout.setVisibility(View.VISIBLE);
        mRecyclerView.setVisibility(View.GONE);
        mErrorMessage.setVisibility(View.INVISIBLE);
        mErrorIntenetMessage.setVisibility(View.VISIBLE);
    }
}
