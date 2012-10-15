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
 *     Last modified: 14.10.12 12:49
 */

package com.p000ison.dev.simpleclans2.exceptions.handling;

import com.p000ison.dev.simpleclans2.util.Logging;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.io.IOException;

/**
 * Represents a ExceptionReport
 */
@SuppressWarnings("unchecked")
public class ExceptionReport {
    private String name;
    private String version;
    private Throwable thrown;
    private long date;

    private static final String PROTOCOL = "http", HOST = "dreamz.bplaced.com", FILE = "/exceptions/index.php";
    private static final int PORT = 80;

    public ExceptionReport(String name, String version, Throwable thrown)
    {
        this.name = name;
        this.version = version;
        this.thrown = thrown;
        date = System.currentTimeMillis();
    }

    private String buildJSON()
    {
        JSONObject report = new JSONObject();
        report.put("plugin", name);
        report.put("version", version);
        report.put("message", thrown.getMessage());
        report.put("date", date);
        report.put("exception", buildThrowableJSON(thrown));

        JSONArray causes = new JSONArray();

        Throwable cause = thrown;

        while ((cause = cause.getCause()) != null) {
            causes.add(buildThrowableJSON(cause));
        }

        report.put("causes", causes);
        return report.toJSONString();
    }

    public static void main(String[] args)
    {
        try {
            try {
                throw new Exception();
            } catch (Exception e) {
                try {
                    throw new Exception(e);
                } catch (Exception ex) {
                    throw new Exception(ex);
                }
            }
        } catch (Exception e) {
            ExceptionReport report = new ExceptionReport("SimpleClans", "2.0", e);
            report.report();
        }
    }

    public boolean report()
    {
        try {
            PHPConnection connection = new PHPConnection(PROTOCOL, HOST, PORT, FILE, true);
            connection.write("report=" + buildJSON());
            String response = connection.getResponse();
            if (response != null && !response.isEmpty()) {
                throw new IOException("Failed at pushing error reports: " + response);
            }
            return true;
        } catch (IOException e) {
            Logging.debug(e, true);
            return false;
        }
    }


    private static Object buildThrowableJSON(Throwable thrown)
    {
        JSONArray stackTrace = new JSONArray();

        for (StackTraceElement element : thrown.getStackTrace()) {
            stackTrace.add(element.toString());
        }

        return stackTrace;
    }
}
