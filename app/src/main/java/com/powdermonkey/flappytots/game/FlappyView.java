package com.powdermonkey.flappytots.game;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.RectF;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.powdermonkey.flappytots.I2DPhysics;
import com.powdermonkey.flappytots.R;

import java.util.ArrayList;
import java.util.Iterator;

/**
 * Game view
 * Created by Peter Davis on 05/10/2016.
 */

public class FlappyView extends SurfaceView implements Runnable {
    private final Paint paint;
    private FrameSprite pig;
    private RotateSprite flower;
    private final Bitmap obstical1;
    private final Bitmap pigbm;
    private FlappyPhysics physics;
    private final SurfaceHolder holder;
    private int surfaceHeight;
    private int surfaceWidth;

    private boolean playing;
    private float framesPerSecond;
    private Thread gameThread;
    private FPS fps;
    private long time;


    private ArrayList<MovingLeft> objects;


    public FlappyView(Context context, int res) {
        super(context);
        // Initialize ourHolder and paint objects
        holder = getHolder();
        paint = new Paint();
        pigbm = BitmapFactory.decodeResource(this.getResources(), res);
        obstical1 = BitmapFactory.decodeResource(this.getResources(), R.drawable.flower);

        objects = new ArrayList<>();

        holder.addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(SurfaceHolder holder) {

            }

            @Override
            public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
                surfaceWidth = width;
                surfaceHeight = height;
                flower = new RotateSprite(obstical1, surfaceHeight / 4, surfaceHeight / 4, 30);
                pig = new FrameSprite(pigbm, surfaceHeight / 4, surfaceHeight / 5, 5);
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

            // Display the current fps on the screen
            canvas.drawText("FPS:" + Math.round(framesPerSecond), 20, 40, paint);

            physics.draw(canvas, paint);

            //draw the background objects

            for(MovingLeft o: objects) {
                o.draw(canvas, paint);
            }

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
            physics.update(time);
            if (physics.getPoint().y > surfaceHeight - physics.getSprite().getHeight(physics.getFrame()) / 2) {
                physics.hitBottom(surfaceHeight - physics.getSprite().getHeight(physics.getFrame()) / 2);
            }
            if (physics.getPoint().y < physics.getSprite().getHeight(physics.getFrame()) / 2) {
                physics.hitTop(physics.getSprite().getHeight(physics.getFrame()) / 2);
            }

            if(frame % 100 == 0) {
                MovingLeft o = new MovingLeft(surfaceWidth + 100, (float) (Math.random() * surfaceHeight), -surfaceWidth / 4);
                o.setSprite(flower);
                objects.add(o);
            }

            for (Iterator<MovingLeft> i = objects.iterator(); i.hasNext(); ) {
                MovingLeft o = i.next();
                o.update(time);
                if(o.getPoint().x < -o.getSprite().getWidth(o.getFrame())) {
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
                float x = event.getX();
                float y = event.getY();
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
