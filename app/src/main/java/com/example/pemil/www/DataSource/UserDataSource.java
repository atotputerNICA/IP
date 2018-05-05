package com.example.pemil.www.DataSource;

import android.util.Log;

import com.example.pemil.www.Models.User;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

/**
 * Created by Baciu Ionut on 4/29/2018.
 */

public class UserDataSource {
    private DatabaseReference mDatabase;

    public UserDataSource() {
        this.mDatabase = FirebaseDatabase.getInstance().getReference();
    }

    public void sendToDB(User user) {
        Log.d("CEVA", "TEST");
        Log.d("DATABASE", mDatabase.child("User").toString());
        mDatabase.child("User").setValue(user);
    }
}
