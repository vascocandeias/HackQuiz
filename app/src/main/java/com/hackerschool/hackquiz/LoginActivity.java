package com.hackerschool.hackquiz;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class LoginActivity extends AppCompatActivity {

    private EditText mEditTextName;
    private EditText mEditTextCourse;
    private EditText mEditTextId;
    private EditText mEditTextMail;
    private TextView mTextViewPlayer;

    private RequestQueue mQueue;
    private int mPlayerCode = 0;
    private int canPlay = 0;
    Button buttonLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mEditTextName = findViewById(R.id.login_name);
        mEditTextCourse = findViewById(R.id.login_course);
        mEditTextId = findViewById(R.id.login_id);
        mEditTextMail = findViewById(R.id.login_mail);
        mTextViewPlayer = findViewById(R.id.you_are);

        mQueue = Volley.newRequestQueue(this);
        buttonLogin = findViewById(R.id.login_button);
        buttonLogin.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                submitLogin();
            }
        });
    }

    private void submitLogin(){

        String paramUrl = getString(R.string.URL) + "/index";

        final StringRequest requestIndex = new StringRequest (Request.Method.POST, paramUrl,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try{
                            Scanner scanner = new Scanner(response);
                            mPlayerCode = scanner.nextInt();
                            Toast.makeText(getApplicationContext(),String.valueOf(mPlayerCode),Toast.LENGTH_SHORT).show();
                        }catch (Exception e){
                            // TODO: erroor
                        }
                        changeActivity();
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
                Map<String,String> loginInfo = new HashMap<>();
                loginInfo.put("name", mEditTextName.getText().toString());
                loginInfo.put("course", mEditTextCourse.getText().toString());
                loginInfo.put("id", mEditTextId.getText().toString());
                loginInfo.put("mail", mEditTextMail.getText().toString());
                return loginInfo;
            }
        };

        mQueue.add(requestIndex);
    }

    private void changeActivity(){

        if(mPlayerCode == 0){
            Toast.makeText(getApplicationContext(),"You cannot play, wait for your turn",Toast.LENGTH_SHORT).show();
            return;
        }


        hideEverything();

        mTextViewPlayer.setText("You are player number " + mPlayerCode);
        mTextViewPlayer.setVisibility(View.VISIBLE);

        Handler h = new Handler();

        h.postDelayed(new Runnable() {

            @Override
            public void run() {

                switch (mPlayerCode){
                    case 1 :
                        mTextViewPlayer.setVisibility(View.GONE);
                        findViewById(R.id.loading).setVisibility(View.VISIBLE);
                        tryAgain();
                        break;
                    case 2 :
                        Intent intent = new Intent(getApplicationContext(), QuestionActivity.class);
                        intent.putExtra("id", mPlayerCode);
                        startActivity(intent);
                        break;
                }

            }
        }, 2000);
    }

    private void tryAgain(){

        String loadingUrl = getString(R.string.URL) + "/wait";

        Toast.makeText(getApplicationContext(),"Trying",Toast.LENGTH_SHORT).show();

        final StringRequest requestWait = new StringRequest(Request.Method.GET, loadingUrl,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        Scanner scanner = new Scanner(response);
                        canPlay = scanner.nextInt();

                        Toast.makeText(getApplicationContext(),String.valueOf(canPlay),Toast.LENGTH_SHORT).show();

                        if(canPlay == 1){
                            Intent intent = new Intent(getApplicationContext(), QuestionActivity.class);
                            intent.putExtra("id", mPlayerCode);
                            startActivity(intent);
                        } else {
                            tryAgain();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });
        mQueue.add(requestWait);
    }

    private void hideEverything(){
        findViewById(R.id.everything).setVisibility(View.GONE);
        buttonLogin.setVisibility(View.GONE);
    }
}
