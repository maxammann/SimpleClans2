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

import com.p000ison.dev.simpleclans2.SimpleClans;
import com.p000ison.dev.simpleclans2.exceptions.handling.ExceptionReporterTask;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * A logger utility.
 */
public final class Logging {
    private static Logger instance;
    private static SimpleClans plugin;

    public static Logger getInstance()
    {
        return instance;
    }

    public static void setInstance(Logger instance, SimpleClans plugin)
    {
        Logging.instance = instance;
        Logging.plugin = plugin;
    }

    public static void close()
    {
        setInstance(null, null);
    }

    public static void debug(String msg, Object... args)
    {
        debugRaw(Level.INFO, String.format(msg, args), null, false);
    }

    public static void debug(Level level, String msg, Object... args)
    {
        debugRaw(level, String.format(msg, args), null, false);
    }

    public static void debug(String msg)
    {
        debugRaw(Level.INFO, msg, null, false);
    }

    public static void debug(Level level, String msg)
    {
        debugRaw(level, msg, null, false);
    }

    public static void debug(Throwable ex, boolean reportException)
    {
        debugRaw(Level.SEVERE, null, ex, reportException);
    }

    public static void debug(Throwable ex, String msg, boolean reportException)
    {
        debugRaw(Level.SEVERE, msg, ex, reportException);
    }

    public static void debug(Throwable ex, boolean reportException, String msg, Object... args)
    {
        debugRaw(Level.SEVERE, String.format(msg, args), ex, reportException);
    }

    private static void debugRaw(Level level, String msg, Throwable ex, boolean reportException)
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
            if (reportException) {
                ExceptionReporterTask task = plugin.getExceptionReporter();
                if (task != null) {
                    boolean success = plugin.getExceptionReporter().addReport(ex, plugin.getName(), plugin.getDescription().getVersion(), plugin.getSettingsManager() == null ? null : plugin.getSettingsManager().getEmail());
                    instance.log(Level.INFO, "------------------------------------------------------------------");
                    instance.log(Level.INFO, success ? "Exception has been reported!" : "Queue overflow!");
                    instance.log(Level.INFO, "------------------------------------------------------------------");
                }
            }
        }
    }
}
