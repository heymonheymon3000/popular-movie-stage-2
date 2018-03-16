package movies.popular.nanodegree.android.popularmovies.model;

import android.net.Uri;
import android.provider.BaseColumns;

public class FavoriteMovieContract {

    public static final String AUTHORITY  = "movies.popular.nanodegree.android.popularmovies";
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://"+AUTHORITY);
    public static final String PATH_FAVORITE_MOVIES = "favorite_movies";

    private FavoriteMovieContract(){}

    public static final class FavoriteMovieEntry implements BaseColumns {
        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_FAVORITE_MOVIES).build();

        public static final String TABLE_NAME = "favorite_movie";
        public static final String COLUMN_ID = "movieId";
        public static final String COLUMN_TITLE = "title";
        public static final String COLUMN_THUMBNAIL = "thumbnail";
        public static final String COLUMN_OVERVIEW = "overview";
        public static final String COLUMN_VOTE_AVERAGE = "voteAverage";
        public static final String COLUMN_RELEASE_DATE = "releaseDate";
        public static final String COLUMN_RUNTIME = "runtime";
        public static final String COLUMN_TIMESTAMP = "timestamp";
    }
}
