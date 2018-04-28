package com.example.pemil.www.DataSource;
import com.example.pemil.www.Models.Category;
import com.google.firebase.storage.*;


import java.util.ArrayList;
import java.util.HashMap;

/**
 * @author atotputerNICA
 *
 */
public class CategoryDataSource {

    private StorageReference mStorageRef;
    private StorageReference categoryStorageRef;
    private static final int NUM_OF_QUESTIONS = 10;
    private static final int NUM_OF_CATEGORIES = 4;
    private static final String CATEGORY_1 = "General Knowledge/";
    private static final String CATEGORY_2 = "Entertainment: Books/";
    private static final String CATEGORY_3 = "Entertainment: Film/";
    private static final String CATEGORY_4 = "Entertainment: Music/";


    public CategoryDataSource() {
        mStorageRef = FirebaseStorage.getInstance().getReference();
        categoryStorageRef = FirebaseStorage.getInstance().getReference("Category/");
    }

    public ArrayList<Category> setCategories() {
        return null;
    }


}
