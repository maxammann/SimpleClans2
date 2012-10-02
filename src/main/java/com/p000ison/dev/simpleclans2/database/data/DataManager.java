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


package com.p000ison.dev.simpleclans2.database.data;

import com.p000ison.dev.simpleclans2.SimpleClans;
import com.p000ison.dev.simpleclans2.clan.Clan;
import com.p000ison.dev.simpleclans2.clan.ClanFlags;
import com.p000ison.dev.simpleclans2.clanplayer.ClanPlayer;
import com.p000ison.dev.simpleclans2.clanplayer.PlayerFlags;
import com.p000ison.dev.simpleclans2.database.Database;
import com.p000ison.dev.simpleclans2.database.data.statements.RemoveRankStatement;
import com.p000ison.dev.simpleclans2.ranks.Rank;
import com.p000ison.dev.simpleclans2.util.DateHelper;
import com.p000ison.dev.simpleclans2.util.JSONUtil;
import com.p000ison.dev.simpleclans2.util.Logging;

import java.sql.*;
import java.util.*;
import java.util.logging.Level;

/**
 * Represents a DataManager
 */
public class DataManager {

    private SimpleClans plugin;
    private Database database;
    private AutoSaver autoSaver;

    public PreparedStatement DELETE_CLAN, UPDATE_CLAN, INSERT_CLAN, RETRIEVE_CLAN_BY_TAG;

    public PreparedStatement DELETE_CLANPLAYER, UPDATE_CLANPLAYER, INSERT_CLANPLAYER, RETRIEVE_CLANPLAYER_BY_NAME;

    public PreparedStatement INSERT_KILL, RETRIEVE_TOTAL_DEATHS_PER_PLAYER;

    public PreparedStatement INSERT_RANK, UPDATE_RANK, DELETE_RANK_BY_ID, RETRIEVE_RANK, RETRIEVE_RANK_BY_NAME;


    public DataManager(SimpleClans plugin)
    {
        this.plugin = plugin;

        database = plugin.getDatabaseManager().getDatabase();

        autoSaver = new AutoSaver(plugin, this);

        //convert to minutes
        long autoSave = plugin.getSettingsManager().getAutoSave() * 1200L;

        if (autoSave > 0) {
            plugin.getServer().getScheduler().scheduleAsyncRepeatingTask(plugin, autoSaver, autoSave, autoSave);
        }

        prepareStatements();
        importAll();
    }

