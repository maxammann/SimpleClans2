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
 *     Last modified: 14.10.12 16:36
 */

package com.p000ison.dev.simpleclans2.exceptions.handling;

import java.util.LinkedList;
import java.util.Queue;

/**
 * Represents a ExceptionReporterTask
 */
public class ExceptionReporterTask extends LinkedList<String> implements Runnable {

    private Queue<ExceptionReport> queue;
    private static final int MAX_REPORTS = 15;

    public ExceptionReporterTask()
    {
        this.queue = new LinkedList<ExceptionReport>();
    }

    public boolean addReport(ExceptionReport exceptionReport)
    {
        if (queue == null) {
            return false;
        }
        if (queue.size() < MAX_REPORTS) {
            queue.add(exceptionReport);
            return true;
        }

        return false;
    }

    public boolean addReport(Throwable thrown, String plugin, String version, String email)
    {
        return addReport(new ExceptionReport(plugin, version, thrown, email));
    }

    @Override
    public void run()
    {
        ExceptionReport report;

        while ((report = queue.poll()) != null) {
            report.report();
        }
    }
}
