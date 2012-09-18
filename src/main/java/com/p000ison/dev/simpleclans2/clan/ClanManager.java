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
 *     Created: 02.09.12 18:33
 */


package com.p000ison.dev.simpleclans2.clan;

import com.p000ison.dev.simpleclans2.SimpleClans;
import com.p000ison.dev.simpleclans2.clanplayer.ClanPlayer;
import org.bukkit.ChatColor;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * The ClanManager handels everything with clans.
 */
public class ClanManager {
    private SimpleClans plugin;
    private Set<Clan> clans = new HashSet<Clan>();


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
        return Collections.unmodifiableSet(clans);
    }

    /**
     * Removes a clan from memory
     *
     * @param clan The clan
     * @return Weather it was successfully
     */
    public boolean removeClan(Clan clan)
    {
        return clans.remove(clan);
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
        String lowerTag = ChatColor.stripColor(tag.toLowerCase());

        for (Clan clan : clans) {
            if (clan.getCleanTag().startsWith(lowerTag)) {
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
        for (Clan clan : clans) {
            if (clan.getId() == id) {
                return clan;
            }
        }

        return null;
    }

    public Clan createClan(Clan clan)
    {
        if (plugin.getDataManager().insertClan(clan)) {
            clan.setId(plugin.getDataManager().retrieveClanId(clan.getTag()));
//            clans.add(clan);
        }


        return clan;
    }

    public Clan createClan(String tag, String name)
    {
        Clan clan = new Clan(plugin, tag, name);
        return createClan(clan);
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

    public void importClans(Set<Clan> clans)
    {
        this.clans = clans;
    }

    public boolean existsClan(String tag)
    {
        for (Clan clan : getClans()) {
            if (clan.getTag().equals(tag)) {
                return true;
            }
        }

        return false;
    }

    /**
     * Gets a clan exactly by tag or null if there is no such clan.
     *
     * @param tag The tag to get.
     * @return The clan of null.
     */
    public Clan getClanExact(String tag)
    {
        for (Clan clan : clans) {
            if (clan.getTag().equals(tag)) {
                return clan;
            }
        }
        return null;
    }

    public void ban(ClanPlayer clanPlayer)
    {
        if (clanPlayer == null) {
            return;
        }

        Clan clan = clanPlayer.getClan();

        if (clan != null) {
            if (clan.isLeader(clanPlayer) && clan.getLeaders().size() == 1) {
                clan.disband();
            } else {
                clanPlayer.setJoinDate(0);
                clan.removeMember(clanPlayer);
                clanPlayer.setBanned(true);

                clanPlayer.update();
            }
        }
    }
}
