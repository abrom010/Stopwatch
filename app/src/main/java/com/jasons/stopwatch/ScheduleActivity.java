package com.jasons.stopwatch;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDialogFragment;

import java.util.Stack;

public class ScheduleActivity extends AppCompatActivity implements AddDialog.DialogListener {
    LinearLayout scroll;
    TextView t;
    Stack<TextView> stack = new Stack<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule);
        scroll = (LinearLayout) findViewById(R.id.insert);
    }

    public void onClickAdd(View view) {
        AddDialog dialog = new AddDialog();
        dialog.show(getSupportFragmentManager(),"dialog");
    }

    public void onClickDelete(View view) {
        if(stack.empty()) return;
        scroll.removeView(stack.pop());
    }

    @Override
    public void apply(String text) {
        t = new TextView(getApplicationContext());
        stack.push(t);
        t.setText(text);
        scroll.addView(t);
        Log.d("debug","added");
    }
}
