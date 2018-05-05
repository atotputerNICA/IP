package com.example.pemil.www.DataSource;
import android.util.Log;

import com.example.pemil.www.Models.Category;
import com.example.pemil.www.Models.Question;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.*;
import org.json.*;



import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

/**
 * @author atotputerNICA
 *
 */

public class CategoryDataSource {
    public FirebaseDatabase dataBase ;
    public DatabaseReference categoryDataBaseRef;
    private static final int NUM_OF_QUESTIONS = 10;
    private static final int NUM_OF_CATEGORIES = 4;
    private static final String[] CATEGORIES = {
            "General Knowledge",
            "Entertainment: Books",
            "Entertainment: Film",
            "Entertainment: Music"
    };
   private static ArrayList<Category> categories;
   public boolean stop = false;

    public CategoryDataSource() {

        FirebaseDatabase dataBase = FirebaseDatabase.getInstance();
        categoryDataBaseRef = dataBase.getReference("Category");
        categories = new ArrayList<>(NUM_OF_CATEGORIES);
        //selectCategories();
        //printCategories();
    }

    /**
     * select from database
     * @return list of Category Object
     */
    public void selectCategories() {

        for (final String categoryString : CATEGORIES) {

            categoryDataBaseRef.child(categoryString).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    Category c = new Category();
                    c.setName(categoryString);
                    ArrayList<Question> questions = new ArrayList<>(NUM_OF_QUESTIONS);
                    if (dataSnapshot.exists() && !stop) {
                        for (int i = 0; i < NUM_OF_QUESTIONS; i++) {
                            Question q = dataSnapshot.child(Integer.toString(i)).getValue(Question.class);
                            questions.add(q);
                        }
                        c.setQuestions(questions);

                        for (int i = 0; i < NUM_OF_QUESTIONS; i++) {
                            Log.i("categ data source",c.getQuestions().get(i).toString());
                        }
                        CategoryDataSource.categories.add(c);
                        Log.i("categ data source", "" + CategoryDataSource.categories.size());
                    } else {
                        categoryDataBaseRef.child(categoryString).removeEventListener(this);
                    }
                    if (categoryString.equals(CATEGORIES[3])) {

                        stop = true;
                    }
                }


                @Override
                public void onCancelled(DatabaseError error) {

                }
            });


        }



    }

    public void printCategories(ArrayList<Category> categories) {

        Log.i("Category DataSource", "cat" + categories.size());
        for (Category c : categories) {
            Log.i("Category DataSource",c.getName());
            for (int i = 0; i < NUM_OF_QUESTIONS; i++) {
                Log.i("Category DataSource",c.getQuestions().get(i).toString());
            }
        }
    }


}
