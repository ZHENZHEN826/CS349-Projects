package ca.uwaterloo.cs349.a4;

import android.support.v7.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

/**
 * Created by tony on 2018-03-27.
 */

public class SelectionActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selection);

        // Get user's name
        String newString;
        if (savedInstanceState == null) {
            // Get the intent that started this activity
            Intent intent = getIntent();
            // extract the intent value
            Bundle extras = intent.getExtras();//("NAME", 0);

            if (extras == null) {
                newString = null;
            } else {
                newString = extras.getString("NAME_STRING");
            }
        } else {
            newString= (String) savedInstanceState.getSerializable("NAME_STRING");
        }

        // Add user's name behind "Welcome"
        TextView welcome = findViewById(R.id.welcome);
        welcome.setText("Welcome " + newString);

        // set spinner value
        Spinner spinner = findViewById(R.id.number_spinner);
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.number_array, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        spinner.setAdapter(adapter);
    }

    // Load first question
    public void onClickLoad(View view) {
        Spinner spinnerView = (Spinner)findViewById(R.id.number_spinner);
        int number = Integer.parseInt(spinnerView.getSelectedItem().toString());

        Intent oldIntent = getIntent();
        String name = oldIntent.getExtras().getString("NAME_STRING");

        // Create new intent with user's name, # of questions, current question(1).
        Intent intent = new Intent(this, Question1.class);
        intent.putExtra("NAME_STRING", name);
        intent.putExtra("totalQuestions", number);
        intent.putExtra("currentQuestion", 1);
        startActivity(intent);
    }

    // Go to welcome page
    public void onClickLogout(View view) {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

}
