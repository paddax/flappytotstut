package com.powdermonkey.flappytots.game;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.Rect;
import android.support.v4.view.MotionEventCompat;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.powdermonkey.flappytots.I2DPhysics;

import java.util.ArrayList;
import java.util.Iterator;

import flappytots.powdermonkey.com.flappytots.R;

/**
 * Created by Peter Davis on 05/10/2016.
 */

public class GameView extends SurfaceView implements Runnable {
    private final Paint paint;
    private final RotateSprite flower;
    private final ArrayList<I2DPhysics> flowers;
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
        flowers = new ArrayList<>();

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
            canvas.drawText("FPS:" + Math.round(framesPerSecond) + " " + flowers.size(), 20, 40, paint);

            synchronized (flowers) {
                for (I2DPhysics ff : flowers) {
                    //paint.setAlpha(somefunction);
                    PointF p = ff.getPoint();
                    flower.draw(canvas, p.x, p.y, paint, ff.getFrame());
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
        synchronized (flowers) {
            for (Iterator<I2DPhysics> iterator = flowers.iterator(); iterator.hasNext(); ) {
                I2DPhysics fcf = iterator.next();
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
                    synchronized (flowers) {
                        I2DPhysics fcf = new Falling(x, y);
                        flowers.add(fcf);
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


    private void printSamples(MotionEvent ev) {
        final int historySize = ev.getHistorySize();
        final int pointerCount = ev.getPointerCount();
        for (int h = 0; h < historySize; h++) {
            Log.w("TEST", String.format("At historical time %d:", ev.getHistoricalEventTime(h)));
            for (int p = 0; p < pointerCount; p++) {
                Log.e("TEST", String.format("  pointer %d: (%f,%f)",
                        ev.getPointerId(p), ev.getHistoricalX(p, h), ev.getHistoricalY(p, h)));
            }
        }
        Log.w("TEST", String.format("At time %d:", ev.getEventTime()));
        for (int p = 0; p < pointerCount; p++) {
            Log.e("TEST", String.format("  pointer %d: (%f,%f)",
                    ev.getPointerId(p), ev.getX(p), ev.getY(p)));
        }
    }
}
