package ca.uwaterloo.cs349.a4;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;

/**
 * Created by tony on 2018-03-27.
 */

public class Question1  extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_questions);

        // Get the intent that started this activity
        Intent intent = getIntent();
        // extract the intent value in int
        int total = intent.getIntExtra("totalQuestions", 0);
        int current = intent.getIntExtra("currentQuestion", 0);

        Log.d("myTag", current + "");

        // If this is the last question, change next button to finish
        if (current == total){
            Button buttonView =  findViewById(R.id.next1);
            buttonView.setText("Finish");
        }

        // For each question, update question title, image, and options
        TextView title = findViewById(R.id.questionTitle);
        ImageView image_view = findViewById(R.id.image1);
        if (current == 1){
            title.setText("Q1: Select the country that has this flag");
            image_view.setImageResource(R.drawable.image1);

            RadioButton radioView =  findViewById(R.id.radioAnswer1);
            radioView.setText("Canada");

            radioView =  findViewById(R.id.radioAnswer2);
            radioView.setText("Taiwan");

            radioView =  findViewById(R.id.radioAnswer3);
            radioView.setText("China");

            radioView =  findViewById(R.id.radioAnswer4);
            radioView.setText("Peru");
        } else if (current == 2){
            title.setText("Q2: Select the countries that have these flags");
            image_view.setImageResource(R.drawable.image2);

            RadioButton radioView =  findViewById(R.id.radioAnswer1);
            radioView.setText("Brazil");

            radioView =  findViewById(R.id.radioAnswer2);
            radioView.setText("Ivory");

            radioView =  findViewById(R.id.radioAnswer3);
            radioView.setText("Coast");

            radioView =  findViewById(R.id.radioAnswer4);
            radioView.setText("Slovakia");
        } else if (current == 3){
            title.setText("Q3: Select the country that has this flag");
            image_view.setImageResource(R.drawable.image3);

            RadioButton radioView =  findViewById(R.id.radioAnswer1);
            radioView.setText("Netherlands");

            radioView =  findViewById(R.id.radioAnswer2);
            radioView.setText("Taiwan");

            radioView =  findViewById(R.id.radioAnswer3);
            radioView.setText("China");

            radioView =  findViewById(R.id.radioAnswer4);
            radioView.setText("Slovakia");
        } else if (current == 4){
            title.setText("Q4: Select the country that has this flag");
            image_view.setImageResource(R.drawable.image4);

            RadioButton radioView =  findViewById(R.id.radioAnswer1);
            radioView.setText("Canada");

            radioView =  findViewById(R.id.radioAnswer2);
            radioView.setText("India");

            radioView =  findViewById(R.id.radioAnswer3);
            radioView.setText("Brazil");

            radioView =  findViewById(R.id.radioAnswer4);
            radioView.setText("South Korea");
        } else if (current == 5){
            title.setText("Q5: Select the countries that have these flags");
            image_view.setImageResource(R.drawable.image5);

            RadioButton radioView =  findViewById(R.id.radioAnswer1);
            radioView.setText("Canada");

            radioView =  findViewById(R.id.radioAnswer2);
            radioView.setText("Taiwan");

            radioView =  findViewById(R.id.radioAnswer3);
            radioView.setText("South Africa");

            radioView =  findViewById(R.id.radioAnswer4);
            radioView.setText("United Kingdom");
        }

        TextView count = findViewById(R.id.questionCount);
        count.setText(current + "/" + total);

    }

    public void onClickNext(View view) {
        Intent intent = getIntent();
        int total = intent.getIntExtra("totalQuestions", 0);
        int current = intent.getIntExtra("currentQuestion", 0);

        if (current == total){
            // Go to result page

        } else {
            // Go to next question
            intent.putExtra("currentQuestion", current+1);
            finish();
            startActivity(intent);
        }

    }

}
