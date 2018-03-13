package movies.popular.nanodegree.android.popularmovies;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.preference.PreferenceManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import movies.popular.nanodegree.android.popularmovies.model.Movie;
import movies.popular.nanodegree.android.popularmovies.utilities.MovieJsonUtils;
import movies.popular.nanodegree.android.popularmovies.utilities.NetworkUtils;

public class MainActivity extends AppCompatActivity implements
        SharedPreferences.OnSharedPreferenceChangeListener,
        MovieAdapter.MovieAdapterOnClickListener {

    private MovieAdapter mMovieAdapter;
    private RecyclerView mRecyclerView;

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
        sharedPreferences.registerOnSharedPreferenceChangeListener(this);

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
        new FetchMovieData().execute();
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        if(key.equals(getString(R.string.pref_sort_order_key))) {
            loadMovieData();
        }
    }

    @Override
    public void onClick(Movie movie) {
        Intent intent = new Intent(getBaseContext(), DetailActivity.class)
         .putExtra("movies.popular.nanodegree.android.popularmovies.model.Movie", movie);
        startActivity(intent);
    }

    public class FetchMovieData extends AsyncTask<String, Void, List<Movie>> {
        private String activityTitle;

        @Override
        protected List<Movie> doInBackground(String... params) {
            URL movieRequestUrl;
            SharedPreferences sharedPreferences =
                    PreferenceManager.getDefaultSharedPreferences(getBaseContext());
            String sortOrder =
                    sharedPreferences.getString(getString(R.string.pref_sort_order_key), getString(R.string.pref_sort_order_default));

            List<Movie> movieList = null;

            movieRequestUrl = NetworkUtils.buildUrl(sortOrder);

            if(sortOrder.equals(getString(R.string.pref_sort_order_popular_value))) {
                activityTitle = "Popular Movies";
            } else {
                activityTitle = "Top Rated Movies";
            }

            try {
                String jsonMovieResponse = NetworkUtils
                        .getResponseFromHttpUrl(movieRequestUrl);

                movieList = MovieJsonUtils.getSimpleMovieStringsFromJson(jsonMovieResponse);
            } catch(Exception e) {
                e.printStackTrace();
            }

            return movieList;
        }

        @Override
        protected void onPostExecute(List<Movie> movieData) {
            if (movieData != null) {
                mMovieAdapter.setMovieData(movieData);
                getSupportActionBar().setTitle(activityTitle);
            } else {
                Toast.makeText(MainActivity.this,
                        "Something went wrong, please check your internet connection and try again!",
                        Toast.LENGTH_SHORT).show();
            }
        }
    }
}
