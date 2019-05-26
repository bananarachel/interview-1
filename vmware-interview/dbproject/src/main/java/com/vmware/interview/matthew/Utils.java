/*
 * Copyright (c) 2019, Matthew Jiang. All rights reserved.
 */

package com.vmware.interview.matthew;

/**
 * Utils class for some helper method
 */
public class Utils
{
    public static String redirectToError(String message)
    {
        return "redirect:/errorinfo?message=" + message;
    }

    public static void writeLog(Exception e)
    {
        //TODO: write error to log file
    }
}
