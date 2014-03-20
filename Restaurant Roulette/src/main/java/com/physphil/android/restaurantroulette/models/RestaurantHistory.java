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
