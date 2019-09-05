package com.alonso.popularmovies;


import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.alonso.popularmovies.model.Movie;
import com.squareup.picasso.Picasso;

public class DetailActivity extends AppCompatActivity {

    private Movie movie;
    private Toolbar myToolbar;
    private ImageView posterImageView;
    private TextView originalTitleTV, releaseDateTV, overviewTV;
    private RatingBar mRatingBar;
    private double rating = 0.0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        myToolbar = findViewById(R.id.my_toolbar);
        posterImageView = findViewById(R.id.poster_iv);
        originalTitleTV = findViewById(R.id.originalTitle_tv);
        releaseDateTV = findViewById(R.id.release_date_tv);
        overviewTV = findViewById(R.id.overview_tv);
        mRatingBar = findViewById(R.id.movie_RB);

        Bundle mBundle = getIntent().getExtras();

        if(mBundle != null){
            movie = mBundle.getParcelable("movie");

            Picasso.with(getApplicationContext()).load("http://image.tmdb.org/t/p/w342//"+movie.getPoster_path()).into(posterImageView);
            originalTitleTV.setText(movie.getOriginal_title());
            releaseDateTV.setText(movie.getRelease_date());
            overviewTV.setText(movie.getOverview());

            rating = Math.round((movie.getVote_average()/2));

            mRatingBar.setRating((float)rating);

            myToolbar.setTitle(movie.getTitle());

        }

        setSupportActionBar(myToolbar);
    }
}
