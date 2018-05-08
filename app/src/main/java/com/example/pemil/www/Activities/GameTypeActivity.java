package com.example.pemil.www.Activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.pemil.www.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.MutableData;
import com.google.firebase.database.Transaction;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * @author atotputerNICA
 */
public class GameTypeActivity extends AppCompatActivity {
    private ProgressDialog progressBar;
    private Button singlePlayer;
    private Button multiPlayer;
    private Intent intent;
    private final String SINGLE_PLAYER = "Starting Game";
    private final String MULTI_PLAYER = "Searching player";
    private ArrayList<Integer> quesId = new ArrayList<>();
    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference mMatchmaker = database.getReference("matchmaker");
    private DatabaseReference mGamesReference = database.getReference("games");
    public static final String TAG = AppCompatActivity.class.getSimpleName();
    private final String NONE = "none";
    private boolean noChange = false;
    private String type;
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private FirebaseUser user = mAuth.getCurrentUser();
    private String UID = user.getUid();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.game_type);
        getSupportActionBar().hide();
        
        intent = getIntent(); //recieving the intent send by the Category activity

        //converting that intent message to string using the getStringExtra() method
        String get = intent.getStringExtra("CATEGORY");

        int numberOfQ = get.equals("Mathematics") ? 40 : get.equals("Arts") ? 20 : 50; // total number of Q in the Data base

        // Generate 10 Q indexes (only the first player will update in the game table
        Random r = new Random();
        for (int i = 0; i < 10; i++) {
            quesId.add(r.nextInt(numberOfQ));
        }
        Log.i("GameTypeActivity", quesId.toString());

    }


    public void onClick(View v) {
        type =((Button) v).getText().toString();
        final String pBarMessage = type.equals("Multi Player") ? MULTI_PLAYER : SINGLE_PLAYER;

        if (((Button) v).getText().toString().equals("Multi Player")) {
            findMatch();
        }

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
            }
        }, 400);
        progressBar = new ProgressDialog(v.getContext());//Create new object of progress bar type
        progressBar.setCancelable(false);//Progress bar cannot be cancelled by pressing any where on screen
        progressBar.setMessage(pBarMessage);//Title shown in the progress bar
        progressBar.setProgressStyle(ProgressDialog.STYLE_SPINNER);//Style of the progress bar
        progressBar.setProgress(0);//attributes
        progressBar.setMax(100);//attributes

        if (((Button) v).getText().toString().equals("Single Player")) {
            progressBar.show();//show the progress bar
            //This handler will add a delay of 3 seconds
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    //Intent start to open the navigation drawer activity
                    progressBar.cancel();//Progress bar will be cancelled (hide from screen) when this run function will execute after 3.5seconds
                    Intent newIntent = new Intent(GameTypeActivity.this, GameActivity.class);
                    newIntent.putExtra("CATEGORY", intent.getStringExtra("CATEGORY"));
                    newIntent.putExtra("GAME_TYPE", type);
                    startActivity(newIntent);
                    finish();
                }

            }, 3000);

        }

    }
    /**
     * This function is the callback of the "Find Match" button.   This function reads the current
     * value of the matchmaker storage location to determine if it thinks that we're the first arriver
     * or the second.
     *
     */
    public void findMatch() {
        mMatchmaker.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                final String matchmaker = dataSnapshot.getValue(String.class);
                Log.d(TAG, "mMatchmaker: " + dataSnapshot.toString() + " " + matchmaker);

                if (matchmaker.equals(NONE)) {
                    findMatchFirstArriver();
                } else {
                    findMatchSecondArriver(matchmaker);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    /**
     * The first arriver needs to create the game, add themselves to it, and then atomically
     * (i.e., using a transaction) verify that no one else has posted a game yet and post the game.
     * If it fails to post the game, it destroys the game.
     */
    private void findMatchFirstArriver() {
        String matchmaker;
        final DatabaseReference dbReference = mGamesReference.push();
        dbReference.child("player1").setValue(UID);
        matchmaker = dbReference.getKey();
        final String newMatchmaker = matchmaker;

        mMatchmaker.runTransaction(new Transaction.Handler() {
            @Override
            public Transaction.Result doTransaction(MutableData mutableData) {
                if (mutableData.getValue(String.class).equals(NONE)) {
                    mutableData.setValue(newMatchmaker);

                    return Transaction.success(mutableData);

                }
                // someone beat us to posting a game, so fail and retry later
                noChange = true;
                return Transaction.abort();
            }

            @Override
            public void onComplete(DatabaseError databaseError, boolean commit, DataSnapshot dataSnapshot) {
                Toast.makeText(getApplicationContext(),
                        commit ? "transaction success" : "transaction failed",
                        Toast.LENGTH_SHORT).show();
                if (!commit) {
                    // we failed to post the game, so destroy the game so we don't leave trash.
                    dbReference.removeValue();
                } else {
                    DatabaseReference gameReference = mGamesReference.child(newMatchmaker);
                    gameReference.child("questions").setValue(quesId);

                    progressBar.show();//show the progress bar
                    //This handler will add a delay of 3 seconds
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            //Intent start to open the navigation drawer activity
                            progressBar.cancel();//Progress bar will be cancelled (hide from screen) when this run function will execute after 3.5seconds
                            Intent newIntent = new Intent(GameTypeActivity.this, GameActivity.class);
                            newIntent.putExtra("CATEGORY", intent.getStringExtra("CATEGORY"));
                            newIntent.putExtra("GAME_TYPE", type);
                            newIntent.putExtra("Questions", TextUtils.join(",", quesId));
                            startActivity(newIntent);
                            finish();
                        }

                    }, 3000);

                }
            }
        });
    }

    /**
     * The second arriver needs atomically (i.e., with a transcation) verify that the game is
     * still available to join and then remove the game from the matchmaker.  It then adds
     * itself to the game, so that player0 gets a notification that the game was joined.
     * @param matchmaker
     */
    private void findMatchSecondArriver(final String matchmaker) {
        mMatchmaker.runTransaction(new Transaction.Handler() {
            @Override
            public Transaction.Result doTransaction(MutableData mutableData) {
                if (mutableData.getValue(String.class).equals(matchmaker)) {
                    mutableData.setValue(NONE);
                    return Transaction.success(mutableData);
                }
                // someone beat us to joining this game, so fail and retry later
                noChange = true;
                return Transaction.abort();
            }

            @Override
            public void onComplete(DatabaseError databaseError, boolean committed,
                                   DataSnapshot dataSnapshot) {
                if (committed) {
                    DatabaseReference gameReference = mGamesReference.child(matchmaker);
                    gameReference.child("player2").setValue(UID);
                    mMatchmaker.setValue(NONE);
                    gameReference.child("questions").addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {

                            Intent newIntent = new Intent(GameTypeActivity.this, GameActivity.class);
                            newIntent.putExtra("CATEGORY", intent.getStringExtra("CATEGORY"));
                            newIntent.putExtra("GAME_TYPE", type);
                            GenericTypeIndicator<List<Long>> t = new GenericTypeIndicator<List<Long>>() {};
                            List<Long> ques = dataSnapshot.getValue(t);
                            newIntent.putExtra("Questions", TextUtils.join(",", ques));
                            startActivity(newIntent);
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });

                }
            }
        });
    }

}
