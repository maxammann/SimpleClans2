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
 *     Last modified: 03.11.12 17:29
 */

package com.p000ison.dev.simpleclans2.updater;

import com.p000ison.dev.simpleclans2.util.DateHelper;
import com.p000ison.dev.simpleclans2.util.Logging;
import org.apache.commons.lang.time.DurationFormatUtils;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.Set;

/**
 * Represents a AutoUpdater
 */
public class AutoUpdater {
    private static final String JOB = "SimpleClans2";
    private Build toUpdate = null;
    private Plugin plugin;

    public AutoUpdater(Plugin plugin, UpdateType type)
    {
        this.plugin = plugin;
        String version = plugin.getDescription().getVersion();

        if (version.equals("unknown-version")) {
            Logging.debug("No maven, heh?");
            return;
        } else if (version.contains("UNKNOWN")) {
            Logging.debug("No jenkins build, heh?");
            return;
        }

        int versionValue = parseVersion(version);

        if (versionValue == -1) {
            Logging.debug("The version string is malformatted: %s", version);
            return;
        }

        try {
            Build fetched = getBuild(type);

            fetched.fetchInformation();

            if (versionValue < fetched.getBuildNumber()) {
                toUpdate = fetched;
                Logging.debug("------------------------------------------------------------------------------------------------------");
                Logging.debug("There is a update for your SimpleClans version!");
                Logging.debug("Build:  " + fetched.getBuildNumber());
                Logging.debug("Type:  " + (fetched.getUpdateType() == UpdateType.LATEST ? "Unofficial" : "Official"));
                Logging.debug("Release date:  " + new Date(fetched.getStarted()));
                Logging.debug("Compiling duration:  " + DurationFormatUtils.formatDuration(fetched.getDuration(), "HH:mm:ss"));
                Logging.debug("Author:  " + fetched.getAuthor());
                Logging.debug("Comment:  " + fetched.getComment() + "(" + fetched.getCommitURL() + ")");

                Set<String> modified = fetched.getModifiedFiles();
                Set<String> created = fetched.getCreatedFiles();
                Set<String> deleted = fetched.getDeletedFiles();

                if (!modified.isEmpty()) {
                    Logging.debug("Modified files(%s):", modified.size());
                    for (String file : modified) {
                        Logging.debug("  * " + file);
                    }
                }
                if (!created.isEmpty()) {
                    Logging.debug("Created files(%s):", created.size());
                    for (String file : created) {
                        Logging.debug("  * " + file);
                    }
                }
                if (!deleted.isEmpty()) {
                    Logging.debug("Deleted files(%s):", deleted.size());
                    for (String file : deleted) {
                        Logging.debug("  * " + file);
                    }
                }
                Logging.debug("------------------------------------------------------------------------------------------------------");
            }
        } catch (IOException e) {
            Logging.debug(e, true, "Failed at fetching the Update information!");
        }
    }

    public boolean isUpdate()
    {
        return toUpdate != null;
    }

    public boolean update()
    {
        if (toUpdate == null) {
            return false;
        }

        try {
            File updateDirectory = Bukkit.getUpdateFolderFile();
            File pluginDirectory = updateDirectory.getParentFile();

            if (pluginDirectory.listFiles() == null || !pluginDirectory.isDirectory()) {
                return false;
            }

            String absolutePath = getClass().getProtectionDomain().getCodeSource().getLocation().getFile();
            String name = absolutePath.substring(absolutePath.lastIndexOf('/') + 1);

            if (!toUpdate.saveToDirectory(updateDirectory, name)) {
                return false;
            }
        } catch (IOException e) {
            Logging.debug(e, true);
            return false;
        }
        return true;
    }

    private static int parseVersion(String version)
    {
        char[] versionArray = version.toCharArray();

        int versionNumber = 0;
        int run = 1;

        for (int i = versionArray.length - 1; i >= 0; i--) {
            char current = versionArray[i];

            switch (current) {
                case '-':
                case 'b':
                    break;
                case '.':
                    continue;
            }

            int number = DateHelper.getDigit(current);

            if (number == -1) {
                if (versionNumber == 0) {
                    return -1;
                }
                break;
            }

            versionNumber += number * run;
            run *= 10;
        }

        return versionNumber;
    }

    public static Build getBuild(UpdateType type)
    {
        return new Build(JOB, type);
    }
}