    private void prepareStatements()
    {
        DELETE_CLAN = database.prepareStatement("DELETE FROM `sc2_clans` WHERE id = ?;");
        INSERT_CLAN = database.prepareStatement("INSERT INTO `sc2_clans` ( `tag`, `name`, `verified`, `last_action` ) VALUES ( ?, ?, ?, ? );");
        UPDATE_CLAN = database.prepareStatement("UPDATE `sc2_clans` SET tag = ?, name = ?, verified = ?, friendly_fire = ?, allies = ?, rivals = ?, warring = ?, last_action = CURRENT_TIMESTAMP, bb = ?, flags = ? WHERE id = ?;");
        RETRIEVE_CLAN_BY_TAG = database.prepareStatement("SELECT id FROM `sc2_clans` WHERE tag = ?;");

        DELETE_CLANPLAYER = database.prepareStatement("DELETE FROM `sc2_players` WHERE id = ?;");
        UPDATE_CLANPLAYER = database.prepareStatement("UPDATE `sc2_players` SET leader = ?, rank = ?, trusted = ?, banned = ?, last_seen = ?, clan = ?, friendly_fire = ?, neutral_kills = ?, rival_kills = ?, civilian_kills = ?, deaths = ?, flags = ? WHERE id = ?;");
        INSERT_CLANPLAYER = database.prepareStatement("INSERT INTO `sc2_players` ( `name`, `leader`, `rank`, `trusted`, `last_seen`, `clan`, `friendly_fire`, `flags` ) VALUES ( ?, ?, ?, ?, ?, ?, ?, ? )");
        RETRIEVE_CLANPLAYER_BY_NAME = database.prepareStatement("SELECT id FROM `sc2_players` WHERE name = ?;");

        INSERT_KILL = database.prepareStatement("INSERT INTO `sc2_kills` ( `attacker`, `attacker_tag`, `victim`, `kill_type`, `victim_tag, `war` ) VALUES ( ?, ?, ?, ?, ?, ? );");
        RETRIEVE_TOTAL_DEATHS_PER_PLAYER = database.prepareStatement("SELECT victim, count(victim) AS kills FROM `sc_kills` GROUP BY victim ORDER BY 2 DESC;");

        INSERT_RANK = database.prepareStatement("INSERT INTO `sc2_ranks` ( `name`, `priority`, `clan` ) VALUES ( ?, ?, ? );");
        UPDATE_RANK = database.prepareStatement("UPDATE `sc2_ranks` SET name = ?, permissions = ?, priority = ?;");
        DELETE_RANK_BY_ID = database.prepareStatement("DELETE FROM `sc2_ranks` WHERE id = ?;");
        RETRIEVE_RANK = database.prepareStatement("SELECT * FROM `sc2_players` WHERE id = ? AND clan = ?;");
        RETRIEVE_RANK_BY_NAME = database.prepareStatement("SELECT id FROM `sc2_players` WHERE name = ? AND clan = ?;");
    }

    public final void importAll()
    {
        importClans();
        importClanPlayers();
    }

    public synchronized void updateClanPlayer(ClanPlayer clanPlayer)
    {
        try {
            UPDATE_CLANPLAYER.setBoolean(1, clanPlayer.isLeader());
            UPDATE_CLANPLAYER.setLong(2, (clanPlayer.getRank() == null ? -1L : clanPlayer.getRank().getId()));
            UPDATE_CLANPLAYER.setBoolean(3, clanPlayer.isTrusted());
            UPDATE_CLANPLAYER.setBoolean(4, clanPlayer.isBanned());
            UPDATE_CLANPLAYER.setTimestamp(5, new Timestamp(System.currentTimeMillis()));

            Clan clan = clanPlayer.getClan();
            System.out.println("update clanplayer : " + (clan == null ? -1L : clan.getId()));

            UPDATE_CLANPLAYER.setLong(6, clan == null ? -1L : clan.getId());
            UPDATE_CLANPLAYER.setBoolean(7, clanPlayer.isFriendlyFireOn());
            UPDATE_CLANPLAYER.setInt(8, clanPlayer.getNeutralKills());
            UPDATE_CLANPLAYER.setInt(9, clanPlayer.getRivalKills());
            UPDATE_CLANPLAYER.setInt(10, clanPlayer.getCivilianKills());
            UPDATE_CLANPLAYER.setInt(11, clanPlayer.getDeaths());
            UPDATE_CLANPLAYER.setString(12, clanPlayer.getFlags().read());
            UPDATE_CLANPLAYER.setLong(13, clanPlayer.getId());

            UPDATE_CLANPLAYER.executeUpdate();
            UPDATE_CLANPLAYER.close();
        } catch (SQLException e) {
            Logging.debug(e, "Failed to update player %s.", clanPlayer.getName());
        }
    }

    public void addStatement(Executable statement)
    {
        getQueue().add(statement);
    }

    public AutoSaver getQueue()
    {
        return autoSaver;
    }

    public void deleteClanPlayer(ClanPlayer clanPlayer)
    {
        deleteClanPlayer(clanPlayer.getId());
    }

    public void deleteClanPlayer(long id)
    {
        try {
            DELETE_CLANPLAYER.setLong(1, id);
            DELETE_CLANPLAYER.executeUpdate();
            DELETE_CLANPLAYER.close();
        } catch (SQLException e) {
            Logging.debug("Failed to delete clan player %s.", Level.SEVERE, id);
        }
    }

