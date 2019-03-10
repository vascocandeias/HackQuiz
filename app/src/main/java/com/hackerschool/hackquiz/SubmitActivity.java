package com.hackerschool.hackquiz;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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

        Bundle b = getIntent().getExtras();
        total = b.getInt("correct");
        sec = b.getInt("sec");
        min = b.getInt("min");
        totalSec = b.getInt("totalSec");
        id = b.getInt("id");

        mTextViewResults = findViewById(R.id.results);
        mTextViewResults.setText("You answered " + String.valueOf(total) + " questions correctly in " + String.valueOf(min) + ":" + String.valueOf(sec));

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

    private void goHome(){

    }
}
