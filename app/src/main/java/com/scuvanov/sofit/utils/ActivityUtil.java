package com.scuvanov.sofit.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import com.scuvanov.sofit.LeftMenusActivity;
import com.scuvanov.sofit.MainActivity;
import com.scuvanov.sofit.SignUpActivity;

/**
 * Created by Sean on 5/31/2017.
 */

public class ActivityUtil {

    public static void goToRegisterActivity(Context context){
        Intent intent = new Intent(context, SignUpActivity.class);
        context.startActivity(intent);
        ((Activity) context).finish();
    }

    public static void goToNavigationActivity(Context context){
        Intent intent = new Intent(context, LeftMenusActivity.class);
        context.startActivity(intent);
        ((Activity) context).finish();
    }

    public static void goToMainActivity(Context context) {
        Intent intent = new Intent(context, MainActivity.class);
        context.startActivity(intent);
        ((Activity) context).finish();
    }
}
