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
 *     Created: 03.09.12 12:19
 */

package com.p000ison.dev.simpleclans2.util;

import org.bukkit.ChatColor;

import java.util.logging.Level;

/**
 * Represents a ExceptionHelper
 */
public class ExceptionHelper {

    public static void handleException(Throwable e, Class<?> happened)
    {
        String msg = e.getMessage();

        if (msg != null) {
            msg.replaceAll("\n", "");
        }

        Logging.debug("Failed because of: '%s'", Level.SEVERE, msg);

        for (StackTraceElement element : e.getStackTrace()) {
            if (element.getClassName().equals(happened.getName())) {
                int line = element.getLineNumber();

                if (line < 0) {
                    continue;
                }

                Logging.debug("Failed to execute line %d!", Level.SEVERE, line);
                return;
            }
        }

        Logging.debug(e, "Failed to print the cause of this exception!");
    }

    public static void handleException(Throwable e)
    {
        Logging.debug("Failed because of: '%s'", Level.SEVERE, e.getLocalizedMessage().replaceAll("\n", ""));

        for (StackTraceElement element : e.getStackTrace()) {

            int line = element.getLineNumber();

            if (line < 0) {
                continue;
            }

            Logging.debug(ChatColor.RED + "Failed to execute line %d!", Level.SEVERE, line);
            return;
        }
    }

    public static void handleCause(Throwable cause)
    {
        if (cause == null) {
            return;
        }

        handleException(cause);

        handleCause(cause.getCause());
    }
}
