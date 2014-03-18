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
