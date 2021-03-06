/**
 * Restaurant Roulette for Android
 * Copyright (C) 2014  Phil Shadlyn
 *
 * Restaurant Roulette is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 * @copyright 2014 Phil Shadlyn - physphil@gmail.com
 * @license GNU General Public License - https://www.gnu.org/licenses/gpl.html
 */

package com.physphil.android.restaurantroulette.models;

import android.content.Context;

import com.physphil.android.restaurantroulette.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

/**
 * Created by pshadlyn on 2/24/14.
 */
public class Restaurant {

    public static final int GENRE_ALL = 0;
    public static final String GENRE_NORTH_AMERICAN = "North American";
    public static final String GENRE_BREAKFAST = "Breakfast";
    public static final String GENRE_CHINESE = "Chinese";
    public static final String GENRE_ETHNIC = "Ethnic";
    public static final String GENRE_FAST_FOOD = "Fast Food";
    public static final String GENRE_INDIAN = "Indian";
    public static final String GENRE_ITALIAN = "Italian";
    public static final String GENRE_JAPANESE = "Japanese";
    public static final String GENRE_MEXICAN = "Mexican";
    public static final String GENRE_OTHER = "Other";
    public static final String GENRE_PIZZA = "Pizza";
    public static final String GENRE_PUB = "Pub";
    public static final String GENRE_SEAFOOD = "Seafood";
    public static final String GENRE_SUSHI = "Sushi";
    public static final String GENRE_VEGETARIAN = "Vegetarian";
    
    private String id;
    private String name;
    private String genre;
    private int userRating;
    private int priceLevel;
    private String notes;

    public Restaurant(){

        this.id = UUID.randomUUID().toString();
    }

    public Restaurant(String name, String genre, int userRating, int priceLevel){

        this.id = UUID.randomUUID().toString();
        this.name = name;
        this.genre = genre;
        this.userRating = userRating;
        this.priceLevel = priceLevel;
    }

    public Restaurant(String name, String genre, int userRating, int priceLevel, String notes){

        this.id = UUID.randomUUID().toString();
        this.name = name;
        this.genre = genre;
        this.userRating = userRating;
        this.priceLevel = priceLevel;
        this.notes = notes;
    }

    public String getRestaurantId() {
        return id;
    }

    public void setRestaurantId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public int getUserRating() {
        return userRating;
    }

    public void setUserRating(int userRating) {
        this.userRating = userRating;
    }

    public int getPriceLevel() {
        return priceLevel;
    }

    public void setPriceLevel(int priceLevel) {
        this.priceLevel = priceLevel;
    }

    public String getNotes(){
        return notes;
    }

    public void setNotes(String notes){
        this.notes = notes;
    }

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

    /**
     * Does the restaurant have a valid, non-zero length name entered by the user
     * @return If the restaurant has a name
     */
    public boolean hasName(){

        return hasName(this.name);
    }

    /**
     * Does the restaurant have a valid, non-zero length name entered by the user
     * @param name Restaurant name
     * @return If the restaurant has a name
     */
    public static boolean hasName(String name){

        return((name != null) && (name.length() > 0));
    }
}
