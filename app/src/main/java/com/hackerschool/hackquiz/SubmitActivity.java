package com.hackerschool.hackquiz;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class SubmitActivity extends AppCompatActivity {

    private int total = 0, sec = 0, min = 0, totalSec, id;
    private RequestQueue mQueue;
    private TextView mTextViewResults;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_submit);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Bundle b = getIntent().getExtras();
        total = b.getInt("correct");
        sec = b.getInt("sec");
        min = b.getInt("min");
        totalSec = b.getInt("totalSec");
        id = b.getInt("id");

        mTextViewResults = findViewById(R.id.results);
        mTextViewResults.setText("You answered " + String.valueOf(total) + " questions correctly in " + String.valueOf(min) + ":" + String.valueOf(sec));

        //mTextViewResults.setText("time: " + String.valueOf(totalSec) + " correct: " + String.valueOf(total) + " code: " + String.valueOf(id) + " id: 4444");

        mQueue = Volley.newRequestQueue(this);
        String submitUrl = getString(R.string.URL) + "/results";



        final StringRequest submit = new StringRequest (Request.Method.POST, submitUrl,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        })
        {
            @Override
            protected Map<String, String> getParams() {
                Map<String,String> results = new HashMap<>();
                results.put("time", String.valueOf(totalSec));
                results.put("code", String.valueOf(id));
                results.put("correct", String.valueOf(total));
                results.put("id","4444");
                return results;
            }
        };

        mQueue.add(submit);
    }

    public void goHome(View v){
        Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
        startActivity(intent);


        /*int mPendingIntentId = 123456;
        PendingIntent mPendingIntent = PendingIntent.getActivity(getApplicationContext(), mPendingIntentId, intent, PendingIntent.FLAG_CANCEL_CURRENT);
        AlarmManager mgr = (AlarmManager)getApplicationContext().getSystemService(Context.ALARM_SERVICE);
        mgr.set(AlarmManager.RTC, System.currentTimeMillis() + 100, mPendingIntent);
        System.exit(0);*/
    }
}
