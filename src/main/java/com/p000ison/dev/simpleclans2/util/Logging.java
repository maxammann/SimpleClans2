/*
 * Copyright (C) 2012 p000ison
 *
 * This work is licensed under the Creative Commons
 * Attribution-NonCommercial-NoDerivs 3.0 Unported License. To view a copy of
 * this license, visit http://creativecommons.org/licenses/by-nc-nd/3.0/ or send
 * a letter to Creative Commons, 171 Second Street, Suite 300, San Francisco,
 * California, 94105, USA.
 */

package com.p000ison.dev.simpleclans2.util;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * A logger utility.
 */
public class Logging {
    private static Logger instance;

    public Logging(Logger logger)
    {
        instance = logger;
    }

    public static void close()
    {
        instance = null;
    }

    public static void debug(String msg, Object... args)
    {
        debug(Level.INFO, String.format(msg, args), null);
    }

    public static void debug(String msg, Level level, Object... args)
    {
        debug(Level.SEVERE, String.format(msg, args), null);
    }

    public static void debug(String msg)
    {
        debug(Level.INFO, msg, null);
    }

    public static void debug(Level level, String msg)
    {
        debug(Level.SEVERE, msg, null);
    }

    public static void debug(Throwable ex)
    {
        debug(Level.SEVERE, null, ex);
    }

    public static void debug(Throwable ex, String msg)
    {
        debug(Level.SEVERE, msg, ex);
    }

    public static void debug(Throwable ex, String msg, Object... args)
    {
        debug(Level.SEVERE, String.format(msg, args), ex);
    }

    public static void debug(Level level, String msg, Throwable ex)
    {

        if (instance == null) {
            return;
        }

        if (level == null) {
            level = Level.INFO;
        }

        instance.log(level, msg, ex);
    }

}
