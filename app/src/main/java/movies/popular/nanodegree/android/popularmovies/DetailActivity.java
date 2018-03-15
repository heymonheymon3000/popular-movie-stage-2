package movies.popular.nanodegree.android.popularmovies;

import android.content.ContentValues;
import android.content.Intent;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import movies.popular.nanodegree.android.popularmovies.model.FavoriteMovieContract;
import movies.popular.nanodegree.android.popularmovies.model.FavoriteMovieDbHelper;
import movies.popular.nanodegree.android.popularmovies.model.Movie;

public class DetailActivity extends AppCompatActivity {
    private TextView title;
    private ImageView thumbnail;
    private TextView overview;
    private TextView voteAverage;
    private TextView releaseDate;
    private TextView runtime;
    private SQLiteDatabase mDb;
    private String id;
    private String postPath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        FavoriteMovieDbHelper dbHelper = new FavoriteMovieDbHelper(this);
        mDb = dbHelper.getWritableDatabase();

        Intent intent = getIntent();
        if(null != intent) {
            if(intent.hasExtra("movies.popular.nanodegree.android.popularmovies.model.Movie")) {
                Movie movie = intent.getExtras().getParcelable("movies.popular.nanodegree.android.popularmovies.model.Movie");

                id = movie.getId();

                title = findViewById(R.id.detail_movie_title);
                title.setText(movie.getTitle());

                postPath = movie.getThumbnail();
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
        // TODO: Before adding movie make sure it does not already exist in the favorite db.
        addNewFavoriteMovie();
    }

    private void addNewFavoriteMovie() {
        if(mDb == null){
            return;
        }

        ContentValues cv = new ContentValues();
        cv.put(FavoriteMovieContract.FavoriteMovieEntry.COLUMN_ID, id);
        cv.put(FavoriteMovieContract.FavoriteMovieEntry.COLUMN_TITLE, title.getText().toString());
        cv.put(FavoriteMovieContract.FavoriteMovieEntry.COLUMN_THUMBNAIL, postPath);
        cv.put(FavoriteMovieContract.FavoriteMovieEntry.COLUMN_OVERVIEW, overview.getText().toString());
        cv.put(FavoriteMovieContract.FavoriteMovieEntry.COLUMN_VOTE_AVERAGE, voteAverage.getText().toString());
        cv.put(FavoriteMovieContract.FavoriteMovieEntry.COLUMN_RELEASE_DATE, releaseDate.getText().toString());
        cv.put(FavoriteMovieContract.FavoriteMovieEntry.COLUMN_RUNTIME, runtime.getText().toString());

        try {
            mDb.beginTransaction();
            mDb.insert(FavoriteMovieContract.FavoriteMovieEntry.TABLE_NAME, null, cv);
            mDb.setTransactionSuccessful();
        }
        catch (SQLException e) {
            //too bad :(
        }
        finally {
            mDb.endTransaction();
            Toast.makeText(DetailActivity.this,
                    "Added " + title.getText().toString() + " as one of your favorite movies.",
                    Toast.LENGTH_SHORT).show();

        }
    }
}