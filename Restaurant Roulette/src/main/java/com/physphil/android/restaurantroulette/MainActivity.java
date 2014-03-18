package com.physphil.android.restaurantroulette;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.view.Menu;
import android.view.MenuItem;

import com.physphil.android.restaurantroulette.data.DatabaseHelper;
import com.physphil.android.restaurantroulette.util.Constants;

public class MainActivity extends BaseActivity implements NavigationDrawerFragment.NavigationDrawerCallbacks {

    /**
     * Fragment managing the behaviors, interactions and presentation of the navigation drawer.
     */
    private NavigationDrawerFragment mNavigationDrawerFragment;

    /**
     * Used to store the last screen title. For use in {@link #restoreActionBar()}.
     */
    private CharSequence mTitle;
    private SharedPreferences prefs;
    private boolean newListFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        prefs = PreferenceManager.getDefaultSharedPreferences(this);
        setContentView(R.layout.activity_main);

        setActionBarFont(Constants.FONT_DEFAULT);

        mNavigationDrawerFragment = (NavigationDrawerFragment) getSupportFragmentManager().findFragmentById(R.id.navigation_drawer);
//        mTitle = getTitle();

        // Set up the drawer.
        mNavigationDrawerFragment.setUp(
                R.id.navigation_drawer,
                (DrawerLayout) findViewById(R.id.drawer_layout));
    }

    @Override
    public void onNavigationDrawerItemSelected(int position) {
        // update the main content by replacing fragments
        FragmentManager fm = getSupportFragmentManager();
        Fragment fragment = fm.findFragmentById(R.id.container);

        switch(position){

            case 0:
                mTitle = getString(R.string.title_restaurant_selector);
                saveMenuSelection(0);

                if(!(fragment instanceof RestaurantSelectorFragment)){
                    fragment = new RestaurantSelectorFragment();
                }

                fm.beginTransaction()
                        .replace(R.id.container, fragment)
                        .commit();
                break;

            case 1:
                mTitle = getString(R.string.title_restaurant_list);
                saveMenuSelection(1);
                newListFragment = false;

                if(!(fragment instanceof RestaurantListFragment)){
                    fragment = new RestaurantListFragment();
                    newListFragment = true;
                }

                fm.beginTransaction()
                        .replace(R.id.container, fragment)
                        .commit();
                break;

            case 2:
                mTitle = getString(R.string.title_restaurant_history);
                saveMenuSelection(2);

                if(!(fragment instanceof HistoryListFragment)){
                    fragment = new HistoryListFragment();
                }

                fm.beginTransaction()
                        .replace(R.id.container, fragment)
                        .commit();
                break;
        }
    }

    /**
     * This is called once selected fragment is loaded and drawer is closed
     */
    public void restoreActionBar() {
        ActionBar actionBar = getSupportActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
        actionBar.setDisplayShowTitleEnabled(true);

        if(mTitle != null) {
            actionBar.setTitle(mTitle);
        }

        // Set up filtering in action bar in RestaurantListFragment
        FragmentManager fm = getSupportFragmentManager();
        Fragment fragment = fm.findFragmentById(R.id.container);

        if(fragment instanceof RestaurantListFragment){

            ((RestaurantListFragment) fragment).setupListFiltering();
        }
    }

    private void confirmClearRestaurantHistory(){

        new AlertDialog.Builder(this)
                .setTitle(R.string.dialog_delete_history_title)
                .setMessage(R.string.dialog_delete_history_message)
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        clearRestaurantHistory();
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

    private void clearRestaurantHistory(){

        DatabaseHelper.getInstance(this).deleteRestaurantHistory();
        Intent i = new Intent(HistoryListFragment.ACTION_HISTORY_CLEARED);
        LocalBroadcastManager.getInstance(this).sendBroadcast(i);
    }

    private void saveMenuSelection(int selection){

        prefs.edit()
                .putInt(NavigationDrawerFragment.PREF_SELECTED_MENU_ITEM, selection)
                .commit();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (!mNavigationDrawerFragment.isDrawerOpen()) {
            // Only show items in the action bar relevant to this screen
            // if the drawer is not showing. Otherwise, let the drawer
            // decide what to show in the action bar.
            getMenuInflater().inflate(R.menu.main, menu);
            restoreActionBar();
            return true;
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.

        switch(item.getItemId()){

            case R.id.menu_clear_restaurant_history:
                confirmClearRestaurantHistory();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
