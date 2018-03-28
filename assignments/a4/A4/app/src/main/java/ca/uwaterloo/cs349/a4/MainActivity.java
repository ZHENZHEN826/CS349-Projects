package ca.uwaterloo.cs349.a4;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.*;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Disable next button
        Button btn = (Button) findViewById(R.id.button_next);
        btn.setEnabled(false);

        // Enable next button when name is not empty
        EditText nameInput = (EditText) findViewById(R.id.name);
        nameInput.addTextChangedListener(new TextWatcher() {

            public void afterTextChanged(Editable s) {
                if (s.length() != 0) {
                    Button btn = (Button) findViewById(R.id.button_next);
                    btn.setEnabled(true);
                }
            }

            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
            }

            public void onTextChanged(CharSequence s, int start,
                                      int before, int count) {
                if (s.length() != 0) {
                    Button btn = (Button) findViewById(R.id.button_next);
                    btn.setEnabled(true);
                }

            }

        });
    }

    // Go to questions selection page
    public void onClickNext(View view) {
        EditText nameView = (EditText)findViewById(R.id.name);
        String name = null;
        name = nameView.getText().toString();

//       if (name != null){
        Intent intent = new Intent(this, SelectionActivity.class);
        intent.putExtra("NAME_STRING", name);
        startActivity(intent);

//        }

    }
}