    public void insertClanPlayer(ClanPlayer clanPlayer)
    {
        try {
            INSERT_CLANPLAYER.setString(1, clanPlayer.getName());
            INSERT_CLANPLAYER.setBoolean(2, clanPlayer.isLeader());
            INSERT_CLANPLAYER.setLong(3, clanPlayer.getRank() == null ? -1L : clanPlayer.getRank().getId());
            INSERT_CLANPLAYER.setBoolean(4, clanPlayer.isTrusted());
            INSERT_CLANPLAYER.setTimestamp(5, new Timestamp(System.currentTimeMillis()));
            INSERT_CLANPLAYER.setLong(6, clanPlayer.getClanId());
            INSERT_CLANPLAYER.setBoolean(7, clanPlayer.isFriendlyFireOn());

            if (clanPlayer.getFlags().hasFlags()) {
                INSERT_CLANPLAYER.setString(8, clanPlayer.getFlags().read());
            } else {
                INSERT_CLANPLAYER.setNull(8, Types.VARCHAR);
            }

            INSERT_CLANPLAYER.executeUpdate();
            INSERT_CLANPLAYER.close();
        } catch (SQLException e) {
            Logging.debug(e, "Failed to insert clan player %s.", clanPlayer);
        }
    }

    public long retrieveClanPlayerId(String name)
    {

        try {
            RETRIEVE_CLANPLAYER_BY_NAME.setString(1, name);
            ResultSet result = RETRIEVE_CLANPLAYER_BY_NAME.executeQuery();

            if (!result.next()) {
                return -1;
            }

            long id = result.getLong("id");
            RETRIEVE_CLANPLAYER_BY_NAME.close();

            return id;

        } catch (SQLException e) {
            Logging.debug(e, "Failed at retrieving clan player id!");
        }
        return -1;
    }

    public void deleteClan(Clan clan)
    {
        deleteClan(clan.getId());
    }

    public boolean insertClan(Clan clan)
    {
        try {
            INSERT_CLAN.setString(1, clan.getTag());
            INSERT_CLAN.setString(2, clan.getName());
            INSERT_CLAN.setBoolean(3, clan.isVerified());
            INSERT_CLAN.setTimestamp(4, new Timestamp(System.currentTimeMillis()));

            int state = INSERT_CLAN.executeUpdate();
            INSERT_CLAN.close();

            return state != 0;
        } catch (SQLException e) {
            Logging.debug(e, "Failed to insert clan %s.", clan);
        }

        return false;
    }

    public void updateClan(Clan clan)
    {
        try {
            UPDATE_CLAN.setString(1, clan.getTag());
            UPDATE_CLAN.setString(2, clan.getName());
            UPDATE_CLAN.setBoolean(3, clan.isVerified());
            UPDATE_CLAN.setBoolean(4, clan.isFriendlyFireOn());

            if (clan.hasAllies()) {
                UPDATE_CLAN.setString(5, JSONUtil.collectionToJSON(clan.getAllies()));
            } else {
                UPDATE_CLAN.setNull(5, Types.VARCHAR);
            }

            if (clan.hasRivals()) {
                UPDATE_CLAN.setString(6, JSONUtil.collectionToJSON(clan.getRivals()));
            } else {
                UPDATE_CLAN.setNull(6, Types.VARCHAR);
            }

            if (clan.hasWarringClans()) {
                UPDATE_CLAN.setString(7, JSONUtil.collectionToJSON(clan.getWarringClans()));
            } else {
                UPDATE_CLAN.setNull(7, Types.VARCHAR);
            }

            if (clan.hasBB()) {
                UPDATE_CLAN.setString(8, JSONUtil.collectionToJSON(clan.getBB()));
            } else {
                UPDATE_CLAN.setNull(8, Types.VARCHAR);
            }

            if (clan.getFlags().hasFlags()) {
                UPDATE_CLAN.setString(9, clan.getFlags().read());
            } else {
                UPDATE_CLAN.setNull(9, Types.VARCHAR);
            }

            UPDATE_CLAN.setLong(10, clan.getId());

            UPDATE_CLAN.executeUpdate();
            UPDATE_CLAN.close();
        } catch (SQLException e) {
            Logging.debug(e, "Failed to update clan %s.", clan);
        }
    }


