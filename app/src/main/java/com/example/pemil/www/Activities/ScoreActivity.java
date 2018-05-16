package com.example.pemil.www.Activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.pemil.www.Models.User;
import com.example.pemil.www.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

/**
 * @author atotputerNICA
 */
public class ScoreActivity extends AppCompatActivity {
    TextView correct, incorrect, attempted, score, you;
    int cor = 0, incorr = 0, attempt = 0, scor = 0, yo = 0;
    int x = 0;
    private static final int def = 0;
    ImageView player1;
    ImageView player2;
    TextView player1_name;
    TextView player2_name;
    TextView result;
    private ProgressDialog progressBar;

    private String player, category, matchmaker;
    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference mGamesReference = database.getReference("games");
    private DatabaseReference userTable = database.getReference("Users");

    private void setImage(String name, String surname, ImageView iv) {
        int a = (int)name.charAt(0);
        int b = (int)surname.charAt(0);
        int hexValue = (((a + b) * 21) - 18) % 6;

        switch(hexValue) {
            case 0:
                iv.setImageResource(R.drawable.ic_monster1);
                break;
            case 1:
                iv.setImageResource(R.drawable.ic_monster2);
                break;
            case 2:
                iv.setImageResource(R.drawable.ic_monster3);
                break;
            case 3:
                iv.setImageResource(R.drawable.ic_monster4);
                break;
            case 4:
                iv.setImageResource(R.drawable.ic_monster5);
                break;
            case 5:
                iv.setImageResource(R.drawable.ic_monster6);
                break;
            default:
                break;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        getSupportActionBar().hide();

        Intent intent = getIntent();
        String gameType = intent.getStringExtra("GAME_TYPE");
        cor = intent.getIntExtra("correct", 0);
        attempt = intent.getIntExtra("attemp", 0);
        category = intent.getStringExtra("CATEGORY");

        if (gameType.equals("Multi Player")) {
            matchmaker = intent.getStringExtra("MATCHMAKER");
            player = intent.getStringExtra("PLAYER");
            Log.i("DANAAAA", "DANA");
            Log.i("DANAAAAAA", gameType + category + matchmaker);
            mGamesReference = mGamesReference.child(category).child(matchmaker);
            Log.i("DANAAAA", mGamesReference.toString());
        }


        incorr = attempt - cor;
        scor = 10 * cor;

        correct = (TextView) findViewById(R.id.correct);
        incorrect = (TextView) findViewById(R.id.incorrect);
        attempted = (TextView) findViewById(R.id.attempted);
        score = (TextView) findViewById(R.id.score);
        you = (TextView) findViewById(R.id.you);

        attempted.setText("  " + attempt);
        correct.setText("  " + cor);
        incorrect.setText("  " + incorr);
        score.setText("Score  :    " + scor);

        float x1 = (cor * 100) / attempt;
        if (x1 < 40)
            you.setText("You Need Improvement");
        else if (x1 < 70)
            you.setText("You are an average Quizzer");
        else if (x1 < 90)
            you.setText("You are an above average Quizzer ");
        else
            you.setText("You are a brilliant  Quizzer ");


        player1 = (ImageView)findViewById(R.id.player1);
        player2 = (ImageView)findViewById(R.id.player2);
        player1_name = (TextView)findViewById(R.id.player1_name);
        player2_name = (TextView)findViewById(R.id.player2_name);
        result = (TextView)findViewById(R.id.versus_result);



        if (gameType.equals("Multi Player")) {
            mGamesReference.child(player+"score").setValue(scor);
            final String[] players = new String[2];
            progressBar = new ProgressDialog(player1.getContext());//Create new object of progress bar type
            progressBar.setCancelable(false);//Progress bar cannot be cancelled by pressing any where on screen
            progressBar.setMessage("Collecting data!");//Title shown in the progress bar
            progressBar.setProgressStyle(ProgressDialog.STYLE_SPINNER);//Style of the progress bar
            progressBar.setProgress(0);//attributes
            progressBar.setMax(100);//attributes
            progressBar.show();//show the progress bar
            //This handler will add a delay of 5 seconds
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    //Intent start to open the navigation drawer activity
                    progressBar.cancel();//Progress bar will be cancelled (hide from screen) when
                                        // this run function will execute after 5 seconds
                }

            }, 5000);
            mGamesReference.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    long result1;
                    long result2;
                    String p1 = dataSnapshot.child("player1").getValue(String.class);
                    String p2 = dataSnapshot.child("player2").getValue(String.class);
                    players[0] = player.equals("player1") ? p1 : p2;
                    players[1] = player.equals("player1") ? p2 : p1;
                    Log.i("FKFDKFKDLFK", players[0] + " " + players[1]);
                    result1 = dataSnapshot.child("player1score").getValue(Long.class);
                    result2 = dataSnapshot.child("player2score").getValue(Long.class);
                    if (result1 > result2) {
                        result.setText("Wew, you won! Congrats!");
                    } else {
                        if (result1 == result2) {
                            result.setText("It's a draw! No one wins! go home!");
                        } else {
                            result.setText("One of us is a looser and that one is not me!");
                        }
                    }

                }


                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });

            userTable.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    User u1 = dataSnapshot.child(players[0]).getValue(User.class);
                    User u2 = dataSnapshot.child(players[1]).getValue(User.class);
                    player1_name.setText(u1.getName());
                    player2_name.setText(u2.getName());
                    setImage(u1.getName(), u1.getSurname(), player1);
                    setImage(u2.getName(), u2.getSurname(), player2);

                    player1.setVisibility(View.VISIBLE);
                    player2.setVisibility(View.VISIBLE);
                    player1_name.setVisibility(View.VISIBLE);
                    player2_name.setVisibility(View.VISIBLE);
                    result.setVisibility(View.VISIBLE);
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });


        }
    }


}