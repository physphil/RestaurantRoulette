package com.physphil.android.restaurantroulette;

import android.graphics.Typeface;
import android.support.v7.app.ActionBarActivity;
import android.widget.TextView;

/**
 * Created by pshadlyn on 3/4/14.
 */
public class RRActivity extends ActionBarActivity {

    /**
     * Sets font in action bar to font specified
     * @param font desired font
     */
    public void setActionBarFont(String font){

        // Only set action bar font if we're above gingerbread
        if(android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.HONEYCOMB){

            int titleId = getResources().getIdentifier("action_bar_title", "id", "android");

            TextView tv = (TextView) findViewById(titleId);
            Typeface tf = Typeface.createFromAsset(getAssets(), font);

            if(tv != null){
                tv.setTypeface(tf);
            }
        }
    }
}
