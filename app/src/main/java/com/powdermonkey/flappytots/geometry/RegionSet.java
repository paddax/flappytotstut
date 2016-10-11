package com.powdermonkey.flappytots.geometry;

import android.content.Context;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by Peter Davis on 10/10/2016.
 */

public class RegionSet {

    public List<List<IRegion>> frames = new ArrayList<>();

    public RegionSet(int width, int height, int f) {
        for(int i=0; i<f; i++) {
            Rect2dF r = new Rect2dF(-width/2, -height/2, width/2, height/2);
            ArrayList<IRegion> frame = new ArrayList<>();
            frame.add(r);
            frames.add(frame);
        }
    }

    public RegionSet(Context context, int res) {
        frames = new ArrayList<>();
        try (InputStream is = context.getResources().openRawResource(res)) {
            InputStreamReader r = new InputStreamReader(is);
            CSVParser parser = new CSVParser(r, CSVFormat.RFC4180);

            for (Iterator<CSVRecord> it = parser.iterator(); it.hasNext(); ) {
                ArrayList<IRegion>  frame = new ArrayList<>();
                frames.add(frame);
                CSVRecord rec = it.next();
                Iterator<String> recit = rec.iterator();
                frame: while (recit.hasNext()) {
                    switch (recit.next()) {
                        case "r":
                            frame.add(readRect(recit));
                            break;
                        case "c":
                            frame.add(readCircle(recit));
                            break;
                        default:
                            break frame;
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static IRegion readCircle(Iterator<String> recit) {
        float x1 = Float.parseFloat(recit.next());
        float y1 = Float.parseFloat(recit.next());
        float r1 = Float.parseFloat(recit.next());
        return new Circle2dF(x1, y1, r1);
    }

    private static IRegion readRect(Iterator<String> recit) {
        float x1 = Float.parseFloat(recit.next());
        float y1 = Float.parseFloat(recit.next());
        float r1 = Float.parseFloat(recit.next());
        float b1 = Float.parseFloat(recit.next());
        return new Rect2dF(x1, y1, r1, b1);
    }

    public void scale(float x, float y) {
        for(List<IRegion> frame: frames) {
           for(IRegion r: frame) {
               r.scale(x, y);
           }
        }
    }

    public void offset(float x, float y) {
        for(List<IRegion> frame: frames) {
            for(IRegion r: frame) {
                r.offset(x, y);
            }
        }

    }
}