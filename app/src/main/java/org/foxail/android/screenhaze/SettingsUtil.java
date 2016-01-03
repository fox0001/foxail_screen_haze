package org.foxail.android.screenhaze;

import android.content.ContentResolver;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.graphics.drawable.ColorDrawable;
import android.provider.Settings;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;

import java.lang.reflect.Method;

public class SettingsUtil {

    private final static String TAG = "SettingsUtil";

    private final static String ACCESSIBILITY_DISPLAY_DALTONIZER_ENABLED = "accessibility_display_daltonizer_enabled";
    private final static String ACCESSIBILITY_DISPLAY_DALTONIZER = "accessibility_display_daltonizer";

    // The file name of settings xml
    public final static String FILE_NAME = "settings";

    public final static String ITEM_FIRST_RUN = "first_run";

    public final static String ITEM_IS_RUNNING = "is_running";

    public final static String ITEM_ENABLE_SINGLE_COLOR = "enable_single_color";

    public final static String ITEM_HAZE_WEIGHT_SCALE = "haze_weight_scale";


    public static SharedPreferences getPreferences(Context context) {
        return context.getSharedPreferences(SettingsUtil.FILE_NAME, Context.MODE_MULTI_PROCESS);
    }

    public static void fistRun(Context context, SharedPreferences preferences) {
        if(preferences == null) {
            preferences = SettingsUtil.getPreferences(context);
        }

        if (!preferences.getBoolean(SettingsUtil.ITEM_FIRST_RUN, true)) {
            return;
        }

        boolean isGranted = PermissionUtil.grantPermission(context);
        if(isGranted){
            SharedPreferences.Editor editor = preferences.edit();
            editor.putBoolean(SettingsUtil.ITEM_FIRST_RUN, false);

            // setting default values
            editor.putBoolean(SettingsUtil.ITEM_ENABLE_SINGLE_COLOR, true);
            editor.putInt(SettingsUtil.ITEM_HAZE_WEIGHT_SCALE, 50);

            editor.commit();
        }
    }

    public static void showSingleColor(ContentResolver contentResolver) {
        int devMode = Settings.Secure.getInt(contentResolver, Settings.Global.DEVELOPMENT_SETTINGS_ENABLED, 0);
        int enableMode = Settings.Secure.getInt(contentResolver, ACCESSIBILITY_DISPLAY_DALTONIZER_ENABLED, 0);

        if (devMode == 0) {
            Settings.Secure.putInt(contentResolver, Settings.Global.DEVELOPMENT_SETTINGS_ENABLED, 1);
        }
        if (enableMode == 0) {
            Settings.Secure.putInt(contentResolver, ACCESSIBILITY_DISPLAY_DALTONIZER_ENABLED, 1);
            Settings.Secure.putInt(contentResolver, ACCESSIBILITY_DISPLAY_DALTONIZER, 0);
        }
    }

    public static void showFullColor(ContentResolver contentResolver) {
        int devMode = Settings.Secure.getInt(contentResolver, Settings.Global.DEVELOPMENT_SETTINGS_ENABLED, 0);
        int enableMode = Settings.Secure.getInt(contentResolver, ACCESSIBILITY_DISPLAY_DALTONIZER_ENABLED, 0);

        if (devMode == 0) {
            return;
        }
        if (enableMode != 0) {
            Settings.Secure.putInt(contentResolver, ACCESSIBILITY_DISPLAY_DALTONIZER_ENABLED, 0);
        }
    }

    public static View showHaze(Context context, WindowManager windowManager, int scale) {
        if(scale <= 0) {
            return null;
        }
        if(scale > 100) {
            scale = 100;
        }
        float dimAmount = (float) (100 - scale) / 100;

        View hazeView = ((LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE))
                .inflate(R.layout.haze_view, null);
        hazeView.setFocusable(false);
        hazeView.setClickable(false);
        hazeView.setKeepScreenOn(false);
        hazeView.setLongClickable(false);
        hazeView.setFocusableInTouchMode(false);
        hazeView.setBackground(new ColorDrawable(Color.argb(0, 0, 0, 0)));

        WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
        Display display = windowManager.getDefaultDisplay();
        try {
            Method getHeight = Display.class.getMethod("getRawHeight", new Class[0]);
            Method getWidth = Display.class.getMethod("getRawWidth", new Class[0]);
            layoutParams.height = ((Integer) getHeight.invoke(display, new Object[0])).intValue();
            layoutParams.width = ((Integer) getWidth.invoke(display, new Object[0])).intValue();
        } catch (Exception e) {
            Log.w(TAG, "Could not get raw size of display", e);
        }
        DisplayMetrics displayMetrics = new DisplayMetrics();
        try {
            Method getMetrics = Display.class.getMethod("getRealMetrics", DisplayMetrics.class);
            getMetrics.invoke(display, displayMetrics);
            layoutParams.height = displayMetrics.heightPixels;
            layoutParams.width = displayMetrics.widthPixels;
        } catch (Exception e) {
            Log.w(TAG, "Could not get real display metrics", e);
            layoutParams.height = -1;
            layoutParams.width = -1;
        }
        layoutParams.flags = WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN
                | WindowManager.LayoutParams.FLAG_DIM_BEHIND
                | WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE
                | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;
        layoutParams.format = PixelFormat.TRANSLUCENT;
        layoutParams.windowAnimations = android.R.style.Animation_Toast;
        layoutParams.type = WindowManager.LayoutParams.TYPE_SYSTEM_OVERLAY;
        layoutParams.setTitle(context.getString(R.string.haze_view_title));
        layoutParams.gravity = Gravity.FILL;
        layoutParams.x = 0;
        layoutParams.y = 0;
        layoutParams.verticalMargin = 0.0f;
        layoutParams.horizontalMargin = 0.0f;
        layoutParams.verticalWeight = 0.0f;
        layoutParams.horizontalWeight = 0.0f;
        layoutParams.dimAmount = dimAmount;

        windowManager.addView(hazeView, layoutParams);

        return hazeView;
    }

    public static void hideHaze(View hazeView, WindowManager windowManager) {
        if (hazeView != null) {
            windowManager.removeView(hazeView);
            hazeView = null;
        }
    }

}