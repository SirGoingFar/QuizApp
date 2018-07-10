package com.eemf.quizapp;

import android.content.Intent;
import android.graphics.drawable.Icon;
import android.icu.util.ULocale;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.TextView;

import java.util.Locale;

import static android.R.attr.button;
import static android.R.attr.process;
import static android.icu.lang.UCharacter.GraphemeClusterBreak.T;
import static android.view.KeyEvent.KEYCODE_BACK;
import static com.eemf.quizapp.R.id.question9;
import static com.eemf.quizapp.R.id.username;

public class ResultSummaryActivity extends AppCompatActivity {

    private static int correctAnswer;
    private static String unbundledUsername;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result_summary);

        //update the username textview with the username
        processUsername();

        //get the intent data and process it
        processIntentData();

        //listen to button events
        onControlButtonsClick();
    }

    private void onControlButtonsClick() {

        //Retry button
        Button retryButton = (Button) findViewById(R.id.retryButton);
        retryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //set the correctAnswer in the previous quiz session to zero
                MainActivity.setCorrectAnswerOutside(0);

                //switch to @link startActivity
                Intent switchToStartActivity = new Intent(ResultSummaryActivity.this, startActivity.class);
                switchToStartActivity.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                ResultSummaryActivity.this.startActivity(switchToStartActivity);
            }
        });

        //Quit button
        Button quitButton = (Button) findViewById(R.id.quitButton);
        quitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //close the app
                MainActivity.setCorrectAnswerOutside(0);
                finishAffinity();
            }
        });
    }

    /**
     * sets @value correctAnswer
     * @param value is the calling method set-value
     */
    public static void setCorrectAnswer(int value) {
        correctAnswer = value;
    }

    public static int getCorrectAnswer() {
        return correctAnswer;
    }

    /**
     * retrieves the intent data and process it accordingly
     */
    private void processIntentData() {
        //get the Intent data
        Intent getIntentData = getIntent();
        String intentData = getIntentData.getDataString();
        int intVersion = Integer.parseInt(intentData);
        setCorrectAnswer(intVersion);

        //set correct answer value textview
        TextView correctAnswerTextView = (TextView) findViewById(R.id.correctAnswer);
        double correctAnswerPercentage = (double)(getCorrectAnswer()*10);
        String holder = String.format(Locale.getDefault(),"%d (%.1f",getCorrectAnswer(),correctAnswerPercentage);
               correctAnswerTextView.setText(holder.concat("%)"));

        //set wrong answer value textview
        TextView wrongAnswerTextView = (TextView) findViewById(R.id.wrongAnswer);
        holder = String.format("%d (%.1f",(10 - getCorrectAnswer()),(100.00 - correctAnswerPercentage));
                 wrongAnswerTextView.setText(holder.concat("%)"));

        //set overall remark
            //reference the overall remark textview, correctIcon and wrongIcon
             TextView overallRemark = (TextView) findViewById(R.id.overallRemark);
             ImageView wrongIcon = (ImageView) findViewById(R.id.wrongIcon);
             ImageView correctIcon = (ImageView) findViewById(R.id.correctIcon);

        if(correctAnswerPercentage >= 50.00){
            overallRemark.setText("PASSED");
            wrongIcon.setVisibility(View.INVISIBLE);
        }
        else{
            overallRemark.setText("FAILED");
            correctIcon.setVisibility(View.INVISIBLE);
        }

    }

    /**
     * sets the username view with the appropriate username
     */
    private void processUsername() {
        //create {@link startActivity.java} object
        startActivity object = new startActivity(); //to get the username, you can use "object.name;" as an alrenative

        //unbundle the username bundled data
        unbundledUsername = startActivity.mUsername.getString("username");

        //add character '!'
        String editedUsername = unbundledUsername.concat("!");

        //update the username
        TextView username = (TextView) findViewById(R.id.username);
        username.setText(editedUsername);
    }

    /**
     * on @value KEYCODE_BACK is pressed, @link onKeyDown(keyCode, event) is called
     * @param keyCode is the key code for the key pressed
     * @param event is the key event
     * @return true or @return the event
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event){
        switch(keyCode){
            case KEYCODE_BACK:
                Intent minimize = new Intent(Intent.ACTION_MAIN);
                       minimize.addCategory(Intent.CATEGORY_HOME);
                       minimize.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                       startActivity(minimize);
                return true;
        }
        return super.onKeyDown(keyCode, event);
    }

}
