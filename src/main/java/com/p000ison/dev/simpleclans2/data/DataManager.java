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


package com.p000ison.dev.simpleclans2.data;

import com.p000ison.dev.simpleclans2.SimpleClans;
import com.p000ison.dev.simpleclans2.clan.Clan;
import com.p000ison.dev.simpleclans2.clan.ClanFlags;
import com.p000ison.dev.simpleclans2.clanplayer.ClanPlayer;
import com.p000ison.dev.simpleclans2.clanplayer.PlayerFlags;
import com.p000ison.dev.simpleclans2.database.Database;
import com.p000ison.dev.simpleclans2.util.DateHelper;
import com.p000ison.dev.simpleclans2.util.JSONUtil;
import com.p000ison.dev.simpleclans2.util.Logging;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
import java.util.logging.Level;

/**
 * Represents a DataManager
 */
public class DataManager {

    private SimpleClans plugin;

    private PreparedStatement deleteClan, updateClan;

    private PreparedStatement deleteClanPlayer, updateClanPlayer;

    private PreparedStatement insertKill, retrieveTotalDeathsPerPlayer;

    private Database database;

    public DataManager(SimpleClans plugin)
    {
        this.plugin = plugin;

        database = plugin.getDatabaseManager().getDatabase();

        prepareStatements();
        importClans();
        importClanPlayers();
    }

    private void prepareStatements()
    {
        deleteClan = database.prepareStatement("DELETE FROM `sc2_clans` WHERE id = ?;");


        deleteClanPlayer = database.prepareStatement("DELETE FROM `sc2_player` WHERE id = ?;");
        updateClanPlayer = database.prepareStatement("UPDATE `sc2_players` SET leader = ?, rank = ?, trusted = ?, banned = ?, last_seen = ?, clan = ?, friendly_fire = ?, neutral_kills = ?, rival_kills = ?, civilian_kills = ?, deaths = ?, flags = ?;");


        insertKill = database.prepareStatement("INSERT INTO `sc2_kills` ( `attacker`, `attacker_tag`, `victim`, `kill_type`, `victim_tag, `war` ) VALUES ( ?, ?, ?, ?, ?, ? );");
        retrieveTotalDeathsPerPlayer = database.prepareStatement("SELECT victim, count(victim) AS kills FROM `sc_kills` GROUP BY victim ORDER BY 2 DESC;");
    }


    public void updateClanPlayer(ClanPlayer clanPlayer)
    {
        try {
            updateClanPlayer.setBoolean(1, clanPlayer.isLeader());
            updateClanPlayer.setString(2, clanPlayer.getRank());
            updateClanPlayer.setBoolean(3, clanPlayer.isTrusted());
            updateClanPlayer.setBoolean(4, clanPlayer.isBanned());
            updateClanPlayer.setLong(5, clanPlayer.getLastSeenDate());

            Clan clan = clanPlayer.getClan();

            updateClanPlayer.setLong(3, clan == null ? -1L : clan.getId());
            updateClanPlayer.setBoolean(7, clanPlayer.isFriendlyFireOn());
            updateClanPlayer.setInt(8, clanPlayer.getNeutralKills());
            updateClanPlayer.setInt(9, clanPlayer.getRivalKills());
            updateClanPlayer.setInt(10, clanPlayer.getCivilianKills());
            updateClanPlayer.setInt(11, clanPlayer.getDeaths());
            updateClanPlayer.setString(12, clanPlayer.getFlags().read());

        } catch (SQLException e) {
            Logging.debug(e, "Failed to update player %s.", clanPlayer.getName());
        }
    }

    public void deleteClanPlayer(ClanPlayer clanPlayer)
    {
        deleteClanPlayer(clanPlayer.getId());
    }

    public void deleteClanPlayer(long id)
    {
        try {
            deleteClanPlayer.setLong(1, id);
            deleteClanPlayer.executeUpdate();
        } catch (SQLException e) {
            Logging.debug("Failed to delete clan player %s.", Level.SEVERE, id);
        }
    }

    public void deleteClan(Clan clan)
    {
        deleteClan(clan.getId());
    }

    public void deleteClan(long id)
    {
        try {
            deleteClan.setLong(1, id);
            deleteClan.executeUpdate();
        } catch (SQLException e) {
            Logging.debug("Failed to delete clan %s.", Level.SEVERE, id);
        }
    }

