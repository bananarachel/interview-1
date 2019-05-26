/*
 * Copyright (c) 2019, Matthew Jiang. All rights reserved.
 */

package com.vmware.interview.matthew.engine;

import com.vmware.interview.matthew.graphics.Graphic;
import com.vmware.interview.matthew.shape.Circle;
import com.vmware.interview.matthew.shape.CustomizeShape;
import com.vmware.interview.matthew.shape.Polygon;
import com.vmware.interview.matthew.shape.Shape;
import org.junit.Test;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class RenderTest
{

    @Test
    public void testDraw()
    {
        Graphic graphicContext = new Graphic();
        Drawing drawingEngine = new Render(graphicContext);

        // Draw circle
        Shape shape = new Circle(0,0, 100);
        drawingEngine.draw(shape);

        // Draw polygon
        Point[] points  = new Point[4];
        Polygon polygon = new Polygon(points);
        drawingEngine.draw(polygon);

        // Draw customize shape
        CustomizeShape customizeShape = new CustomizeShape(points[0], points[1]);
        drawingEngine.draw(customizeShape);

        // Draw a list of shapes
        List<Shape> shapes = new ArrayList<>();
        for (int i = 0; i < 5; i++)
        {
            shapes.add(new Circle(i, i, i * 10));
        }

        drawingEngine.draw(shapes);
    }
}