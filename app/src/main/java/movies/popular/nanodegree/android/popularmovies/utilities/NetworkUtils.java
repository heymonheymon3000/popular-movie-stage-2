package movies.popular.nanodegree.android.popularmovies.utilities;

import android.net.Uri;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;
import movies.popular.nanodegree.android.popularmovies.BuildConfig;
import movies.popular.nanodegree.android.popularmovies.R;

public final class NetworkUtils {
    private static final String TAG = NetworkUtils.class.getSimpleName();

    private static final String MOVIE_POPULAR_BASE_URL =
            "http://api.themoviedb.org/3/movie/popular";
    private static final String MOVIE_TOP_RATED_BASE_URL =
            "http://api.themoviedb.org/3/movie/top_rated";
    private static final String API_KEY_PARAM = "api_key";

    public static URL buildUrl(String sortOrder) {
        Uri builtUri;
        URL url = null;

        if(sortOrder.equals(R.string.pref_sort_order_top_rated_value)) {
            builtUri = Uri.parse(MOVIE_TOP_RATED_BASE_URL).buildUpon()
                    .appendQueryParameter(API_KEY_PARAM, BuildConfig.MOVIE_DB_API_KEY)
                    .build();
        } else {
            builtUri = Uri.parse(MOVIE_POPULAR_BASE_URL).buildUpon()
                    .appendQueryParameter(API_KEY_PARAM, BuildConfig.MOVIE_DB_API_KEY)
                    .build();
        }

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
