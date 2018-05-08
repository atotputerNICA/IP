package com.example.pemil.www.Activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;

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
    private ProgressDialog progressBar;
    private Button.OnClickListener awesomeOnClickListener = new Button.OnClickListener() {
        @Override
        public void onClick(View b) {
            buttonClicked((Button) b);
        }
    };

    private ImageView menu;
    String[] menuitems;
    TypedArray menuIcons;
    private CharSequence mDrawerTitle;
    private CharSequence mTitle;
    private static DrawerLayout mDrawerLayout;
    private static ListView mDrawerList;
    private ActionBarDrawerToggle mDrawerToggle;
    private LinearLayout layout;

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
        Log.i("DSD", parseId(id));
        //To show button click
        new Handler().postDelayed(new Runnable() {@Override public void run(){}}, 400);

        progressBar = new ProgressDialog(b.getContext());//Create new object of progress bar type
        progressBar.setCancelable(false);//Progress bar cannot be cancelled by pressing any where on screen
        progressBar.setMessage("Getting Questions Ready ...");//Title shown in the progress bar
        progressBar.setProgressStyle(ProgressDialog.STYLE_SPINNER);//Style of the progress bar
        progressBar.setProgress(0);//attributes
        progressBar.setMax(100);//attributes
        progressBar.show();//show the progress bar
        //This handler will add a delay of 3 seconds
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                //Intent start to open the navigation drawer activity
                progressBar.cancel();//Progress bar will be cancelled (hide from screen) when this run function will execute after 3.5seconds
                Intent intent = new Intent(CategoryActivity.this, GameTypeActivity.class);
                intent.putExtra("CATEGORY", id);
                startActivity(intent);
            }

        }, 2000);


    }
    public String parseId(String id) {
        switch (id) {
            case "Technology":
                return "c1";
            case "Sports":
                return "c2";
            case "Movies":
                return "c3";
            case "General Knowledge":
                return "c4";
            case "Science":
                return "c5";
            case "Arts":
                return "c6";
            case "Books":
                return "c7";
            case "Mathematics":
                return "c8";
            case "Geography":
                return "c9";
            case "Music":
                return "c10";
            default:
                return "c0";
        }

    }

    @Override
    public void onBackPressed() {
    }
}
