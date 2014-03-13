package com.physphil.android.restaurantroulette.util;

import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesClient;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.location.LocationClient;

/**
 * Helper for connection to location services
 * Created by pshadlyn on 3/13/14.
 */
public class LocationHelper implements GooglePlayServicesClient.ConnectionCallbacks, GooglePlayServicesClient.OnConnectionFailedListener {

    public static final String ACTION_LOCATION_SERVICES_CONNECTED = "com.physphil.android.restaurantroulette.ACTION_LOCATION_SERVICES_CONNECTED";
    public static final String EXTRA_PLAY_SERVICES_AVAILABLE = "com.physphil.android.restaurantroulette.EXTRA_PLAY_SERVICES_CONNECTED";
    public static final String EXTRA_LOCATION = "com.physphil.android.restaurantroulette.EXTRA_LOCATION";

    private Context mContext;
    private Location mLocation;
    private LocationClient mLocationClient;

    public LocationHelper(Context context){

        mContext = context;
        mLocationClient = new LocationClient(mContext, this, this);
    }

    public boolean servicesConnected(){

        // Check that Google Play Services is available on device
        int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(mContext);

        // TODO - make this more thorough, display Google error dialog to user
        return (resultCode == ConnectionResult.SUCCESS);
    }

    /**
     * Connect to Location Services.  Once connected, ACTION_LOCATION_SERVICES_CONNECTED broadcast is sent
     * and location info is sent in EXTRA_LOCATION. Boolean EXTRA_PLAY_SERVICES_AVAILABLE contains if Google
     * Play Services is available on device
     */
    public void connect(){

        if(servicesConnected()){

            mLocationClient.connect();
        }
        else{

            sendFailureBroadcast();
        }
    }

    /**
     * Get curent location object
     * @return current location, null if location not available
     */
    public Location getLocation(){

        // Try to refresh location before sending
        if(mLocationClient.isConnected()){

            mLocation = mLocationClient.getLastLocation();
        }

        return mLocation;
    }

    /**
     * Google play services not available on device, send broadcast indicating failure
     */
    private void sendFailureBroadcast(){

        Intent i = new Intent(ACTION_LOCATION_SERVICES_CONNECTED);
        i.putExtra(EXTRA_PLAY_SERVICES_AVAILABLE, false);
        LocalBroadcastManager.getInstance(mContext).sendBroadcast(i);
    }

    // When LocationClient is connected to Location Services
    @Override
    public void onConnected(Bundle bundle) {

        Log.v("PS", "Location Services connected");
        mLocation = mLocationClient.getLastLocation();

        Intent i = new Intent(ACTION_LOCATION_SERVICES_CONNECTED);
        i.putExtra(EXTRA_PLAY_SERVICES_AVAILABLE, true);
        i.putExtra(EXTRA_LOCATION, mLocation);
        LocalBroadcastManager.getInstance(mContext).sendBroadcast(i);
    }

    // When LocationClient disconnects from Location Services
    @Override
    public void onDisconnected() {
        Log.v("PS", "Location Services disconnected");
    }

    // If connection to LocationServices drops
    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        Log.v("PS", "Location Services connection failed");
    }
}
