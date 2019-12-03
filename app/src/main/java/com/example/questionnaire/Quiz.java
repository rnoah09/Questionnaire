package com.example.questionnaire;

import android.util.Log;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class Quiz {
    private int currentQuestion;
    private int questionNumber;
    private List<Question> questions;
    private ArrayList<Integer> randomIDList;
    private int score;
    private String chosenCategory;

    public Quiz() {
        score = 0;
        questionNumber = 0;
        this.randomIDList = new ArrayList<>();
        randomIDGenerator();
    }

    public void shuffleOptions(Question question){
        Collections.shuffle(question.getChoices());
    }

    public int getQuestionNumber(){ return questionNumber;}

    public List<Question> getQuestions() {
        return questions;
    }

    public int getCurrentQuestion() {
        return currentQuestion;
    }

    public int getScore() {
        return score;
    }

    public void randomIDGenerator() {
        for (int i = 0; i <= 14; i++){
            randomIDList.add(i);
        }
        for (int i = 0; i < 5; i++){
            randomIDList.remove((int)(Math.random() * (randomIDList.size() - 1)));
        }
        Collections.shuffle(randomIDList);
        currentQuestion = randomIDList.get(0);
    }


    public int checkAnswer(String choice){
        boolean check = questions.get(currentQuestion).checkAnswer(choice);
        Log.d("answer check", "" + check);
        if (check == true){
            score += 10;
        }
        return questions.get(currentQuestion).getChoices().indexOf(questions.get(currentQuestion).getAnswer());
    }

    public Question nextQuestion(){
        questionNumber++;
        if (questionNumber <= 10){
            currentQuestion = randomIDList.get(questionNumber);
        }
        return questions.get(currentQuestion);
    }

    public void setQuestions(List<Question> questions) {
        this.questions = questions;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public void setChosenCategory(String chosenCategory) {
        this.chosenCategory = chosenCategory;
    }
}
