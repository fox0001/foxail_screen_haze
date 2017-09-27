package org.foxail.android.screenhaze;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

public class Settings extends Activity {

	private final static String TAG = "ScreenHazeSettings";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "onCreate");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        Intent intent = new Intent(this, ScreenHaze.class);
        intent.putExtra(ScreenHaze.SWITCH_ACTION, ScreenHaze.SWITCH_ACTION_STOP);
        this.startActivity(intent);

        //finish();
        //System.exit(0);
    }
}
