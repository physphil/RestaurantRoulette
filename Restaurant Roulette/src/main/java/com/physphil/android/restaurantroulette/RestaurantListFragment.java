package com.physphil.android.restaurantroulette;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

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
    private List<Restaurant> mRestaurants;

    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

        mDatabaseHelper = DatabaseHelper.getInstance(getActivity());
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState){
        super.onActivityCreated(savedInstanceState);

        mRestaurants = mDatabaseHelper.getAllRestaurants();
        ArrayList<String> names = new ArrayList<String>();

        for(Restaurant r : mRestaurants){

            names.add(r.getName());
        }

        ArrayAdapter adapter = new ArrayAdapter(getActivity(), android.R.layout.simple_list_item_1, names);
        setListAdapter(adapter);
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id){

        String restaurantId = mRestaurants.get(position).getId();
        viewRestaurantDetail(restaurantId);
    }

    /**
     * Start activity to view restaurant information
     * @param id database id of restaurant to view, or null if a new restaurant
     */
    private void viewRestaurantDetail(String id){

        Intent i = new Intent(getActivity(), RestaurantActivity.class);

        if(id != null){
            i.putExtra(RestaurantFragment.EXTRA_RESTAURANT_ID, id);
        }

        startActivity(i);

    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater){
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_restaurant_list, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){

        switch(item.getItemId()){

            case R.id.menu_add_restaurant:
                viewRestaurantDetail(null);

                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

}
