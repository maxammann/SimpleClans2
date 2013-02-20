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
 *     Last modified: 19.02.13 21:27
 */

package com.p000ison.dev.simpleclans2.updater.bamboo;

import com.p000ison.dev.simpleclans2.updater.Artifact;

import java.io.File;
import java.net.URL;

/**
 * Represents a BambooArtifact
 */
public class BambooArtifact implements Artifact {

    @Override
    public URL getURL() {
        return null;
    }

    @Override
    public String getName() {
        return null;
    }

    @Override
    public String getDestination() {
        return null;
    }

    @Override
    public File getDestinationFile() {
        return null;
    }

    @Override
    public void setURL(URL url) {
    }
}
