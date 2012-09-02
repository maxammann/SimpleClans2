/*
 * This file is part of SimpleClans2 (2012).
 *
 *     SimpleClans2 is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     Foobar is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with Foobar.  If not, see <http://www.gnu.org/licenses/>.
 *
 *     Created: 02.09.12 18:29
 */


package com.p000ison.dev.simpleclans2.clan;

import com.p000ison.dev.simpleclans2.Flags;
import com.p000ison.dev.simpleclans2.vectors.SerializablePlayerVector;
import org.bukkit.Location;

import java.util.Set;

/**
 * Represents a the Flags of a clan
 */
public class ClanFlags extends Flags {

    /**
     * Sets the home point of this clan to a {@link Location}
     *
     * @param location
     * @see Location
     */
    public void setHomeLocation(Location location)
    {
        super.setString("home", new SerializablePlayerVector(location).serialize());
    }

    /**
     * Gets the home location of this clan or null if no location is set.
     *
     * @return The location
     * @see Location
     */
    public Location getHomeLocation()
    {
        String locationString = super.getString("home");

        if (locationString == null) {
            return null;
        }

        return SerializablePlayerVector.deserialize(locationString).toLocation();
    }

    /**
     * Adds a warring clan to this clan.
     *
     * @param id The id of the clan to add.
     */
    public void addWarringClan(long id)
    {
        super.getSet("warring").add(id);
    }

    /**
     * Removes a warring clan to this clan.
     *
     * @param id The id of the clan to remove.
     */
    public void removeWarringClan(long id)
    {
        Set warring = super.getSet("warring");

        if (warring.contains(id)) {
            warring.remove(id);
        }
    }

    /**
     * Adds a ally clan from this clan.
     *
     * @param id The id of the clan to add.
     */
    public void addAllyClan(long id)
    {
        super.getSet("allies").add(id);
    }

    /**
     * Removes a ally clan from this clan.
     *
     * @param id The id of the clan to remove.
     */
    public void removeAllyClan(long id)
    {
        Set allies = super.getSet("allies");

        if (allies.contains(id)) {
            allies.remove(id);
        }
    }

    public Set<Long> getAllies()
    {
        return super.getSet("allies");
    }

    /**
     * Adds a rival clan from this clan.
     *
     * @param id The id of the clan to add.
     */
    public void addRivalClan(long id)
    {
        super.getSet("rivals").add(id);
    }

    /**
     * Removes a rival clan from this clan.
     *
     * @param id The id of the clan to remove.
     */
    public void removeRivalClan(long id)
    {
        Set rivals = super.getSet("rivals");

        if (rivals.contains(id)) {
            rivals.remove(id);
        }
    }

    public Set<Long> getRivals()
    {
        return super.getSet("rivals");
    }

    public Set<Long> getWarringClans()
    {
        return super.getSet("warring");
    }
}
