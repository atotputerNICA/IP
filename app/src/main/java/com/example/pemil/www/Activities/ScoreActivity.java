package com.example.pemil.www.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.pemil.www.R;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        getSupportActionBar().hide();

        Intent intent = getIntent();
        cor = intent.getIntExtra("correct", 0);
        attempt = intent.getIntExtra("attemp", 0);
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

        //TODO
        player1 = (ImageView)findViewById(R.id.player1);
        player2 = (ImageView)findViewById(R.id.player2);
        player1_name = (TextView)findViewById(R.id.player1_name);
        player2_name = (TextView)findViewById(R.id.player2_name);
        result = (TextView)findViewById(R.id.versus_result);

        int result1 = 0;
        int result2 = 0;
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
}