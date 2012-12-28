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
 *     Last modified: 10.10.12 21:57
 */


package com.p000ison.dev.simpleclans2.database.data;

import com.p000ison.dev.simpleclans2.SimpleClans;
import com.p000ison.dev.simpleclans2.clan.Clan;
import com.p000ison.dev.simpleclans2.clan.ranks.Rank;
import com.p000ison.dev.simpleclans2.clanplayer.ClanPlayer;
import com.p000ison.dev.simpleclans2.database.data.response.Response;
import com.p000ison.dev.simpleclans2.database.data.response.ResponseTask;
import com.p000ison.dev.simpleclans2.util.Logging;
import com.p000ison.dev.simpleclans2.util.comparators.ReverseIntegerComparator;
import com.p000ison.dev.sqlapi.Database;
import com.p000ison.dev.sqlapi.DatabaseConfiguration;
import com.p000ison.dev.sqlapi.DatabaseManager;
import com.p000ison.dev.sqlapi.exception.DatabaseConnectionException;
import com.p000ison.dev.sqlapi.jbdc.JBDCDatabase;
import com.p000ison.dev.sqlapi.mysql.MySQLConfiguration;
import com.p000ison.dev.sqlapi.mysql.MySQLDatabase;
import com.p000ison.dev.sqlapi.query.PreparedQuery;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.*;

/**
 * Represents a DataManager
 */
public class DataManager {

    private SimpleClans plugin;


    private JBDCDatabase database;
    private AutoSaver autoSaver;
    private ResponseTask responseTask;

    private PreparedQuery unsetClanPlayer;
    private PreparedQuery retriveKillsPerPlayer, retriveMostKills, insertKill;
    private PreparedQuery deleteRankById;
    private PreparedQuery retrieveBBLimit, insertBB, purgeBB;

    public DataManager(SimpleClans plugin) throws DatabaseConnectionException
    {
        this.plugin = plugin;

        DatabaseConfiguration config = plugin.getSettingsManager().getDatabaseConfiguration();
        if (plugin.getSettingsManager().getDatabaseConfiguration() instanceof MySQLConfiguration) {
            database = (JBDCDatabase) DatabaseManager.registerConnection(new MySQLDatabase(config));
        }

        if (database == null) {
            Logging.debug("No database found! Skipping...");
            return;
        }

        database.registerTable(Clan.class).registerConstructor(plugin);
        database.registerTable(ClanPlayer.class).registerConstructor(plugin);
        database.registerTable(Rank.class);

        autoSaver = new AutoSaver(plugin, this);
        responseTask = new ResponseTask();

        plugin.getServer().getScheduler().scheduleAsyncRepeatingTask(plugin, responseTask, 0L, 5L);


        //convert to minutes
        long autoSave = plugin.getSettingsManager().getAutoSave() * 1200L;

        if (autoSave > 0) {
            plugin.getServer().getScheduler().scheduleAsyncRepeatingTask(plugin, autoSaver, autoSave, autoSave);
        }

        unsetClanPlayer = database.createPreparedStatement("UPDATE `sc2_players` SET clan = -1, trusted = 0, ranks = null WHERE clan = ?;");

        retriveKillsPerPlayer = database.createPreparedStatement("SELECT victim, count(victim) AS kills FROM `sc2_kills` WHERE attacker = ? GROUP BY victim ORDER BY count(victim) DESC;");
        retriveMostKills = database.createPreparedStatement("SELECT attacker, victim, count(victim) AS kills FROM `sc2_kills` GROUP BY attacker, victim ORDER BY 3 DESC;");
        insertKill = database.createPreparedStatement("INSERT INTO `sc2_kills` ( `attacker`, `attacker_clan`, `victim`, `victim_clan`, `war`, `type`, `date` ) VALUES ( ?, ?, ?, ?, ?, ?, ? );");

        deleteRankById = database.createPreparedStatement("DELETE FROM `sc2_ranks` WHERE id = ?;");

        retrieveBBLimit = database.createPreparedStatement("SELECT `text` FROM `sc2_bb` WHERE clan = ? ORDER BY `date` DESC LIMIT ?, ?;");
        insertBB = database.createPreparedStatement("INSERT INTO `sc2_bb` ( `clan`, `text` ) VALUES ( ?, ? );");
        purgeBB = database.createPreparedStatement("DELETE FROM `sc2_bb` WHERE clan = ?;");

        importAll();
    }