    public long retrieveClanId(String tag)
    {

        try {
            RETRIEVE_CLAN_BY_TAG.setString(1, tag);
            ResultSet result = RETRIEVE_CLAN_BY_TAG.executeQuery();


            if (!result.next()) {
                return -1;
            }

            long id = result.getLong("id");

            RETRIEVE_CLAN_BY_TAG.close();

            return id;
        } catch (SQLException e) {
            Logging.debug(e, "Failed at retrieving clan id!");
        }

        return -1;
    }


    public void deleteClan(long id)
    {
        try {
            DELETE_CLAN.setLong(1, id);
            DELETE_CLAN.executeUpdate();
        } catch (SQLException e) {
            Logging.debug("Failed to delete clan %s.", Level.SEVERE, id);
        }

        try {
            DELETE_CLAN.close();
        } catch (SQLException e) {
            Logging.debug(e, "Failed at closing the prepared statement!");
        }
    }

    private void importClans()
    {
        String query = "SELECT * FROM `sc2_clans`;";

        ResultSet result = database.query(query);
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
                List<String> rawBB = JSONUtil.JSONToStringList(result.getString("bb"));

                if (rawBB != null) {
                    clan.loadBB(new LinkedList<String>(rawBB));
                }

                //allies
                Set<Long> rawAllies = JSONUtil.JSONToLongSet(result.getString("allies"));

                if (rawAllies != null) {
                    alliesToAdd.put(clan, rawAllies);
                }

                //rivals
                Set<Long> rawRivals = JSONUtil.JSONToLongSet(result.getString("rivals"));

                if (rawRivals != null) {
                    rivalsToAdd.put(clan, rawRivals);
                }

                //warring
                Set<Long> rawWarring = JSONUtil.JSONToLongSet(result.getString("warring"));

                if (rawWarring != null) {
                    warringToAdd.put(clan, rawWarring);
                }

                clan.setRanks(retrieveRanks(id));

                clans.add(clan);
            }
        } catch (SQLException e) {
            Logging.debug(e, "Failed at retrieving clans!");
        }

        try {
            result.close();
        } catch (SQLException e) {
            Logging.debug(e, "Failed at closing result!");
        }

        plugin.getClanManager().importClans(clans);
        importAssociated(rivalsToAdd, alliesToAdd, warringToAdd);
    }

    private Set<Rank> retrieveRanks(long clanId)
    {
        String query = "SELECT * FROM `sc2_ranks` WHERE clan = " + clanId + ";";

        ResultSet result = database.query(query);
        Set<Rank> ranks = new HashSet<Rank>();

        try {
            while (result.next()) {

                long id = result.getLong("id");
                String name = result.getString("name");
                int priority = result.getInt("priority");
                Set<Integer> permissions = JSONUtil.JSONToSet("permissions", new HashSet<Integer>());

                ranks.add(new Rank(id, name, priority, clanId, permissions));
            }
        } catch (SQLException e) {
            Logging.debug(e, "Failed at retrieving ranks!");
        }

        try {
            result.close();
        } catch (SQLException e) {
            Logging.debug(e, "Failed at closing result!");
        }

        return ranks;
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

        ResultSet result = database.query(query);
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
                    } else {
                        clanPlayer.setClan(clan);

                        long rank = result.getLong("rank");

                        if (rank != -1) {
                            clanPlayer.setRank(clan.getRank(rank));
                        }
                    }
                }


                clanPlayers.add(clanPlayer);
            }
        } catch (SQLException e) {
            Logging.debug(e, "Failed at retrieving clans!");
        }

        try {
            result.close();
        } catch (SQLException e) {
            Logging.debug(e, "Failed at closing result!");
        }

        plugin.getClanPlayerManager().importClanPlayers(clanPlayers);
    }

    public boolean updateRank(Clan clan, Rank rank)
    {
        try {
            UPDATE_RANK.setString(1, rank.getName());
            UPDATE_RANK.setLong(2, clan.getId());
            UPDATE_RANK.setString(3, JSONUtil.collectionToJSON(rank.getPermissions()));
            UPDATE_RANK.setInt(4, rank.getPriority());

            int state = UPDATE_RANK.executeUpdate();

            try {
                UPDATE_RANK.close();
            } catch (SQLException e) {
                Logging.debug(e, "Failed at closing the prepared statement!");
            }

            return state != 0;
        } catch (SQLException e) {
            Logging.debug(e, "Failed at inserting rank!");
        }

        return false;
    }

    public boolean insertRank(Clan clan, Rank rank)
    {
        try {
            INSERT_RANK.setString(1, rank.getName());
            INSERT_RANK.setInt(2, rank.getPriority());
            INSERT_RANK.setLong(3, clan.getId());

            int state = INSERT_RANK.executeUpdate();

            try {
                INSERT_RANK.close();
            } catch (SQLException e) {
                Logging.debug(e, "Failed at closing the prepared statement!");
            }

            return state != 0;
        } catch (SQLException e) {
            Logging.debug(e, "Failed at inserting rank!");
        }

        return false;
    }

    public void deleteRank(long id)
    {
        addStatement(new RemoveRankStatement(id));
    }

    public long retrieveRankId(String name, Clan clan)
    {

        try {
            RETRIEVE_RANK_BY_NAME.setString(1, name);
            RETRIEVE_RANK_BY_NAME.setLong(2, clan.getId());
            ResultSet result = RETRIEVE_RANK_BY_NAME.executeQuery();


            if (!result.next()) {
                return -1;
            }

            long id = result.getLong("id");

            try {
                result.close();
            } catch (SQLException e) {
                Logging.debug(e, "Failed at closing result!");
            }

            return id;

        } catch (SQLException e) {
            Logging.debug(e, "Failed at retrieving rank!");
        }
        return -1;
    }

