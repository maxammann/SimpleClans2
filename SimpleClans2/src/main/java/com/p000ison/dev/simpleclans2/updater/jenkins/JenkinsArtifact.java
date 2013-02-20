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
 *     Last modified: 26.01.13 15:28
 */

package com.p000ison.dev.simpleclans2.updater.jenkins;

import java.io.File;
import java.net.URL;

/**
 * Represents a JenkinsArtifact
 */
public class JenkinsArtifact {
    private String name;
    private URL url;
    private String destination;

    public JenkinsArtifact(String name, String destination) {
        this.name = name;
        this.destination = destination;
    }

    public URL getURL() {
        return url;
    }

    public String getName() {
        return name;
    }

    public String getDestination() {
        return destination;
    }

    public File getDestinationFile() {
        return new File(destination);
    }

    public void setURL(URL url) {
        this.url = url;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        JenkinsArtifact artifact = (JenkinsArtifact) o;

        if (name != null ? !name.equals(artifact.name) : artifact.name != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return name != null ? name.hashCode() : 0;
    }
}
