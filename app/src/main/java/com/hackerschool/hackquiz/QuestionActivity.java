package com.hackerschool.hackquiz;

import android.content.Intent;
import android.os.CountDownTimer;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class QuestionActivity extends AppCompatActivity {

    private RequestQueue mQueue;
    private String[] question;
    private String[][] answers;
    int[] correctAnswer;
    private TextView questionView;
    private TextView[] answerViews = new TextView[4];
    private int curQuestion = 0;
    private int total = 0, sec = 0, min = 0, totalSec = 0, id;
    private TextView timeView;
    private CountDownTimer start;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question);
        String questionsUrl = getString(R.string.URL) + "/questions";
        mQueue = Volley.newRequestQueue(this);
        questionView = findViewById(R.id.question);
        answerViews[0] = findViewById(R.id.answer1);
        answerViews[1] = findViewById(R.id.answer2);
        answerViews[2] = findViewById(R.id.answer3);
        answerViews[3] = findViewById(R.id.answer4);
        timeView = findViewById(R.id.time);

        Bundle b = getIntent().getExtras();
        id = b.getInt("totalSec");

        start = new CountDownTimer(300000000, 1000) {
            public void onTick(long millisUntilFinished) {

                sec++;
                totalSec++;
                if (sec == 59) {
                    min++;
                    sec = 0;
                }

                timeView.setText(String.valueOf(min) + ":" + String.valueOf(sec));
            }

            public void onFinish() {
                Log.e("TIMER:", "Finished");
            }
        };

        final JsonArrayRequest requestQuestions = new JsonArrayRequest(Request.Method.GET, questionsUrl, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {

                        question = new String[response.length()];
                        answers = new String[response.length()][4];
                        correctAnswer = new int[response.length()];

                        try {
                            for (int i = 0; i < response.length(); i++){
                                JSONObject questionDetails = response.getJSONObject(i);

                                question[i] = questionDetails.getString("question");
                                answers[i][0] = questionDetails.getString("answer1");
                                answers[i][1] = questionDetails.getString("answer2");
                                answers[i][2] = questionDetails.getString("answer3");
                                answers[i][3] = questionDetails.getString("answer4");
                                correctAnswer[i] = questionDetails.getInt("correct_answer") - 1;
                                //correctAnswer[i] = 0;
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        setQuestions();
                        start.start();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });

        mQueue.add(requestQuestions);

    }

    public void setQuestions(){
        questionView.setText(question[curQuestion]);
        for(int j = 0; j < answerViews.length; j++){
        //    Toast.makeText(getApplicationContext(),String.valueOf(j),Toast.LENGTH_SHORT).show();
            answerViews[j].setBackgroundColor(getResources().getColor(R.color.colorAccent));
            answerViews[j].setText(answers[curQuestion][j]);
        }
    }

    public void clicked(View v){
        int m = Integer.parseInt(v.getTag().toString());

        if(m == correctAnswer[curQuestion]){
            answerViews[m].setBackgroundColor(getResources().getColor(R.color.colorPrimary));
            total++;
        } else {
            answerViews[m].setBackgroundColor(getResources().getColor(R.color.colorPrimaryDark));
            answerViews[correctAnswer[curQuestion]].setBackgroundColor(getResources().getColor(R.color.colorPrimary));
        }

        Toast.makeText(getApplicationContext(),String.valueOf(total),Toast.LENGTH_SHORT).show();


        Handler h = new Handler();

        h.postDelayed(new Runnable() {

            @Override
            public void run() {
                if(curQuestion++ < answerViews.length) setQuestions();
                else{
                    start.cancel();
                    Toast.makeText(getApplicationContext(),String.valueOf(min) + ":" + String.valueOf(sec),Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(getApplicationContext(), SubmitActivity.class);
                    intent.putExtra("correct", total);
                    intent.putExtra("sec", sec);
                    intent.putExtra("min", min);
                    intent.putExtra("totalSec", totalSec);
                    intent.putExtra("id", id);
                    startActivity(intent);
                }
            }
        }, 1000);
    }

}
