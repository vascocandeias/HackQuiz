package com.hackerschool.hackquiz;

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

public class LoginActivity extends AppCompatActivity {

    private EditText mEditTextName;
    private EditText mEditTextCourse;
    private EditText mEditTextId;
    private EditText mEditTextMail;
    private RequestQueue mQueue;
    private String url = "http://localhost:8081";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mEditTextName = findViewById(R.id.login_name);
        mEditTextCourse = findViewById(R.id.login_course);
        mEditTextId = findViewById(R.id.login_id);
        mEditTextMail = findViewById(R.id.login_mail);
        Button buttonLogin = findViewById(R.id.login_button);
        mQueue = Volley.newRequestQueue(this);
        buttonLogin.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                submitLogin();
            }
        });
    }

    private void submitLogin(){

        String postUrl = url + "/PlayerInformation";

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
}
