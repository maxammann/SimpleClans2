/*
 * Copyright (C) 2012 p000ison
 *
 * This work is licensed under the Creative Commons
 * Attribution-NonCommercial-NoDerivs 3.0 Unported License. To view a copy of
 * this license, visit http://creativecommons.org/licenses/by-nc-nd/3.0/ or send
 * a letter to Creative Commons, 171 Second Street, Suite 300, San Francisco,
 * California, 94105, USA.
 */

package com.p000ison.dev.simpleclans2.clanplayer;

import com.p000ison.dev.simpleclans2.SimpleClans;
import com.p000ison.dev.simpleclans2.database.tables.ClanPlayerTable;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;

/**
 * Represents a ClanPlayerManager
 */
public class ClanPlayerManager {

    private Map<String, ClanPlayer> players = new HashMap<String, ClanPlayer>();
    private SimpleClans plugin;

    public ClanPlayerManager(SimpleClans plugin)
    {
        this.plugin = plugin;
    }

    public ClanPlayer createClanPlayer(Player player)
    {
        return createClanPlayer(player.getName());
    }

    public ClanPlayer createClanPlayer(String name)
    {
        ClanPlayer clanPlayer = new ClanPlayer(plugin, name);

        players.put(name, clanPlayer);

        ClanPlayerTable clanTable = new ClanPlayerTable();

        clanTable.name = name;

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

    public ClanPlayer getCreateClanPlayerExact(Player player)
    {
        return getCreateClanPlayerExact(player.getName());
    }

    public long getClanPlayerId(String name)
    {
        ClanPlayerTable clanPlayerTable = plugin.getDatabaseManager().getDatabase().select(ClanPlayerTable.class).where().equal("name", name).execute().findOne();

        return clanPlayerTable.id;
    }

    public ClanPlayer getClanPlayer(String name)
    {

        if (name == null) {
            return null;
        }

        String lowerName = name.toLowerCase();

        for (ClanPlayer clanPlayer : players.values()) {
            if (clanPlayer.getName().toLowerCase().startsWith(lowerName)) {
                return clanPlayer;
            }
        }
        return null;
    }

    public ClanPlayer getClanPlayerExact(Player player)
    {
        return getClanPlayerExact(player.getName());
    }

    public ClanPlayer getClanPlayerExact(String name)
    {
        return players.get(name);
    }
}
