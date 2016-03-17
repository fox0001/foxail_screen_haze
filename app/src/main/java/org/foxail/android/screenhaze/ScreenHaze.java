package org.foxail.android.screenhaze;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
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

    public final static String SWITCH_ACTION = "SWITCH_ACTION";
    public final static int SWITCH_ACTION_SWITCH = 0;
    public final static int SWITCH_ACTION_STOP = 1;
    public final static int SWITCH_ACTION_RELOAD = 2;

    private ContentResolver contentResolver = null;
    private WindowManager windowManager = null;
    private static View hazeView = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate");

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

        Intent intent = getIntent();
        int action = intent.getIntExtra(SWITCH_ACTION, SWITCH_ACTION_SWITCH);
        switchHaze(action);

        finish();
        //System.exit(0);
    }

    private void switchHaze(int action) {
        SharedPreferences preferences =  SettingsUtil.getPreferences(this);

        boolean isRunning = isRunning();
        boolean isEnableSingleColor = preferences.getBoolean(SettingsUtil.ITEM_ENABLE_SINGLE_COLOR, false);

        switch (action) {
            case SWITCH_ACTION_STOP:
                Log.d(TAG, "SWITCH_ACTION_STOP");
                if(isRunning) {
                    stopRunning(isEnableSingleColor);
                }
                break;

            case SWITCH_ACTION_RELOAD:
                Log.d(TAG, "SWITCH_ACTION_RELOAD");
                if(isRunning) {
                    stopRunning(isEnableSingleColor);
                }
                startRunning(isEnableSingleColor);
                break;

            case SWITCH_ACTION_SWITCH:
            default:
                Log.d(TAG, "SWITCH_ACTION_SWITCH");

                if (isRunning) {
                    stopRunning(isEnableSingleColor);
                } else {
                    startRunning(isEnableSingleColor);
                }
                break;
        }

        // save running state
        Editor editor = preferences.edit();
        editor.putBoolean(SettingsUtil.ITEM_IS_RUNNING, isRunning());
        editor.commit();
    }

    private boolean isRunning() {
        return (hazeView != null);
    }

    private void stopRunning(boolean isEnableSingleColor) {
        Log.d(TAG, "ScreenHaze is running");
        if (isEnableSingleColor) {
            Log.d(TAG, "showFullColor");
            SettingsUtil.showFullColor(contentResolver);
        }

        Log.d(TAG, "hideHaze");
        SettingsUtil.hideHaze(hazeView, windowManager);
        hazeView = null;

        stopService(new Intent(this, HazeService.class));
    }

    private void startRunning(boolean isEnableSingleColor) {
        Log.d(TAG, "ScreenHaze is not running");
        if (isEnableSingleColor) {
            Log.d(TAG, "showSingleColor");
            SettingsUtil.showSingleColor(contentResolver);
        }

        Log.d(TAG, "showHaze");
        SharedPreferences preferences = SettingsUtil.getPreferences(this);
        int scale = preferences.getInt(SettingsUtil.ITEM_HAZE_WEIGHT_SCALE, 0);
        hazeView = SettingsUtil.showHaze(this, windowManager, scale);

        startService(new Intent(this, HazeService.class));
    }

    /*
    protected void showToast(String msg) {
        Toast toast = Toast.makeText(ScreenHaze.this, msg, Toast.LENGTH_SHORT);
        toast.show();
    }
    */
}
