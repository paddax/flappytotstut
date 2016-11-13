package com.powdermonkey.flappytots.gameold;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.powdermonkey.flappytots.I2DPhysics;

import java.util.ArrayList;
import java.util.Iterator;

import com.powdermonkey.flappytots.R;

import javax.vecmath.Point2f;
import javax.vecmath.Vector2f;

/**
 * Game view
 * Created by Peter Davis on 05/10/2016.
 */

public class GameView extends SurfaceView implements Runnable {
    private final Paint paint;
    private final RotateSprite flower;
    private final FrameSprite pig;
    private final ArrayList<I2DPhysics> physics;
    private final SurfaceHolder holder;
    public int surfaceHeight;
    public int surfaceWidth;


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
        pig = new FrameSprite(BitmapFactory.decodeResource(this.getResources(), R.drawable.piggledy_colour), 150, 150, 5);
        physics = new ArrayList<>();

        holder.addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(SurfaceHolder holder) {

            }

            @Override
            public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
                surfaceWidth = width;
                surfaceHeight = height;
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

            // Display the current fps on the screen
            canvas.drawText("FPS:" + Math.round(framesPerSecond) + " " + physics.size(), 20, 40, paint);

            synchronized (physics) {
                for (I2DPhysics phys : physics) {
                    //paint.setAlpha(somefunction);
                    Point2f p = phys.getPoint();
                    pig.draw(canvas, p.x, p.y, paint, phys.getFrame());
                    //flower.draw(canvas, p.x, p.y, paint, phys.getFrame());
                }
            }

            // Draw everything to the screen
            holder.unlockCanvasAndPost(canvas);
        }

    }

    /**
     * Runs the game logic updating position and state of all players
     */
    private void update() {
        long time = System.currentTimeMillis();
        synchronized (physics) {
            for (Iterator<I2DPhysics> iterator = physics.iterator(); iterator.hasNext(); ) {
                I2DPhysics fcf = iterator.next();
                if(fcf.getPoint().x > surfaceWidth) {
                    Vector2f v = fcf.getVector();
                    fcf.setVector(-v.x, v.y);
                    Point2f p = fcf.getPoint();
                    fcf.setPoint(surfaceWidth, p.y);
                }
                if(fcf.getPoint().x < 0) {
                    Vector2f v = fcf.getVector();
                    fcf.setVector(-v.x, v.y);
                    Point2f p = fcf.getPoint();
                    fcf.setPoint(0, p.y);
                }
                fcf.update(time);
                if (fcf.getPoint().y > surfaceHeight + flower.getHeight(fcf.getFrame())) {
                    iterator.remove();
                }
            }
        }

        frame++;
    }

    // If Game Activity is paused/stopped
    // shutdown our thread.
    public void pause() {
        playing = false;
        try {
            gameThread.join();
        } catch (InterruptedException e) {
            Log.e("Error:", "joining thread");
        }

    }


    // If Game Activity is started
    // start our thread.
    public void resume() {
        playing = true;
        gameThread = new Thread(this);
        gameThread.start();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        for (int i = 0; i < event.getPointerCount(); i++) {
            switch (event.getAction() & MotionEvent.ACTION_MASK) {
                case MotionEvent.ACTION_POINTER_DOWN:
                case MotionEvent.ACTION_DOWN:
                case MotionEvent.ACTION_MOVE:
                    float x = event.getX(i);
                    float y = event.getY(i);
                    synchronized (physics) {
                        I2DPhysics fcf = new Running(x, y);
                        physics.add(fcf);
                        if(event.getHistorySize() > 1) {
                            float x1 = event.getHistoricalX(i, 0);
                            float y1 = event.getHistoricalY(i, 0);
                            x = x - x1;
                            y = y - y1;
                            fcf.setVector(x, y);
                        }
                    }
            }

        }
        return true;

    }
}
