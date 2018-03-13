package movies.popular.nanodegree.android.popularmovies.utilities;

import android.net.Uri;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import movies.popular.nanodegree.android.popularmovies.model.Movie;

public final class MovieJsonUtils {
    private static final String MOVIE_RESULTS = "results";
    private static final String POSTER_PATH = "poster_path";
    private static final String TITLE = "title";
    private static final String VOTE_AVERAGE = "vote_average";
    private static final String RELEASE_DATE = "release_date";
    private static final String OVERVIEW = "overview";
    private static final String BASE_IMAGE_URL = "http://image.tmdb.org/t/p/";
    private static final String IMAGE_SIZE_W185 = "w342";

    private final static Uri BASE_IMAGE_URI = Uri.parse(BASE_IMAGE_URL).buildUpon()
            .appendPath(IMAGE_SIZE_W185).build();

    public static List<Movie> getSimpleMovieStringsFromJson(String movieJsonStr)
            throws JSONException {

        JSONObject movieJson = new JSONObject(movieJsonStr);
        JSONArray jsonMovieArray = movieJson.getJSONArray(MOVIE_RESULTS);
        List<Movie> movieValues = new ArrayList<>();

        for (int i = 0; i < jsonMovieArray.length(); i++) {
            JSONObject movieObject = jsonMovieArray.getJSONObject(i);
            String title = movieObject.getString(TITLE);
            String overview = movieObject.getString(OVERVIEW);
            String poster_path = movieObject.getString(POSTER_PATH);
            Uri imageURI = BASE_IMAGE_URI.buildUpon()
                    .appendEncodedPath(poster_path).build();
            double voteAverage = movieObject.getDouble(VOTE_AVERAGE);
            String releaseDate = movieObject.getString(RELEASE_DATE);

            Movie movie = new Movie(
                    title,
                    imageURI.toString(),
                    overview,
                    voteAverage,
                    releaseDate);

            movieValues.add(movie);
        }

        return movieValues;
    }
}