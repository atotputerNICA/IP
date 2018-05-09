package com.example.pemil.www.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.example.pemil.www.R;

/**
 * @author atotputerNICA
 */
public class CategoryActivity extends AppCompatActivity {
    Button musicC;
    Button artC;
    Button booksC;
    Button generalC;
    Button techC;
    Button mathsC;
    Button moviesC;
    Button sportsC;
    Button scienceC;
    Button geoC;
    Button profile;

    private Button.OnClickListener awesomeOnClickListener = new Button.OnClickListener() {
        @Override
        public void onClick(View b) {
            buttonClicked((Button) b);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.category);
        musicC = (Button) findViewById(R.id.music);
        artC = (Button) findViewById(R.id.arts);
        booksC = (Button) findViewById(R.id.books);
        generalC = (Button) findViewById(R.id.general);
        techC = (Button) findViewById(R.id.tech);
        mathsC = (Button) findViewById(R.id.maths);
        moviesC = (Button) findViewById(R.id.movies);
        sportsC = (Button) findViewById(R.id.sports);
        scienceC = (Button) findViewById(R.id.science);
        geoC = (Button) findViewById(R.id.geo);
        profile = (Button) findViewById(R.id.to_profile);
        musicC.setOnClickListener(awesomeOnClickListener);
        artC.setOnClickListener(awesomeOnClickListener);
        booksC.setOnClickListener(awesomeOnClickListener);
        generalC.setOnClickListener(awesomeOnClickListener);
        techC.setOnClickListener(awesomeOnClickListener);
        mathsC.setOnClickListener(awesomeOnClickListener);
        moviesC.setOnClickListener(awesomeOnClickListener);
        sportsC.setOnClickListener(awesomeOnClickListener);
        scienceC.setOnClickListener(awesomeOnClickListener);
        geoC.setOnClickListener(awesomeOnClickListener);

        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), UserProfileActivity.class);
                startActivity(intent);
            }
        });

        getSupportActionBar().hide();
    }

    public void buttonClicked(Button b) {
        final String id = b.getText().toString();

        Intent intent = new Intent(CategoryActivity.this, GameTypeActivity.class);
        intent.putExtra("CATEGORY", id);
        startActivity(intent);

    }

    @Override
    public void onBackPressed() {
    }
}
