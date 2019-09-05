package com.alonso.popularmovies.adapters;

import android.app.Activity;
import android.content.Context;

import android.content.Intent;
import android.support.v7.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;


import com.alonso.popularmovies.DetailActivity;
import com.alonso.popularmovies.R;
import com.alonso.popularmovies.model.Movie;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by aaton on 3/13/2018.
 */

public class MovieAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<Movie> movieArray;
    private Context mContext;
    private Activity activity;

    public MovieAdapter(Context context, List<Movie> dataset, Activity activity)
    {
        movieArray = dataset;
        mContext = context;
        this.activity = activity;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.movie_list_item, parent, false);
        MovieHolder movieHolder = new MovieHolder(v);
        return  movieHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        if(holder instanceof MovieHolder){
            final MovieHolder movieHolder = (MovieHolder)holder;

            Picasso.with(mContext).load("http://image.tmdb.org/t/p/w342//"+movieArray.get(position).getPoster_path()).into(movieHolder.posterIV);

            movieHolder.posterIV.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent i = new Intent(mContext, DetailActivity.class);
                    Movie movie = movieArray.get(position);
                    i.putExtra("movie", movie);
                    activity.startActivity(i);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return movieArray.size();
    }

    public class MovieHolder extends RecyclerView.ViewHolder {

    public ImageView posterIV;

        public MovieHolder(View itemView) {
            super(itemView);

            posterIV = itemView.findViewById(R.id.movie_image_iv);
        }
    }
}
