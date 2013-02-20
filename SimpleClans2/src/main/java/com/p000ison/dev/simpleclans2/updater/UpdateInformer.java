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

import com.p000ison.dev.simpleclans2.api.logging.Logging;
import com.p000ison.dev.simpleclans2.updater.bamboo.BambooBuild;
import com.p000ison.dev.simpleclans2.util.DateHelper;
import org.bukkit.plugin.Plugin;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.util.Date;

/**
 * Represents a UpdateInformer
 */
public class UpdateInformer {
    private Build toUpdate = null;

    public UpdateInformer(Plugin plugin, String job, UpdateType type, boolean longReport) {
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
            Build update = getBuild(type, job);
            update.fetchInformation();
            if (versionValue < update.getBuildNumber()) {
                Logging.debug(getBuildInfo(update, longReport));
                toUpdate = update;
            }
        } catch (SocketTimeoutException e) {
            Logging.debug("Failed at contacting the update service! Maybe it is a bit slow.-------------------------------------------");
        } catch (UnknownHostException e) {
            Logging.debug(e, true, "The jenkins is down! Please contact the developers!");
        } catch (FileNotFoundException e) {
            Logging.debug(e, true, "The file was not found on the jenkins server! Please contact the developers");
        } catch (IOException e) {
            Logging.debug(e, true, "Failed at fetching the Update information! Maybe something is down. Please contact the developers");
        } catch (FailedBuildException e) {
            Logging.debug("The last build failed! Sorry but I was unable to find a new build!");
        }
    }

    public static String getBuildInfo(Build build, boolean longReport) {
        if (build == null) {
            return null;
        }

        StringBuilder updateInfo = new StringBuilder();

        updateInfo.append("------------------------------------------------------------------------------------------------------\n");
        updateInfo.append("There is a update for your SimpleClans version!\n");
        if (longReport) {
            updateInfo.append("Build:  ").append(build.getBuildNumber()).append('\n');
            updateInfo.append("Type:  ").append(build.getUpdateType() == UpdateType.LATEST ? "Unofficial\n" : "Official\n");
            updateInfo.append("Release date:  ").append(new Date(build.getStarted())).append('\n');
            updateInfo.append("Commit:  ").append(build.getCommitURL()).append("\n");
        }
        updateInfo.append("------------------------------------------------------------------------------------------------------\n");
        return updateInfo.toString();
    }

    public boolean isUpdate() {
        return toUpdate != null;
    }

    private static int parseVersion(String version) {
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

    public static Build getBuild(UpdateType type, String job) {
        return new BambooBuild(job, type);
    }
}
