package com.physphil.android.restaurantroulette.models;

import java.util.UUID;

/**
 * Created by pshadlyn on 2/24/14.
 */
public class Restaurant {

    private String id;
    private String name;
    private int genre;
    private int userRating;
    private String notes;

    public Restaurant(){

        this.id = UUID.randomUUID().toString();
    }

    public Restaurant(String name, int genre, int userRating, String notes){

        this.id = UUID.randomUUID().toString();
        this.name = name;
        this.genre = genre;
        this.userRating = userRating;
        this.notes = notes;
    }

    public Restaurant(String name, int genre, int userRating){

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

    public String getNotes(){
        return notes;
    }

    public void setNotes(String notes){
        this.notes = notes;
    }
}
