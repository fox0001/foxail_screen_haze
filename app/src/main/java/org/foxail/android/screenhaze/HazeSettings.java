package org.foxail.android.screenhaze;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.TextView;

public class HazeSettings extends Activity {

	private final static String TAG = "ScreenHazeSettings";

    private SharedPreferences preferences;
    private TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "onCreate");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        preferences = SettingsUtil.getPreferences(getBaseContext());

        int rate = preferences.getInt(SettingsUtil.ITEM_DARKNESS_RATE, 0);
        boolean singleColor = preferences.getBoolean(SettingsUtil.ITEM_ENABLE_SINGLE_COLOR, false);

        textView = (TextView) findViewById(R.id.darknessRateTextView);
        textView.setText(getDarknessTxt(rate));
        
        SeekBar rateSeekRate = (SeekBar) findViewById(R.id.rateSeekBar);
        rateSeekRate.setProgress(rate);
        rateSeekRate.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                @Override
                public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                    textView.setText(getDarknessTxt(progress));
                    
                    SharedPreferences.Editor editor = preferences.edit();
                    editor.putInt(SettingsUtil.ITEM_DARKNESS_RATE, progress);
                    editor.commit();
                }

                @Override
                public void onStartTrackingTouch(SeekBar seekBar) {

                }

                @Override
                public void onStopTrackingTouch(SeekBar seekBar) {

                }
            });

        Switch singleColorSwitch = (Switch) findViewById(R.id.singleColorSwitch);
        singleColorSwitch.setChecked(singleColor);
        singleColorSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                    SharedPreferences.Editor editor = preferences.edit();
                    editor.putBoolean(SettingsUtil.ITEM_ENABLE_SINGLE_COLOR, b);
                    editor.commit();
                }
            });

        Button runBtn = (Button) findViewById(R.id.runBtn);
        runBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    startService(new Intent(view.getContext(), HazeService.class));
                    finish();
                }
            });

        Button exitBtn = (Button) findViewById(R.id.exitBtn);
        exitBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    finish();
                    System.exit(0);
                }
            });

        stopService(new Intent(this, HazeService.class));
    }
    
    private String getDarknessTxt(int rate) {
        return getResources().getString(R.string.txt_darkness_rate) 
            + " " + String.valueOf(rate) + "%";
    }
}
