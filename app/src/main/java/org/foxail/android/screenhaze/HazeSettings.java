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

public class HazeSettings extends Activity {

	private final static String TAG = "ScreenHazeSettings";

    private SharedPreferences preferences;
    private EditText rateNum;
    private SeekBar rateSeekRate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "onCreate");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        preferences = SettingsUtil.getPreferences(getBaseContext());

        initView();

        stopService(new Intent(this, HazeService.class));
    }

    private void initView() {
        int rate = preferences.getInt(SettingsUtil.ITEM_DARKNESS_RATE, 0);
        boolean singleColor = preferences.getBoolean(SettingsUtil.ITEM_ENABLE_SINGLE_COLOR, false);

        rateNum = (EditText) findViewById(R.id.rateNum);
        rateNum.setText(rate);
        rateNum.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                int num = Integer.getInteger(rateNum.getText().toString(), 0);
                rateSeekRate.setProgress(num);

                SharedPreferences.Editor editor = preferences.edit();
                editor.putInt(SettingsUtil.ITEM_DARKNESS_RATE, num);
                editor.commit();
            }
        });

        rateSeekRate = (SeekBar) findViewById(R.id.rateSeekRate);
        rateSeekRate.setProgress(rate);
        rateSeekRate.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if(fromUser) {
                    EditText rateNum = (EditText) seekBar.findViewById(R.id.rateNum);
                    rateNum.setText(i);
                }
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
    }
}
