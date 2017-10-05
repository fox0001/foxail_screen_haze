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

    /*
    public final static String SWITCH_ACTION = "SWITCH_ACTION";
    public final static int SWITCH_ACTION_SWITCH = 0;
    public final static int SWITCH_ACTION_STOP = 1;
    public final static int SWITCH_ACTION_RELOAD = 2;
    public final static int SWITCH_ACTION_SETTINGS = 3;
    */

    //private ContentResolver contentResolver = null;
    //private WindowManager windowManager = null;
    //private static View hazeView = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate");

        SettingsUtil.fistRun(this, null);

        boolean isRunning = SettingsUtil.isRunning(getBaseContext());
        if (isRunning) {
            stopService(new Intent(this, HazeService.class));
        } else {
            startService(new Intent(this, HazeService.class));
        }

        // save running state
        //Editor editor = preferences.edit();
        //editor.putBoolean(SettingsUtil.ITEM_IS_RUNNING, !isRunning);
        //editor.commit();

        finish();
        //System.exit(0);
    }

    /*
    protected void showToast(String msg) {
        Toast toast = Toast.makeText(ScreenHaze.this, msg, Toast.LENGTH_SHORT);
        toast.show();
    }
    */
}
