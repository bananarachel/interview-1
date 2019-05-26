/*
 * Copyright (c) 2019, Matthew Jiang. All rights reserved.
 */

package com.vmware.interview.matthew.shape;


import com.vmware.interview.matthew.graphics.Graphic;

public class Circle implements Shape
{
    private int centerX;
    private int centerY;
    private int radius;

    public Circle(int x, int y, int radius)
    {
        this.centerX = x;
        this.centerY = y;
        this.radius = radius;
    }

    @Override
    public boolean onDraw(Graphic g)
    {
        System.out.println("Drawing circle");
        g.drawCircle(centerX, centerY, radius);
        return true;
    }
}
