/*
 * This file is part of SimpleClans2 (2012).
 *
 *     SimpleClans2 is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     SimpleClans2 is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with SimpleClans2.  If not, see <http://www.gnu.org/licenses/>.
 *
 *     Created: 02.09.12 18:33
 */


package com.p000ison.dev.simpleclans2.util;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * A logger utility.
 */
public class Logging {
    private static Logger instance;

    public static void setInstance(Logger instance)
    {
        Logging.instance = instance;
    }

    public static void close()
    {
        setInstance(null);
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
