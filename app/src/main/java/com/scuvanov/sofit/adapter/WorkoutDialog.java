package com.scuvanov.sofit.adapter;

import android.app.Dialog;
import android.content.Context;
import android.view.Window;
import com.scuvanov.sofit.R;

/**
 * Created by Sean on 3/12/2015.
 */
public class WorkoutDialog extends Dialog {
    Dialog dialog;

    public WorkoutDialog(Context context) {
        super(context);
        init();
    }

    public WorkoutDialog(Context context, int theme) {
        super(context, theme);
    }

    protected WorkoutDialog(Context context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }
    private void init(){

        dialog = new Dialog(getContext());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawableResource(R.drawable.card_background_white);
        dialog.setContentView(R.layout.dialog_workout_layout);

    }
}
