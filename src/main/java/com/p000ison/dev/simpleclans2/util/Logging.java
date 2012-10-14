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
 *     Last modified: 10.10.12 21:57
 */


package com.p000ison.dev.simpleclans2.util;

import com.p000ison.dev.simpleclans2.exceptions.handling.ExceptionReport;
import com.p000ison.dev.simpleclans2.exceptions.handling.ExceptionReporterTask;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * A logger utility.
 */
public final class Logging {
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
        debugRaw(Level.INFO, String.format(msg, args), null);
    }

    public static void debug(Level level, String msg, Object... args)
    {
        debugRaw(level, String.format(msg, args), null);
    }

    public static void debug(String msg)
    {
        debugRaw(Level.INFO, msg, null);
    }

    public static void debug(Level level, String msg)
    {
        debugRaw(level, msg, null);
    }

    public static void debug(Throwable ex)
    {
        debugRaw(Level.SEVERE, null, ex);
    }

    public static void debug(Throwable ex, String msg)
    {
        debugRaw(Level.SEVERE, msg, ex);
    }

    public static void debug(Throwable ex, String msg, Object... args)
    {
        debugRaw(Level.SEVERE, String.format(msg, args), ex);
    }

    private static void debugRaw(Level level, String msg, Throwable ex)
    {

        if (instance == null) {
            return;
        }

        if (level == null) {
            level = Level.INFO;
        }

        if (ex == null) {
            instance.log(level, msg);
        } else {
            instance.log(level, msg, ex);
            ExceptionReporterTask.addReport(new ExceptionReport("SimpleClans", "1.0", ex));
        }
    }
}
