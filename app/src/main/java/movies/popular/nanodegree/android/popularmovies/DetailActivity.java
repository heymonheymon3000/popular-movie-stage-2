package movies.popular.nanodegree.android.popularmovies;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import movies.popular.nanodegree.android.popularmovies.model.Movie;

public class DetailActivity extends AppCompatActivity {
    private TextView title;
    private ImageView thumbnail;
    private TextView overview;
    private  TextView voteAverage;
    private TextView releaseDate;
    private TextView runtime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        Intent intent = getIntent();
        if(null != intent) {
            if(intent.hasExtra("movies.popular.nanodegree.android.popularmovies.model.Movie")) {
                Movie movie = intent.getExtras().getParcelable("movies.popular.nanodegree.android.popularmovies.model.Movie");

                title = findViewById(R.id.detail_movie_title);
                title.setText(movie.getTitle());

                thumbnail = findViewById(R.id.detail_movie_thumbnail);
                Picasso.with(getBaseContext()).load(movie.getThumbnail()).into(thumbnail);

                overview = findViewById(R.id.detail_movie_overview);
                overview.setText(movie.getOverview());

                voteAverage = findViewById(R.id.detail_movie_vote_average);
                voteAverage.setText(String.valueOf(movie.getVoteAverage()));

                releaseDate = findViewById(R.id.detail_movie_release_date);
                releaseDate.setText(movie.getReleaseDate());

                runtime = findViewById(R.id.detail_movie_runtime);
                runtime.setText(movie.getRuntime());
            }
        }
    }

    public void onClickMarkAsFavorite(View view) {

    }
}