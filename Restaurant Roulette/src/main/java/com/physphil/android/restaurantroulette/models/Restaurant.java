package com.physphil.android.restaurantroulette.models;

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
    private String notes;

    public Restaurant(){

        this.id = UUID.randomUUID().toString();
    }

    public Restaurant(String name, String genre, int userRating, String notes){

        this.id = UUID.randomUUID().toString();
        this.name = name;
        this.genre = genre;
        this.userRating = userRating;
        this.notes = notes;
    }

    public Restaurant(String name, String genre, int userRating){

        this.id = UUID.randomUUID().toString();
        this.name = name;
        this.genre = genre;
        this.userRating = userRating;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
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

    public String getNotes(){
        return notes;
    }

    public void setNotes(String notes){
        this.notes = notes;
    }
}
