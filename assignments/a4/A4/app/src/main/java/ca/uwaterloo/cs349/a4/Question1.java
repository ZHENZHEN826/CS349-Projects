package ca.uwaterloo.cs349.a4;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
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
        String name = intent.getExtras().getString("NAME_STRING");

        // Set user name
        TextView userName = findViewById(R.id.userName3);
        userName.setText("Name: " + name);

        Log.d("myTag", current + "");

        // If this is the last question, change next button to finish
        if (current == total){
            Button buttonView =  findViewById(R.id.next1);
            buttonView.setText("Finish");
        }
        if (current == 1){
            Button buttonView =  findViewById(R.id.previous1);
            buttonView.setEnabled(false);
        }

        // For each question, update question title, image, and options
        TextView title = findViewById(R.id.questionTitle);
        ImageView image_view = findViewById(R.id.image1);
        if (current == 1 || current ==3 || current == 4){
            CheckBox checkboxView = (CheckBox)findViewById(R.id.checkBox1);
            checkboxView.setVisibility(View.INVISIBLE);
            checkboxView = (CheckBox)findViewById(R.id.checkBox2);
            checkboxView.setVisibility(View.INVISIBLE);
            checkboxView = (CheckBox)findViewById(R.id.checkBox3);
            checkboxView.setVisibility(View.INVISIBLE);
            checkboxView = (CheckBox)findViewById(R.id.checkBox4);
            checkboxView.setVisibility(View.INVISIBLE);

            RadioButton radioView = findViewById(R.id.radioAnswer1);
            radioView.setVisibility(View.VISIBLE);
            radioView = findViewById(R.id.radioAnswer2);
            radioView.setVisibility(View.VISIBLE);
            radioView = findViewById(R.id.radioAnswer3);
            radioView.setVisibility(View.VISIBLE);
            radioView = findViewById(R.id.radioAnswer4);
            radioView.setVisibility(View.VISIBLE);
        }
        if (current == 2 || current == 5){
            CheckBox checkboxView = (CheckBox)findViewById(R.id.checkBox1);
            checkboxView.setVisibility(View.VISIBLE);
            checkboxView = (CheckBox)findViewById(R.id.checkBox2);
            checkboxView.setVisibility(View.VISIBLE);
            checkboxView = (CheckBox)findViewById(R.id.checkBox3);
            checkboxView.setVisibility(View.VISIBLE);
            checkboxView = (CheckBox)findViewById(R.id.checkBox4);
            checkboxView.setVisibility(View.VISIBLE);

            RadioButton radioView = findViewById(R.id.radioAnswer1);
            radioView.setVisibility(View.INVISIBLE);
            radioView = findViewById(R.id.radioAnswer2);
            radioView.setVisibility(View.INVISIBLE);
            radioView = findViewById(R.id.radioAnswer3);
            radioView.setVisibility(View.INVISIBLE);
            radioView = findViewById(R.id.radioAnswer4);
            radioView.setVisibility(View.INVISIBLE);
        }
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

            int choice = intent.getIntExtra("q1", 0);
            if (choice != 0){
                SetRadioChoice(choice);
            }

        } else if (current == 2){
            title.setText("Q2: Select the countries that have these flags");
            image_view.setImageResource(R.drawable.image2);

            CheckBox checkboxView = (CheckBox)findViewById(R.id.checkBox1);
            checkboxView.setText("Brazil");

            checkboxView =  (CheckBox)findViewById(R.id.checkBox2);
            checkboxView.setText("Ivory");

            checkboxView =  (CheckBox)findViewById(R.id.checkBox3);
            checkboxView.setText("Coast");

            checkboxView =  (CheckBox)findViewById(R.id.checkBox4);
            checkboxView.setText("Slovakia");

            int box1 = intent.getIntExtra("q2-1", 0);
            int box2 = intent.getIntExtra("q2-2", 0);
            int box3 = intent.getIntExtra("q2-3", 0);
            int box4 = intent.getIntExtra("q2-4", 0);
            SetCheckBoxChoice(box1, box2, box3, box4);
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

            int choice = intent.getIntExtra("q3", 0);
            if (choice != 0){
                SetRadioChoice(choice);
            }
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

            int choice = intent.getIntExtra("q4", 0);
            if (choice != 0){
                SetRadioChoice(choice);
            }
        } else if (current == 5){
            title.setText("Q5: Select the countries that have these flags");
            image_view.setImageResource(R.drawable.image5);

            CheckBox checkboxView = (CheckBox)findViewById(R.id.checkBox1);
            checkboxView.setText("Canada");

            checkboxView =  (CheckBox)findViewById(R.id.checkBox2);
            checkboxView.setText("Taiwan");

            checkboxView =  (CheckBox)findViewById(R.id.checkBox3);
            checkboxView.setText("South Africa");

            checkboxView =  (CheckBox)findViewById(R.id.checkBox4);
            checkboxView.setText("United Kingdom");

            int box1 = intent.getIntExtra("q5-1", 0);
            int box2 = intent.getIntExtra("q5-2", 0);
            int box3 = intent.getIntExtra("q5-3", 0);
            int box4 = intent.getIntExtra("q5-4", 0);
            SetCheckBoxChoice(box1, box2, box3, box4);
        }

        TextView count = findViewById(R.id.questionCount);
        count.setText(current + "/" + total);

    }

    void SetCheckBoxChoice(int box1, int box2, int box3, int box4){
        CheckBox checkboxView = (CheckBox)findViewById(R.id.checkBox1);
        if (box1 == 1){
            checkboxView.setChecked(true);
        } else {
            checkboxView.setChecked(false);
        }

        checkboxView = (CheckBox)findViewById(R.id.checkBox2);
        if (box2 == 1){
            checkboxView.setChecked(true);
        } else {
            checkboxView.setChecked(false);
        }

        checkboxView = (CheckBox)findViewById(R.id.checkBox3);
        if (box3 == 1){
            checkboxView.setChecked(true);
        } else {
            checkboxView.setChecked(false);
        }

        checkboxView = (CheckBox)findViewById(R.id.checkBox4);
        if (box4 == 1){
            checkboxView.setChecked(true);
        } else {
            checkboxView.setChecked(false);
        }

    }

    void SetRadioChoice(int choice){

        RadioButton radioView =  findViewById(R.id.radioAnswer1);
        switch(choice){
            case 1:
                radioView.setChecked(true);
                radioView =  findViewById(R.id.radioAnswer2);
                radioView.setChecked(false);
                radioView =  findViewById(R.id.radioAnswer3);
                radioView.setChecked(false);
                radioView =  findViewById(R.id.radioAnswer4);
                radioView.setChecked(false);
                break;
            case 2:
                radioView.setChecked(false);
                radioView =  findViewById(R.id.radioAnswer2);
                radioView.setChecked(true);
                radioView =  findViewById(R.id.radioAnswer3);
                radioView.setChecked(false);
                radioView =  findViewById(R.id.radioAnswer4);
                radioView.setChecked(false);
                break;
            case 3:
                radioView.setChecked(false);
                radioView =  findViewById(R.id.radioAnswer2);
                radioView.setChecked(false);
                radioView =  findViewById(R.id.radioAnswer3);
                radioView.setChecked(true);
                radioView =  findViewById(R.id.radioAnswer4);
                radioView.setChecked(false);
                break;
            case 4:
                radioView.setChecked(false);
                radioView =  findViewById(R.id.radioAnswer2);
                radioView.setChecked(false);
                radioView =  findViewById(R.id.radioAnswer3);
                radioView.setChecked(false);
                radioView =  findViewById(R.id.radioAnswer4);
                radioView.setChecked(true);
                break;

        }
    }


    public void onClickNext(View view) {
        Intent intent = getIntent();
        Intent resultIntent = new Intent(this, Result.class);

        String name = intent.getExtras().getString("NAME_STRING");
        int current = intent.getIntExtra("currentQuestion", 0);
        int total = intent.getIntExtra("totalQuestions", 0);

        RadioGroup radioGroup = findViewById(R.id.radioGroup);
        int radio_id = radioGroup.getCheckedRadioButtonId();

        int choice = 0;
        switch (radio_id){
            case R.id.radioAnswer1:
                choice = 1;
                break;
            case R.id.radioAnswer2:
                choice = 2;
                break;
            case R.id.radioAnswer3:
                choice = 3;
                break;
            case R.id.radioAnswer4:
                choice = 4;
                break;
        }

        // store checkbox answers

        if (current == 2) {
            CheckBox box = findViewById(R.id.checkBox1);
            if (box.isChecked()) {
                intent.putExtra("q2-1", 1);
                resultIntent.putExtra("q2-1", 1);
            } else {
                intent.putExtra("q2-1", 0);
                resultIntent.putExtra("q2-1", 0);
            }

            box = findViewById(R.id.checkBox2);
            if (box.isChecked()) {
                intent.putExtra("q2-2", 1);
                resultIntent.putExtra("q2-2", 1);
            } else {
                intent.putExtra("q2-2", 0);
                resultIntent.putExtra("q2-2", 0);
            }

            box = findViewById(R.id.checkBox3);
            if (box.isChecked()) {
                intent.putExtra("q2-3", 1);
                resultIntent.putExtra("q2-3", 1);
            } else {
                intent.putExtra("q2-3", 0);
                resultIntent.putExtra("q2-3", 0);
            }

            box = findViewById(R.id.checkBox4);
            if (box.isChecked()) {
                intent.putExtra("q2-4", 1);
                resultIntent.putExtra("q2-4", 1);
            } else {
                intent.putExtra("q2-4", 0);
                resultIntent.putExtra("q2-4", 0);
            }
        }  else if (current == 5){
            CheckBox box = findViewById(R.id.checkBox1);
            if (box.isChecked()) {
                intent.putExtra("q5-1", 1);
                resultIntent.putExtra("q5-1", 1);
            } else {
                intent.putExtra("q5-1", 0);
                resultIntent.putExtra("q5-1", 0);
            }

            box = findViewById(R.id.checkBox2);
            if (box.isChecked()) {
                intent.putExtra("q5-2", 1);
                resultIntent.putExtra("q5-2", 1);
            } else {
                intent.putExtra("q5-2", 0);
                resultIntent.putExtra("q5-2", 0);
            }

            box = findViewById(R.id.checkBox3);
            if (box.isChecked()) {
                intent.putExtra("q5-3", 1);
                resultIntent.putExtra("q5-3", 1);
            }else {
                intent.putExtra("q5-3", 0);
                resultIntent.putExtra("q5-3", 0);
            }

            box = findViewById(R.id.checkBox4);
            if (box.isChecked()) {
                intent.putExtra("q5-4", 1);
                resultIntent.putExtra("q5-4", 1);
            } else {
                intent.putExtra("q5-4", 0);
                resultIntent.putExtra("q5-4", 0);
            }
        }

        // Store radio questions' answers
        if (current == 1){
            intent.putExtra("q1", choice);
            resultIntent.putExtra("q1", choice);
        } else if (current == 3){
            intent.putExtra("q3", choice);
            resultIntent.putExtra("q3", choice);
        } else if (current == 4){
            intent.putExtra("q4", choice);
            resultIntent.putExtra("q4", choice);
        }

        if (current == total){
            // Go to result page
            resultIntent.putExtra("NAME_STRING", name);
            resultIntent.putExtra("totalQuestions", total);

            int q1 = intent.getIntExtra("q1", 0);
            int q3 = intent.getIntExtra("q3", 0);
            int q4 = intent.getIntExtra("q4", 0);
            resultIntent.putExtra("q1", q1);
            resultIntent.putExtra("q3", q3);
            resultIntent.putExtra("q4", q4);

            int q21 = intent.getIntExtra("q2-1", 0);
            int q22 = intent.getIntExtra("q2-2", 0);
            int q23 = intent.getIntExtra("q2-3", 0);
            int q24 = intent.getIntExtra("q2-4", 0);
            resultIntent.putExtra("q2-1", q21);
            resultIntent.putExtra("q2-2", q22);
            resultIntent.putExtra("q2-3", q23);
            resultIntent.putExtra("q2-4", q24);

            int q51 = intent.getIntExtra("q5-1", 0);
            int q52 = intent.getIntExtra("q5-2", 0);
            int q53 = intent.getIntExtra("q5-3", 0);
            int q54 = intent.getIntExtra("q5-4", 0);
            resultIntent.putExtra("q5-1", q51);
            resultIntent.putExtra("q5-2", q52);
            resultIntent.putExtra("q5-3", q53);
            resultIntent.putExtra("q5-4", q54);

            startActivity(resultIntent);
        } else {
            // Go to next question
            intent.putExtra("currentQuestion", current+1);
            finish();
            startActivity(intent);
        }

    }

    public void onClickPrevious(View view) {
        Intent intent = getIntent();
        int total = intent.getIntExtra("totalQuestions", 0);
        int current = intent.getIntExtra("currentQuestion", 0);

        intent.putExtra("currentQuestion", current-1);
        finish();
        startActivity(intent);
    }

    public void onClickLogout(View view) {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

}
