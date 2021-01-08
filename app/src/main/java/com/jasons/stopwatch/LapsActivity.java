package com.jasons.stopwatch;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jasons.stopwatch.R;

public class LapsActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_laps);

        final LinearLayout scroll
                = (LinearLayout) findViewById(
                R.id.scroll);

        for(int i = 1; i<= MainActivity.lapTimes.size(); i++) {
            StringBuilder s = new StringBuilder("Lap " + i);
            while(s.length()<12) {
                s.append(" ");
            }

            s.append(MainActivity.lapTimes.get(i - 1));
            TextView t = new TextView(getApplicationContext());
            t.setText(s.toString());
            t.setTextColor(Color.WHITE);
            t.setTextSize(45);
            scroll.addView(t);

            TextView t2 = new TextView(getApplicationContext());
            t2.setText("----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------");
            t2.setTextColor(Color.WHITE);
            scroll.addView(t2);
        }

        StringBuilder s = new StringBuilder("Total");
        while(s.length()<13) {
            s.append(" ");
        }

        s.append(MainActivity.totalTime);
        TextView t = new TextView(getApplicationContext());
        t.setText(s.toString());
        t.setTextColor(Color.WHITE);
        t.setTextSize(45);
        scroll.addView(t);

        /* personal record code
        JSONArray arr = new JSONArray(lapTimes);
        String FILENAME = "record.json";

        try {
            FileOutputStream fos = MainActivity.this.openFileOutput(FILENAME, Context.MODE_PRIVATE);
            fos.write(arr.toString().getBytes());
            fos.close();
        } catch(Exception e) {
            e.printStackTrace();
        }
         */
    }

    public void onClickShow(View view) {
        Intent showReportIntent = new Intent(getApplicationContext(), ReportActivity.class);
        startActivity(showReportIntent);
    }
}
