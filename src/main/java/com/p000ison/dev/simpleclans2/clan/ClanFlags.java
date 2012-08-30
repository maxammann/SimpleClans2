/*
 * Copyright (C) 2012 p000ison
 *
 * This work is licensed under the Creative Commons
 * Attribution-NonCommercial-NoDerivs 3.0 Unported License. To view a copy of
 * this license, visit http://creativecommons.org/licenses/by-nc-nd/3.0/ or send
 * a letter to Creative Commons, 171 Second Street, Suite 300, San Francisco,
 * California, 94105, USA.
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
