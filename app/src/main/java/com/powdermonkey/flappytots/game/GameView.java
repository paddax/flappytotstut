package com.powdermonkey.flappytots.game;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.util.ArrayList;
import java.util.Iterator;

import flappytots.powdermonkey.com.flappytots.R;

/**
 * Created by Peter Davis on 05/10/2016.
 */

public class GameView extends SurfaceView implements Runnable {
    private final Paint paint;
    private final RotateSprite flower;
    private final ArrayList<FallingFlower> flowers;
    private final SurfaceHolder holder;

    private boolean playing;
    private float framesPerSecond;
    private Thread gameThread;
    private FPS fps;


    public GameView(Context context) {
        super(context);

        // Initialize ourHolder and paint objects
        holder = getHolder();
        paint = new Paint();
        flower = new RotateSprite(BitmapFactory.decodeResource(this.getResources(), R.drawable.flower), 220, 220, 90);
        flowers = new ArrayList<>();
    }

    @Override
    public void run() {
        fps = new FPS();
        long count = 0;
        while (playing) {

            // Update the frame
            update();

            // Draw the frame
            draw();


            if(++count%10 == 0) {
                framesPerSecond = fps.fps() * 10;
                count = 0;
            }
        }

    }

    long frame = 0;
    /**
     * Draw the entire screen
     */
    private void draw() {
        // Make sure our drawing surface is valid before using
        if (holder.getSurface().isValid()) {
            // Lock the canvas ready to draw
            Canvas canvas = holder.lockCanvas();
            long time = System.currentTimeMillis();
            canvas.drawColor(Color.argb(255,  99, 99, 99)); // Draw the background color
            paint.setTextSize(45);
            paint.setDither(true);
            paint.setAntiAlias(true);

            // Display the current fps on the screen
            canvas.drawText("FPS:" + Math.round(framesPerSecond) + " " + flowers.size(), 20, 40, paint);

            synchronized (flowers) {
                for (Iterator<FallingFlower> iterator = flowers.iterator(); iterator.hasNext(); ) {
                    FallingFlower fcf = iterator.next();
                    fcf.update(time);
                    flower.draw(canvas, (int) fcf.getX(), (int) fcf.getY(), paint, fcf.getFrame());
                    if (fcf.getY() > canvas.getHeight() + flower.getHeight(fcf.getFrame())) {
                        iterator.remove();
                    }
                }
            }

            frame++;
            // Draw everything to the screen
            holder.unlockCanvasAndPost(canvas);
        }

    }

    /**
     * Runs the game logic updating position and state of all players
     */
    private void update() {
    }

    // If SimpleGameEngine Activity is paused/stopped
    // shutdown our thread.
    public void pause() {
        playing = false;
        try {
            gameThread.join();
        } catch (InterruptedException e) {
            Log.e("Error:", "joining thread");
        }

    }


    // If SimpleGameEngine Activity is started theb
    // start our thread.
    public void resume() {
        playing = true;
        gameThread = new Thread(this);
        gameThread.start();
    }


    float lastX, lastY;
    @Override
    public boolean onTouchEvent(MotionEvent motionEvent) {
        switch (motionEvent.getAction() & MotionEvent.ACTION_MASK) {
            case MotionEvent.ACTION_DOWN:
            case MotionEvent.ACTION_MOVE:
                float x = motionEvent.getX();
                float y = motionEvent.getY();
                if(Math.abs(lastX - motionEvent.getX()) > 5 && Math.abs(lastY - motionEvent.getY()) > 5) {
                    lastX = x;
                    lastY = y;
                    synchronized (flowers) {
                        FallingFlower fcf = new FallingFlower(x, y);
                        flowers.add(fcf);
                    }
                }

        }
        return true;
    }
}
