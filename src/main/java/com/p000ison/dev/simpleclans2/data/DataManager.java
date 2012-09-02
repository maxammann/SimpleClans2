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


package com.p000ison.dev.simpleclans2.data;

import com.p000ison.dev.simpleclans2.SimpleClans;
import com.p000ison.dev.simpleclans2.clan.Clan;
import com.p000ison.dev.simpleclans2.clan.ClanFlags;
import com.p000ison.dev.simpleclans2.clanplayer.ClanPlayer;
import com.p000ison.dev.simpleclans2.clanplayer.PlayerFlags;
import com.p000ison.dev.simpleclans2.database.tables.ClanPlayerTable;
import com.p000ison.dev.simpleclans2.database.tables.ClanTable;
import com.p000ison.dev.simpleclans2.util.Logging;

import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;

/**
 * Represents a DataManager
 */
public class DataManager {

    private SimpleClans plugin;

    public DataManager(SimpleClans plugin)
    {
        this.plugin = plugin;

        importClans();
        importClanPlayers();
    }

    public void importClanPlayers()
    {
        List<ClanPlayerTable> rawClanPlayers = plugin.getDatabaseManager().getDatabase().select(ClanPlayerTable.class).execute().find();
        Set<ClanPlayer> clanPlayers = new HashSet<ClanPlayer>();

        for (ClanPlayerTable rawClanPlayer : rawClanPlayers) {

            ClanPlayer clanPlayer = new ClanPlayer(plugin, rawClanPlayer.name);

            clanPlayer.setId(rawClanPlayer.id);
            clanPlayer.setBanned(rawClanPlayer.banned);

            Clan clan = plugin.getClanManager().getClan(rawClanPlayer.clan);

            if (clan == null) {
                Logging.debug("The clan for %s was not found!", Level.WARNING, clanPlayer.getName());
            }

            clanPlayer.setClan(clan);
            clanPlayer.setFriendlyFire(rawClanPlayer.friendly_fire);
            clanPlayer.setJoinDate(rawClanPlayer.join_date);
            clanPlayer.setLastSeenDate(rawClanPlayer.last_seen);
            clanPlayer.setLeader(rawClanPlayer.leader);
            clanPlayer.setTrusted(rawClanPlayer.trusted);
            clanPlayer.setRank(rawClanPlayer.rank);

            PlayerFlags flags = new PlayerFlags();
            flags.write(rawClanPlayer.flags);

            clanPlayer.setFlags(flags);

            clanPlayers.add(clanPlayer);
        }

        checkClanPlayers(clanPlayers);

        plugin.getClanPlayerManager().importClanPlayers(clanPlayers);
    }

    public void checkClanPlayers(Set<ClanPlayer> clanPlayers)
    {
        Iterator<ClanPlayer> it = clanPlayers.iterator();

        while (it.hasNext()) {
            ClanPlayer clanPlayer = it.next();

            if (clanPlayer.getInactiveDays() > 7) {
                purgeClanPlayer(clanPlayer.getId());
                it.remove();
            }
        }
    }

    public void purgeClanPlayer(long id)
    {
        ClanPlayerTable clanPlayerTable = new ClanPlayerTable();
        clanPlayerTable.id = id;
        plugin.getDatabaseManager().getDatabase().remove(clanPlayerTable);
    }


    public void importClans()
    {
        List<ClanTable> rawClans = plugin.getDatabaseManager().getDatabase().select(ClanTable.class).execute().find();
        Set<Clan> clans = new HashSet<Clan>();

        for (ClanTable rawClan : rawClans) {
            Clan clan = new Clan(plugin, rawClan.id, rawClan.tag, rawClan.name);

            clan.setFoundedDate(rawClan.founded);
            clan.setFriendlyFire(rawClan.friendly_fire);
            clan.setVerified(rawClan.verified);
            clan.setLastActionDate(rawClan.last_action);

            ClanFlags flags = new ClanFlags();
            flags.write(rawClan.flags);

            clan.setFlags(flags);

            clans.add(clan);
        }

        checkClans(clans);

        plugin.getClanManager().importClans(clans);
    }

    public void checkClans(Set<Clan> clans)
    {
        Iterator<Clan> it = clans.iterator();

        while (it.hasNext()) {
            Clan clan = it.next();

            if (clan.getInactiveDays() > 7) {
                purgeClan(clan.getId());
                it.remove();
            }
        }
    }

    public void purgeClan(long id)
    {
        ClanTable clanTable = new ClanTable();
        clanTable.id = id;
        plugin.getDatabaseManager().getDatabase().remove(clanTable);
    }


    public void addKill(String attacker, String attackerTag, String victim, String victimTag, KillType type, boolean inWar)
    {
        KillEntry kill = new KillEntry();

        kill.setAttacker(attacker);
        kill.setAttackerTag(attackerTag);
        kill.setVictim(victim);
        kill.setVictimTag(victimTag);
        kill.setKillType(type.getType());
        kill.setDate(System.currentTimeMillis());
        kill.setWar(inWar);

        plugin.getDatabaseManager().getDatabase().save(kill);
    }
}
