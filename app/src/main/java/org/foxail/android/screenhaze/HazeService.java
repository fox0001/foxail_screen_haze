package org.foxail.android.screenhaze;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.Service;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;

public class HazeService extends Service {

    public final static String TAG = "HazeService";

    //private static WindowManager windowManager = null;
    //private static ContentResolver contentResolver = null;
    //private static View hazeView = null;

    private final IBinder binder = new LocalBinder();

    public class LocalBinder extends Binder {
        HazeService getService() {
            return HazeService.this;
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        return binder;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(TAG, "onCreate() executed");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(TAG, "onStartCommand() executed");

        int result = super.onStartCommand(intent, flags, startId);

        Notification notification = SettingsUtil.createNotification(getBaseContext());
        try {
            getClass().getMethod("startForeground", new Class[]{int.class, Notification.class})
                    .invoke(this, new Object[]{Integer.valueOf(SettingsUtil.NOTIFICATION_ID), notification});
        } catch(Exception e) {
            Log.w(TAG, "error calling startForeground", e);
            setForegroundBackwardsCompat(true);
            ((NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE))
                    .notify(SettingsUtil.NOTIFICATION_ID, notification);
        }

        return result;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy() executed");

        setForegroundBackwardsCompat(false);
        SettingsUtil.hideNotification(getBaseContext());
    }

    private void setForegroundBackwardsCompat(boolean foreground) {
        try {
            getClass().getMethod("setForeground", new Class[]{boolean.class})
                    .invoke(this, new Object[]{Boolean.valueOf(foreground)});
        } catch(Exception e) {
            Log.w(TAG, "Error calling setForeground", e);
        }
    }
}
