package com.physphil.android.restaurantroulette.models;

/**
 * Created by pshadlyn on 2/24/14.
 */
public class Restaurant {

    private int id;
    private String name;
    private int genre;
    private int userRating;

    public Restaurant(){}

    public Restaurant(String name, int genre, int userRating){

        this.name = name;
        this.genre = genre;
        this.userRating = userRating;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getGenre() {
        return genre;
    }

    public void setGenre(int genre) {
        this.genre = genre;
    }

    public int getUserRating() {
        return userRating;
    }

    public void setUserRating(int userRating) {
        this.userRating = userRating;
    }
}
