package com.powdermonkey.flappytots.game;

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
    private Point2f[] offset;

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
        offset = new Point2f[frames];
        for(int i=0; i<offset.length; i++) {
            offset[i] = new Point2f(size);
            offset[i].scale(0.5f);
        }

        collision = new RegionSet(new Rect2dF(-offset[0].x, -offset[0].y, offset[0].x, offset[0].y), frames);
        for(int i=0; i<offset.length; i++) {
            collision.frames.get(i);
        }
    }

    @Override
    public RegionSet getRegions() {
        return collision;
    }

    @Override
    public void draw(Canvas canvas, float x, float y, Paint paint, int frame) {
        frame = frame % frameCount;
        from.left = (int) (frame * size.x);
        from.right = (int) (from.left + size.x);


        int nx = (int) (x - offset[frame].x);
        int ny = (int)(y - offset[frame].y);
        where.set(nx, ny, (int) (nx + size.x), (int) (ny + size.y));

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
        return offset[frame];
    }

    public void setRegions(RegionSet regions) {
        if(collision.frames.size() < getFrameCount())
            throw new RuntimeException("Invalid frame count");
        this.collision = regions;
        this.collision.scale(size.x / (rawImageWidth / getFrameCount()), size.y / rawImageHeight);
        for(int i=0; i<getFrameCount(); i++) {
            this.collision.offset(i, -offset[i].x, -offset[i].y);
        }
    }

    public void setOffset(int frame, Point2f o) {
        Point2f x = new Point2f(offset[frame]);
        x.sub(o);
        this.collision.offset(frame, x.x, x.y);
        offset[frame].set(o);
    }
}
