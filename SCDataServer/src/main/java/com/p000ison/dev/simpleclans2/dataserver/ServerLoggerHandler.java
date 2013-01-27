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
 *     Last modified: 27.01.13 11:49
 */

package com.p000ison.dev.simpleclans2.dataserver;

import org.apache.commons.lang.exception.ExceptionUtils;
import org.apache.commons.lang.time.FastDateFormat;

import java.util.logging.ConsoleHandler;
import java.util.logging.Formatter;
import java.util.logging.LogRecord;

/**
 * Represents a ServerLoggerHandler
 */
public class ServerLoggerHandler extends ConsoleHandler {

    private static final FastDateFormat DATE_FORMAT = FastDateFormat.getInstance("k:m");

    public ServerLoggerHandler() {
        super.setOutputStream(System.out);
    }

    @Override
    public Formatter getFormatter() {
        return new Formatter() {

            @Override
            public String format(LogRecord record) {
                String message = DATE_FORMAT.format(record.getMillis()) + " [" + record.getLevel().toString() + "] " + record.getMessage() + "\n";
                if (record.getThrown() != null) {
                    message += ExceptionUtils.getStackTrace(record.getThrown()) + '\n';
                }
                return message;
            }
        };
    }
}
