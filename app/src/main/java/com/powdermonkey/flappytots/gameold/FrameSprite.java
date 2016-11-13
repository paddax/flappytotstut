package com.powdermonkey.flappytots.gameold;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;

import com.powdermonkey.flappytots.ISprite;
import com.powdermonkey.flappytots.geometry.Rect2dF;
import com.powdermonkey.flappytots.geometry.RegionSet;

import javax.vecmath.Point2f;

/**
 * Creates an animated rotatable image from a single image.
 *
 * Created by Peter Davis on 05/10/2016.
 */

public class FrameSprite implements ISprite {

    private final Bitmap image;
    private final int frameCount;
    private final float rawImageWidth;
    private final float rawImageHeight;
    private Rect from;
    private Rect where;
    private RegionSet collision;
    private Point2f size;
    private Point2f offset;

    /**
     * The src bitmap is rescaled to (width  * frames) and height
     * @param src Source bitmap
     * @param width Width of final bitmap
     * @param height Height of final bitmap
     * @param frames Number of frames in final animation
     */
    public FrameSprite(Bitmap src, int width, int height, int frames) {
        rawImageWidth = src.getWidth();
        rawImageHeight = src.getHeight();
        image = Bitmap.createScaledBitmap(src, (width * frames), height, false);
        from = new Rect(0, 0, width, height);
        where = new Rect(0, 0, width, height);
        frameCount = frames;
        size = new Point2f(width, height);
        offset = new Point2f(size);
        offset.scale(0.5f);

        collision = new RegionSet(new Rect2dF(-offset.x, -offset.y, offset.x, offset.y), frames);
    }

    @Override
    public RegionSet getRegions() {
        return collision;
    }

    @Override
    public void draw(Canvas canvas, float x, float y, Paint paint, int frame) {
        int i = frame % frameCount;
        from.left = (int) (i * size.x);
        from.right = (int) (from.left + size.y);

        where.set((int)(x - offset.x), (int)(y - offset.y),(int)( x + offset.x), (int) (y + offset.y));

        canvas.drawBitmap(image,
                from,
                where, paint);
    }


    @Override
    public int getFrameCount() {
        return frameCount;
    }

    public int getHeight(int frame) {
        return (int) size.y;
    }
    public int getWidth(int frame) {
        return (int) size.x;
    }

    @Override
    public Point2f getSize(int frame) {
        return size;
    }

    @Override
    public Point2f getOffset(int frame) {
        return offset;
    }

    public void setRegions(RegionSet regions) {
        this.collision = regions;
        this.collision.scale(size.x / (rawImageWidth / getFrameCount()), size.y / rawImageHeight);
        this.collision.offset(size.x / -2.0f, size.y / -2.0f);
        if(collision.frames.size() < getFrameCount())
            throw new RuntimeException("Invalid frame count");
    }
}
