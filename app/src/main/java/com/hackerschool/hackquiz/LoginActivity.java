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
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends AppCompatActivity {

    private EditText mEditTextName;
    private EditText mEditTextCourse;
    private EditText mEditTextId;
    private EditText mEditTextMail;
    private RequestQueue mQueue;

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
        String url = "http://localhost:8081";

        String postUrl = url + "/PlayerInformation";

        final StringRequest request = new StringRequest(Request.Method.POST, postUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Toast.makeText(getApplicationContext(), response, Toast.LENGTH_SHORT).show();
                //aqui vai-se passar à outra atividade ou pedir as perguntas... maybe perguntas
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
