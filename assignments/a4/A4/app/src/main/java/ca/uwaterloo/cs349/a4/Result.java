package ca.uwaterloo.cs349.a4;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

/**
 * Created by tony on 2018-03-27.
 */

public class Result  extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        Intent intent = getIntent();
        // Get values from intent
        int total = intent.getIntExtra("totalQuestions", 0);
        String name = intent.getExtras().getString("NAME_STRING");

        // Answers for Question 1-5
        int q1 = intent.getIntExtra("q1", 0);
        int q3 = intent.getIntExtra("q3", 0);
        int q4 = intent.getIntExtra("q4", 0);

        int q21 = intent.getIntExtra("q2-1", 0);
        int q22 = intent.getIntExtra("q2-2", 0);
        int q23 = intent.getIntExtra("q2-3", 0);
        int q24 = intent.getIntExtra("q2-4", 0);

        int q51 = intent.getIntExtra("q5-1", 0);
        int q52 = intent.getIntExtra("q5-2", 0);
        int q53 = intent.getIntExtra("q5-3", 0);
        int q54 = intent.getIntExtra("q5-4", 0);

//        Log.d("q21", q21 + "");
//        Log.d("q22", q22 + "");
//        Log.d("q23", q23 + "");
//        Log.d("q24", q24 + "");

        // Calculate marks
        int radioMarks = radioScores(q1,q3,q4);
        int q2Marks = q2Score(q21,q22,q23,q24);
        int q5Marks = q5Score(q51,q52,q53,q54);
        int totalMarks = radioMarks + q2Marks + q5Marks;

        // Set user name
        TextView userName = findViewById(R.id.userName3);
        userName.setText("Name: " + name);

        // Set result
        TextView result = findViewById(R.id.result);
        result.setText("Your Score: " + totalMarks + "/" + total);
    }

    // Return 1 if answer for question 2 is right
    int q2Score(int q21,int q22, int q23,int q24) {
        if ((q21 ==1) && (q22 == 0) && (q23 == 1) && (q24 == 0)){
            return 1;
        } else {
            return 0;
        }
    }

    // Return 1 if answer for question 5 is right
    int q5Score(int q51,int q52, int q53,int q54) {
        if ((q51 ==0) && (q52 == 0) && (q53 == 1) && (q54 == 1)){
            return 1;
        } else {
            return 0;
        }
    }

    // Return score for question1, 3, 4
    int radioScores(int q1,int q3,int q4) {
        int score = 0;
        if (q1 == 1){
            score += 1;
        }
        if (q3 == 3){
            score += 1;
        }
        if (q4 == 4){
            score += 1;
        }
        return score;
    }

    // Go back to topic selection page
    public void onClickTopicSelection(View view) {
        Intent intent = getIntent();
        String name = intent.getExtras().getString("NAME_STRING");

        Intent newIntent = new Intent(this, SelectionActivity.class);
        newIntent.putExtra("NAME_STRING", name);
        startActivity(newIntent);
    }

    // Go back to welcome page
    public void onClickLogout(View view) {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}