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

    public static final String ACTION_LOCATION_RETRIEVED = "com.physphil.android.restaurantroulette.ACTION_LOCATION_RETRIEVED";
    public static final String ACTION_LOCATION_SERVICES_DISCONNECTED = "com.physphil.android.restaurantroulette.ACTION_LOCATION_SERVICES_DISCONNECTED";
    public static final String EXTRA_PLAY_SERVICES_AVAILABLE = "com.physphil.android.restaurantroulette.EXTRA_PLAY_SERVICES_CONNECTED";
    public static final String EXTRA_LOCATION = "com.physphil.android.restaurantroulette.EXTRA_LOCATION";

    private Context mContext;
    private Location mLocation;
    private LocationClient mLocationClient;
    private LocalBroadcastManager mBroadcastManager;

    public LocationHelper(Context context){

        mContext = context;
        mBroadcastManager = LocalBroadcastManager.getInstance(mContext);
    }

    private boolean googlePlayServicesAvailable(){

        // Check that Google Play Services is available on device
        int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(mContext);

        // TODO - make this more thorough, display Google error dialog to user
        return (resultCode == ConnectionResult.SUCCESS);
    }

    /**
     * Connect to Location Services and retrieve location.  Once connected, ACTION_LOCATION_RETRIEVED broadcast is sent
     * and location info is sent in EXTRA_LOCATION. Boolean EXTRA_PLAY_SERVICES_AVAILABLE contains if Google
     * Play Services is available on device
     */
    public void connectAndGetLocation(){

        mLocationClient = new LocationClient(mContext, this, this);

        if(googlePlayServicesAvailable()){

            mLocationClient.connect();
        }
        else{

            sendFailureBroadcast();
        }
    }

    /**
     * Disconnect from Location Services
     */
    public void disconnect(){

        if(mLocationClient != null) {
            mLocationClient.disconnect();
        }
    }

    /**
     * Get current Location object
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

        Intent i = new Intent(ACTION_LOCATION_RETRIEVED);
        i.putExtra(EXTRA_PLAY_SERVICES_AVAILABLE, false);
        mBroadcastManager.sendBroadcast(i);
    }

    // When LocationClient is connected to Location Services
    @Override
    public void onConnected(Bundle bundle) {

        Log.d("PS", "Location Services connected");
        mLocation = mLocationClient.getLastLocation();

        Intent i = new Intent(ACTION_LOCATION_RETRIEVED);
        i.putExtra(EXTRA_PLAY_SERVICES_AVAILABLE, true);
        i.putExtra(EXTRA_LOCATION, mLocation);
        mBroadcastManager.sendBroadcast(i);
    }

    // When LocationClient disconnects from Location Services
    @Override
    public void onDisconnected() {
        Log.d("PS", "Location Services disconnected");
        mBroadcastManager.sendBroadcast(new Intent(ACTION_LOCATION_SERVICES_DISCONNECTED));
    }

    // If connection to LocationServices drops
    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        Log.d("PS", "Location Services connection failed");
    }
}
