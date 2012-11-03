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

package com.p000ison.dev.simpleclans2;

import com.p000ison.dev.simpleclans2.updater.Build;
import com.p000ison.dev.simpleclans2.util.DateHelper;
import com.p000ison.dev.simpleclans2.util.Logging;
import org.apache.commons.lang.time.DurationFormatUtils;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;

import java.io.IOException;
import java.util.Date;

/**
 * Represents a AutoUpdater
 */
public class AutoUpdater {
    private static final String JOB = "SimpleClans2";
    private Build toUpdate = null;

    public AutoUpdater(Plugin plugin)
    {
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

        Build fetched;

        try {
            if (version.contains("b")) {
                fetched = getLatestBuild();
            } else {
                fetched = getLatestPromotedBuild();
            }

            fetched.fetchInformation();

            if (versionValue < fetched.getBuildNumber()) {
                toUpdate = fetched;
                Logging.debug("------------------------------------------------------------------------------------------------------");
                Logging.debug("There is a update for your SimpleClans version!");
                Logging.debug("Build: " + fetched.getBuildNumber());
                Logging.debug("Type: " + (fetched.getUpdateType() == Build.UpdateType.LATEST ? "Unofficial" : "Official"));
                Logging.debug("Release date: " + new Date(fetched.getStarted()));
                Logging.debug("Compiling duration: " + DurationFormatUtils.formatDuration(fetched.getDuration(), "HH:mm:ss"));
                Logging.debug("Compiling result:  " + fetched.getResult());
                Logging.debug("Compiling cause:  " + fetched.getCause());
                Logging.debug("------------------------------------------------------------------------------------------------------");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public boolean update()
    {
        if (toUpdate == null) {
            return false;
        }

        try {
            toUpdate.saveToDirectory(Bukkit.getUpdateFolderFile());
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

    public static Build getLatestBuild()
    {
        return new Build(JOB, Build.UpdateType.LATEST);
    }

    public static Build getLatestPromotedBuild()
    {
        return new Build(JOB, Build.UpdateType.LATEST_PROMOTED);
    }
}
