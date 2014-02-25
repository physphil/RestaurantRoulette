package com.physphil.android.restaurantroulette;


import android.app.AlertDialog;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.support.v4.content.LocalBroadcastManager;
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
    private RestaurantListAdapter mAdapter;

    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

        mDatabaseHelper = DatabaseHelper.getInstance(getActivity());
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState){
        super.onActivityCreated(savedInstanceState);
        setEmptyText(getString(R.string.empty_listview_restaurants));

        mRestaurants = mDatabaseHelper.getAllRestaurants();
        mAdapter = new RestaurantListAdapter(getActivity(), mRestaurants);
        setListAdapter(mAdapter);
    }

    @Override
    public void onResume(){
        super.onResume();

        LocalBroadcastManager.getInstance(getActivity()).registerReceiver(mReceiver,
                new IntentFilter(RestaurantFragment.ACTION_DELETE_RESTAURANT));
    }

    @Override
    public void onPause(){
        super.onPause();

        LocalBroadcastManager.getInstance(getActivity()).unregisterReceiver(mReceiver);
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

        // delete from db
        mDatabaseHelper.deleteAllRestaurants();

        // clear adapter
        mRestaurants.clear();
        mAdapter.notifyDataSetChanged();
    }

    /**
     * Delete restaurant from database and adapter
     * @param id id of restaurant to delete
     */
    private void deleteRestaurant(String id){

        // delete from db
        mDatabaseHelper.deleteRestaurantById(id);

        // find in adapter, delete and refresh
        mRestaurants.remove(getIndex(id));
        mAdapter.notifyDataSetChanged();
    }

    /**
     * Get index of Restaurant object according to id
     * @param id id of restaurant to find
     * @return index of restaurant in mRestaurants, -1 if not found
     */
    private int getIndex(String id){

        for(int i = 0; i < mRestaurants.size(); i++){

            if(mRestaurants.get(i).getId().equals(id)){

                return i;
            }
        }

        return -1;
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

    /**
     * Receiver to catch all broadcasts for this fragment
     */
    private BroadcastReceiver mReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {

            if(intent.getAction().equals(RestaurantFragment.ACTION_DELETE_RESTAURANT)){

                String id = intent.getStringExtra(RestaurantFragment.EXTRA_RESTAURANT_ID);

                if(id != null){

                    deleteRestaurant(id);
                }
            }
        }
    };

}
