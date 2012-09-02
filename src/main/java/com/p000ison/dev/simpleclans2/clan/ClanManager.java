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

import com.p000ison.dev.simpleclans2.SimpleClans;
import com.p000ison.dev.simpleclans2.database.tables.ClanTable;

import java.util.*;

/**
 * The ClanManager handels everything with clans.
 */
public class ClanManager {
    private SimpleClans plugin;
    private Map<Long, Clan> clans = new HashMap<Long, Clan>();


    public ClanManager(SimpleClans plugin)
    {
        this.plugin = plugin;
    }

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

    public void createClan(Clan clan)
    {
        clans.put(clan.getId(), clan);

        ClanTable clanTable = new ClanTable();

        clanTable.tag = clan.getTag();
        clanTable.name = clan.getName();
        clanTable.last_action = System.currentTimeMillis();
        clanTable.founded = System.currentTimeMillis();
        clanTable.friendly_fire = clan.isFriendlyFireOn();
        clanTable.verified = clan.isVerified();

        plugin.getDatabaseManager().getDatabase().save(clanTable);

        clan.setId(getClanId(clan.getTag()));
    }

    public void createClan(String tag, String name)
    {
        Clan clan = new Clan(plugin, tag, name);
        createClan(clan);
    }

    public Set<Clan> convertIdSetToClanSet(Set<Long> ids)
    {
        HashSet<Clan> allies = new HashSet<Clan>();

        for (long clanId : ids) {
            Clan ally = plugin.getClanManager().getClan(clanId);
            allies.add(ally);
        }

        return allies;
    }

    public long getClanId(String tag)
    {
        ClanTable clanTable = plugin.getDatabaseManager().getDatabase().select(ClanTable.class).where().equal("tag", tag).execute().findOne();

        return clanTable.id;
    }

    public void importClans(Set<Clan> clans)
    {
        for (Clan clan : clans) {
            this.clans.put(clan.getId(), clan);
        }
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
