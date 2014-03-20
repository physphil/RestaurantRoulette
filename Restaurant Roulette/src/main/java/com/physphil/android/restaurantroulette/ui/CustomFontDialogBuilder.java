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

package com.physphil.android.restaurantroulette.ui;

import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Typeface;
import android.widget.Button;
import android.widget.TextView;

import com.physphil.android.restaurantroulette.util.Constants;

/**
 * Created by pshadlyn on 3/18/14.
 */
public class CustomFontDialogBuilder extends AlertDialog.Builder {

    private Context mContext;

    public CustomFontDialogBuilder(Context context) {
        super(context);
        mContext = context;
    }

    @Override
    public AlertDialog show() {
        AlertDialog dialog = super.show();

        Typeface tf = Typeface.createFromAsset(mContext.getAssets(), Constants.FONT_DEFAULT);
        ((Button) dialog.findViewById(android.R.id.button1)).setTypeface(tf);
        ((Button) dialog.findViewById(android.R.id.button2)).setTypeface(tf);
        ((Button) dialog.findViewById(android.R.id.button3)).setTypeface(tf);
        ((TextView) dialog.findViewById(android.R.id.message)).setTypeface(tf);

        return dialog;
    }
}
