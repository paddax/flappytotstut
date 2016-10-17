package com.powdermonkey.flappytots.game;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.powdermonkey.flappytots.R;
import com.powdermonkey.flappytots.geometry.RegionSet;

import java.util.ArrayList;
import java.util.Iterator;

/**
 * Game view
 * Created by Peter Davis on 05/10/2016.
 */

public class FlappyView extends SurfaceView implements Runnable {
    private final Paint paint;
    private final RegionSet pighit;
    private final Bitmap obsticle2;
    private final Bitmap obsticle3;
    private final Bitmap obsticle4;
    private FrameSprite pig;
    private FlowerSprite flower;
    private final Bitmap obsticle1;
    private final Bitmap pigbm;
    private FlappyPhysics physics;
    private final SurfaceHolder holder;
    private int surfaceHeight;
    private int surfaceWidth;
    private int score;

    private boolean playing;
    private float framesPerSecond;
    private Thread gameThread;
    private FPS fps;
    private long time;


    private ArrayList<FlowerPhysics> objects;
    private boolean hit;
    private long nextFrame;


    public FlappyView(Context context, int res) {
        super(context);
        // Initialize ourHolder and paint objects
        holder = getHolder();
        paint = new Paint();
        pigbm = BitmapFactory.decodeResource(this.getResources(), res);
        obsticle1 = BitmapFactory.decodeResource(this.getResources(), R.drawable.wilted);
        obsticle2 = BitmapFactory.decodeResource(this.getResources(), R.drawable.flower90);
        obsticle3 = BitmapFactory.decodeResource(this.getResources(), R.drawable.flower);
        obsticle4 = BitmapFactory.decodeResource(this.getResources(), R.drawable.poof);

        pighit = new RegionSet(context, R.raw.piggledy_hit);

        objects = new ArrayList<>();

        holder.addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(SurfaceHolder holder) {

            }

            @Override
            public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
                surfaceWidth = width;
                surfaceHeight = height;
                flower = new FlowerSprite(obsticle1, surfaceHeight / 4, surfaceHeight / 4, 30);
                flower.setModeBitmaps(obsticle2, obsticle3, obsticle4);
                pig = new FrameSprite(pigbm, surfaceHeight / 4, surfaceHeight / 5, 5);
                pig.setRegions(pighit);
                physics = new FlappyPhysics(width, height);
                physics.setPoint(width / 4, height / 2);
                physics.setSprite(pig);
            }

            @Override
            public void surfaceDestroyed(SurfaceHolder holder) {

            }
        });
    }

    @Override
    public void run() {
        fps = new FPS();
        long count = 0;
        while (playing) {

            // Update the frame
            update();

            // Draw the frame
            if(pig != null)
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
            canvas.drawColor(Color.argb(255, 66, 220, 120)); // Draw the background color
            paint.setTextSize(45);
            paint.setDither(true);
            paint.setAntiAlias(true);
            paint.setColor(Color.argb(255, 156, 112, 233));

            paint.setAlpha(255);
            paint.setStyle(Paint.Style.FILL_AND_STROKE);

            // Display the current fps on the screen
            //canvas.drawText("FPS:" + Math.round(framesPerSecond), 20, 40, paint);
            canvas.drawText("SCORE:" + score, 20, 40, paint);


            paint.setStyle(Paint.Style.STROKE);
            //draw the background objects
            for(FlowerPhysics o: objects) {
                o.draw(canvas, paint);
            }

            // Draw the pig
            paint.setAlpha(hit?45:255); // Set the alpha from the hit state
            physics.draw(canvas, paint);

            // Draw everything to the screen
            holder.unlockCanvasAndPost(canvas);
        }

    }

    /**
     * Runs the game logic updating position and state of all players
     */
    private void update() {
        if(physics != null) {
            time = System.currentTimeMillis();
            physics.update(time); // update the physics

            // Bounds check the pig and tell the physics if we are outside of bounds
            if (physics.getPoint().y > surfaceHeight - physics.getSprite().getHeight(physics.getFrame()) / 2) {
                physics.hitBottom(surfaceHeight - physics.getSprite().getHeight(physics.getFrame()) / 2);
            }
            else if (physics.getPoint().y < physics.getSprite().getHeight(physics.getFrame()) / 2) {
                physics.hitTop(physics.getSprite().getHeight(physics.getFrame()) / 2);
            }

            // nextFrame contains the next frame for creating an obstacle
            if(frame > nextFrame) {
                // Randomly determine when next flower occurs (this should not be frame based but time based)
                nextFrame =  frame + 40 + Math.round(Math.random() * 60);
                // construct a new obstacle somewhere off the right of the screen
                FlowerPhysics o = new FlowerPhysics(surfaceWidth + 100, (float) (Math.random() * surfaceHeight), -surfaceWidth / 4);
                o.setSprite(flower);
                objects.add(o);
            }

            // update the list of obstacles
            for (Iterator<FlowerPhysics> i = objects.iterator(); i.hasNext(); ) {
                FlowerPhysics o = i.next();
                o.update(time);
                if(o.getPoint().x < -o.getSprite().getWidth(o.getFrame())) {
                    i.remove();
                    switch(o.thatsLife) {
                        case GROWING:
                            score++;
                            break;
                        case WILTING:
                        case DYING:
                        case DEAD:
                            score--;
                            break;
                    }
                }
            }

            // Check for collision between pig and any flower
/*
            physics.updateRegions();
            hit = false;
            for(MovingLeft ml: objects) {
                ml.updateRegions();
                if(physics.collide(ml.getRegions())) {
                    hit = true;
                }
            }
*/
            physics.updateRegions();
            for (Iterator<FlowerPhysics> i = objects.iterator(); i.hasNext(); ) {
                FlowerPhysics ml = i.next();
                ml.updateRegions();
                ml.setCollision(physics.collide(ml.getRegions()));
                if(ml.isDead()) {
                    i.remove();
                }
            }


            frame++;
        }
    }

    /**
     * shutdown game thread.
     */
    public void pause() {
        playing = false;
        try {
            gameThread.join();
        } catch (InterruptedException e) {
            Log.e("Error:", "joining thread");
        }
    }


    /**
     * start the game thread.
     */
    public void resume() {
        playing = true;
        gameThread = new Thread(this);
        gameThread.start();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction() & MotionEvent.ACTION_MASK) {
            case MotionEvent.ACTION_DOWN:
                physics.impulse(time);
                physics.glide(true);
                break;
            case MotionEvent.ACTION_UP:
                physics.glide(false);
                break;
        }

        return true;
    }
}
