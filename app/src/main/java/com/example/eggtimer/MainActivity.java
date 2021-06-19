package com.example.eggtimer;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    private SeekBar eggTimerSeekBar;
    private TextView eggTimerDisplayTextView;
    private Button eggTimerTriggerButton;

    private void initialise() {
        eggTimerSeekBar = findViewById(R.id.eggTimerSeekBar);
        eggTimerDisplayTextView = findViewById(R.id.eggTimerDisplayTextView);
        eggTimerTriggerButton = findViewById(R.id.eggTimerTriggerButton);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initialise();
    }
}