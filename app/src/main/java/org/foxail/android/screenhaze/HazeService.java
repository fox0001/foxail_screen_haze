package org.foxail.android.screenhaze;

import android.app.Service;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;

public class HazeService extends Service {

    public final static String TAG = "HazeService";

    private static WindowManager windowManager = null;
    private static ContentResolver contentResolver = null;
    private static View hazeView = null;

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(TAG, "onCreate() executed");

        if (windowManager == null) {
            try {
                windowManager = (WindowManager) getApplicationContext().getSystemService(Context.WINDOW_SERVICE);
            } catch (Exception e) {
                Log.e(TAG, "Can not get WindowManage.", e);
            }
        }

        if (contentResolver == null) {
            contentResolver = getContentResolver();
        }

        SettingsUtil.fistRun(this, null);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(TAG, "onStartCommand() executed");
        int result = super.onStartCommand(intent, flags, startId);

        SharedPreferences preferences = SettingsUtil.getPreferences(this);
        if(preferences.getBoolean(SettingsUtil.ITEM_IS_RUNNING, false)) {
            this.stopSelf();
        } else {
            if(preferences.getBoolean(SettingsUtil.ITEM_ENABLE_SINGLE_COLOR, false)) {
                SettingsUtil.showSingleColor(contentResolver);
            } else {
                SettingsUtil.showFullColor(contentResolver);
            }

            if(hazeView != null) {
                Log.d(TAG, "hazeView existed, do hideHaze");
                SettingsUtil.hideHaze(hazeView, windowManager);
            }
            int scale = preferences.getInt(SettingsUtil.ITEM_HAZE_WEIGHT_SCALE, 0);
            hazeView = SettingsUtil.showHaze(this, windowManager, scale);

            setIsRunning(preferences, true);
        }

        return result;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy() executed");

        SharedPreferences preferences = SettingsUtil.getPreferences(this);
        if(preferences.getBoolean(SettingsUtil.ITEM_ENABLE_SINGLE_COLOR, false)) {
            SettingsUtil.showFullColor(contentResolver);
        }
        SettingsUtil.hideHaze(hazeView, windowManager);

        setIsRunning(preferences, false);
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    private void setIsRunning(SharedPreferences preferences, boolean isRunning) {
        Log.d(TAG, "setting isRunning: " + isRunning);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean(SettingsUtil.ITEM_FIRST_RUN, isRunning);
        editor.commit();
    }
}
