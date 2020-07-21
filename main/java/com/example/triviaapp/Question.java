//Author: Émilie Fortin

package com.example.triviaapp;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.widget.TextView;
import android.widget.Button;
import android.view.View;
import android.os.Bundle;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.util.Random;

public class Question extends AppCompatActivity {
    Button Answer1button, Answer2button, Answer3button, Answer4button;
    TextView screen;
    final static int numberOfQuestions = 13;
    int questionNum;
    int numberOfCorrectAnswers;
    int[] questionOrder;
    String fileName = "triviaQuestions.txt";
    String questionText = "";
    String correctAnswer = "";
    String decoyAnswer1 = "";
    String decoyAnswer2 = "";
    String decoyAnswer3 = "";
    int[] answerOrder = {1,2,3,4};
    boolean isButton1True;
    boolean isButton2True;
    boolean isButton3True;
    boolean isButton4True;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question);

        // retrieves the data transmitted to it when it passes from one activity to the other
        Intent intent = getIntent();
        numberOfCorrectAnswers = intent.getIntExtra("numberOfCorrectAnswers",0);
        questionNum = intent.getIntExtra("numberOfQuestions",0);
        questionOrder=intent.getIntArrayExtra("questionNumbers");


        //This generates 10 random integer in the range 0 and the total number of questions
        //Then ensures that the question asked by the quiz will be different from each other
        //Only called the first time
        generateQuestionNumber(questionNum, numberOfQuestions);

        //This gets the question according to the number in position randomly generated by the previous equation
        //then sets the strings representing to said question, and answers
        getQuestionFromFile(fileName, questionOrder, questionNum);

        //This portion determines the order in which the answers will be shown
        //To prevent the correct answer from always being in the same location.
        //By shuffling the answerOrder array.
        shuffleArray(answerOrder);

        //Initializing the TextView and setting the text to be the question
        screen = (TextView) findViewById(R.id.screen);
        screen.setText(questionText);

        //Initializing of all the buttons and assigning the appropriate answer text to them
        Answer1button = (Button) findViewById(R.id.Answer1button);
        isButton1True = assignTextToButton(0, Answer1button, answerOrder);

        Answer2button = (Button) findViewById(R.id.Answer2button);
        isButton2True = assignTextToButton(1, Answer2button, answerOrder);

        Answer3button = (Button) findViewById(R.id.Answer3button);
        isButton3True = assignTextToButton(2, Answer3button, answerOrder);

        Answer4button = (Button) findViewById(R.id.Answer4button);
        isButton4True = assignTextToButton(3, Answer4button, answerOrder);

        //The code for when buttons are clicked.
        //The buttons will change the display to the results page
        Answer1button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goToResultsActivity(isButton1True);
            }
        });
        Answer2button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goToResultsActivity(isButton2True);
            }
        });
        Answer3button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goToResultsActivity(isButton3True);
            }
        });
        Answer4button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goToResultsActivity(isButton4True);
            }
        });

    }


    public void generateQuestionNumber(int questionNumber, int numberOfQuestion){
        if(questionNumber==0) {
            Random rand = new Random();
            boolean isNotFull=true;
            int i = 0;
            while (isNotFull) {
                int randomInt = rand.nextInt(numberOfQuestion);
                if (isUnique(randomInt, questionOrder)) {
                    questionOrder[i] = randomInt;
                    i++;
                }
                if (i >= 10) {
                    isNotFull=false;
                }
            }
        }
        else{return;}

    }

    public boolean isUnique(int value, int[] array){
        for (int i=0; i<array.length;i++){
            if(array[i]==value){
                return false;
            }
        }
        return true;
    }

    public void getQuestionFromFile(String fileName, int[] array, int questionNumber){
        try{
            InputStream is = getAssets().open(fileName);
            BufferedReader readBuffer = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8));
            for (int line = 1; line <= (numberOfQuestions * 5); line++) {
                if (line == ((array[questionNumber] * 5) + 1)) {
                    questionText = readBuffer.readLine();
                } else if (line == ((array[questionNumber] * 5) + 2)) {
                    correctAnswer = readBuffer.readLine();
                } else if (line == ((array[questionNumber] * 5) + 3)) {
                    decoyAnswer1 = readBuffer.readLine();
                } else if (line == ((array[questionNumber] * 5) + 4)) {
                    decoyAnswer2 = readBuffer.readLine();
                } else if (line == ((array[questionNumber] * 5) + 5)) {
                    decoyAnswer3 = readBuffer.readLine(); }
                else {
                    readBuffer.readLine();
                }
            }
        }
        catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void shuffleArray(int[] array){
        Random rand = new Random();
        for(int i=0; i<array.length; i++){
            int indexForSwapping = rand.nextInt(array.length);
            int temporarilySavedValue = array[indexForSwapping];
            array[indexForSwapping]=array[i];
            array[i]=temporarilySavedValue;
        }
    }

    public boolean assignTextToButton(int number, Button button, int[] array){
        boolean isTrue = false;
         switch(array[number]){
             case 1:
                 button.setText(correctAnswer);
                 isTrue=true;
                 break;
             case 2:
                 button.setText(decoyAnswer1);
                 isTrue=false;
                 break;
             case 3:
                 button.setText(decoyAnswer2);
                 isTrue=false;
                 break;
             case 4:
                 button.setText(decoyAnswer3);
                 isTrue=false;
                 break;
         }
         return isTrue;
     }

    public void goToResultsActivity(boolean isTrue){
        Intent intents = new Intent(this, Results.class);
        intents.putExtra("numberOfQuestions", questionNum+1);
        intents.putExtra("questionNumbers", new int[10]);
        intents.putExtra("isCorrect", isTrue);
        if(isTrue){
            intents.putExtra("numberOfCorrectAnswers",numberOfCorrectAnswers+1);
        }
        else{
            intents.putExtra("numberOfCorrectAnswers",numberOfCorrectAnswers);
        }
        startActivity(intents);
    }
}