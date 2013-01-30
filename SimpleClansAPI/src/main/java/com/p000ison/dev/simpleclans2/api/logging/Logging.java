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
 *     Last modified: 30.01.13 18:09
 */

package com.p000ison.dev.simpleclans2.api.logging;

import java.util.logging.Level;

/**
 * Represents a Logging
 */
public class Logging {
    private static Logger apiLogger;
    private static ReportableLogger defaultLogger;

    public static void setAPILogger(java.util.logging.Logger logger) {
        apiLogger = new Logger(logger);
    }

    public static void setDefaultLogger(ReportableLogger logger) {
        defaultLogger = logger;
    }

    public static Logger getDefaultAPILogger() {
        if (apiLogger == null) {
            throw new RuntimeException("The api logger is not initialized!");
        }
        return apiLogger;
    }

    public static ReportableLogger getDefaultLogger() {
        if (apiLogger == null) {
            throw new RuntimeException("The default logger is not initialized!");
        }
        return defaultLogger;
    }

    public static void debug(String msg, Object... args) {
        getDefaultLogger().debug(msg, args);
    }

    public static void debug(Level level, String msg, Object... args) {
        getDefaultLogger().debug(level, msg, args);
    }

    public static void debug(String msg) {
        getDefaultLogger().debug(msg);
    }

    public static void debug(Level level, String msg) {
        getDefaultLogger().debug(level, msg);
    }

    public static void debug(Throwable ex, boolean reportException) {
        getDefaultLogger().debug(ex, reportException);
    }

    public static void debug(Throwable ex, String msg, boolean reportException) {
        getDefaultLogger().debug(ex, msg, reportException);
    }

    public static void debug(Throwable ex, boolean reportException, String msg, Object... args) {
        getDefaultLogger().debug(ex, reportException, msg, args);
    }

    public static void clear() {
        apiLogger.clear();
        defaultLogger.clear();
    }
}
