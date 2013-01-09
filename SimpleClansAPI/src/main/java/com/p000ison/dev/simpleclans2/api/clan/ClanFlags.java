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
 *     Last modified: 09.01.13 19:15
 */

package com.p000ison.dev.simpleclans2.api.clan;

import com.p000ison.dev.simpleclans2.api.Flags;
import org.bukkit.Location;

import java.io.Serializable;

/**
 *
 */
public interface ClanFlags extends Serializable, Flags {
    /**
     * Sets the home point of this clan to a {@link org.bukkit.Location}
     *
     * @param location The location to set
     * @see org.bukkit.Location
     */
    void setHomeLocation(Location location);

    /**
     * Gets the home location of this clan or null if no location is set.
     *
     * @return The location
     * @see org.bukkit.Location
     */
    Location getHomeLocation();

    /**
     * Sets the clan cape url
     *
     * @param url The URL
     */
    void setClanCapeURL(String url);

    /**
     * Gets the clan cape url
     *
     * @return The cape url
     */
    String getClanCapeURL();

    void setFriendlyFire(boolean bool);

    boolean isFriendlyFireEnabled();
}
