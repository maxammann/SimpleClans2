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
 *     Last modified: 30.01.13 18:12
 */

package com.p000ison.dev.simpleclans2.api.logging;

import java.util.logging.Level;

/**
 * Represents a Logger
 */
public class Logger {
    private java.util.logging.Logger logger;

    public Logger(java.util.logging.Logger logger) {
        this.logger = logger;
    }

    public void debug(String msg, Object... args) {
        debugRaw(Level.INFO, String.format(msg, args), null);
    }

    public void debug(Level level, String msg, Object... args) {
        debugRaw(level, String.format(msg, args), null);
    }

    public void debug(String msg) {
        debugRaw(Level.INFO, msg, null);
    }

    public void debug(Level level, String msg) {
        debugRaw(level, msg, null);
    }

    public void debug(Throwable ex) {
        debugRaw(Level.SEVERE, null, ex);
    }

    public void debug(Throwable ex, String msg) {
        debugRaw(Level.SEVERE, msg, ex);
    }

    public void debug(Throwable ex, String msg, Object... args) {
        debugRaw(Level.SEVERE, String.format(msg, args), ex);
    }

    public java.util.logging.Logger getLogger() {
        return logger;
    }

    protected void debugRaw(Level level, String msg, Throwable ex) {

        if (logger == null) {
            return;
        }

        if (level == null) {
            level = Level.INFO;
        }

        if (ex == null) {
            logger.log(level, msg);
        } else {
            logger.log(level, msg, ex);
        }
    }

    public void clear() {
        logger = null;
    }
}
