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


package com.p000ison.dev.simpleclans2.clanplayer;

import com.p000ison.dev.simpleclans2.SimpleClans;
import com.p000ison.dev.simpleclans2.database2.tables.ClanPlayerTable;
import org.bukkit.entity.Player;

import java.util.Collections;
import java.util.HashSet;
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

    public ClanPlayer createClanPlayer(String name)
    {
        ClanPlayer clanPlayer = new ClanPlayer(plugin, name);

        players.add(clanPlayer);

        ClanPlayerTable clanTable = new ClanPlayerTable();

        clanTable.name = name;

//        plugin.getDatabaseManager().getDatabase().save(clanTable);

        clanPlayer.setId(getClanPlayerId(name));

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

    public long getClanPlayerId(String name)
    {
//        ClanPlayerTable clanPlayerTable = plugin.getDatabaseManager().getDatabase().select(ClanPlayerTable.class).where().equal("name", name).execute().findOne();

//        return clanPlayerTable.id;
        return 1;
    }

    public ClanPlayer getClanPlayer(String name)
    {

        if (name == null) {
            return null;
        }

        String lowerName = name.toLowerCase();

        for (ClanPlayer clanPlayer : players) {
            if (clanPlayer.getName().toLowerCase().startsWith(lowerName)) {
                return clanPlayer;
            }
        }
        return null;
    }

    public ClanPlayer getClanPlayer(Player player)
    {
        return getClanPlayerExact(player.getName());
    }

    public ClanPlayer getClanPlayerExact(String name)
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
}
