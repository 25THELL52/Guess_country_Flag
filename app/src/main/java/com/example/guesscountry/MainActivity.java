package com.example.guesscountry;

import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

public class MainActivity extends AppCompatActivity {


    Button button;
    ListView countrieslist;
    ImageView flagimage;
    TextView Scoretv;
    List<String> countries;
    Scanner scan;
    ArrayAdapter<String> mycountrieslistadapter;
    int s;
    String drawableName;
    int score = 0;
    int counter = 0;
    String[] countries_to_display;
    int rightanswerpos;
    MediaPlayer mpr;
    MediaPlayer mpw;


    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        MediaManager.InitMediaPlayer(this);


        // Drawable greendivider= new ColorDrawable(getResources().getColor(R.color.green));
        //Drawable reddivider= new ColorDrawable(getResources().getColor(R.color.red));

        Scoretv = (TextView) findViewById(R.id.Scoretv);
        button = (Button) findViewById(R.id.newGame_btn);
        flagimage = (ImageView) findViewById(R.id.flagimage);
        countries = new ArrayList<String>();
        scan = new Scanner(getResources().openRawResource(R.raw.countries));
        countrieslist = (ListView) findViewById(R.id.countrieslist);

        //filling the arraylist with the countries.txt content
        while (scan.hasNextLine()) {

            String line = scan.nextLine();
            countries.add(line);

        }
        scan.close();

        setup();

        countrieslist.setOnItemClickListener((list, row, index, rowID) -> {


            if (counter < 10) {

                if (index == rightanswerpos) {
                    score++;

                    MediaManager.LoadMusic(R.raw.correctanswersound);
                    MediaManager.PlayMusic();

                    Scoretv.setText(String.valueOf(score) + "/10");

                } else {

                    MediaManager.LoadMusic(R.raw.wronganswersound);
                    MediaManager.PlayMusic();

                }

                counter++;
                setup();
            } else {
                button.setBackgroundTintList(ContextCompat.getColorStateList(this, R.color.purple_500));
                button.setTextColor(Color.WHITE);
            }
        });
    }


    public void setup() {

//filling the flagimage imageview randomly

        s = countries.size();
        Random rand = new Random();
        int displayedFlagIndex = rand.nextInt(s);
        drawableName = countries.get(displayedFlagIndex);
        int resid = getResources().getIdentifier(drawableName.replaceAll(" ", ""), "drawable", getPackageName());
        flagimage.setImageResource(resid);


        /// filling the Listview array countries_to_display
        // adding the answer randomly to the countries_to_display array
        Random rand1 = new Random();
        rightanswerpos = rand1.nextInt(5);
        countries_to_display = new String[5];
        countries_to_display[rightanswerpos] = drawableName;
        Log.i("user", "right answer position is:" + rightanswerpos);

        // randomly filling the rest of the list except for the already added answers or the already filled
        // index of the added answer

        int r;
        List<Integer> alreadyickedCountries = new ArrayList<>();
        alreadyickedCountries.add(displayedFlagIndex);

        for (int i = 0; i < 5; i++) {

            if (i == rightanswerpos) continue;
            else {
                r = rand.nextInt(s);
                while (alreadyickedCountries.contains(r)) {
                    r = rand.nextInt(s);
                }

                countries_to_display[i] = countries.get(r);
                alreadyickedCountries.add(r);
            }


        }

        // creating a ArrayAdapter for the ListView countrieslist

        mycountrieslistadapter = new ArrayAdapter<String>(this, R.layout.simple_list_item_1, R.id.tvforadapter,
                countries_to_display);
        countrieslist.setAdapter(mycountrieslistadapter);


    }

    public void onclickbutton(View view) {
        counter = 0;
        score = 0;
        Scoretv.setText(String.valueOf(score));
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            button.setBackgroundTintList(ContextCompat.getColorStateList(this, R.color.purple_200));
        }
        button.setTextColor(Color.BLACK);

        setup();


    }


}


