package com.powdermonkey.flappytots;

import android.content.Intent;
import android.media.AudioManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.powdermonkey.flappytots.game.FlappyView;

public class FlappyActivity extends AppCompatActivity {

    public static final String IMAGE_ID = "imageid";
    private FlappyView flappyView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setVolumeControlStream(AudioManager.STREAM_MUSIC);
        Intent intent = getIntent();
        int res = intent.getIntExtra(IMAGE_ID, R.drawable.piggledy_colour);
        flappyView = new FlappyView(this, res);
        setContentView(flappyView);
    }

    @Override
    protected void onResume() {
        super.onResume();
        flappyView.resume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        flappyView.pause();
    }
}
