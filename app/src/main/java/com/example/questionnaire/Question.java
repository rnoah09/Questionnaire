package com.example.questionnaire;

import android.util.Log;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Question {
    private String answer;
    private String question;
    private int id;
    private String category;
    private List<String> choices;

    public String getAnswer() {
        return answer;
    }

    public String getQuestion() {
        return question;
    }

    public int getId() {
        return id;
    }

    public String getCategory() {
        return category;
    }

    public List<String> getChoices() {
        return choices;
    }

    public Boolean checkAnswer(String choice){
        if(choice.equals(answer)){
            return true;
        }
        else{
            return false;
        }
    }

}
