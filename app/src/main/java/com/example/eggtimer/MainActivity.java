package com.example.eggtimer;

import androidx.appcompat.app.AppCompatActivity;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;

import java.time.Duration;

public class MainActivity extends AppCompatActivity {
    // Views
    private SeekBar eggTimerSeekBar;
    private TextView eggTimerDisplayTextView;

    // Variables
    private long eggTimerDefaultValueMillis;
    private long eggTimerMaxValueMillis;
    private boolean isTimerOngoing;
    private boolean isResetTimerRequired;
    private MediaPlayer mediaPlayer;
    private CountDownTimer countDownTimer;

    private void initialise() {
        eggTimerSeekBar = findViewById(R.id.eggTimerSeekBar);
        eggTimerDisplayTextView = findViewById(R.id.eggTimerDisplayTextView);

        eggTimerDefaultValueMillis = 30*1000; // 30 seconds
        eggTimerMaxValueMillis = 10*60*1000; // 10 minutes
        isTimerOngoing = false;
        isResetTimerRequired = false;
        mediaPlayer = MediaPlayer.create(getApplicationContext(), R.raw.airhorn);
    }

    private void updateTimerDisplay(long timerDurationMillis) {
        Duration d = Duration.ofMillis(timerDurationMillis);
        long minutes = d.toMinutes();
        long seconds = (d.toMillis()/1000L)%60L;
        eggTimerDisplayTextView.setText(String.format("%02d:%02d", minutes, seconds));
    }

    public void triggerEggTimer(View view) {
        if (!isTimerOngoing) { /* Timer yet to start, starts after this click */
            ((Button) view).setText("Stop"); // After click, set button text to "Stop"
            eggTimerSeekBar.setEnabled(false); // Disable the SeekBar
            isTimerOngoing = true; // Since, now the Timer is on-going

            /*
            If we are using the Offset value 100 milliseconds to compensate for the amount of value
            lost in the 'progress' property of the SeekBar which is about 50-100 milliseconds or so.
            This time is lost till the time control flow of the program reaches to the following line
            where progress is SeekBar is accessed by - eggTimerSeekBar.getProgress()
            */
            countDownTimer = new CountDownTimer(eggTimerSeekBar.getProgress() + 100 /* Offset value */,
                    1000) {
                @Override
                public void onTick(long millisUntilFinished) {
                    // Keep changing the timer display in a Timely manner
                    updateTimerDisplay(millisUntilFinished);
                }
                @Override
                public void onFinish() {
                    mediaPlayer.start(); // Play horn after timer completed
                    ((Button) view).setText("Set Timer");
                    isResetTimerRequired = true; // SeekBar is still disabled, so reset is required
                }
            };
            countDownTimer.start();
        }
        else { /* Timer is either (on-going) OR (was completed and reset is required) */
            ((Button) view).setText("Go!"); // After click, set button text to "Go!"
            eggTimerSeekBar.setEnabled(true); // Enable the SeekBar

            // Setting Timer display to default value
            eggTimerSeekBar.setProgress((int) eggTimerDefaultValueMillis);
            updateTimerDisplay(eggTimerDefaultValueMillis);

            if (isResetTimerRequired) {
                isResetTimerRequired = false; // Timer reset completed
            }
            else {
                isTimerOngoing = false;
                countDownTimer.cancel();
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialise views and variables
        initialise();

        // Initialise SeekBar to default value
        eggTimerSeekBar.setMax((int) eggTimerMaxValueMillis);
        eggTimerSeekBar.setProgress((int) eggTimerDefaultValueMillis);
        updateTimerDisplay(eggTimerDefaultValueMillis);

        // Setting SeekBar onClickListener
        eggTimerSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                Log.i("SeekBar Changed", "progress = " + progress);
                eggTimerSeekBar.setProgress(progress);
                updateTimerDisplay((long) progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {}
        });
    }
}