package com.physphil.android.restaurantroulette;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;

/**
 * Created by pshadlyn on 2/24/14.
 */
public class RestaurantActivity extends ActionBarActivity {

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_host_fragment);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        String id = getIntent().getStringExtra(RestaurantFragment.EXTRA_RESTAURANT_ID);

        FragmentManager fm = getSupportFragmentManager();
        fm.beginTransaction()
                .replace(R.id.fragment_container, RestaurantFragment.newInstance(id))
                .commit();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch(item.getItemId()){

            case android.R.id.home:
                finish();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

}
