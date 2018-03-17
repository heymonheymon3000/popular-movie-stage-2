package movies.popular.nanodegree.android.popularmovies.model;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

public class FavoriteContentProvider extends ContentProvider {

    private FavoriteMovieDbHelper mFavoriteMovieDbHelper;
    private final static int FAVORITE_MOVIES = 100;
    private final static int FAVORITE_MOVIE_WITH_ID = 101;
    private static final UriMatcher sUriMatcher =buildUriMatcher();

    @Override
    public boolean onCreate() {
        Context context = getContext();
        mFavoriteMovieDbHelper = new FavoriteMovieDbHelper(context);
        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection,
                        @Nullable String selection, @Nullable String[] selectionArgs,
                        @Nullable String sortOrder) {
        final SQLiteDatabase db = mFavoriteMovieDbHelper.getReadableDatabase();
        int match = sUriMatcher.match(uri);
        Cursor returnCursor;

        switch (match) {
            case FAVORITE_MOVIES:
                returnCursor = db.query(
                        FavoriteMovieContract.FavoriteMovieEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder);
                break;
            case FAVORITE_MOVIE_WITH_ID:
                String id = uri.getPathSegments().get(1);
                String mSelection = FavoriteMovieContract.FavoriteMovieEntry.COLUMN_ID + " = ?";
                String[] mSelectionArgs = new String[]{ id };

                returnCursor = db.query(
                        FavoriteMovieContract.FavoriteMovieEntry.TABLE_NAME,
                        projection,
                        mSelection,
                        mSelectionArgs,
                        null,
                        null,
                        sortOrder);
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri " + uri);
        }

        returnCursor.setNotificationUri(getContext().getContentResolver(), uri);

        return returnCursor;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
        final SQLiteDatabase db = mFavoriteMovieDbHelper.getWritableDatabase();
        int match = sUriMatcher.match(uri);
        Uri returnUri;
        switch (match) {
            case FAVORITE_MOVIES:
                long id = db.insert(FavoriteMovieContract.FavoriteMovieEntry.TABLE_NAME,
                        null, values);
                if(id > 0) {
                    returnUri =
                            ContentUris.withAppendedId(
                                    FavoriteMovieContract.FavoriteMovieEntry.CONTENT_URI, id);
                } else {
                    throw new android.database.SQLException("Failed to insert row into " + uri);
                }
                break;
            default:
                throw new UnsupportedOperationException("Unknown uir " + uri);
        }

        getContext().getContentResolver().notifyChange(uri, null);

        return returnUri;
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection,
                      @Nullable String[] selectionArgs) {
        final SQLiteDatabase db = mFavoriteMovieDbHelper.getWritableDatabase();
        int match = sUriMatcher.match(uri);
        int returnId;
        switch (match) {
            case FAVORITE_MOVIE_WITH_ID:
                String id = uri.getPathSegments().get(1);
                String mSelection = FavoriteMovieContract.FavoriteMovieEntry.COLUMN_ID + " = ?";
                String[] mSelectionArgs = new String[]{ id };
                returnId = db.delete(FavoriteMovieContract.FavoriteMovieEntry.TABLE_NAME,
                        mSelection, mSelectionArgs);
                break;
            default:
                throw new UnsupportedOperationException("Unknown uir " + uri);
        }
        return returnId;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values,
                      @Nullable String selection, @Nullable String[] selectionArgs) {
        return 0;
    }

    public static UriMatcher buildUriMatcher(){
        UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI(FavoriteMovieContract.AUTHORITY,
                FavoriteMovieContract.PATH_FAVORITE_MOVIES, FAVORITE_MOVIES);
        uriMatcher.addURI(FavoriteMovieContract.AUTHORITY,
                FavoriteMovieContract.PATH_FAVORITE_MOVIES+"/#", FAVORITE_MOVIE_WITH_ID);

        return uriMatcher;
    }
}
