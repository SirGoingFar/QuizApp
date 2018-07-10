package com.eemf.quizapp;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;
import java.util.Random;


public class MainActivity extends AppCompatActivity {

    private static int correctAnswer = 0;
    private static int robotCheckAnswer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //append username to the @link greetings
        userGreeting();

        //attach onClickListener to the submit button
        submitButtonListener();

        //Set Robot Check Operands
        processRobotCheck();
    }

    /**
     * the method is called by the @link onCreate() to attach a listener
     * to the submit button
     */
    private void submitButtonListener() {
        Button submitButton = (Button) findViewById(R.id.submitButton);
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onSubmitClick();
            }
        });
    }

    /**
     * the method is called by the onCreate method to extract the user name]
     * from the Intent data and append it to the welcome greeting
     */
    private void userGreeting() {

        //To get Intent data from @link startActivity.java
        Intent getIntentData = getIntent();
        String username = getIntentData.getDataString();

        //append the username to the welcome greeting
        TextView greetings = (TextView) findViewById(R.id.greetings);
        greetings.setText(greetings.getText().toString().concat(username + "!"));

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
            case KeyEvent.KEYCODE_BACK:
                Intent minimize = new Intent(Intent.ACTION_MAIN);
                       minimize.addCategory(Intent.CATEGORY_HOME);
                       minimize.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                       startActivity(minimize);
                return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    /**
     * this adds 1 to @value robotCheckAnswer
     */
    public static void setRobotCheckAnswer(int value) {
        robotCheckAnswer = value;
    }

    /**
     * @return @value robotCheckAnswer
     */
    public static int getRobotCheckAnswer() {
        return robotCheckAnswer;
    }

    /**
     * called by onCreate()
     * this handles the robot check:
     * using the Random class to generate random numbers
     */
    private void processRobotCheck() {
        TextView operand1 = (TextView) findViewById(R.id.operand1);
        TextView operand2 = (TextView) findViewById(R.id.operand2);
        TextView operator = (TextView) findViewById(R.id.operator);

        //Random Number Generator initialization
        Random rng1 = new Random();
        Random rng2 = new Random();

        //set operand values using the initialized RNG
        int value1 = rng1.nextInt(100);
        int value2 = rng2.nextInt(80);

        //free the memory
        rng1 = null;
        rng2 = null;

        operand1.setText(String.format("%d",value1));
        operand2.setText(String.format("%d",value2));

        //randomize operator
        if(value1 >= value2)
            operator.setText("-");

        else
            operator.setText("+");

        //get the real operator sign
        String operatorSign = operator.getText().toString();

        int answer;

        //compute RobotCheck Answer
        switch(operatorSign){

            //when it is an SUBTRACTION operator
            case "-": {
                answer = (value1 - value2);
                setRobotCheckAnswer(answer);
                break;
            }

            //when it is an ADD operator
            case "+": {
                answer = (value1 + value2);
                setRobotCheckAnswer(answer);
                break;
            }
        }

    }

    /**
     * this adds 1 to @value correctAnswer
     */
    public static void setCorrectAnswer(){
        correctAnswer+=1;
    }

    /**
     * @return @value correctAnswer
     */
    public static int getCorrectAnswer() {

        return correctAnswer;
    }

    public static void setCorrectAnswerOutside(int value){
        correctAnswer = 0;
    }


    /**
     * the method gets called when the submit button is clicked
     */
    public void onSubmitClick(){

        //referencing the Robot Check Answer EditText view
        EditText robotCheckEditTextView = (EditText) findViewById(R.id.robotCheckAnswer);
        String getEditTextValue = robotCheckEditTextView.getText().toString();

        //when the EditTextView empty
        if(TextUtils.isEmpty(getEditTextValue))
            Toast.makeText(getApplicationContext(), "Answer the ROBOT CHECK question first!", Toast.LENGTH_SHORT).show();

            //when Robot Check Answer EditText field is not empty
        else{

            int integerVersion = Integer.parseInt(getEditTextValue);

            //when user answer is WRONG
            if(integerVersion != getRobotCheckAnswer()) {
                Toast.makeText(getApplicationContext(), "Wrong ROBOT CHECK answer, retry!", Toast.LENGTH_SHORT).show();
                processRobotCheck();
                robotCheckEditTextView.getText().clear();
            }

            //when user answer supplied is RIGHT
            else if(integerVersion == getRobotCheckAnswer()){

                //computing questions with RadioGroup options

                //get all instances of radio group question option
                RadioGroup question1 = (RadioGroup) findViewById(R.id.question1);
                RadioGroup question2 = (RadioGroup) findViewById(R.id.question2);
                RadioGroup question3 = (RadioGroup) findViewById(R.id.question3);
                RadioGroup question6 = (RadioGroup) findViewById(R.id.question6);
                RadioGroup question7 = (RadioGroup) findViewById(R.id.question7);
                RadioGroup question8 = (RadioGroup) findViewById(R.id.question8);
                RadioGroup question9 = (RadioGroup) findViewById(R.id.question9);
                RadioGroup question10 = (RadioGroup) findViewById(R.id.question10);

                RadioGroup[] rgList = {question1, question2, question3, question6, question7, question8, question9, question10};


                //get their corresponding answers
                int answer1 = R.id.answer1;
                int answer2 = R.id.answer2;
                int answer3 = R.id.answer3;
                int answer6 = R.id.answer6;
                int answer7 = R.id.answer7;
                int answer8 = R.id.answer8;
                int answer9 = R.id.answer9;
                int answer10 = R.id.answer10;

                int[] answerList = {answer1, answer2, answer3, answer6, answer7, answer8, answer9, answer10};

                //call marking method for the RadioGroup option questions
                markRadioGroupQuestions(rgList, answerList);

                //call marking methods for other questions with different option varieties
                Question4();
                Question5();


                //create and start Explicit Intent to the Result Summary Activity
                Intent resultSummaryActivity = new Intent(this,ResultSummaryActivity.class);
                resultSummaryActivity.setData(Uri.parse(String.format("%d",getCorrectAnswer())));
                startActivity(resultSummaryActivity);
            }
        }
    }

    /**
     * marks questions with RadioGroup as options
     * @param rgList is the array of all RadioGroup instances
     * @param answerList is the array of all correct answer references to the RadioGroup instances
     */
    private void markRadioGroupQuestions(RadioGroup[] rgList, int[] answerList) {

        for(int loopCount = 0; loopCount < rgList.length; loopCount++){

            if(rgList[loopCount].getCheckedRadioButtonId() == answerList[loopCount]) {
                setCorrectAnswer();
            }

            //when no option is selected
            else if(rgList[loopCount].getCheckedRadioButtonId() == -1)
                return;

            else
                return;
        }
    }

    private void Question4() {

        EditText question4 = (EditText) findViewById(R.id.question4);
        String getEditTextValue = question4.getText().toString();

       if(TextUtils.isEmpty(getEditTextValue))
            return;

       else if(Integer.parseInt(getEditTextValue) == 36)
            setCorrectAnswer();

        else
            return;
    }

    private void Question5(){

        CheckBox box1 = (CheckBox) findViewById(R.id.question5_1);
        CheckBox box2 = (CheckBox) findViewById(R.id.question5_2);
        CheckBox box3 = (CheckBox) findViewById(R.id.question5_3);
        CheckBox box4 = (CheckBox) findViewById(R.id.question5_4);
        CheckBox box5 = (CheckBox) findViewById(R.id.question5_5);
        CheckBox box6 = (CheckBox) findViewById(R.id.question5_6);
        CheckBox box7 = (CheckBox) findViewById(R.id.question5_7);
        CheckBox box8 = (CheckBox) findViewById(R.id.question5_8);
        CheckBox box9 = (CheckBox) findViewById(R.id.question5_9);
        CheckBox box10 = (CheckBox) findViewById(R.id.question5_10);
        CheckBox box11 = (CheckBox) findViewById(R.id.question5_11);
        CheckBox box12 = (CheckBox) findViewById(R.id.question5_12);

        CheckBox[] boxArray = {box1,box2,box3,box4,box5,box6,box7,box8,box9,box10,box11,box12};
        int counter = 0;

        for(CheckBox individualBox : boxArray){
            if(individualBox.isChecked() & ((individualBox.getId() == R.id.question5_3) || (individualBox.getId() == R.id.question5_7) || (individualBox.getId() == R.id.question5_8) || (individualBox.getId() == R.id.question5_10) || (individualBox.getId() == R.id.question5_11))){
                counter+=1;
            }
        }

        //at least get 5 correct before having the question
        if(counter >= 5)
            setCorrectAnswer();

    }
}
