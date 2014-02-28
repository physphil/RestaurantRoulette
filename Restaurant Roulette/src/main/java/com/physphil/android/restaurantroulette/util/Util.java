package com.physphil.android.restaurantroulette.util;

import android.content.Context;

import com.physphil.android.restaurantroulette.R;
import com.physphil.android.restaurantroulette.models.Restaurant;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Utility methods
 * Created by pshadlyn on 2/27/14.
 */
public class Util {

    /**
     * Get list of genres used for filtering. Includes all listed genres, plus All Restaurants inserted at position 0.
     * @param context context
     * @return  list of genres to be used for filtering
     */
    public static List<String> getGenresForAdapter(Context context){

        List<String> genres = new ArrayList<String>(Arrays.asList(context.getResources().getStringArray(R.array.genres)));
        genres.add(Restaurant.GENRE_ALL, context.getString(R.string.all_restaurants));

        return genres;
    }

}
