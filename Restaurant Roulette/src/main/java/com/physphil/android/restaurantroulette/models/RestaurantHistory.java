package com.physphil.android.restaurantroulette.models;

import java.util.Date;

/**
 * Created by pshadlyn on 2/24/14.
 */
public class RestaurantHistory {

    private int id;
    private String restaurantId;
    private Date date;

    /**
     * Create new RestaurantHistory object
     * @param id id from database
     * @param restaurantId restaurant id from database
     * @param date selection date from db, in ms
     */
    public RestaurantHistory(int id, String restaurantId, String date){

        this.id = id;
        this.restaurantId = restaurantId;
        this.date = new Date(Long.parseLong(date));
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getRestaurantId() {
        return restaurantId;
    }

    public void setRestaurantId(String restaurantId) {
        this.restaurantId = restaurantId;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
}
