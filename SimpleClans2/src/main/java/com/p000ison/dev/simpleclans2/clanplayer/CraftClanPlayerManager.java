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
import com.p000ison.dev.simpleclans2.api.clan.Clan;
import com.p000ison.dev.simpleclans2.api.clanplayer.ClanPlayer;
import com.p000ison.dev.simpleclans2.api.clanplayer.ClanPlayerManager;
import com.p000ison.dev.simpleclans2.api.events.ClanPlayerCreateEvent;
import org.bukkit.entity.Player;

import java.util.Collections;
import java.util.HashSet;
import java.util.Locale;
import java.util.Set;

/**
 * Represents a ClanPlayerManager
 */
public class CraftClanPlayerManager implements ClanPlayerManager {

    private Set<ClanPlayer> players = new HashSet<ClanPlayer>();
    private SimpleClans plugin;

    public CraftClanPlayerManager(SimpleClans plugin) {
        this.plugin = plugin;
    }

    public void importClanPlayers(Set<CraftClanPlayer> clanPlayers) {
        this.players.addAll(clanPlayers);
    }

    @Override
    public ClanPlayer createClanPlayer(Player player) {
        return createClanPlayer(player.getName());
    }

    @Override
    public ClanPlayer createClanPlayer(String player) {
        return createClanPlayer(new CraftClanPlayer(plugin, player));
    }

    @Override
    public ClanPlayer createClanPlayer(ClanPlayer clanPlayer) {
        ClanPlayerCreateEvent event = new ClanPlayerCreateEvent(clanPlayer);
        plugin.getServer().getPluginManager().callEvent(event);

        if (event.isCancelled()) {
            return null;
        }

        for (ClanPlayer cp : players) {
            if (cp.equals(clanPlayer)) {
                return cp;
            }
        }

        clanPlayer.updateLastSeen();
        ((CraftClanPlayer) clanPlayer).setJoinTime(System.currentTimeMillis());

        plugin.getDataManager().getDatabase().save((CraftClanPlayer) clanPlayer);
        players.add(clanPlayer);

        clanPlayer.update();

        return clanPlayer;
    }

    @Override
    public ClanPlayer getCreateClanPlayerExact(String name) {
        ClanPlayer clanPlayer = getAnyClanPlayerExact(name);

        if (clanPlayer != null) {
            return clanPlayer;
        }

        return createClanPlayer(name);
    }

    @Override
    public void ban(ClanPlayer clanPlayer) {
        if (clanPlayer == null) {
            return;
        }

        Clan clan = clanPlayer.getClan();

        if (clan != null) {
            if (clan.isLeader(clanPlayer) && clan.getLeaders().size() == 1) {
                clan.disband();
            } else {
                clan.removeMember(clanPlayer);
                clanPlayer.setBanned(true);

                clanPlayer.update();
            }
        }
    }

    @Override
    public Set<ClanPlayer> getClanPlayers() {
        return Collections.unmodifiableSet(players);
    }

    @Override
    public ClanPlayer getCreateClanPlayerExact(Player player) {
        return getCreateClanPlayerExact(player.getName());
    }

    @Override
    public ClanPlayer getAnyClanPlayer(String name) {

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

    @Override
    public ClanPlayer getClanPlayer(String name) {

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

    @Override
    public ClanPlayer getClanPlayer(long id) {

        if (id == -1) {
            return null;
        }
        for (ClanPlayer clanPlayer : players) {
            if (clanPlayer.getID() == id) {
                return clanPlayer;
            }
        }
        return null;
    }

    @Override
    public ClanPlayer getClanPlayer(Player player) {
        return getClanPlayerExact(player.getName());
    }

    @Override
    public ClanPlayer getAnyClanPlayer(Player player) {
        return getAnyClanPlayerExact(player.getName());
    }

    @Override
    public ClanPlayer getAnyClanPlayerExact(String name) {
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

    @Override
    public ClanPlayer getClanPlayerExact(String name) {
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

    public void updateOnlinePlayers() {
        for (Player player : plugin.getServer().getOnlinePlayers()) {
            CraftClanPlayer clanPlayer = (CraftClanPlayer) getClanPlayer(player);
            if (clanPlayer != null) {
                clanPlayer.setupOnlineVersion();
            }
        }
    }
}
