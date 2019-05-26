/*
 * Copyright (c) 2019, Matthew Jiang. All rights reserved.
 */

package com.vmware.interview.matthew.shape;

import com.vmware.interview.matthew.graphics.Graphic;

import java.awt.*;

public class CustomizeShape implements Shape
{
    private Point startPoint;
    private Point endPoint;

    public CustomizeShape(Point startPoint, Point endPoint)
    {
        this.startPoint = startPoint;
        this.endPoint = endPoint;
    }


    @Override
    public boolean onDraw(Graphic g)
    {
        System.out.println("Drawing customize shape");
        return true;
    }
}
