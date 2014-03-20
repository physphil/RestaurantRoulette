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

import android.graphics.Typeface;
import android.support.v7.app.ActionBarActivity;
import android.widget.TextView;

/**
 * Created by pshadlyn on 3/4/14.
 */
public class BaseActivity extends ActionBarActivity {

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