    private void importClans()
    {
        String query = "SELECT * FROM `sc2_clans`;";

        ResultSet result = database.select(query);
        Set<Clan> clans = new HashSet<Clan>();

        Map<Clan, Set<Long>> rivalsToAdd = new HashMap<Clan, Set<Long>>();
        Map<Clan, Set<Long>> alliesToAdd = new HashMap<Clan, Set<Long>>();
        Map<Clan, Set<Long>> warringToAdd = new HashMap<Clan, Set<Long>>();

        long currentTime = System.currentTimeMillis();

        try {
            while (result.next()) {

                long lastAction = result.getTimestamp("last_action").getTime();
                boolean verified = result.getBoolean("verified");
                long id = result.getLong("id");

                if (verified) {
                    if (DateHelper.differenceInDays(lastAction, currentTime) > plugin.getSettingsManager().getPurgeInactiveClansDays()) {
                        deleteClan(id);
                        continue;
                    }
                } else {
                    if (DateHelper.differenceInDays(lastAction, currentTime) > plugin.getSettingsManager().getPurgeUnverifiedClansDays()) {
                        deleteClan(id);
                        continue;
                    }
                }

                Clan clan = new Clan(plugin, id, result.getString("tag"), result.getString("name"));

                clan.setVerified(verified);
                clan.setFoundedDate(result.getLong("founded"));
                clan.setFriendlyFire(result.getBoolean("friendly_fire"));
                clan.setLastActionDate(lastAction);

                //flags
                ClanFlags flags = new ClanFlags();
                flags.write(result.getString("flags"));

                clan.setFlags(flags);

                //bb
                Collection rawBB = JSONUtil.JSONToList(result.getString("bb"), "bb");

                if (rawBB != null) {
                    clan.loadBB(new LinkedList<String>(rawBB));
                }

                //allies
                Collection rawAllies = JSONUtil.JSONToList(result.getString("allies"), "allies");

                if (rawAllies != null) {
                    alliesToAdd.put(clan, new HashSet<Long>(rawAllies));
                }

                //rivals
                Collection rawRivals = JSONUtil.JSONToList(result.getString("rivals"), "rivals");

                if (rawRivals != null) {
                    rivalsToAdd.put(clan, new HashSet<Long>(rawRivals));
                }

                //warring
                Collection rawWarring = JSONUtil.JSONToList(result.getString("warring"), "warring");

                if (rawWarring != null) {
                    warringToAdd.put(clan, new HashSet<Long>(rawWarring));
                }

                clans.add(clan);
            }
        } catch (SQLException e) {
            Logging.debug(e, "Failed at retrieving clans!");
        }

        plugin.getClanManager().importClans(clans);
        importAssociated(rivalsToAdd, alliesToAdd, warringToAdd);
    }

    private void importAssociated(Map<Clan, Set<Long>> rivals, Map<Clan, Set<Long>> allies, Map<Clan, Set<Long>> warring)
    {
        for (Map.Entry<Clan, Set<Long>> entry : rivals.entrySet()) {

            for (Long rivalId : entry.getValue()) {

                Clan rival = plugin.getClanManager().getClan(rivalId);

                if (rival == null) {
                    continue;
                }

                entry.getKey().addRival(rival);
            }
        }

        for (Map.Entry<Clan, Set<Long>> entry : allies.entrySet()) {

            for (Long allyId : entry.getValue()) {

                Clan ally = plugin.getClanManager().getClan(allyId);

                if (ally == null) {
                    continue;
                }

                entry.getKey().addAlly(ally);
            }
        }

        for (Map.Entry<Clan, Set<Long>> entry : warring.entrySet()) {

            for (Long warringId : entry.getValue()) {

                Clan warringClan = plugin.getClanManager().getClan(warringId);

                if (warringClan == null) {
                    continue;
                }

                entry.getKey().addWarringClan(warringClan);
            }
        }
    }

    private void importClanPlayers()
    {
        String query = "SELECT * FROM `sc2_players`;";

        ResultSet result = database.select(query);
        Set<ClanPlayer> clanPlayers = new HashSet<ClanPlayer>();

        long currentTime = System.currentTimeMillis();

        try {
            while (result.next()) {

                long lastSeen = result.getTimestamp("last_seen").getTime();
                long id = result.getLong("id");

                if (DateHelper.differenceInDays(lastSeen, currentTime) > plugin.getSettingsManager().getPurgeInactivePlayersDays()) {
                    deleteClanPlayer(id);
                    continue;
                }

                ClanPlayer clanPlayer = new ClanPlayer(plugin, id, result.getString("name"));

                clanPlayer.setBanned(result.getBoolean("banned"));
                clanPlayer.setLeader(result.getBoolean("leader"));
                clanPlayer.setRank(result.getString("rank"));
                clanPlayer.setTrusted(result.getBoolean("trusted"));
                clanPlayer.setLastSeenDate(lastSeen);
                clanPlayer.setJoinDate(result.getLong("join_date"));
                clanPlayer.setFriendlyFire(result.getBoolean("friendly_fire"));
                clanPlayer.setNeutralKills(result.getInt("neutral_kills"));
                clanPlayer.setRivalKills(result.getInt("rival_kills"));
                clanPlayer.setCivilianKills(result.getInt("civilian_kills"));
                clanPlayer.setDeaths(result.getInt("deaths"));

                PlayerFlags flags = new PlayerFlags();

                flags.write(result.getString("flags"));

                clanPlayer.setFlags(flags);

                long clanId = result.getLong("clan");

                if (clanId != -1) {

                    Clan clan = plugin.getClanManager().getClan(clanId);

                    if (clan == null) {
                        Logging.debug("Failed to find clan for %s.", Level.WARNING, clanPlayer.getName());
                    }

                    clanPlayer.setClan(clan);
                }


                clanPlayers.add(clanPlayer);
            }
        } catch (SQLException e) {
            Logging.debug(e, "Failed at retrieving clans!");
        }

        plugin.getClanPlayerManager().importClanPlayers(clanPlayers);
    }

    public Map<String, Integer> getTotalDeathsPerPlayer()
    {
        Map<String, Integer> out = new HashMap<String, Integer>();

        try {
            ResultSet res = retrieveTotalDeathsPerPlayer.executeQuery();

            if (res != null) {

                while (res.next()) {
                    String victim = res.getString("victim");

                    out.put(victim, res.getInt("kills"));
                }
            }
        } catch (SQLException e) {
            Logging.debug(e, "Failed at retrieving deaths!");
        }

        return out;
    }

    public void addKill(String attacker, String attackerTag, String victim, String victimTag, KillType type, boolean inWar)
    {
        try {
            insertKill.setString(1, attacker);
            insertKill.setString(2, attackerTag);
            insertKill.setString(3, victim);
            insertKill.setString(4, victimTag);
            insertKill.setByte(5, type.getType());
            insertKill.setBoolean(6, inWar);
        } catch (SQLException e) {
            Logging.debug(e, "Failed to insert kill for victim attacker %s and victim %s.", attacker, victim);
        }
    }
}
