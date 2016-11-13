package com.powdermonkey.flappytots.gameold;

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

public class FlappyViewOld extends SurfaceView implements Runnable {
    private final Paint paint;
    private final RegionSet pighit;
    private final Bitmap obsticle2;
    private final Bitmap obsticle3;
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
    private Bitmap[] dying = new Bitmap[4];
    private MovingLeft[] floors;
    private final FrameSprite floor;
    private boolean ready = false;


    public FlappyViewOld(Context context, int res) {
        super(context);
        // Initialize ourHolder and paint objects
        holder = getHolder();
        paint = new Paint();
        pigbm = BitmapFactory.decodeResource(this.getResources(), R.drawable.simple_bee_300);
        obsticle1 = BitmapFactory.decodeResource(this.getResources(), R.drawable.flower_droop_pink_300);
        obsticle2 = BitmapFactory.decodeResource(this.getResources(), R.drawable.basic_flower_pink_300);
        obsticle3 = BitmapFactory.decodeResource(this.getResources(), R.drawable.basic_flower_pink_300);
        dying[0] = BitmapFactory.decodeResource(this.getResources(), R.drawable.basic_flower_pink_300);
        dying[1] = BitmapFactory.decodeResource(this.getResources(), R.drawable.basic_flower_pink_300);
        dying[2] = BitmapFactory.decodeResource(this.getResources(), R.drawable.basic_flower_pink_300);
        dying[3] = BitmapFactory.decodeResource(this.getResources(), R.drawable.basic_flower_pink_300);
        floor = new FrameSprite(BitmapFactory.decodeResource(this.getResources(), R.drawable.repeatable_floor_300), 3 * 400 / 2, 3 * 238 / 2, 1);

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
                flower.setModeBitmaps(obsticle2, obsticle3, dying);
                pig = new FrameSprite(pigbm, surfaceHeight / 4, surfaceHeight / 5, 1);
                pig.setRegions(pighit);
                physics = new FlappyPhysics(width, height);
                physics.setPoint(width / 4, height / 2);
                physics.setSprite(pig);
                floors = new MovingLeft[width / floor.getWidth(0) + 2];
                for(int i=0; i<floors.length; i++) {
                    floors[i] = new MovingLeft(i * floor.getWidth(0) + (floor.getWidth(0) / 2), height - (floor.getHeight(0) / 2), -surfaceWidth / 4);
                    floors[i].setSprite(floor);
                }
                ready = true;
            }

            @Override
            public void surfaceDestroyed(SurfaceHolder holder) {
                ready = false;
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
        if (holder.getSurface().isValid() && ready) {
            // Lock the canvas ready to draw
            Canvas canvas = holder.lockCanvas();
            canvas.drawColor(Color.argb(255, 66, 220, 120)); // Draw the background color
            paint.setTextSize(45);
            paint.setDither(true);
            paint.setAntiAlias(true);
            paint.setColor(Color.argb(255, 156, 112, 233));
            paint.setStyle(Paint.Style.STROKE);
            canvas.drawText("SCORE:" + score, 20, 40, paint);

            paint.setAlpha(255);
            paint.setStyle(Paint.Style.FILL_AND_STROKE);

            for(int i=0; i<floors.length; i++) {
                floors[i].draw(canvas, paint);
            }

            // Display the current fps on the screen
            //canvas.drawText("FPS:" + Math.round(framesPerSecond), 20, 40, paint);


            paint.setStyle(Paint.Style.STROKE);
            //draw the background objects
            for(FlowerPhysics o: objects) {
                if(o.getMode() >= 3) {
                    int alpha = 200 - ((o.getMode() - 3) * 67);
                    if(alpha < 0)
                        alpha = 0;
                    paint.setAlpha(alpha);
                } else
                    paint.setAlpha(255);
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
        if(physics != null && ready) {
            time = System.currentTimeMillis();
            physics.update(time); // update the physics

            // Bounds check the pig and tell the physics if we are outside of bounds
            if (physics.getPoint().y > surfaceHeight - physics.getSize().y / 2) {
                physics.hitBottom((int) (surfaceHeight - (physics.getSize().y) / 2));
            }
            else if (physics.getPoint().y < physics.getSize().y / 2) {
                physics.hitTop(physics.getSize().y / 2);
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

            for(int i=0; i<floors.length; i++) {
                floors[i].update(time);
                if(floors[i].getPoint().x < -(floors[i].getSize().x / 2)) {
                    floors[i].getPoint().x += floors[i].getSize().x * floors.length;
                }
            }

            // update the list of obstacles
            for (Iterator<FlowerPhysics> i = objects.iterator(); i.hasNext(); ) {
                FlowerPhysics o = i.next();
                o.update(time);
                if(o.getPoint().x < -o.getSize().x) {
                    i.remove();
                    switch(o.thatsLife) {
                        case GROWING:
                            score+=4;
                            break;
                        case WILTING:
                            score-=2;
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
