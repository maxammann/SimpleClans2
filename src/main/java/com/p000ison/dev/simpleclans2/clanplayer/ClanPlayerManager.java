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
 *     Last modified: 11.10.12 14:37
 */


package com.p000ison.dev.simpleclans2.clanplayer;

import com.p000ison.dev.simpleclans2.SimpleClans;
import com.p000ison.dev.simpleclans2.api.events.ClanPlayerCreateEvent;
import com.p000ison.dev.simpleclans2.exceptions.ClanPlayerCreateException;
import org.bukkit.entity.Player;

import java.util.Collections;
import java.util.HashSet;
import java.util.Locale;
import java.util.Set;

/**
 * Represents a ClanPlayerManager
 */
public class ClanPlayerManager {

    private Set<ClanPlayer> players = new HashSet<ClanPlayer>();
    private SimpleClans plugin;

    public ClanPlayerManager(SimpleClans plugin)
    {
        this.plugin = plugin;
    }

    public void importClanPlayers(Set<ClanPlayer> clanPlayers)
    {
        this.players = clanPlayers;
    }

    public ClanPlayer createClanPlayer(Player player)
    {
        return createClanPlayer(player.getName());
    }

    public ClanPlayer createClanPlayer(String player)
    {
        return createClanPlayer(new ClanPlayer(plugin, player));
    }

    public ClanPlayer createClanPlayer(ClanPlayer clanPlayer)
    {
        for (ClanPlayer cp : players) {
            if (cp.equals(clanPlayer)) {
                return cp;
            }
        }

        long id = plugin.getDataManager().retrieveClanPlayerId(clanPlayer.getName());

        if (id < 0) {
            throw new ClanPlayerCreateException(clanPlayer, id);
        }

        clanPlayer.setId(id);

        players.add(clanPlayer);

        plugin.getDataManager().insertClanPlayer(clanPlayer);

        plugin.getServer().getPluginManager().callEvent(new ClanPlayerCreateEvent(clanPlayer));

        return clanPlayer;
    }

    public ClanPlayer getCreateClanPlayerExact(String name)
    {
        ClanPlayer clanPlayer = getClanPlayerExact(name);

        if (clanPlayer != null) {
            return clanPlayer;
        }

        return createClanPlayer(name);
    }


    public Set<ClanPlayer> getClanPlayers()
    {
        return Collections.unmodifiableSet(players);
    }

    public ClanPlayer getCreateClanPlayerExact(Player player)
    {
        return getCreateClanPlayerExact(player.getName());
    }

    public ClanPlayer getAnyClanPlayer(String name)
    {

        if (name == null) {
            return null;
        }

        String lowerName = name.toLowerCase(Locale.US);

        for (ClanPlayer clanPlayer : players) {
            if (clanPlayer.getName().toLowerCase(Locale.US).startsWith(lowerName)) {
                return clanPlayer;
            }
        }
        return null;
    }

    public ClanPlayer getClanPlayer(String name)
    {

        if (name == null) {
            return null;
        }

        String lowerName = name.toLowerCase(Locale.US);

        for (ClanPlayer clanPlayer : players) {
            if (clanPlayer.getName().toLowerCase(Locale.US).startsWith(lowerName)) {
                if (clanPlayer.getClan() == null) {
                    return null;
                } else {
                    return clanPlayer;
                }
            }
        }
        return null;
    }

    public ClanPlayer getClanPlayer(Player player)
    {
        return getClanPlayerExact(player.getName());
    }

    public ClanPlayer getAnyClanPlayer(Player player)
    {
        return getAnyClanPlayerExact(player.getName());
    }

    public ClanPlayer getAnyClanPlayerExact(String name)
    {
        if (name == null) {
            return null;
        }

        for (ClanPlayer clanPlayer : players) {
            if (clanPlayer.getName().equals(name)) {
                return clanPlayer;
            }
        }

        return null;
    }

    public ClanPlayer getClanPlayerExact(String name)
    {
        if (name == null) {
            return null;
        }

        for (ClanPlayer clanPlayer : players) {
            if (clanPlayer.getName().equals(name)) {
                if (clanPlayer.getClan() == null) {
                    return null;
                } else {
                    return clanPlayer;
                }
            }
        }

        return null;
    }
}
