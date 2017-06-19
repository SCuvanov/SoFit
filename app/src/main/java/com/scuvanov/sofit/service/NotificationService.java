package com.scuvanov.sofit.service;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.util.Log;

import java.util.Calendar;

public class NotificationService extends Service {


    private String TAG = NotificationService.class.getCanonicalName();
    private AlarmManager alarmMgr;
    private PendingIntent pendingIntent;

    private Boolean isServiceRunning = false;
    SharedPreferences prefs;

    public NotificationService() {
    }

    @Override
    public void onCreate() {
        super.onCreate();
        prefs = getSharedPreferences("com.scuvanov.sofit", Context.MODE_PRIVATE);
        alarmMgr = (AlarmManager) this.getSystemService(Context.ALARM_SERVICE);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        handleIntent(intent);
        return START_STICKY;
    }

    private void handleIntent(Intent intent) {
        if (intent != null) {
            if (intent.getAction().equals("START_SERVICE")) {
                if (prefs != null) {
                    prefs.edit().putBoolean("SERVICE_STATE", true).apply();
                }
                notifyUser();
            }
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.e(TAG, "STOPPING SERVICE");
        if (prefs != null) {
            prefs.edit().putBoolean("SERVICE_STATE", false).apply();
        }
        stopSelf();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    private void notifyUser() {
        if (alarmMgr != null) {
            startAlarm();
        } else {
            alarmMgr = (AlarmManager) this.getSystemService(Context.ALARM_SERVICE);
            startAlarm();
        }
    }

    private void startAlarm() {
        Log.e(TAG, "Starting Alarm");
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.set(Calendar.HOUR_OF_DAY, 7);

        Intent alarmIntent = new Intent(this, NotificationReceiver.class);
        pendingIntent = PendingIntent.getBroadcast(this, 0, alarmIntent, 0);

        alarmMgr.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),
                AlarmManager.INTERVAL_DAY, pendingIntent);
    }
}
