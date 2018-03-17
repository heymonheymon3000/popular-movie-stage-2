package movies.popular.nanodegree.android.popularmovies;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.preference.PreferenceManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import movies.popular.nanodegree.android.popularmovies.model.FavoriteMovieContract;
import movies.popular.nanodegree.android.popularmovies.model.Movie;
import movies.popular.nanodegree.android.popularmovies.model.Video;
import movies.popular.nanodegree.android.popularmovies.utilities.MovieJsonUtils;
import movies.popular.nanodegree.android.popularmovies.utilities.NetworkUtils;

public class DetailActivity extends AppCompatActivity {
    private TextView title;
    private TextView overview;
    private TextView voteAverage;
    private TextView releaseDate;
    private TextView runtime;
    private Button mFavoriteButton;
    private String id;
    private String postPath;
    private TrailerAdapter mTrailerAdapter;
    private String sortOrder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        mTrailerAdapter = new TrailerAdapter(this);
        LinearLayoutManager layoutManager =
                new LinearLayoutManager(this,
                        LinearLayoutManager.VERTICAL, false);
        RecyclerView mRecyclerView = findViewById(R.id.rv_trailers);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setAdapter(mTrailerAdapter);
        mRecyclerView.setLayoutManager(layoutManager);

        SharedPreferences sharedPreferences =
                PreferenceManager.getDefaultSharedPreferences(getBaseContext());
        sortOrder =
                sharedPreferences.getString(getString(R.string.pref_sort_order_key),
                        getString(R.string.pref_sort_order_default));

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

                mFavoriteButton = findViewById(R.id.detail_movie_mark_as_favorite);
                if(sortOrder.equals(getString(R.string.pref_sort_order_popular_value)) ||
                    sortOrder.equals(getString(R.string.pref_sort_order_top_rated_value))) {
                    mFavoriteButton.setText(R.string.action_favorite_movie);
                } else {
                    mFavoriteButton.setText(R.string.action_unfavorite_movie);
                }
            }
        }

        loadTrailers();
    }

    private void loadTrailers() {
        new FetchTrailers().execute();
    }

    public void onClickAddFavoriteMovie(View view) {
        if(sortOrder.equals(getString(R.string.pref_sort_order_popular_value)) ||
                sortOrder.equals(getString(R.string.pref_sort_order_top_rated_value))) {
            if(!isFavoriteStoredAlready(id)) {
                addNewFavoriteMovie();
            } else {
                Toast.makeText(DetailActivity.this,
                        title.getText().toString() + " is already store as one of your favorite movies",
                        Toast.LENGTH_SHORT).show();
            }
        } else {
            removeNewFavoriteMovie();
            // TODO: Need to trigger load movies again.


            finish();
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

    private void removeNewFavoriteMovie() {
        Uri uri = FavoriteMovieContract.FavoriteMovieEntry.CONTENT_URI;
        uri = uri.buildUpon().appendPath(id).build();
        getContentResolver().delete(uri, null, null);
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
}