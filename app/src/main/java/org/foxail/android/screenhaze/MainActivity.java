package org.foxail.android.screenhaze;

import android.content.ContentResolver;
import android.content.pm.PackageManager;
import android.provider.Settings;
import android.app.Activity;
import android.os.Bundle;
import android.widget.Toast;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        PackageManager pm = getPackageManager();
        int isGranted = pm.checkPermission("android.permission.WRITE_SECURE_SETTINGS", getPackageName());
        if(PackageManager.PERMISSION_DENIED == isGranted){
            PermissionUtil.grantPermission();
        }

        switchScreenColor();

        finish();
        System.exit(0);
    }

    private void switchScreenColor(){
        final String ACCESSIBILITY_DISPLAY_DALTONIZER_ENABLED = "accessibility_display_daltonizer_enabled";
        final String ACCESSIBILITY_DISPLAY_DALTONIZER = "accessibility_display_daltonizer";
        final ContentResolver cr = getContentResolver();

        int devMode = Settings.Secure.getInt(cr, Settings.Global.DEVELOPMENT_SETTINGS_ENABLED, 0);
        int enableMode = Settings.Secure.getInt(cr, ACCESSIBILITY_DISPLAY_DALTONIZER_ENABLED, 0);

        if (devMode == 0) {
            Settings.Secure.putInt(cr, Settings.Global.DEVELOPMENT_SETTINGS_ENABLED, 1);
        }
        if (enableMode == 1) {
            Settings.Secure.putInt(cr, ACCESSIBILITY_DISPLAY_DALTONIZER_ENABLED, 0);
        } else {
            Settings.Secure.putInt(cr, ACCESSIBILITY_DISPLAY_DALTONIZER_ENABLED, 1);
            Settings.Secure.putInt(cr, ACCESSIBILITY_DISPLAY_DALTONIZER, 0);
        }
    }

    protected void showToast(String msg){
        Toast toast = Toast.makeText(MainActivity.this, msg, Toast.LENGTH_SHORT);
        //toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();
    }

    /*
    private void showHaze(){
        super.onCreate(savedInstanceState);
        try{
            WindowManager windowManager = (WindowManager) Class.forName("android.view.WindowManagerImpl").getMethod("getDefault", new Class[0]).invoke(null, new Object[0]);

            View view = findViewById(R.id.background);
            view.setFocusable(false);
            view.setClickable(false);
            view.setKeepScreenOn(false);
            view.setLongClickable(false);
            view.setFocusableInTouchMode(false);

            LayoutParams layoutParams = new LayoutParams();
            layoutParams.height = LayoutParams.FILL_PARENT;
            layoutParams.width = LayoutParams.FILL_PARENT;
            layoutParams.flags = LayoutParams.FLAG_FULLSCREEN;
            //layoutParams.format = PixelFormat.TRANSLUCENT; // You can try different formats
            layoutParams.windowAnimations = android.R.style.Animation_Toast; // You can use only animations that the system to can access
            layoutParams.type = LayoutParams.TYPE_SYSTEM_OVERLAY;
            //layoutParams.gravity = Gravity.BOTTOM;
            layoutParams.x = 0;
            layoutParams.y = 0;
            layoutParams.verticalWeight = 1.0F;
            layoutParams.horizontalWeight = 1.0F;
            layoutParams.verticalMargin = 0.0F;
            layoutParams.horizontalMargin = 0.0F;
            //layoutParams.setBackgroundDrawable();

            windowManager.addView(view, layoutParams);
        } catch (Exception e){

        }
    }
    */
}
