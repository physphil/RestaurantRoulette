package com.physphil.android.restaurantroulette.ui;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.Image;
import android.support.v4.content.LocalBroadcastManager;
import android.text.Layout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.physphil.android.restaurantroulette.R;
import com.physphil.android.restaurantroulette.RestaurantFragment;
import com.physphil.android.restaurantroulette.models.Restaurant;

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
        ImageButton btnDelete = (ImageButton) convertView.findViewById(R.id.restaurant_delete_button);

        tvName.setText(restaurant.getName());
        tvGenre.setText(restaurant.getGenre());

        btnDelete.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Log.v("PS", "Delete restaurant " + restaurant.getName());
                new AlertDialog.Builder(mContext)
                        .setTitle("Delete item?")
                        .setMessage("Delete restaurant " + restaurant.getName() + "?")
                        .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                // Send broadcast to hosting fragment to delete restaurant from database
                                Intent i = new Intent(RestaurantFragment.ACTION_DELETE_RESTAURANT);
                                i.putExtra(RestaurantFragment.EXTRA_RESTAURANT_ID, restaurant.getId());
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
