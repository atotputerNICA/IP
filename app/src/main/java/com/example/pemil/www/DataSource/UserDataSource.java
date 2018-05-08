package com.example.pemil.www.DataSource;

import android.util.Log;

import com.example.pemil.www.Models.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class UserDataSource {
    private DatabaseReference table;

    public UserDataSource() {
        FirebaseAuth auth = FirebaseAuth.getInstance();
        FirebaseDatabase dataBase = FirebaseDatabase.getInstance();
        table = dataBase.getReference("Users");
    }

    public void sendToDB(User user) {
            table.child(user.getId()).setValue(user);
            Log.d("DATABASE USER", user.toString());
    }
}
