package com.jasons.stopwatch;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import java.util.ArrayList;

public class ReportActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report);

        ArrayList<String> lapTimes = MainActivity.lapTimes;

        TableRow.LayoutParams params = new TableRow.LayoutParams(TableLayout.LayoutParams.WRAP_CONTENT,TableLayout.LayoutParams.WRAP_CONTENT);
        params.setMargins(1, 1, 1, 1);

        TableLayout layout = (TableLayout)findViewById(R.id.table);
        layout.setBackgroundColor(Color.BLACK);

        for(String lap:lapTimes) {
            TableRow row = new TableRow(this);
            row.setBackgroundColor(Color.BLACK);

            for(int i=0; i<3; i++) {
                TextView textView = new TextView(this);
                textView.setBackgroundColor(Color.WHITE);
                textView.setTextColor(Color.BLACK);
                textView.setText(lap);
                textView.setGravity(Gravity.CENTER);
                textView.setLayoutParams(params);
                row.addView(textView,params);
            }

            layout.addView(row);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    public void onClickSchedule(View view) {
        Intent createScheduleIntent = new Intent(getApplicationContext(), ScheduleActivity.class);
        startActivity(createScheduleIntent);
    }

}
