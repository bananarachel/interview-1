/*
 * Copyright (c) 2019, Matthew Jiang. All rights reserved.
 */

package com.vmware.interview.matthew.engine;

import com.vmware.interview.matthew.shape.Shape;

import java.util.List;

public interface Drawing
{
    boolean draw(Shape shape);

    boolean draw(List<Shape> shapes);
}
