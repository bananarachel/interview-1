/*
 * Copyright (c) 2019, Matthew Jiang. All rights reserved.
 */

package com.vmware.interview.matthew.engine;

import com.vmware.interview.matthew.graphics.Graphic;
import com.vmware.interview.matthew.shape.Shape;

import java.util.List;

/**
 * Render engine, hold graphic context and driver draw engine
 */
public class Render implements Drawing
{

    /**
     * Graphic context
     */
    private Graphic graphic;

    public Render(Graphic graphic)
    {
        this.graphic = graphic;
    }


    private void preprocess()
    {
        System.out.println("Render - preprocess");
        graphic.prepareGraphic();
    }

    private void postprocess()
    {
        System.out.println("Render - postprocess");
        graphic.cleanupGraphic();
    }


    @Override
    public boolean draw(Shape shape)
    {
        preprocess();
        shape.onDraw(this.graphic);
        postprocess();

        return true;
    }

    @Override
    public boolean draw(List<Shape> shapes)
    {
        for (Shape shape : shapes)
        {
            if(!draw(shape))
            {
                return false;
            }
        }
        return true;
    }
}
