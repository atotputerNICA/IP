package com.example.pemil.www.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.pemil.www.DataSource.UserDataSource;
import com.example.pemil.www.Models.User;
import com.example.pemil.www.R;
import com.facebook.login.LoginManager;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

/**
 * Created by pemil on 28.04.2018.
 */

public class UserProfileActivity extends AppCompatActivity {
    private Button back;
    private TextView name;
    private TextView country;
    private EditText userId;
    private EditText email;
    private FirebaseUser user;
    private ImageView img;
    private LinearLayout logout;
    private FirebaseUser fbUser;
    private UserDataSource source;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_profile);
        getSupportActionBar().hide();

        back = (Button) findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), CategoryActivity.class);
                startActivity(intent);
            }
        });

        name = (TextView) findViewById(R.id.user_name);
        country = (TextView) findViewById(R.id.country);
        userId = (EditText) findViewById(R.id.user_id);
        userId.setEnabled(false);
        email = (EditText) findViewById(R.id.email);
        email.setEnabled(false);
        img = (ImageView) findViewById(R.id.profile_image);
        logout = (LinearLayout) findViewById(R.id.logout);

        getUserDataFromFirebase();

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                source.getAuth().getInstance().signOut();
                GoogleSignInClient mGoogleSignInClient ;
                GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                        .requestIdToken(getString(R.string.default_web_client_id))
                        .requestEmail()
                        .build();
                mGoogleSignInClient = GoogleSignIn.getClient(getBaseContext(), gso);
                mGoogleSignInClient.signOut().addOnCompleteListener(UserProfileActivity.this,
                        new OnCompleteListener<Void>() {  //signout Google
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                FirebaseAuth.getInstance().signOut(); //signout firebase
                                Intent setupIntent = new Intent(getBaseContext(), MainActivity.class);
                                setupIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                startActivity(setupIntent);
                                finish();
                            }
                        });
                LoginManager.getInstance().logOut();
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
            }
        });
    }

    private void getUserDataFromFirebase() {
        source = new UserDataSource();

        fbUser = source.getAuth().getCurrentUser();

        source.getTable().child(fbUser.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                User thisUser = dataSnapshot.getValue(User.class);
                name.setText(thisUser.getName() + " " + thisUser.getSurname());
                if (thisUser.getCountry().equals("")) {
                    country.setText("Country");
                } else {
                    country.setText(thisUser.getCountry());
                }
                userId.setText(thisUser.getId());
                email.setText(thisUser.getEmail());
                setProfileImage(thisUser.getName(), thisUser.getSurname());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void setProfileImage(String s1, String s2) {
        int a = (int)s1.charAt(0);
        int b = (int)s2.charAt(0);
        int hexValue = (((a + b) * 21) - 18) % 6;
        switch(hexValue) {
            case 0:
                img.setImageResource(R.drawable.ic_monster1);
                break;
            case 1:
                img.setImageResource(R.drawable.ic_monster2);
                break;
            case 2:
                img.setImageResource(R.drawable.ic_monster3);
                break;
            case 3:
                img.setImageResource(R.drawable.ic_monster4);
                break;
            case 4:
                img.setImageResource(R.drawable.ic_monster5);
                break;
            case 5:
                img.setImageResource(R.drawable.ic_monster6);
                break;
            default:
                break;
        }
    }
}
