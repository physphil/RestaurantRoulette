package com.physphil.android.restaurantroulette.models;

import java.util.Date;

/**
 * Created by pshadlyn on 2/24/14.
 */
public class RestaurantHistory {

    private int id;
    private int restaurantId;
    private Date date;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getRestaurantId() {
        return restaurantId;
    }

    public void setRestaurantId(int restaurantId) {
        this.restaurantId = restaurantId;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
}
