package movies.popular.nanodegree.android.popularmovies.utilities;

import android.net.Uri;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;
import movies.popular.nanodegree.android.popularmovies.BuildConfig;

public final class NetworkUtils {
    private static final String MOVIE_POPULAR_BASE_URL =
            "http://api.themoviedb.org/3/movie/popular";
    private static final String MOVIE_TOP_RATED_BASE_URL =
            "http://api.themoviedb.org/3/movie/top_rated";
    private static final String MOVIE_RUNTIME_BASE_URL =
            "http://api.themoviedb.org/3/movie/{id}";
    private static final String MOVIE_VIDEO_BASE_URL =
            "http://api.themoviedb.org/3/movie/{id}/videos";
    private static final String MOVIE_REVIEW_BASE_URL =
            "http://api.themoviedb.org/3/movie/{id}/reviews";
    private static final String API_KEY_PARAM = "api_key";
    private static final String YOUTUBE_BASE_URL =
            "https://www.youtube.com/watch";
    private static final String YOUTUBE_KEY = "v";

    public static Uri buildYouTubeUri(String key) {
        Uri builtUri;

        builtUri = Uri.parse(YOUTUBE_BASE_URL).buildUpon()
                .appendQueryParameter(YOUTUBE_KEY, key)
                .build();

        return builtUri;
    }

    public static URL buildPopularMovieUrl() {
        Uri builtUri;
        URL url = null;

        builtUri = Uri.parse(MOVIE_POPULAR_BASE_URL).buildUpon()
                .appendQueryParameter(API_KEY_PARAM, BuildConfig.MOVIE_DB_API_KEY)
                .build();

        try {
            url = new URL(builtUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        return url;
    }

    public static URL buildTopRatedMovieUrl() {
        Uri builtUri;
        URL url = null;

        builtUri = Uri.parse(MOVIE_TOP_RATED_BASE_URL).buildUpon()
                .appendQueryParameter(API_KEY_PARAM, BuildConfig.MOVIE_DB_API_KEY)
                .build();
        try {
            url = new URL(builtUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        return url;
    }

    public static URL buildRuntimeUrlById(String id) {
        Uri builtUri;
        URL url = null;

        String runtimeUrl = MOVIE_RUNTIME_BASE_URL.replace("{id}", id);
        builtUri = Uri.parse(runtimeUrl).buildUpon()
                .appendQueryParameter(API_KEY_PARAM, BuildConfig.MOVIE_DB_API_KEY)
                .build();

        try {
            url = new URL(builtUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        return url;
    }

    public static URL buildMovieVideosByMovieId(String id) {
        Uri builtUri;
        URL url = null;

        String runtimeUrl = MOVIE_VIDEO_BASE_URL.replace("{id}", id);
        builtUri = Uri.parse(runtimeUrl).buildUpon()
                .appendQueryParameter(API_KEY_PARAM, BuildConfig.MOVIE_DB_API_KEY)
                .build();

        try {
            url = new URL(builtUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        return url;
    }

    public static URL buildMovieReviewsByMovieId(String id) {
        Uri builtUri;
        URL url = null;

        String runtimeUrl = MOVIE_REVIEW_BASE_URL.replace("{id}", id);
        builtUri = Uri.parse(runtimeUrl).buildUpon()
                .appendQueryParameter(API_KEY_PARAM, BuildConfig.MOVIE_DB_API_KEY)
                .build();

        try {
            url = new URL(builtUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        return url;
    }

    public static String getResponseFromHttpUrl(URL url) throws IOException {
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        try {
            InputStream in = urlConnection.getInputStream();

            Scanner scanner = new Scanner(in);
            scanner.useDelimiter("\\A");

            boolean hasInput = scanner.hasNext();
            String response = null;
            if (hasInput) {
                response = scanner.next();
            }
            scanner.close();
            return response;
        } finally {
            urlConnection.disconnect();
        }
    }
}