    public final void importAll()
    {
        Set<Clan> clans = database.<Clan>select().from(Clan.class).prepare().getResults(new HashSet<Clan>());

        Set<ClanPlayer> clanPlayers = database.<ClanPlayer>select().from(ClanPlayer.class).prepare().getResults(new HashSet<ClanPlayer>());

        plugin.getClanManager().importClans(clans);
        plugin.getClanPlayerManager().importClanPlayers(clanPlayers);

        database.saveStoredValues(Clan.class);
        database.saveStoredValues(ClanPlayer.class);
    }

    //
    // if clan == null setTrusted(false);
    // purge clan if it is too old
    // unset clanplayer if the clan gets purged
    // statements, converter, create methods   X
    //

    public List<String> retrieveBB(Clan clan, int start, int end)
    {

        List<String> bb = new ArrayList<String>();

        ResultSet res = null;

        try {
//            retrieveBBLimit.setLong(1, clan.getId());
//            retrieveBBLimit.setInt(2, start);
//            retrieveBBLimit.setInt(3, end);
//            res = RETRIEVE_BB_LIMIT.executeQuery();

            if (res != null) {
                while (res.next()) {
                    bb.add(res.getString("text"));
                }
            }
        } catch (SQLException e) {
            Logging.debug(e, true, "Failed at retrieving bb for %s!", clan.getTag());
        }

        try {
            if (res != null) {
                res.close();

            }
        } catch (SQLException e) {
            Logging.debug(e, true, "Failed at closing result!");
        }

        return bb;
    }

    public void purgeBB(Clan clan)
    {
        purgeBB.set(0, clan.getId());
        purgeBB.update();
    }

    public void insertBBMessage(Clan clan, String message)
    {
        insertBB.set(1, clan.getId());
        insertBB.set(2, message);
        insertBB.update();
    }

    public boolean insertKill(long attacker, long attackerClan, long victim, long victimClan, boolean war, byte killType, Timestamp timestamp)
    {
        insertKill.set(0, attacker);
        insertKill.set(1, attackerClan);
        insertKill.set(2, victim);
        insertKill.set(3, victimClan);
        insertKill.set(4, war);
        insertKill.set(5, killType);
        insertKill.set(6, timestamp);

        return insertKill.update();
    }


    /**
     * Returns a map of victim->count of all kills that specific player did
     *
     * @param playerId The player to look for
     * @return A map of victims and a count
     */
    public SortedMap<Integer, Long> getKillsPerPlayer(long playerId)
    {
        TreeMap<Integer, Long> out = new TreeMap<Integer, Long>(new ReverseIntegerComparator());

//        try {
//            RETRIEVE_KILLS_PER_PLAYER.setLong(1, playerId);
//            ResultSet res = RETRIEVE_KILLS_PER_PLAYER.executeQuery();
//
//            if (res != null) {
//                while (res.next()) {
//                    Long victim = res.getLong("victim");
//                    int kills = res.getInt("kills");
//                    out.put(kills, victim);
//                }
//            }
//        } catch (SQLException e) {
//            Logging.debug(e, true, "Failed at querying the kills of %s!", playerId);
//        }

        return out;
    }

    /**
     * Returns a map of tag->count of all kills
     *
     * @return A map of tag->count of all kills
     */
    public List<Conflicts> getMostKilled()
    {
        List<Conflicts> out = new ArrayList<Conflicts>();
//        try {
//            ResultSet res = RETRIEVE_MOST_KILLS.executeQuery();
//
//            if (res != null) {
//
//                while (res.next()) {
//                    long attacker = res.getLong("attacker");
//                    long victim = res.getLong("victim");
//                    int kills = res.getInt("kills");
//                    out.add(new Conflicts(attacker, victim, kills));
//                }
//            }
//        } catch (SQLException e) {
//            Logging.debug(e, true, "Failed at collecting the most killed useres!");
//        }

        return out;
    }

    public Database getDatabase()
    {
        return database;
    }

    public void addResponse(Response response)
    {
        responseTask.add(response);
    }

    public void addStatement(Executable executable)
    {
        getAutoSaver().addExecutable(executable);
    }

    public AutoSaver getAutoSaver()
    {
        return autoSaver;
    }

    public boolean deleteRankById(int id)
    {
        deleteRankById.set(0, id);
        return deleteRankById.update();
    }


}
