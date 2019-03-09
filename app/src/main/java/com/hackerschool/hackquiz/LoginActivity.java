package com.hackerschool.hackquiz;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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
    private RequestQueue mQueue = Volley.newRequestQueue(this);
    private int mPlayerCode = 1;
    private boolean canPlay = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mEditTextName = findViewById(R.id.login_name);
        mEditTextCourse = findViewById(R.id.login_course);
        mEditTextId = findViewById(R.id.login_id);
        mEditTextMail = findViewById(R.id.login_mail);
        Button buttonLogin = findViewById(R.id.login_button);
        buttonLogin.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                submitLogin();
            }
        });
    }

    private void submitLogin(){

        String paramUrl = R.string.URL + "/index";

        final StringRequest requestIndex = new StringRequest (Request.Method.POST, paramUrl,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try{
                            Scanner scanner = new Scanner(response);
                            mPlayerCode = scanner.nextInt();
                          //  Toast.makeText(getApplicationContext(),String.valueOf(mPlayerCode),Toast.LENGTH_SHORT).show();
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

        // TODO: dizer quem é. You're player number X get ready

        switch (mPlayerCode){
            case 0 : // TODO: can't play. reload app
                break;
            case 1 :
                tryAgain();
                break;
            case 2 :
                Intent intent = new Intent(getApplicationContext(), QuestionActivity.class);
                startActivity(intent);
                break;
        }
    }

    private void tryAgain(){

        String loadingUrl = R.string.URL + "/wait";

        final StringRequest requestWait = new StringRequest(Request.Method.GET, loadingUrl,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        Scanner scanner = new Scanner(response);
                        canPlay = scanner.nextBoolean();

                        if(canPlay){
                            Intent intent = new Intent(getApplicationContext(), QuestionActivity.class);
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





/* //version where we get the questions
    private void submitLogin(){

        String postUrl = URL + "/PlayerInformation";

        final JsonArrayRequest request = new JsonArrayRequest(Request.Method.POST, postUrl, null,
                new Response.Listener<JSONArray>() {
                @Override
                public void onResponse(JSONArray response) {

                    try {
                        for (int i = 0; i < response.length(); i++){

                            JSONObject questionDetails = response.getJSONObject(i);

                            String question = questionDetails.getString("question");
                            String answer1 = questionDetails.getString("answer1");
                            String answer2 = questionDetails.getString("answer2");
                            String answer3 = questionDetails.getString("answer3");
                            String answer4 = questionDetails.getString("answer4");
                            int correctAnswer = questionDetails.getInt("correct_answer");

                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        }){
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

        mQueue.add(request);
    }
    */


}