//    private Rank retrieveRank(long rankId, long clanId)
//    {
//
//        try {
//            RETRIEVE_RANK.setLong(1, rankId);
//            RETRIEVE_RANK.setLong(2, clanId);
//            ResultSet result = RETRIEVE_RANK.executeQuery();
//
//            if (!result.next()) {
//                return null;
//            }
//
//            Rank rank = new Rank(rankId, result.getString("name"));
//            rank.setPermissions(JSONUtil.JSONToSet(result.getString("permissions"), new HashSet<Integer>()));
//
//            RETRIEVE_RANK.close();
//
//            return rank;
//
//        } catch (SQLException e) {
//            Logging.debug(e, "Failed at retrieving rank!");
//        }
//        return null;
//    }

    public Map<String, Integer> getTotalDeathsPerPlayer()
    {
        Map<String, Integer> out = new HashMap<String, Integer>();

        ResultSet res = null;

        try {
            res = RETRIEVE_TOTAL_DEATHS_PER_PLAYER.executeQuery();

            if (res != null) {

                while (res.next()) {
                    String victim = res.getString("victim");

                    out.put(victim, res.getInt("kills"));
                }
            }
        } catch (SQLException e) {
            Logging.debug(e, "Failed at retrieving deaths!");
        }

        try {
            if (res != null) {
                res.close();
            }
        } catch (SQLException e) {
            Logging.debug(e, "Failed at closing result!");
        }

        return out;
    }
}
