package movies.popular.nanodegree.android.popularmovies;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;

import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.preference.PreferenceManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import movies.popular.nanodegree.android.popularmovies.model.FavoriteMovieContract;
import movies.popular.nanodegree.android.popularmovies.model.Movie;
import movies.popular.nanodegree.android.popularmovies.model.PopularMoviePreferences;
import movies.popular.nanodegree.android.popularmovies.utilities.MovieJsonUtils;
import movies.popular.nanodegree.android.popularmovies.utilities.NetworkUtils;

public class MainActivity extends AppCompatActivity implements
        SharedPreferences.OnSharedPreferenceChangeListener,
        MovieAdapter.MovieAdapterOnClickListener, LoaderManager.LoaderCallbacks<List<Movie>> {
    private MovieAdapter mMovieAdapter;
    private RecyclerView mRecyclerView;
    private ItemTouchHelper itemTouchHelper;
    private String activityTitle;

    private final static int POPULAR_MOVIES_LOADER = 22;

    private final static String TAG = MainActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mMovieAdapter = new MovieAdapter(this, this);

        GridAutofitLayoutManager layoutManager =
                new GridAutofitLayoutManager(this, 342);

        mRecyclerView = findViewById(R.id.rv_movies);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setAdapter(mMovieAdapter);
        mRecyclerView.setLayoutManager(layoutManager);

        SharedPreferences sharedPreferences =
                PreferenceManager.getDefaultSharedPreferences(this);
        String sortOrder =
                PopularMoviePreferences.getSortOrder(this);
        sharedPreferences.registerOnSharedPreferenceChangeListener(this);

        itemTouchHelper = new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT |
                ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(RecyclerView recyclerView,
                                  RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                String id = (String) viewHolder.itemView.getTag();
                Uri uri = FavoriteMovieContract.FavoriteMovieEntry.CONTENT_URI;
                uri = uri.buildUpon().appendPath(id).build();
                getContentResolver().delete(uri, null, null);
                loadMovieData();
            }
        });

        if(sortOrder.equals(getString(R.string.pref_sort_order_favorites_value))) {
            itemTouchHelper.attachToRecyclerView(mRecyclerView);
        }

        if(savedInstanceState == null || !savedInstanceState.containsKey("movies")) {
          loadMovieData();
        } else {
            ArrayList<Movie> movieArrayList = savedInstanceState.getParcelableArrayList("movies");
            mMovieAdapter.setMovieData(movieArrayList);
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putParcelableArrayList("movies", (ArrayList<? extends Parcelable>) mMovieAdapter.getMovieData());
        super.onSaveInstanceState(outState);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.sort_order_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if(id == R.id.action_settings) {
            Intent intent = new Intent(this, SettingsActivity.class);
            startActivity(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onDestroy() {
        SharedPreferences sharedPreferences =
                PreferenceManager.getDefaultSharedPreferences(this);
        sharedPreferences.unregisterOnSharedPreferenceChangeListener(this);
        super.onDestroy();
    }

    private void loadMovieData() {
        LoaderManager loaderManager = getSupportLoaderManager();
        Loader<List<Movie>> movieLoader = loaderManager.getLoader(POPULAR_MOVIES_LOADER);
        if(movieLoader == null) {
            loaderManager.initLoader(POPULAR_MOVIES_LOADER, null, MainActivity.this);
        } else {
            loaderManager.restartLoader(POPULAR_MOVIES_LOADER,null, MainActivity.this );
        }
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        if(key.equals(getString(R.string.pref_sort_order_key))) {
            String sortOrder =
                    sharedPreferences.getString(getString(R.string.pref_sort_order_key),
                            getString(R.string.pref_sort_order_default));

            if(sortOrder.equals(getString(R.string.pref_sort_order_favorites_value))) {
                itemTouchHelper.attachToRecyclerView(mRecyclerView);
            } else {
                itemTouchHelper.attachToRecyclerView(null);
            }

            loadMovieData();
        }
    }

    @Override
    public void onClick(Movie movie) {
        Intent intent = new Intent(getBaseContext(), DetailActivity.class)
                .putExtra("movies.popular.nanodegree.android.popularmovies.model.Movie", movie);
        startActivity(intent);
    }

    public Cursor getAllFavoriteMovies() {
        try {
            return getContentResolver().query(
                    FavoriteMovieContract.FavoriteMovieEntry.CONTENT_URI,
                    null,
                    null,
                    null,
                    null);
        } catch(Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @SuppressLint("StaticFieldLeak")
    @NonNull
    @Override
    public Loader<List<Movie>> onCreateLoader(int id, @Nullable final Bundle args) {
        return new AsyncTaskLoader<List<Movie>>(this) {
            List<Movie> movieList = null;

            @Override
            protected void onStartLoading() {
                if(movieList != null) {
                    deliverResult(movieList);
                } else {
                    forceLoad();
                }
            }

            public void deliverResult(List<Movie>  data) {
                movieList = data;
                super.deliverResult(data);
            }

            @Nullable
            @Override
            public List<Movie> loadInBackground() {
                URL movieRequestUrl;

                String sortOrder =
                        PopularMoviePreferences.getSortOrder(getBaseContext());

                if(sortOrder.equals(getString(R.string.pref_sort_order_popular_value))) {
                    activityTitle = "Popular Movies";
                    movieRequestUrl = NetworkUtils.buildPopularMovieUrl();
                    movieList = getMovieListFromUrl(movieRequestUrl);
                } else if(sortOrder.equals(getString(R.string.pref_sort_order_top_rated_value))) {
                    activityTitle = "Top Rated Movies";
                    movieRequestUrl = NetworkUtils.buildTopRatedMovieUrl();
                    movieList = getMovieListFromUrl(movieRequestUrl);
                } else {
                    activityTitle = "Favorite Movies";
                    Cursor movieCursor = getAllFavoriteMovies();
                    movieList = getMovieListFromCursor(movieCursor);
                }

                return movieList;
            }

            private List<Movie> getMovieListFromCursor(Cursor movieCursor) {
                List<Movie> movieList = new ArrayList<>();

                for(int i = 0; i < movieCursor.getCount(); i++) {
                    movieCursor.moveToPosition(i);
                    Movie movie = new Movie(
                            movieCursor.getString(
                                    movieCursor.getColumnIndex(
                                            FavoriteMovieContract.FavoriteMovieEntry.COLUMN_ID)),
                            movieCursor.getString(
                                    movieCursor.getColumnIndex(
                                            FavoriteMovieContract.FavoriteMovieEntry.COLUMN_TITLE)),
                            movieCursor.getString(
                                    movieCursor.getColumnIndex(
                                            FavoriteMovieContract.FavoriteMovieEntry.COLUMN_THUMBNAIL)),
                            movieCursor.getString(
                                    movieCursor.getColumnIndex(
                                            FavoriteMovieContract.FavoriteMovieEntry.COLUMN_OVERVIEW)),
                            movieCursor.getDouble(
                                    movieCursor.getColumnIndex(
                                            FavoriteMovieContract.FavoriteMovieEntry.COLUMN_VOTE_AVERAGE)),
                            movieCursor.getString(
                                    movieCursor.getColumnIndex(
                                            FavoriteMovieContract.FavoriteMovieEntry.COLUMN_RELEASE_DATE)),
                            movieCursor.getString(
                                    movieCursor.getColumnIndex(
                                            FavoriteMovieContract.FavoriteMovieEntry.COLUMN_RUNTIME)));
                    movieList.add(movie);
                }
                return movieList;
            }

            private List<Movie> getMovieListFromUrl(URL movieRequestUrl) {
                List<Movie> movieList = new ArrayList<>();
                try {
                    String jsonMovieResponse = NetworkUtils
                            .getResponseFromHttpUrl(movieRequestUrl);

                    movieList = MovieJsonUtils.getSimpleMovieStringsFromJson(jsonMovieResponse);
                } catch(Exception e) {
                    e.printStackTrace();
                }

                return movieList;
            }
        };
    }

    @Override
    public void onLoadFinished(@NonNull Loader<List<Movie>> loader, List<Movie> data) {
        if (data != null) {
            mMovieAdapter.setMovieData(data);
            getSupportActionBar().setTitle(activityTitle);
        } else {
            Toast.makeText(MainActivity.this,
                    "Something went wrong, please check your internet connection and try again!",
                    Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onLoaderReset(@NonNull Loader<List<Movie>> loader) {}
}