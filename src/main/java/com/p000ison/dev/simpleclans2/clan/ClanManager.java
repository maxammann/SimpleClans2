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

import com.google.common.collect.Maps;
import com.p000ison.dev.simpleclans2.clan.Clan;

import java.util.*;

/**
 * The ClanManager handels everything with clans.
 */
public class ClanManager {
    private Map<Long, Clan> clans = new HashMap<Long, Clan>();


    /**
     * Returns a unmodifiable Set of all clans.
     *
     * @return A set of all clans
     * @see Set
     */
    public Set<Clan> getClans()
    {
        return Collections.unmodifiableSet(new HashSet<Clan>(clans.values()));
    }

    /**
     * Returns a unmodifiable raw collection of all clans.
     *
     * @return A collection of all clans.
     */
    public Collection<Clan> getClansRaw()
    {
        return Collections.unmodifiableCollection(clans.values());
    }

    /**
     * Gets a clan by tag or null if there is no such clan.
     * <p>This checks for the best result and returns this.</p>
     *
     * @param tag The tag to search
     * @return The clan.
     */
    public Clan getClan(String tag)
    {
        String lowerTag = tag.toLowerCase();

        for (Clan clan : clans.values()) {
            if (clan.getTag().toLowerCase().startsWith(lowerTag)) {
                return clan;
            }
        }
        return null;
    }

    /**
     * Gets a clan by id or null if there is no such clan.
     *
     * @param id The id
     * @return The clan or null.
     */
    public Clan getClan(long id)
    {
        return clans.get(id);
    }

    /**
     * Gets a clan exactly by tag or null if there is no such clan.
     *
     * @param tag The tag to get.
     * @return The clan of null.
     */
    public Clan getClanExact(String tag)
    {
        for (Clan clan : clans.values()) {
            if (clan.getTag().equals(tag)) {
                return clan;
            }
        }
        return null;
    }
}
