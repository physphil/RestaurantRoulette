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

package com.physphil.android.restaurantroulette.util;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Location;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.Toast;

import com.physphil.android.restaurantroulette.R;
import com.physphil.android.restaurantroulette.models.Restaurant;
import com.physphil.android.restaurantroulette.ui.CustomFontDialogBuilder;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

/**
 * Utility methods
 * Created by pshadlyn on 3/13/14.
 */
public class Util {

    public static void showOnMap(Context context, String name, Location location){

        if(Restaurant.hasName(name)){

            try{
                // Encode restaurant name and generate maps URI
                String encodedName = URLEncoder.encode(name, "UTF-8");
                Uri mapUri;

                if(location != null) {
                    Log.d("PS", "Latitude = " + location.getLatitude() + ", Longitude = " + location.getLongitude());
                    mapUri = Uri.parse("geo:" + location.getLatitude() + "," + location.getLongitude() +
                            "+?q=" + encodedName + "&z=12");
                }
                else{
                    Log.d("PS", "No Location Available");
                    Toast.makeText(context, R.string.toast_location_unavailable, Toast.LENGTH_LONG).show();
                    mapUri = Uri.parse("geo:0,0?q=" + encodedName + "&z=13");
                }

                // Launch maps application and search by restaurant name
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(mapUri);
                context.startActivity(i);
            }
            catch(UnsupportedEncodingException e){
                // This shouldn't happen, do nothing.
            }
            catch(ActivityNotFoundException e){
                Toast.makeText(context, R.string.toast_no_maps_app, Toast.LENGTH_SHORT).show();
            }
        }
        else{

            Toast.makeText(context, R.string.toast_no_restaurant_name, Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Show help dialog to user
     * @param context context
     * @param title resource of title
     * @param message resource of message
     * @param pref shared preference to track if dialog has been shown
     */
    public static void showHelpDialog(Context context, int title, int message, final String pref){

        final SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);

//        new AlertDialog.Builder(context)
          new CustomFontDialogBuilder(context)
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton(R.string.btn_confirm, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        // Don't show again
                        prefs.edit()
                                .putBoolean(pref, false)
                                .commit();
                    }
                })
                .setCancelable(false)
                .show();
    }
}
