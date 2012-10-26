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

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import static java.lang.System.getProperty;

/**
 * Represents a ExceptionReport
 */
@SuppressWarnings("unchecked")
public class ExceptionReport {
    private String name;
    private String version;
    private Throwable thrown;
    private String email;
    private long date;

    public ExceptionReport(String name, String version, Throwable thrown, String email)
    {
        this.name = name;
        this.version = version;
        this.thrown = thrown;
        this.email = email;
        date = System.currentTimeMillis();
    }

    public JSONObject getJSONObject()
    {
        JSONObject report = new JSONObject();
        report.put("plugin", name);
        report.put("version", version);
        report.put("date", date);
        report.put("message", thrown.getMessage());
        report.put("exception", buildThrowableJSON(thrown));
        report.put("plugins", /*Arrays.asList(Bukkit.getPluginManager().getPlugins()).toString()*/"b");
        report.put("bukkit_version", /*Bukkit.getBukkitVersion()*/"a");
        report.put("java_version", getProperty("java.version"));
        report.put("os_arch", getProperty("os.arch"));
        report.put("os_name", getProperty("os.name"));
        report.put("os_version", getProperty("os.version"));

        if (email != null) {
            report.put("email", email);
        }

        JSONArray causes = new JSONArray();

        Throwable cause = thrown;

        while ((cause = cause.getCause()) != null) {
            causes.add(buildThrowableJSON(cause));
        }

        report.put("causes", causes);
        return report;
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
