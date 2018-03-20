package movies.popular.nanodegree.android.popularmovies;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import movies.popular.nanodegree.android.popularmovies.model.FavoriteMovieContract;
import movies.popular.nanodegree.android.popularmovies.model.Movie;
import movies.popular.nanodegree.android.popularmovies.model.Review;
import movies.popular.nanodegree.android.popularmovies.model.Video;
import movies.popular.nanodegree.android.popularmovies.utilities.MovieJsonUtils;
import movies.popular.nanodegree.android.popularmovies.utilities.NetworkUtils;

public class DetailActivity extends AppCompatActivity {
    private TextView title;
    private TextView overview;
    private TextView voteAverage;
    private TextView releaseDate;
    private TextView runtime;
    private String id;
    private String postPath;
    private TrailerAdapter mTrailerAdapter;
    private ReviewAdapter mReviewAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        mTrailerAdapter = new TrailerAdapter(this);
        RecyclerView mTrailersRecyclerView = findViewById(R.id.rv_trailers);
        mTrailersRecyclerView.setHasFixedSize(true);
        mTrailersRecyclerView.setAdapter(mTrailerAdapter);
        mTrailersRecyclerView.setLayoutManager(new LinearLayoutManager(this,
                LinearLayoutManager.VERTICAL, false));

        mReviewAdapter = new ReviewAdapter(this);
        RecyclerView mReviewsRecyclerView = findViewById(R.id.rv_reviews);
        mReviewsRecyclerView.setHasFixedSize(true);
        mReviewsRecyclerView.setAdapter(mReviewAdapter);
        mReviewsRecyclerView.setLayoutManager(new LinearLayoutManager(this,
                LinearLayoutManager.VERTICAL, false));

        Intent intent = getIntent();
        if(null != intent) {
            if(intent.hasExtra("movies.popular.nanodegree.android.popularmovies.model.Movie")) {
                Movie movie = intent.getExtras().getParcelable("movies.popular.nanodegree.android.popularmovies.model.Movie");

                id = movie.getId();

                title = findViewById(R.id.detail_movie_title);
                title.setText(movie.getTitle());

                postPath = movie.getThumbnail();
                ImageView thumbnail = findViewById(R.id.detail_movie_thumbnail);
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

        loadTrailers();
        loadReviews();

    }

    private void loadReviews() { new FetchReviews().execute(); }
    private void loadTrailers() {
        new FetchTrailers().execute();
    }

    public void onClickAddFavoriteMovie(View view) {
        if(!isFavoriteStoredAlready(id)) {
            addNewFavoriteMovie();
        } else {
            Toast.makeText(DetailActivity.this,
                    title.getText().toString() + " is already store as one of your favorite movies",
                    Toast.LENGTH_SHORT).show();
        }
    }

    private boolean isFavoriteStoredAlready(String id) {
        Uri uri = FavoriteMovieContract.FavoriteMovieEntry.CONTENT_URI;
        uri = uri.buildUpon().appendPath(id).build();

        Cursor cursor = getContentResolver().query(
                uri,
                null,
                null,
                null,
                null);

        return ( cursor.getCount() > 0 ) ;
    }

    private void addNewFavoriteMovie() {
        ContentValues cv = new ContentValues();
        cv.put(FavoriteMovieContract.FavoriteMovieEntry.COLUMN_ID, id);
        cv.put(FavoriteMovieContract.FavoriteMovieEntry.COLUMN_TITLE, title.getText().toString());
        cv.put(FavoriteMovieContract.FavoriteMovieEntry.COLUMN_THUMBNAIL, postPath);
        cv.put(FavoriteMovieContract.FavoriteMovieEntry.COLUMN_OVERVIEW,
                overview.getText().toString());
        cv.put(FavoriteMovieContract.FavoriteMovieEntry.COLUMN_VOTE_AVERAGE,
                voteAverage.getText().toString());
        cv.put(FavoriteMovieContract.FavoriteMovieEntry.COLUMN_RELEASE_DATE,
                releaseDate.getText().toString());
        cv.put(FavoriteMovieContract.FavoriteMovieEntry.COLUMN_RUNTIME,
                runtime.getText().toString());

        Uri uri =
                getContentResolver().insert(FavoriteMovieContract.FavoriteMovieEntry.CONTENT_URI, cv);
        if(uri != null) {
            Toast.makeText(DetailActivity.this,
                    "Added " + title.getText().toString() + " as one of your favorite movies.\n"+
                    uri.toString(),
                    Toast.LENGTH_SHORT).show();
        }
    }

    @SuppressLint("StaticFieldLeak")
    public class FetchTrailers extends AsyncTask<Void, Void, List<Video>> {
        @Override
        protected List<Video> doInBackground(Void... voids) {
            URL trailerRequestUrl =
                    NetworkUtils.buildMovieVideosByMovieId(id);
            return getVideoListFromUrl(trailerRequestUrl);
        }

        private List<Video> getVideoListFromUrl(URL trailerRequestUrl) {
            List<Video> videoList = new ArrayList<>();
            try {
                String jsonVideoResponse = NetworkUtils
                        .getResponseFromHttpUrl(trailerRequestUrl);

                videoList = MovieJsonUtils.getMovieVideoFromJson(jsonVideoResponse);
            } catch(Exception e) {
                e.printStackTrace();
            }

            return videoList;
        }

        @Override
        protected void onPostExecute(List<Video> videos) {
            if(videos != null) {
                mTrailerAdapter.setVideoData(videos);
            } else {
                Toast.makeText(DetailActivity.this,
                "Something went wrong, please check your internet connection and try again!",
                Toast.LENGTH_SHORT).show();
            }
        }
    }

    @SuppressLint("StaticFieldLeak")
    public class FetchReviews extends AsyncTask<Void, Void, List<Review>> {
        @Override
        protected List<Review> doInBackground(Void... voids) {
            URL reviewRequestUrl =
                    NetworkUtils.buildMovieReviewsByMovieId(id);
            return getReviewListFromUrl(reviewRequestUrl);
        }

        private List<Review> getReviewListFromUrl(URL reviewRequestUrl) {
            List<Review> reviewList = new ArrayList<>();
            try {
                String jsonReviewResponse = NetworkUtils
                        .getResponseFromHttpUrl(reviewRequestUrl);

                reviewList = MovieJsonUtils.getMovieReviewFromJson(jsonReviewResponse);
            } catch(Exception e) {
                e.printStackTrace();
            }

            return reviewList;
        }

        @Override
        protected void onPostExecute(List<Review> reviews) {
            if(reviews != null) {
                mReviewAdapter.setReviewData(reviews);
            } else {
                Toast.makeText(DetailActivity.this,
                        "Something went wrong, please check your internet connection and try again!",
                        Toast.LENGTH_SHORT).show();
            }
        }
    }
}