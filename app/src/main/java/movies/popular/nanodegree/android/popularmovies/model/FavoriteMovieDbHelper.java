package movies.popular.nanodegree.android.popularmovies.model;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class FavoriteMovieDbHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "favorite_movie.db";
    private static final int DATABASE_VERSION = 1;

    public FavoriteMovieDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        final String SQL_CREATE_FAVORITE_MOVIE_TABLE = "CREATE TABLE " + FavoriteMovieContract.FavoriteMovieEntry.TABLE_NAME + " (" +
                FavoriteMovieContract.FavoriteMovieEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                FavoriteMovieContract.FavoriteMovieEntry.COLUMN_ID + " INTEGER NOT NULL," +
                FavoriteMovieContract.FavoriteMovieEntry.COLUMN_TITLE + " TEXT NOT NULL, " +
                FavoriteMovieContract.FavoriteMovieEntry.COLUMN_THUMBNAIL + " TEXT NOT NULL, " +
                FavoriteMovieContract.FavoriteMovieEntry.COLUMN_OVERVIEW + " TEXT NOT NULL, " +
                FavoriteMovieContract.FavoriteMovieEntry.COLUMN_VOTE_AVERAGE + " TEXT NOT NULL, " +
                FavoriteMovieContract.FavoriteMovieEntry.COLUMN_RELEASE_DATE + " TEXT NOT NULL, " +
                FavoriteMovieContract.FavoriteMovieEntry.COLUMN_RUNTIME + " TEXT NOT NULL, " +
                FavoriteMovieContract.FavoriteMovieEntry.COLUMN_TIMESTAMP + " TIMESTAMP DEFAULT CURRENT_TIMESTAMP" +
                "); ";

        sqLiteDatabase.execSQL(SQL_CREATE_FAVORITE_MOVIE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + FavoriteMovieContract.FavoriteMovieEntry.TABLE_NAME);
        onCreate(sqLiteDatabase);
    }
}