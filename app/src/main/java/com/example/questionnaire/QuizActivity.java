package com.example.questionnaire;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class QuizActivity extends AppCompatActivity {

    private TextView textViewQuestion;
    private TextView textViewOptions;
    private Button buttonA;
    private Button buttonB;
    private Button buttonC;
    private Button buttonD;
    private TextView textViewScore;
    private Button buttonMainMenu;
    private Quiz mainQuiz;
    private Button buttonPressed;
    private int correctAnswer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.quiz_activity);

        wireWidgets();
        setListeners();
        Intent lastIntent = getIntent();

        textViewQuestion.setVisibility(View.VISIBLE);
        textViewOptions.setVisibility(View.VISIBLE);
        buttonA.setVisibility(View.VISIBLE);
        buttonB.setVisibility(View.VISIBLE);
        buttonC.setVisibility(View.VISIBLE);
        buttonD.setVisibility(View.VISIBLE);
        textViewScore.setVisibility(View.GONE);
        buttonMainMenu.setVisibility(View.GONE);

        buttonA.setBackgroundResource(android.R.drawable.btn_default);
        buttonB.setBackgroundResource(android.R.drawable.btn_default);
        buttonC.setBackgroundResource(android.R.drawable.btn_default);
        buttonD.setBackgroundResource(android.R.drawable.btn_default);

        InputStream XmlFileInputStream = getResources().openRawResource(R.raw.questions);
        String sxml = readTextFile(XmlFileInputStream);

        Gson gson = new Gson();
        Question[] questions =  gson.fromJson(sxml, Question[].class);
        List<Question> questionList = Arrays.asList(questions);

        List<Question> questionsPlaceHolder = new ArrayList<Question>();
        for (Question a : questionList){
            if (a.getCategory().equals(lastIntent.getStringExtra(MainActivity.EXTRA_CATEGORY))){
                questionsPlaceHolder.add(a);
            }
        }
        mainQuiz = new Quiz();
        mainQuiz.setQuestions(questionsPlaceHolder);
        textViewQuestion.setText(mainQuiz.getQuestions().get(mainQuiz.getCurrentQuestion()).getQuestion());
        mainQuiz.shuffleOptions(mainQuiz.getQuestions().get(mainQuiz.getCurrentQuestion()));
        textViewOptions.setText("A: " + mainQuiz.getQuestions().get(mainQuiz.getCurrentQuestion()).getChoices().get(0) + "\n" +
                "B: " + mainQuiz.getQuestions().get(mainQuiz.getCurrentQuestion()).getChoices().get(1) + "\n" +
                "C: " + mainQuiz.getQuestions().get(mainQuiz.getCurrentQuestion()).getChoices().get(2) + "\n" +
                "D: " + mainQuiz.getQuestions().get(mainQuiz.getCurrentQuestion()).getChoices().get(3));

        Log.e("OnCreate", "onCreate: " + questionList.toString());


        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_PORTRAIT);
        }
    }




    private class AsyncTaskRunner extends AsyncTask<Void, Boolean, Void> {
        @Override
        protected Void doInBackground(Void... voids) {
            delay(50);
            publishProgress(false);
            delay(1950);
            publishProgress(true);
            return null;
        }

        @Override
        protected void onProgressUpdate(Boolean... values) { // values is an array of booleans....  get the first item out of the array and use ==
            super.onProgressUpdate(values);
            ConstraintLayout layout = findViewById(R.id.constrainlayout_layout_quiz);
            if (!values[0]) {
                for (int index = 0; index < layout.getChildCount(); ++index) {
                    View nextChild = layout.getChildAt(index);
                    if (nextChild instanceof Button) {
                        nextChild.setEnabled(false);
                    }
                }
            } else {
                for (int index = 0; index < layout.getChildCount(); ++index) {
                    View nextChild = layout.getChildAt(index);
                    if (nextChild instanceof Button) {
                        nextChild.setEnabled(true);
                    }
                }
            }
        }

        private void delay(int millisDelay){
            try {
                Thread.sleep(millisDelay);
            }
            catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        @Override
        protected void onPreExecute() {
                    if (correctAnswer == 0){
                        buttonPressed.setBackgroundColor(Color.rgb(255, 102, 102));
                        buttonA.setBackgroundColor(Color.rgb(128, 255, 128));
                    }
                    else if (correctAnswer == 1){
                        buttonPressed.setBackgroundColor(Color.rgb(255, 102, 102));
                        buttonB.setBackgroundColor(Color.rgb(128, 255, 128));
                    }
                    else if (correctAnswer == 2) {
                        buttonPressed.setBackgroundColor(Color.rgb(255, 102, 102));
                    }
                    else{
                        buttonPressed.setBackgroundColor(Color.rgb(255, 102, 102));
                        buttonD.setBackgroundColor(Color.rgb(128, 255, 128));
                    }
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            buttonA.setBackgroundResource(android.R.drawable.btn_default);
            buttonB.setBackgroundResource(android.R.drawable.btn_default);
            buttonC.setBackgroundResource(android.R.drawable.btn_default);
            buttonD.setBackgroundResource(android.R.drawable.btn_default);
            nextQuestion();
        }
    }


    private void wireWidgets() {
        textViewQuestion = findViewById(R.id.textview_question_quiz);
        textViewOptions = findViewById(R.id.textview_choices_quiz);
        buttonA = findViewById(R.id.button_a_quiz);
        buttonB = findViewById(R.id.button_b_quiz);
        buttonC = findViewById(R.id.button_c_quiz);
        buttonD = findViewById(R.id.button_d_quiz);
        textViewScore = findViewById(R.id.textview_score_quiz);
        buttonMainMenu = findViewById(R.id.button_menu_quiz);

    }

    private void toScoreScreen(){
        textViewQuestion.setVisibility(View.GONE);
        textViewOptions.setVisibility(View.GONE);
        buttonA.setVisibility(View.GONE);
        buttonB.setVisibility(View.GONE);
        buttonC.setVisibility(View.GONE);
        buttonD.setVisibility(View.GONE);
        textViewScore.setVisibility(View.VISIBLE);
        buttonMainMenu.setVisibility(View.VISIBLE);
        textViewScore.setText(textViewScore.getText().toString() + mainQuiz.getScore());
    }

    private void nextQuestion()
    {
        if (mainQuiz.getQuestionNumber() == 9){
            toScoreScreen();
        }
        else {
            textViewQuestion.setText(mainQuiz.nextQuestion().getQuestion());
            mainQuiz.shuffleOptions(mainQuiz.getQuestions().get(mainQuiz.getCurrentQuestion()));
            textViewOptions.setText("A: " + mainQuiz.getQuestions().get(mainQuiz.getCurrentQuestion()).getChoices().get(0) + "\n" +
                    "B: " + mainQuiz.getQuestions().get(mainQuiz.getCurrentQuestion()).getChoices().get(1) + "\n" +
                    "C: " + mainQuiz.getQuestions().get(mainQuiz.getCurrentQuestion()).getChoices().get(2) + "\n" +
                    "D: " + mainQuiz.getQuestions().get(mainQuiz.getCurrentQuestion()).getChoices().get(3));
        }
    }

    private void revealAnswer(Button clicked){
        buttonPressed = clicked;
        AsyncTaskRunner runner = new AsyncTaskRunner();
        runner.execute();
    }

    private void setListeners() {

        buttonA.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                correctAnswer = mainQuiz.checkAnswer(mainQuiz.getQuestions().get(mainQuiz.getCurrentQuestion()).getChoices().get(0));
                revealAnswer(buttonA);

            }
        });

        buttonB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                correctAnswer = mainQuiz.checkAnswer(mainQuiz.getQuestions().get(mainQuiz.getCurrentQuestion()).getChoices().get(1));
                revealAnswer(buttonB);

            }
        });

        buttonC.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                correctAnswer = mainQuiz.checkAnswer(mainQuiz.getQuestions().get(mainQuiz.getCurrentQuestion()).getChoices().get(2));
                revealAnswer(buttonC);

            }
        });

        buttonD.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                correctAnswer = mainQuiz.checkAnswer(mainQuiz.getQuestions().get(mainQuiz.getCurrentQuestion()).getChoices().get(3));
                revealAnswer(buttonD);

            }
        });

        buttonMainMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent targetIntent = new Intent(QuizActivity.this, MainActivity.class);
                startActivity(targetIntent);
                finish();
            }
        });
    }

    public String readTextFile(InputStream inputStream) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        byte buf[] = new byte[1024];
        int len;
        try {
            while ((len = inputStream.read(buf)) != -1) {
                outputStream.write(buf, 0, len);
            }
            outputStream.close();
            inputStream.close();
        } catch (IOException e) {

        }
        return outputStream.toString();
    }
}
