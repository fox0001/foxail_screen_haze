package org.foxail.android.screenhaze;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

import java.lang.reflect.Method;

public class ScreenHaze extends Activity {

	private final static String TAG = "ScreenHaze";

    private SharedPreferences preferences = null;
    private static ContentResolver contentResolver = null;
    private static WindowManager windowManager = null;
    private static View hazeView = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate");

        if (preferences == null) {
            preferences =  SettingsUtil.getPreferences(this);
        }

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

        SettingsUtil.fistRun(this, preferences);

        switchHaze();

        finish();
        //System.exit(0);
    }

    private void switchHaze() {
        boolean isRunning = isRunning();
        boolean isEnableSingleColor = preferences.getBoolean(SettingsUtil.ITEM_ENABLE_SINGLE_COLOR, false);

        if (isRunning) {
            Log.d(TAG, "ScreenHaze is running");
            if (isEnableSingleColor) {
                Log.d(TAG, "showFullColor");
                SettingsUtil.showFullColor(contentResolver);
            }

            Log.d(TAG, "hideHaze");
            SettingsUtil.hideHaze(hazeView, windowManager);
            hazeView = null;
        } else {
            Log.d(TAG, "ScreenHaze is not running");
            if (isEnableSingleColor) {
                Log.d(TAG, "showSingleColor");
                SettingsUtil.showSingleColor(contentResolver);
            }

            Log.d(TAG, "showHaze");
            hazeView = SettingsUtil.showHaze(this, windowManager, 50);
        }

        // save running state
        Editor editor = preferences.edit();
        editor.putBoolean(SettingsUtil.ITEM_IS_RUNNING, !isRunning);
        editor.commit();
    }

    private boolean isRunning() {
        return (hazeView != null);
    }

    protected void showToast(String msg) {
        Toast toast = Toast.makeText(ScreenHaze.this, msg, Toast.LENGTH_SHORT);
        toast.show();
    }
}
