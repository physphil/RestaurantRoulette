package com.physphil.android.restaurantroulette;


import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.physphil.android.restaurantroulette.data.DatabaseHelper;
import com.physphil.android.restaurantroulette.models.Restaurant;
import com.physphil.android.restaurantroulette.ui.RestaurantListAdapter;

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

        // ArrayAdapter adapter = new ArrayAdapter(getActivity(), android.R.layout.simple_list_item_1, names);
        RestaurantListAdapter adapter = new RestaurantListAdapter(getActivity(), mRestaurants);
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

    /**
     * Prompt user to confirm deletion of all restaurants saved in database
     */
    private void confirmDeleteAllRestaurants(){

        new AlertDialog.Builder(getActivity())
                .setTitle(R.string.dialog_delete_all_restaurants_title)
                .setMessage(R.string.dialog_delete_all_restaurants_message)
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener(){

                    @Override
                    public void onClick(DialogInterface dialog, int which){

                        deleteAllRestaurants();
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

    /**
     * Delete all restaurants in database
     */
    private void deleteAllRestaurants(){

        mDatabaseHelper.deleteAllRestaurants();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater){
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.restaurant_list, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){

        switch(item.getItemId()){

            case R.id.menu_add_restaurant:
                viewRestaurantDetail(null);
                return true;

            case R.id.menu_delete_all_restaurants:
                confirmDeleteAllRestaurants();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

}
