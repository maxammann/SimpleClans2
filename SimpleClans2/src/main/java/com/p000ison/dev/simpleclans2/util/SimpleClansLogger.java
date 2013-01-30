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
 *     Last modified: 30.01.13 19:57
 */

package com.p000ison.dev.simpleclans2.util;

import com.p000ison.dev.simpleclans2.SimpleClans;
import com.p000ison.dev.simpleclans2.api.logging.ReportableLogger;
import com.p000ison.dev.simpleclans2.exceptions.handling.ExceptionReporterTask;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Represents a SimpleClansLogger
 */
public class SimpleClansLogger extends ReportableLogger {

    private static SimpleClans plugin;

    public SimpleClansLogger(Logger logger) {
        super(logger);
    }

    @Override
    public void debug(Throwable ex, boolean reportException) {
        debug(ex, null, reportException);
    }

    @Override
    public void debug(Throwable ex, String msg, boolean reportException) {
        debug(ex, msg, reportException, null);
    }

    @Override
    public void debug(Throwable ex, boolean reportException, String msg, Object... args) {
        if (ex != null) {
            if (reportException) {
                final ExceptionReporterTask task = plugin.getExceptionReporter();
                if (task != null) {
                    boolean success = task.addReport(ex, plugin.getName(), plugin.getDescription().getVersion(), plugin.getSettingsManager() == null ? null : plugin.getSettingsManager().getEmail());
                    debug(Level.INFO, "------------------------------------------------------------------");
                    debug(Level.INFO, success ? "Exception has been reported!" : "Queue overflow!");
                    debug(Level.INFO, "------------------------------------------------------------------");
                }
            }
        }
        super.debug(ex, msg, args);
    }

    @Override
    public void clear() {
        if (plugin != null) {
            plugin = null;
        }
        super.clear();
    }
}
