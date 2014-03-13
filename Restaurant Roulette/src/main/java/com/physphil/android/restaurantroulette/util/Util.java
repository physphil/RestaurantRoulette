package com.physphil.android.restaurantroulette.util;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.net.Uri;
import android.util.Log;
import android.widget.Toast;

import com.physphil.android.restaurantroulette.R;
import com.physphil.android.restaurantroulette.models.Restaurant;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

/**
 * Utility methods
 * Created by pshadlyn on 3/13/14.
 */
public class Util {

    /**
     * Open google maps and search for restaurant
     * @param context context
     * @param name restaurant name
     */
    public static void showOnMap(Context context, String name){

        if(Restaurant.hasName(name)){

            try{
                // Encode restaurant name and generate maps URI
                String encodedName = URLEncoder.encode(name, "UTF-8");
                Uri mapUri = Uri.parse("geo:0,0?q=" + encodedName + "&z=11");

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

    public static void showOnMap(Context context, String name, Location location){

        if(Restaurant.hasName(name)){

            try{
                // Encode restaurant name and generate maps URI
                String encodedName = URLEncoder.encode(name, "UTF-8");
                Log.v("PS", "Latitude = " + location.getLatitude() + ", Longitude = " + location.getLongitude());
                Uri mapUri = Uri.parse("geo:" + location.getLatitude() + "," + location.getLongitude() +
                        "+?q=" + encodedName + "&z=13");

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

}
