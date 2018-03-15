package movies.popular.nanodegree.android.popularmovies.model;

import android.provider.BaseColumns;

public class FavoriteMovieContract {

    private FavoriteMovieContract(){}

    public static final class FavoriteMovieEntry implements BaseColumns {
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
