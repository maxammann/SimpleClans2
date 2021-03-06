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
 *     Last modified: 19.02.13 21:18
 */

package com.p000ison.dev.simpleclans2.updater;

import com.p000ison.dev.simpleclans2.updater.jenkins.JenkinsArtifact;

import java.io.IOException;
import java.util.List;
import java.util.Set;

/**
 * Represents a JenkinsBuild
 */
public interface Build {

    void fetchInformation() throws IOException, FailedBuildException;

    List<JenkinsArtifact> getDownloadURLs() throws IOException;

    UpdateType getUpdateType();

    long getDuration();

    long getStarted();

    int getBuildNumber();

    Set<String> getDeletedFiles();

    String getCommitURL();

    String getComment();

    Set<String> getModifiedFiles();

    Set<String> getCreatedFiles();

    String getAuthor();

    String getDownloadLink();
}
