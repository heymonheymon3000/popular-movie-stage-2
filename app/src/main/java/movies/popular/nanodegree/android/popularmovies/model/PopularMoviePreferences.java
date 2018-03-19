package movies.popular.nanodegree.android.popularmovies.model;


import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.preference.PreferenceManager;

import movies.popular.nanodegree.android.popularmovies.R;

public final class PopularMoviePreferences {
    public static String getSortOrder(Context context) {
        SharedPreferences sharedPreferences =
                PreferenceManager.getDefaultSharedPreferences(context);
        return sharedPreferences.getString(context.getString(R.string.pref_sort_order_key),
                        context.getString(R.string.pref_sort_order_default));
    }
}
