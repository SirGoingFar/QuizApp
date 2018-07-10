package com.eemf.quizapp;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import java.util.regex.*;

import static java.util.regex.Pattern.compile;


public class startActivity extends AppCompatActivity {

    public static String name;
    public static Bundle mUsername;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        Button startButton = (Button) findViewById(R.id.startButton);
               startButton.setOnClickListener(new View.OnClickListener() {
                   @Override
                   public void onClick(View view) {
                       startButtonProcessed();
                   }
               });
    }

    /**
     * Method is invoked when start button is clicked. It will:
     * - retrieve the username
     * - switch to @link MainActivity
     */
    private void startButtonProcessed() {

        //Name field processing
        EditText namefield = (EditText) findViewById(R.id.start_namefield);
        String tempName = namefield.getText().toString();

        if(tempName.isEmpty())
            Toast.makeText(getApplicationContext(), "Enter your name", Toast.LENGTH_SHORT).show();

        else {

            if(processUsername(tempName)) {
                name = tempName;
                mUsername = new Bundle(); //initialize the bundle

                //bundle a data to be used in another activity logic
                startActivity.mUsername.putString("username", name);

                //Sending Explicit Intent to MainActivity.java
                Intent switchToMainActivity = new Intent(this, MainActivity.class);
                       switchToMainActivity.setData(Uri.parse(name));
                startActivity(switchToMainActivity);
            }

        }

    }

    private boolean processUsername(String tempName) {
        Pattern p = compile("[A-Za-z][A-Za-z. ]*");
        Matcher m = p.matcher(tempName);

        if (m.find() && m.group().equals(tempName)) {
            name = tempName;
            return true;
        } else {
            Toast.makeText(getApplicationContext(), "Enter a valid name", Toast.LENGTH_SHORT).show();
            return false;
        }

    }

}
