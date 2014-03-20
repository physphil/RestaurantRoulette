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

package com.physphil.android.restaurantroulette;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.view.Menu;
import android.view.MenuItem;

import com.physphil.android.restaurantroulette.util.Constants;

/**
 * Created by pshadlyn on 2/24/14.
 */
public class RestaurantActivity extends BaseActivity {

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_host_fragment);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(R.string.title_edit_restaurant);

        String id = getIntent().getStringExtra(RestaurantFragment.EXTRA_RESTAURANT_ID);

        // Attempt to find previously existing fragment.  If not found create new one.
        FragmentManager fm = getSupportFragmentManager();
        RestaurantFragment fragment = (RestaurantFragment) fm.findFragmentById(R.id.fragment_container);

        if(fragment == null){
            fragment = RestaurantFragment.newInstance(id);
        }

        fm.beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .commit();
    }

    @Override
    protected void onResume() {
        super.onResume();

        setActionBarFont(Constants.FONT_DEFAULT);
    }

    /**
     * Retrieve intent to start this activity and launch RestaurantFragment
     * @param context context
     * @param id id of restaurant to open, or null if new restaurant
     * @return intent used to start activity
     */
    public static Intent getLaunchingIntent(Context context, String id){

        Intent i = new Intent(context, RestaurantActivity.class);

        if(id != null){
            i.putExtra(RestaurantFragment.EXTRA_RESTAURANT_ID, id);
        }

        return i;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.edit_restaurant, menu);
        return true;
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
