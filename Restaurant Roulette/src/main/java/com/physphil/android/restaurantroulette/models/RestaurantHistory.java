package com.physphil.android.restaurantroulette.models;

import java.util.Date;

/**
 * Created by pshadlyn on 2/24/14.
 */
public class RestaurantHistory extends Restaurant{

    private int id;
    private Date date;

    /**
     * Create new RestaurantHistory object
     * @param id id from database
     * @param date selection date from db, in ms
     * @param restaurant Restaurant object that was selected
     */
    public RestaurantHistory(int id, String date, Restaurant restaurant){

        this.id = id;
        this.date = new Date(Long.parseLong(date));
        setRestaurantId(restaurant.getRestaurantId());
        setName(restaurant.getName());
        setGenre(restaurant.getGenre());
        setUserRating(restaurant.getUserRating());
    }

    /**
     * Create new RestaurantHistory object
     * @param id id from database
     * @param date selection date from db, in ms
     */
    public RestaurantHistory(int id, String restaurantId, String date){

        this.id = id;
        setRestaurantId(restaurantId);
        this.date = new Date(Long.parseLong(date));
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

//    public String getRestaurantId() {
//        return restaurantId;
//    }
//
//    public void setRestaurantId(String restaurantId) {
//        this.restaurantId = restaurantId;
//    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

//    public String getName() {
//        return name;
//    }
//
//    public void setName(String name) {
//        this.name = name;
//    }
}
