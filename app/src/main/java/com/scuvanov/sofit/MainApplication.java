package com.scuvanov.sofit;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.scuvanov.sofit.service.NotificationService;


public class MainApplication extends Application {

    String TAG = MainApplication.class.getCanonicalName();

    @Override
    public void onCreate() {
        super.onCreate();

        SharedPreferences prefs = getSharedPreferences("com.scuvanov.sofit", Context.MODE_PRIVATE);
        if (prefs != null) {
            if (prefs.getBoolean("SERVICE_STATE", false)) {
                Log.e(TAG, "Doing nothing, service already running.");
            } else {
                Log.e(TAG, "Starting Service");
                Intent intent = new Intent(this, NotificationService.class);
                intent.setAction("START_SERVICE");
                startService(intent);
            }
        }

        ImageLoader.getInstance()
                .init(ImageLoaderConfiguration.createDefault(this));
    }
}
