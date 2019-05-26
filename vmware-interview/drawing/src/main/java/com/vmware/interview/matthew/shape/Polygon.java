/*
 * Copyright (c) 2019, Matthew Jiang. All rights reserved.
 */

package com.vmware.interview.matthew.shape;

import com.vmware.interview.matthew.graphics.Graphic;

import java.awt.*;

public class Polygon implements Shape
{
    Point[] points;

    public Polygon(Point[] points)
    {
        this.points = points;
    }

    @Override
    public boolean onDraw(Graphic g)
    {
        System.out.println("Drawing polygon");
        g.moveToPoint(points[0]);
        for (int i = 1; i < points.length; ++i)
        {
            g.lineToPoint(points[i]);
        }
        return true;
    }
}
