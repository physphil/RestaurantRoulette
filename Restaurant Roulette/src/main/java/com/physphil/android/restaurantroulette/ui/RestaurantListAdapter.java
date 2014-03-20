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

package com.physphil.android.restaurantroulette.ui;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.support.v4.content.LocalBroadcastManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.RatingBar;
import android.widget.TextView;

import com.physphil.android.restaurantroulette.R;
import com.physphil.android.restaurantroulette.RestaurantFragment;
import com.physphil.android.restaurantroulette.models.Restaurant;
import com.physphil.android.restaurantroulette.util.Constants;

import java.util.List;

/**
 * Created by pshadlyn on 2/25/14.
 */
public class RestaurantListAdapter extends BaseAdapter {

    private Context mContext;
    private List<Restaurant> mRestaurants;

    public RestaurantListAdapter(Context context, List<Restaurant> restaurants){

        mContext = context;
        mRestaurants = restaurants;
    }

    @Override
    public int getCount() {
        return mRestaurants.size();
    }

    @Override
    public Object getItem(int position) {
        return mRestaurants.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        final Restaurant restaurant = mRestaurants.get(position);

        if(convertView == null){
            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.row_restaurant_list, parent, false);
        }

        TextView tvName = (TextView) convertView.findViewById(R.id.restaurant_name);
        TextView tvGenre = (TextView) convertView.findViewById(R.id.restaurant_genre);
        RatingBar rbRating = (RatingBar) convertView.findViewById(R.id.restaurant_rating);
        ImageButton btnDelete = (ImageButton) convertView.findViewById(R.id.restaurant_delete_button);
        Typeface defaultFont = Typeface.createFromAsset(mContext.getAssets(), Constants.FONT_DEFAULT);

        tvName.setText(restaurant.getName());
        tvName.setTypeface(defaultFont);
        tvGenre.setText(restaurant.getGenre());
        tvGenre.setTypeface(defaultFont);
        rbRating.setRating(restaurant.getUserRating());

        btnDelete.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

//                new AlertDialog.Builder(mContext)
                new CustomFontDialogBuilder(mContext)
                        .setTitle(R.string.dialog_delete_restaurant_title)
                        .setMessage(R.string.dialog_delete_restaurant_message)
                        .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                // Send broadcast to hosting fragment to delete restaurant from database
                                Intent i = new Intent(RestaurantFragment.ACTION_DELETE_RESTAURANT);
                                i.putExtra(RestaurantFragment.EXTRA_RESTAURANT_ID, restaurant.getRestaurantId());
                                LocalBroadcastManager.getInstance(mContext).sendBroadcast(i);
                            }
                        })
                        .setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                // Do nothing
                            }
                        })
                        .show();
            }
        });

        return convertView;
    }
}
