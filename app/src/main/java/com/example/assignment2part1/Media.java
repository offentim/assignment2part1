package com.example.assignment2part1;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class Media extends AppCompatActivity {
    private int x;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        x = 5;
        if (x == 5) {
            MediaPlayer mediaPlayer = MediaPlayer.create(this, R.raw.sound1);
            mediaPlayer.pause(); // no need to call prepare(); create() does that for you
        }
        else if(x==6){
            MediaPlayer mediaPlayer = MediaPlayer.create(this, R.raw.sound2);
            mediaPlayer.start(); // no need to call prepare(); create() does that for you
        }
        else if(x==7){
            MediaPlayer mediaPlayer = MediaPlayer.create(this, R.raw.sound2);
            mediaPlayer.start(); // no need to call prepare(); create() does that for you
        }
        else if(x==8){
            MediaPlayer mediaPlayer = MediaPlayer.create(this, R.raw.sound2);
            mediaPlayer.start(); // no need to call prepare(); create() does that for you
        }
    }
}
