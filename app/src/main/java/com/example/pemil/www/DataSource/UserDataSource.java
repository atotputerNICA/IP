package com.example.pemil.www.DataSource;

import android.util.Log;

import com.example.pemil.www.Models.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class UserDataSource {
    private DatabaseReference table;
    private FirebaseAuth auth;
    private FirebaseDatabase dataBase;
    private static User thisUser;

    public UserDataSource() {
        this.auth = FirebaseAuth.getInstance();
        this.dataBase = FirebaseDatabase.getInstance();
        this.table = dataBase.getReference("Users");
    }

    public void sendToDB(User user) {
        this.table.child(user.getId()).setValue(user);
        Log.d("DATABASE USER", user.toString());
    }

    public void getFromDB() {
        final FirebaseUser user = this.auth.getCurrentUser();
        table.child(user.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                thisUser = dataSnapshot.getValue(User.class);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public DatabaseReference getTable() {
        return this.table;
    }

    public FirebaseAuth getAuth() {
        return this.auth;
    }

    public FirebaseDatabase getDataBase() {
        return this.dataBase;
    }
}
