package com.example.pemil.www.Models;

import java.util.ArrayList;

/**
 *  @author atotputerNICA
 *  class representing a Question in the Category table
 */
public class Question {

    private String category;
    private String correct_answer;
    private String difficulty;
    private ArrayList<String> incorrect_answers;
    private String question;
    private String type;


    public Question(String category,
                    String correct_answer,
                    String difficulty,
                    ArrayList<String> incorrect_answers,
                    String question,
                    String type) {
        this.category = category;
        this.correct_answer = correct_answer;
        this.difficulty = difficulty;
        this.incorrect_answers = incorrect_answers;
        this.question = question;
        this.type = type;
    }

    public Question() {

    }


    public String getCategory() {
        return category;
    }

    public String getCorrect_answer() {
        return correct_answer;
    }

    public String getDifficulty() {
        return difficulty;
    }

    public ArrayList<String> getIncorrect_answers() {
        return incorrect_answers;
    }

    public String getQuestion() {
        return question;
    }

    public String getType() {
        return type;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public void setCorrect_answer(String correct_answer) {
        this.correct_answer = correct_answer;
    }

    public void setDifficulty(String difficulty) {
        this.difficulty = difficulty;
    }

    public void setIncorrect_answers(ArrayList<String> incorrect_answers) {
        this.incorrect_answers = incorrect_answers;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "Question{" +
                "category='" + category + '\'' +
                ", type='" + type + '\'' +
                ", difficulty='" + difficulty + '\'' +
                ", question='" + question + '\'' +
                ", correct_answer='" + correct_answer + '\'' +
                ", incorrect_answers=" + incorrect_answers +
                '}';
    }
}
