package com.powdermonkey.flappytots.game;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.Rect;

import com.powdermonkey.flappytots.ISprite;
import com.powdermonkey.flappytots.geometry.IRegion;
import com.powdermonkey.flappytots.geometry.RegionSet;

import java.util.List;

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
    private final float rawImageWidth;
    private final float rawImageHeight;
    private Rect from;
    private Rect where;
    private RegionSet collision;

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
        frameWidth = width;
        frameHeight = height;

        collision = new RegionSet(width, height, frames);
    }

    @Override
    public List<? extends IRegion> getRegions(int frame) {
        return collision.frames.get(frame % getFrameCount());
    }

    @Override
    public boolean collide(List<? extends IRegion> regions, int frame) {
        for (IRegion r : collision.frames.get(frame)) {
            for (IRegion r1 : regions) {
                if (r.intersect(r1))
                    return true;
            }
        }
        return false;
    }

    @Override
    public void updateRegions(PointF p, int frame) {
        for(IRegion r: collision.frames.get(frame % getFrameCount()))
            r.move(p);
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

    public void setRegions(RegionSet regions) {
        this.collision = regions;
        this.collision.scale(frameWidth / (rawImageWidth / getFrameCount()), frameHeight / rawImageHeight);
        this.collision.offset(frameWidth / -2.0f, frameHeight / -2.0f);
        if(collision.frames.size() < getFrameCount())
            throw new RuntimeException("Invalid frame count");
    }
}
