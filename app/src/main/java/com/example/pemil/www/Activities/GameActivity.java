package com.example.pemil.www.Activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import com.example.pemil.www.DataSource.CategoryDataSource;
import com.example.pemil.www.Models.Category;
import com.example.pemil.www.Models.Question;
import com.github.lzyzsd.circleprogress.DonutProgress;


import com.example.pemil.www.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;

/**
 * @author atotputerNICA
 */
public class GameActivity extends AppCompatActivity {
    DonutProgress donutProgress;
    int variable =0;
    TextView ques;
    Button OptA, OptB, OptC, OptD;
    Button play_button;
    String get;
    //Objects of different classes
    private CategoryDataSource dataSource;
    public int visibility = 0, c1 = 0, c2 = 0, c3 = 0, c4 = 0, c5 = 0, c6 = 0, c7 = 0, c8 = 0,
            c9 = 0, c10 = 0, i, j = 0, k = 0, l = 0;
    String global = null, Ques, Opta, Optb, Optc, Optd;
    ArrayList<Integer> ques_number = new ArrayList<Integer>();
    Toast toast;
    MediaPlayer mediaPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.game);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
       // setSupportActionBar(toolbar);
        dataSource = new CategoryDataSource();

        SharedPreferences shared = getSharedPreferences("Score", Context.MODE_PRIVATE);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Intent intent = getIntent();//recieving the intent send by the Navigation activity
        get = intent.getStringExtra("CATEGORY");//converting that intent message to string using the getStringExtra() method
        toast = new Toast(this);

        //attribute of the circular progress bar
        donutProgress = (DonutProgress) findViewById(R.id.donut_progress);
        donutProgress.setMax(100);
        donutProgress.setFinishedStrokeColor(Color.parseColor("#FFFB385F"));
        donutProgress.setTextColor(Color.parseColor("#FFFB385F"));
        donutProgress.setKeepScreenOn(true);
        SharedPreferences sp = getSharedPreferences("Score", Context.MODE_PRIVATE);

        //To play background sound
        if (sp.getInt("Sound", 0) == 0) {
            mediaPlayer = MediaPlayer.create(this, R.raw.alef);
            mediaPlayer.start();
            mediaPlayer.setLooping(true);
        }

        OptA = (Button) findViewById(R.id.OptionA);
        OptB = (Button) findViewById(R.id.OptionB);
        OptC = (Button) findViewById(R.id.OptionC);
        OptD = (Button) findViewById(R.id.OptionD);
        ques = (TextView) findViewById(R.id.Questions);
        play_button = (Button) findViewById(R.id.play_button);//Play button to start the game
        for (i = 0; i < 11; i++)
            ques_number.add(i-1);
        //Collections.shuffle(ques_number);
    }
    public void onClick(View v) {

        // finished questions
        if (j == 10) {
            j = 0;
            Intent intent = new Intent(GameActivity.this, ScoreActivity.class);
            intent.putExtra("correct", l);
            intent.putExtra("attemp", k);
            startActivity(intent);
            finish();
        }

        /*
         * When this method is executed then there will be new question came
         * and also same method for play button
         */
        final SharedPreferences shared = getSharedPreferences("Score", Context.MODE_PRIVATE);
        k++;
        if (visibility == 0) {
            //showing the buttons which were previously invisible
            OptA.setVisibility(View.VISIBLE);
            OptB.setVisibility(View.VISIBLE);
            OptC.setVisibility(View.VISIBLE);
            OptD.setVisibility(View.VISIBLE);
            play_button.setVisibility(View.GONE);
            donutProgress.setVisibility(View.VISIBLE);
            visibility = 1;
            new CountDownTimer(50000, 1000) {//countdowntimer
                int i = 100;

                @Override
                public void onTick(long millisUntilFinished) {
                    i = i - 2;
                    donutProgress.setProgress(i);


                }

                @Override
                public void onFinish() {
                    toast.cancel();
                    SharedPreferences.Editor editor = shared.edit();
                    /*
                     * here we are saving the data when the countdown timer will finish and
                     * it is saved in shared prefrence file that is defined in onCreate method as score
                     */
                    editor.putInt("Questions", k).commit();
                    if (get.equals("c1") && shared.getInt("Technology", 0) < l)
                        editor.putInt("Computer", l * 10).apply();
                    else if (get.equals("c2") && shared.getInt("Sports", 0) < l)
                        editor.putInt("Sports", l * 10).apply();
                    else if (get.equals("c3") && shared.getInt("Movies", 0) < l)
                        editor.putInt("Inventions", l * 10).apply();
                    else if (get.equals("c4") && shared.getInt("General Knowledge", 0) < l)
                        editor.putInt("General", l * 10).apply();
                    else if (get.equals("c5") && shared.getInt("Science", 0) < l)
                        editor.putInt("Science", l * 10).apply();
                    else if (get.equals("c6") && shared.getInt("Arts", 0) < l)
                        editor.putInt("English", l * 10).apply();
                    else if (get.equals("c7") && shared.getInt("Books", 0) < l)
                        editor.putInt("Books", l * 10).apply();
                    else if (get.equals("c8") && shared.getInt("Mathematics", 0) < l)
                        editor.putInt("Maths", l * 10).apply();
                    else if (get.equals("c9") && shared.getInt("Geography", 0) < l)
                        editor.putInt("Capitals", l * 10).apply();
                    else if (get.equals("c10") && shared.getInt("Music", 0) < l)
                        editor.putInt("Currency", l * 10).apply();
                    donutProgress.setProgress(0);
                    if(variable==0) {
                        Intent intent = new Intent(GameActivity.this, ScoreActivity.class);
                        intent.putExtra("correct", l);
                        intent.putExtra("attemp", k);
                        startActivity(intent);
                        finish();
                    }
                }
            }.start();
        }

        // notice correct answer
        if (global != null) {
            if (global.equals("A")) {
                if (v.getId() == R.id.OptionA) {
                    /*
                     * Here we use the snackbar because if we use the toast then they will be stacked
                     * an user cannot idetify which questions answer is it showing
                     */
                    Snackbar.make(v, "         Correct ☺", Snackbar.LENGTH_SHORT).show();

                    l++;
                } else {
                    Snackbar.make(v, "Incorrect\t      Answer :" + Opta + "", Snackbar.LENGTH_SHORT).show();
                }

            } else if (global.equals("B")) {
                if (v.getId() == R.id.OptionB) {
                    Snackbar.make(v, "          Correct ☺", Snackbar.LENGTH_SHORT).show();
                    l++;
                } else {
                    Snackbar.make(v, "Incorrect\t      Answer :" + Optb + "", Snackbar.LENGTH_SHORT).show();
                }

            } else if (global.equals("C")) {
                if (v.getId() == R.id.OptionC) {

                    Snackbar.make(v, "         Correct ☺", Snackbar.LENGTH_SHORT).show();
                    l++;
                } else {
                    Snackbar.make(v, "Incorrect\tAnswer :" + Optc + "", Snackbar.LENGTH_SHORT).show();
                }
            } else if (global.equals("D")) {
                if (v.getId() == R.id.OptionD) {
                    Snackbar.make(v, "        Correct ☺", Snackbar.LENGTH_SHORT).show();
                    l++;
                } else {

                    Snackbar.make(v, "Incorrect\tAnswer :" + Optd + "", Snackbar.LENGTH_SHORT).show();
                }
            }
        }




        dataSource.categoryDataBaseRef.child("General Knowledge").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                ArrayList<Question> questions = new ArrayList<>(10);
                if (dataSnapshot.exists()) {

                    Question q = dataSnapshot.child(Integer.toString(ques_number.get(j))).getValue(Question.class);
                    questions.add(q);


                    Log.i("blalala", q.toString());
                    Ques = q.getQuestion();
                    ArrayList<String> ans = new ArrayList<>(4);
                    ans.addAll(q.getIncorrect_answers());
                    ans.add(q.getCorrect_answer());

                    Log.i("size",ans.size() + "");
                    Collections.shuffle(ans);
                    // boolean type
                    if (q.getType().equals("boolean")) {
                        Opta = ans.get(0);
                        Optb = ans.get(1);
                        OptC.setVisibility(View.INVISIBLE);
                        OptD.setVisibility(View.INVISIBLE);
                    } else {
                        // multiple choice type
                        OptC.setVisibility(View.VISIBLE);
                        OptD.setVisibility(View.VISIBLE);
                        Opta = ans.get(0);
                        Optb = ans.get(1);
                        Optc = ans.get(2);
                        Optd = ans.get(3);
                    }
                    int opt =  ans.indexOf(q.getCorrect_answer());
                    char gl = (char )(opt + 65);
                    global = Character.toString(gl); // set correct answer

                    ques.setText("" + Ques);
                    OptA.setText(Opta);
                    OptB.setText(Optb);
                    OptC.setText(Optc);
                    OptD.setText(Optd);
                } else {
                    dataSource.categoryDataBaseRef.child("General Knowledge").removeEventListener(this);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        j++;
    }

    @Override
    protected void onPause() {
        super.onPause();
        variable =1;
        SharedPreferences sp = getSharedPreferences("Score", Context.MODE_PRIVATE);
        if (sp.getInt("Sound", 0) == 0)
            mediaPlayer.pause();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        variable =1;
        SharedPreferences sp = getSharedPreferences("Score", Context.MODE_PRIVATE);
        if (sp.getInt("Sound", 0) == 0)
            mediaPlayer.start();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        variable = 1;
        finish();
    }
}