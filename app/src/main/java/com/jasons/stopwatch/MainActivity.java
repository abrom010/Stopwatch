package com.jasons.stopwatch;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.jasons.stopwatch.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Locale;

public class MainActivity extends Activity {
    private long lastTime;
    private double seconds = 0;
    private double lapSeconds = 0;
    private boolean running;
    private boolean wasRunning;
    public static String totalTime = "";

    protected static ArrayList<String> lapTimes = new ArrayList<>();

    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        int action = event.getAction();
        int keyCode = event.getKeyCode();

        if (keyCode == KeyEvent.KEYCODE_VOLUME_UP) {
            if (action == KeyEvent.ACTION_DOWN) {
                if(!running) {
                    onClickStart((Button) findViewById(R.id.start_button));
                } else {
                    onClickLap((Button) findViewById(R.id.lap_button));
                }
                return true;
            }
        }
        return super.dispatchKeyEvent(event);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (savedInstanceState != null) {
            seconds
                    = savedInstanceState
                    .getDouble("seconds");
            running
                    = savedInstanceState
                    .getBoolean("running");
            wasRunning
                    = savedInstanceState
                    .getBoolean("wasRunning");
        }

        final TextView lapView
                = (TextView)findViewById(
                R.id.last_lap);

        lapView.setText("0.0");
        runTimer();
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        savedInstanceState
                .putDouble("seconds", seconds);
        savedInstanceState
                .putBoolean("running", running);
        savedInstanceState
                .putBoolean("wasRunning", wasRunning);
    }

    @Override
    protected void onPause() {
        super.onPause();
        wasRunning = running;
        running = false;
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (wasRunning) {
            running = true;
        }
    }

    public void onClickStart(View view) {
        lastTime = System.currentTimeMillis();
        running = true;
    }

    public void onClickLap(View view) {
        double temp = lapSeconds;
        lapSeconds = 0;
        final Handler handler
                = new Handler();

        int hours = (int)temp / 3600;
        int minutes = ((int)temp % 3600) / 60;
        int secs = (int)temp % 60;

        String doubleAsString = String.valueOf(temp);
        int indexOfDecimal = doubleAsString.indexOf(".");
        String decimal = doubleAsString.substring(indexOfDecimal, Math.min(indexOfDecimal + 3, doubleAsString.length()));

        String fTime
                = String
                .format(Locale.getDefault(),
                        "%d:%02d:%02d", hours,
                        minutes, secs);
        if(decimal.length()<3) decimal += "0";
        fTime += decimal;

        lapTimes.add(fTime);

        final TextView lapView
                = (TextView)findViewById(
                R.id.last_lap);
        final TextView timeView
                = (TextView)findViewById(
                R.id.time_view);
        final TextView timeLap
                = (TextView)findViewById(
                R.id.time_lap);
        final Button startButton
                = (Button)findViewById(
                R.id.start_button);
        final Button lapButton
                = (Button)findViewById(
                R.id.lap_button);
        final Button stopButton
                = (Button)findViewById(
                R.id.stop_button);
        final Button resetButton
                = (Button)findViewById(
                R.id.reset_button);
        final Button showButton
                = (Button)findViewById(
                R.id.show_button);

        DecimalFormat df = new DecimalFormat("0.0");

        String time = df.format(temp);
        if(time.length()>3) {
            time = time.substring(1);
        }

        lapView.setText(time);
        lapView.setVisibility(View.VISIBLE);
        timeView.setVisibility(View.INVISIBLE);
        timeLap.setVisibility(View.INVISIBLE);
        startButton.setVisibility(View.INVISIBLE);
        lapButton.setVisibility(View.INVISIBLE);
        stopButton.setVisibility(View.INVISIBLE);
        resetButton.setVisibility(View.INVISIBLE);
        showButton.setVisibility(View.INVISIBLE);


        final Runnable r = new Runnable() {
            @Override
            public void run() {
                lapView.setVisibility(View.INVISIBLE);
                timeView.setVisibility(View.VISIBLE);
                timeLap.setVisibility(View.VISIBLE);
                startButton.setVisibility(View.VISIBLE);
                lapButton.setVisibility(View.VISIBLE);
                stopButton.setVisibility(View.VISIBLE);
                resetButton.setVisibility(View.VISIBLE);
                showButton.setVisibility(View.VISIBLE);
            }
        };
        handler.postDelayed(r, 8000);
    }

    public void onClickStop(View view) {
        if(!running) return;
        running = false;

        int hours = (int)lapSeconds / 3600;
        int minutes = ((int)lapSeconds % 3600) / 60;
        int secs = (int)lapSeconds % 60;

        String doubleAsString = String.valueOf(lapSeconds);
        int indexOfDecimal = doubleAsString.indexOf(".");
        String decimal = doubleAsString.substring(indexOfDecimal, Math.min(indexOfDecimal + 3, doubleAsString.length()));

        String fTime
                = String
                .format(Locale.getDefault(),
                        "%d:%02d:%02d", hours,
                        minutes, secs);
        if(decimal.length()<3) decimal += "0";
        fTime += decimal;

        lapTimes.add(fTime);
    }

    public void onClickReset(View view) {
        lapTimes = new ArrayList<>();
        running = false;
        seconds = 0;
        lapSeconds = 0;
    }

    public void onClickShow(View view) {
        if(running) {
            onClickStop((Button)findViewById(R.id.stop_button));
        }

        Intent showLapsIntent = new Intent(getApplicationContext(), LapsActivity.class);
        startActivity(showLapsIntent);
    }

    private void runTimer() {
        // Get the text views.
        final TextView timeView
                = (TextView)findViewById(
                R.id.time_view);

        final TextView lapView
                = (TextView)findViewById(
                R.id.time_lap);

        // Creates a new Handler
        final Handler handler
                = new Handler();

        handler.post(new Runnable() {
            @Override
            public void run() {
                if(running) {
                    long currentTime = System.currentTimeMillis();
                    long timeDiff = currentTime - lastTime;

                    lastTime = currentTime;

                    double secondsPast = timeDiff / 1000.0;
                    seconds += secondsPast;
                    lapSeconds += secondsPast;
                }

                /* TOTAL TIME */
                int hours = (int)seconds / 3600;
                int minutes = ((int)seconds % 3600) / 60;
                int secs = (int)seconds % 60;

                String doubleAsString = String.valueOf(seconds);
                int indexOfDecimal = doubleAsString.indexOf(".");
                String decimal = doubleAsString.substring(indexOfDecimal, Math.min(indexOfDecimal + 3, doubleAsString.length()));

                totalTime
                        = String
                        .format(Locale.getDefault(),
                                "%d:%02d:%02d", hours,
                                minutes, secs);
                if(decimal.length()<3) decimal += "0";
                totalTime += decimal;

                timeView.setText(totalTime);
                /* TOTAL TIME */

                /* LAP TIME */
                hours = (int)lapSeconds / 3600;
                minutes = ((int)lapSeconds % 3600) / 60;
                secs = (int)lapSeconds % 60;

                doubleAsString = String.valueOf(lapSeconds);
                indexOfDecimal = doubleAsString.indexOf(".");
                decimal = doubleAsString.substring(indexOfDecimal, Math.min(indexOfDecimal + 3, doubleAsString.length()));

                String time
                        = String
                        .format(Locale.getDefault(),
                                "%d:%02d:%02d", hours,
                                minutes, secs);
                if(decimal.length()<3) decimal += "0";
                time += decimal;

                lapView.setText(time);
                /* LAP TIME */

                handler.postDelayed(this, 16);

            }
        });
    }

}
