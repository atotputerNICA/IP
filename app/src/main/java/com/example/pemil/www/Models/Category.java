package com.example.pemil.www.Models;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * @author atotputerNICA
 * class representing
 */
public class Category {
    private String name;
    private ArrayList<Question> questions;

    public Category(String name, ArrayList<Question> questions) {
        this.name = name;
        this.questions = questions;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setQuestions(ArrayList<Question> questions) {
        this.questions = questions;
    }

    public String getName() {
        return name;
    }

    public ArrayList<Question> getQuestions() {
        return questions;
    }
}
