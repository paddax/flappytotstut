package com.powdermonkey.flappytots;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.powdermonkey.flappytots.game.FlappyView;
import com.powdermonkey.flappytots.game.GameView;

public class FlappyActivity extends AppCompatActivity {

    public static final String IMAGE_ID = "imageid";
    private FlappyView flappyView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        int res = intent.getIntExtra(IMAGE_ID, R.drawable.piggledy_colour);
        flappyView = new FlappyView(this, res);
        setContentView(flappyView);
    }

    // This method executes when the player starts the game
    @Override
    protected void onResume() {
        super.onResume();

        // Tell the gameView resume method to execute
        flappyView.resume();
    }

    // This method executes when the player quits the game
    @Override
    protected void onPause() {
        super.onPause();

        // Tell the gameView pause method to execute
        flappyView.pause();
    }
}
