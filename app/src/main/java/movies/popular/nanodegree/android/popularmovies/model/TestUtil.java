package movies.popular.nanodegree.android.popularmovies.model;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import java.util.ArrayList;
import java.util.List;
import android.database.SQLException;
import android.net.Uri;

import movies.popular.nanodegree.android.popularmovies.utilities.MovieJsonUtils;

public class TestUtil {

    public static void insertFakeData(SQLiteDatabase db){
        if(db == null){
            return;
        }
        List<ContentValues> list = new ArrayList<>();

        ContentValues cv = new ContentValues();
        cv.put(FavoriteMovieContract.FavoriteMovieEntry.COLUMN_ID, 269149);
        cv.put(FavoriteMovieContract.FavoriteMovieEntry.COLUMN_TITLE, "Zootopia");
        cv.put(FavoriteMovieContract.FavoriteMovieEntry.COLUMN_THUMBNAIL, buildImageUrl("/sM33SANp9z6rXW8Itn7NnG1GOEs.jpg"));
        cv.put(FavoriteMovieContract.FavoriteMovieEntry.COLUMN_OVERVIEW, "Determined to prove herself, Officer Judy Hopps, the first bunny on Zootopia's police force, jumps at the chance to crack her first case - even if it means partnering with scam-artist fox Nick Wilde to solve the mystery.");
        cv.put(FavoriteMovieContract.FavoriteMovieEntry.COLUMN_VOTE_AVERAGE, "7.7");
        cv.put(FavoriteMovieContract.FavoriteMovieEntry.COLUMN_RELEASE_DATE, "2016-02-11");
        cv.put(FavoriteMovieContract.FavoriteMovieEntry.COLUMN_RUNTIME, "120");
        list.add(cv);

        cv = new ContentValues();
        cv.put(FavoriteMovieContract.FavoriteMovieEntry.COLUMN_ID, 399055);
        cv.put(FavoriteMovieContract.FavoriteMovieEntry.COLUMN_TITLE, "The Shape of Water");
        cv.put(FavoriteMovieContract.FavoriteMovieEntry.COLUMN_THUMBNAIL, buildImageUrl("/k4FwHlMhuRR5BISY2Gm2QZHlH5Q.jpg"));
        cv.put(FavoriteMovieContract.FavoriteMovieEntry.COLUMN_OVERVIEW, "An other-worldly story, set against the backdrop of Cold War era America circa 1962, where a mute janitor working at a lab falls in love with an amphibious man being held captive there and devises a plan to help him escape.");
        cv.put(FavoriteMovieContract.FavoriteMovieEntry.COLUMN_VOTE_AVERAGE, "7.7");
        cv.put(FavoriteMovieContract.FavoriteMovieEntry.COLUMN_RELEASE_DATE, "2016-02-11");
        cv.put(FavoriteMovieContract.FavoriteMovieEntry.COLUMN_RUNTIME, "120");
        list.add(cv);

        cv = new ContentValues();
        cv.put(FavoriteMovieContract.FavoriteMovieEntry.COLUMN_ID, 284054);
        cv.put(FavoriteMovieContract.FavoriteMovieEntry.COLUMN_TITLE, "Black Panther");
        cv.put(FavoriteMovieContract.FavoriteMovieEntry.COLUMN_THUMBNAIL, buildImageUrl("/uxzzxijgPIY7slzFvMotPv8wjKA.jpg"));
        cv.put(FavoriteMovieContract.FavoriteMovieEntry.COLUMN_OVERVIEW, "After the events of Captain America: Civil War, King T'Challa returns home to the reclusive, technologically advanced African nation of Wakanda to serve as his country's new leader. However, T'Challa soon finds that he is challenged for the throne from factions within his own country. When two foes conspire to destroy Wakanda, the hero known as Black Panther must team up with C.I.A. agent Everett K. Ross and members of the Dora Milaje, Wakandan special forces, to prevent Wakanda from being dragged into a world war.");
        cv.put(FavoriteMovieContract.FavoriteMovieEntry.COLUMN_VOTE_AVERAGE, "7.7");
        cv.put(FavoriteMovieContract.FavoriteMovieEntry.COLUMN_RELEASE_DATE, "2016-02-11");
        cv.put(FavoriteMovieContract.FavoriteMovieEntry.COLUMN_RUNTIME, "120");
        list.add(cv);

        try {
            db.beginTransaction();
            db.delete (FavoriteMovieContract.FavoriteMovieEntry.TABLE_NAME,null,null);
//            for(ContentValues c:list){
//                db.insert(FavoriteMovieContract.FavoriteMovieEntry.TABLE_NAME, null, c);
//            }
            db.setTransactionSuccessful();
        }
        catch (SQLException e) {
            //too bad :(
        }
        finally {
            db.endTransaction();
        }
    }

    private static String buildImageUrl(String poster_path) {
        Uri imageURI = MovieJsonUtils.BASE_IMAGE_URI.buildUpon()
                .appendEncodedPath(poster_path).build();
        return imageURI.toString();
    }
}