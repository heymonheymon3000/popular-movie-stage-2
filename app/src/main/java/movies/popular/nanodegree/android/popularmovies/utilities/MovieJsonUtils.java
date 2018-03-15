package movies.popular.nanodegree.android.popularmovies.utilities;

import android.net.Uri;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import movies.popular.nanodegree.android.popularmovies.model.Movie;

public final class MovieJsonUtils {
    private static final String MOVIE_RESULTS = "results";
    private static final String POSTER_PATH = "poster_path";
    private static final String ID = "id";
    private static final String TITLE = "title";
    private static final String VOTE_AVERAGE = "vote_average";
    private static final String RELEASE_DATE = "release_date";
    private static final String OVERVIEW = "overview";
    private static final String BASE_IMAGE_URL = "http://image.tmdb.org/t/p/";
    private static final String IMAGE_SIZE_W342 = "w342";
    private static final String RUNTIME = "runtime";

    private final static Uri BASE_IMAGE_URI = Uri.parse(BASE_IMAGE_URL).buildUpon()
            .appendPath(IMAGE_SIZE_W342).build();

    public static List<Movie> getSimpleMovieStringsFromJson(String movieJsonStr)
            throws JSONException {

        JSONObject movieJson = new JSONObject(movieJsonStr);
        JSONArray jsonMovieArray = movieJson.getJSONArray(MOVIE_RESULTS);
        List<Movie> movieValues = new ArrayList<>();

        for (int i = 0; i < jsonMovieArray.length(); i++) {
            JSONObject movieObject = jsonMovieArray.getJSONObject(i);
            String id = String.valueOf(movieObject.getInt(ID));
            String title = movieObject.getString(TITLE);
            String overview = movieObject.getString(OVERVIEW);
            String poster_path = movieObject.getString(POSTER_PATH);
            Uri imageURI = BASE_IMAGE_URI.buildUpon()
                    .appendEncodedPath(poster_path).build();
            double voteAverage = movieObject.getDouble(VOTE_AVERAGE);
            String releaseDate = movieObject.getString(RELEASE_DATE);
            String runtime = getMovieRuntimeById(id);

            Movie movie = new Movie(
                    id,
                    title,
                    imageURI.toString(),
                    overview,
                    voteAverage,
                    releaseDate,
                    runtime);

            movieValues.add(movie);
        }

        return movieValues;
    }

    public static String getMovieRuntimeFromJson(String movieRuntimeJsonStr)
            throws JSONException {
        JSONObject movieRuntimeJson = new JSONObject(movieRuntimeJsonStr);
        String runtime = movieRuntimeJson.getString(RUNTIME);
        return runtime;
    }

    private static String getMovieRuntimeById(String id) {
        String runtime = null;
        URL runtimeUrl = NetworkUtils.buildRuntimeUrlById(id);

        try {
            String jsonMovieRuntimeResponse =
                    NetworkUtils.getResponseFromHttpUrl(runtimeUrl);
            runtime = getMovieRuntimeFromJson(jsonMovieRuntimeResponse);
        } catch(Exception e) {
            e.printStackTrace();
        }

        return runtime;
    }
}