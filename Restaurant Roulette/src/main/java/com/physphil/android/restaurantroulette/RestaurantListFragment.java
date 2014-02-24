package com.physphil.android.restaurantroulette;


import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import com.physphil.android.restaurantroulette.data.DatabaseHelper;
import com.physphil.android.restaurantroulette.models.Restaurant;

import java.util.ArrayList;
import java.util.List;

/**
 * Show list of restaurants stored in user database
 * Created by pshadlyn on 2/24/14.
 */
public class RestaurantListFragment extends ListFragment {

    private DatabaseHelper mDatabaseHelper;

    /**
     * 1. adapter for list is from db
     * 2. set list adapter
     * 3. show list
     */

    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

        mDatabaseHelper = DatabaseHelper.getInstance(getActivity());
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState){
        super.onActivityCreated(savedInstanceState);

        List<Restaurant> restaurants = mDatabaseHelper.getAllRestaurants();
        ArrayList<String> names = new ArrayList<String>();

        for(Restaurant r : restaurants){

            names.add(r.getName());
        }

        ArrayAdapter adapter = new ArrayAdapter(getActivity(), android.R.layout.simple_list_item_1, names);
        setListAdapter(adapter);
    }

}
