package com.example.pemil.www.Models;

import java.util.ArrayList;

/**
 *  @author atotputerNICA
 *  class representing a Question in the Category table
 */
public class Question {

    private int questionNumber;
    private String categoryName;
    private String correctAnswer;
    private String difficulty;
    private ArrayList<String> incorrectAnswers;
    private String questionString;
    private String type;

    public Question(int questionNumber, String categoryName,
                    String correctAnswer, String difficulty,
                    ArrayList<String> incorrectAnswers, String questionString,
                    String type) {
        this.questionNumber = questionNumber;
        this.categoryName = categoryName;
        this.correctAnswer = correctAnswer;
        this.difficulty = difficulty;
        this.incorrectAnswers = incorrectAnswers;
        this.questionString = questionString;
        this.type = type;
    }

    public int getQuestionNumber() {
        return questionNumber;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public String getCorrectAnswer() {
        return correctAnswer;
    }

    public String getDifficulty() {
        return difficulty;
    }

    public ArrayList<String> getIncorrectAnswers() {
        return incorrectAnswers;
    }

    public String getQuestionString() {
        return questionString;
    }

    public String getType() {
        return type;
    }
}
