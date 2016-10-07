package com.powdermonkey.flappytots.game;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;

import com.powdermonkey.flappytots.ISprite;

/**
 * Creates an animated rotatable image from a single image.
 *
 * Created by Peter Davis on 05/10/2016.
 */

public class FrameSprite implements ISprite {

    private final Bitmap image;
    private final int frameCount;
    private final int frameWidth;
    private final int frameHeight;
    private Rect from;
    private Rect where;

    /**
     * The src bitmap is rescaled to (width  * frames) and height
     * @param src Source bitmap
     * @param width Width of final bitmap
     * @param height Height of final bitmap
     * @param frames Number of frames in final animation
     */
    public FrameSprite(Bitmap src, int width, int height, int frames) {
        image = Bitmap.createScaledBitmap(src, (width * frames), height, false);
        from = new Rect(0, 0, width, height);
        where = new Rect(0, 0, width, height);
        frameCount = frames;
        frameWidth = width;
        frameHeight = height;
    }

    @Override
    public boolean collide(ISprite s) {
        return false;
    }

    @Override
    public void draw(Canvas canvas, float x, float y, Paint paint, int frame) {
        int i = frame % frameCount;
        from.left = i * frameWidth;
        from.right = from.left + frameWidth;

        where.set((int)(x - frameWidth / 2), (int)(y - frameHeight / 2),(int)( x + frameWidth / 2), (int) (y + frameHeight / 2));

        canvas.drawBitmap(image,
                from,
                where, paint);
    }


    @Override
    public int getFrameCount() {
        return frameCount;
    }

    public int getHeight(int frame) {
        return frameHeight;
    }
    public int getWidth(int frame) {
        return frameWidth;
    }

}
