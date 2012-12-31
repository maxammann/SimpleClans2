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


package com.p000ison.dev.simpleclans2.database;

import com.p000ison.dev.simpleclans2.SimpleClans;
import com.p000ison.dev.simpleclans2.clan.Clan;
import com.p000ison.dev.simpleclans2.clan.ranks.Rank;
import com.p000ison.dev.simpleclans2.clanplayer.ClanPlayer;
import com.p000ison.dev.simpleclans2.database.response.Response;
import com.p000ison.dev.simpleclans2.database.response.ResponseTask;
import com.p000ison.dev.simpleclans2.database.statements.KillStatement;
import com.p000ison.dev.simpleclans2.util.DateHelper;
import com.p000ison.dev.simpleclans2.util.Logging;
import com.p000ison.dev.simpleclans2.util.comparators.ReverseIntegerComparator;
import com.p000ison.dev.sqlapi.Database;
import com.p000ison.dev.sqlapi.DatabaseConfiguration;
import com.p000ison.dev.sqlapi.exception.DatabaseConnectionException;
import com.p000ison.dev.sqlapi.jbdc.JBDCDatabase;
import com.p000ison.dev.sqlapi.jbdc.JBDCPreparedQuery;
import com.p000ison.dev.sqlapi.mysql.MySQLConfiguration;
import com.p000ison.dev.sqlapi.mysql.MySQLDatabase;
import com.p000ison.dev.sqlapi.query.PreparedSelectQuery;
import com.p000ison.dev.sqlapi.sqlite.SQLiteConfiguration;
import com.p000ison.dev.sqlapi.sqlite.SQLiteDatabase;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

/**
 * Represents a DatabaseManager
 */
public class DatabaseManager {

    private SimpleClans plugin;


    private JBDCDatabase database;
    private AutoSaver autoSaver;
    private ResponseTask responseTask;

    private JBDCPreparedQuery retrieveKillsPerPlayer, retrieveMostKills;
    private JBDCPreparedQuery deleteRankById;
    private JBDCPreparedQuery retrieveBBLimit, insertBB, purgeBB;

    public DatabaseManager(SimpleClans plugin) throws DatabaseConnectionException
    {
        this.plugin = plugin;

        Database.setLogger(Logging.getInstance());

        DatabaseConfiguration config = plugin.getSettingsManager().getDatabaseConfiguration();
        if (config instanceof MySQLConfiguration) {
            database = (JBDCDatabase) com.p000ison.dev.sqlapi.DatabaseManager.registerConnection(new MySQLDatabase(config));
        } else if (config instanceof SQLiteConfiguration) {
            database = (JBDCDatabase) com.p000ison.dev.sqlapi.DatabaseManager.registerConnection(new SQLiteDatabase(config));
        }

        if (database == null) {
            Logging.debug("No database found! Skipping...");
            return;
        }

        database.registerTable(KillStatement.class);
        database.registerTable(BBTable.class);
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

        retrieveKillsPerPlayer = database.createPreparedStatement("SELECT victim, count(victim) AS kills FROM `sc2_kills` WHERE attacker = ? GROUP BY victim ORDER BY count(victim) DESC;");
        retrieveMostKills = database.createPreparedStatement("SELECT attacker, victim, count(victim) AS kills FROM `sc2_kills` GROUP BY attacker, victim ORDER BY 3 DESC;");

        deleteRankById = database.createPreparedStatement("DELETE FROM `sc2_ranks` WHERE id = ?;");

        retrieveBBLimit = database.createPreparedStatement("SELECT `text` FROM `sc2_bb` WHERE clan = ? ORDER BY `date` DESC LIMIT ?, ?;");
        insertBB = database.createPreparedStatement("INSERT INTO `sc2_bb` ( `clan`, `text` ) VALUES ( ?, ? );");
        purgeBB = database.createPreparedStatement("DELETE FROM `sc2_bb` WHERE clan = ?;");

        importAll();
    }

    public void close()
    {
        retrieveKillsPerPlayer.close();
        retrieveMostKills.close();
        deleteRankById.close();
        retrieveBBLimit.close();
        insertBB.close();
        purgeBB.close();

        getDatabase().close();
    }

    public final void importAll()
    {
        Set<Clan> clans = database.<Clan>select().from(Clan.class).prepare().getResults(new HashSet<Clan>());
        long currentTime = System.currentTimeMillis();
        PreparedSelectQuery<Rank> rankQuery = database.<Rank>select().from(Rank.class).where().preparedEquals("clan").select().prepare();

        Iterator<Clan> clanIterator = clans.iterator();
        while (clanIterator.hasNext()) {
            Clan clan = clanIterator.next();
            int maxInactiveDays = clan.isVerified() ? plugin.getSettingsManager().getPurgeInactiveClansDays() : plugin.getSettingsManager().getPurgeUnverifiedClansDays();

            if (DateHelper.differenceInDays(clan.getLastUpdated(), currentTime) > maxInactiveDays) {
                Logging.debug("Purging clan %s because it was too long inactive! (id=%s)", clan.getTag(), clan.getID());
                getDatabase().delete(clan);
                clanIterator.remove();
            } else {
                rankQuery.set(0, clan.getID());
                clan.loadRanks(rankQuery.getResults(new HashSet<Rank>()));
            }
        }

        rankQuery.close();

        plugin.getClanManager().importClans(clans);
        database.saveStoredValues(Clan.class);

        Set<ClanPlayer> clanPlayers = database.<ClanPlayer>select().from(ClanPlayer.class).prepare().getResults(new HashSet<ClanPlayer>());

        Iterator<ClanPlayer> clanPlayerIterator = clanPlayers.iterator();
        while (clanPlayerIterator.hasNext()) {
            ClanPlayer cp = clanPlayerIterator.next();
            if (DateHelper.differenceInDays(cp.getLastSeenDate(), currentTime) > plugin.getSettingsManager().getPurgeInactivePlayersDays()) {
                Logging.debug("Purging player %s because it was too long inactive! (id=%s)", cp.getName(), cp.getId());
                getDatabase().delete(cp);
                clanPlayerIterator.remove();
            }


            //validate some stuff
            if (cp.getClan() == null) {
                if (cp.unset()) {
                    cp.update();
                }
            } else {
                cp.getClan().addMemberInternally(cp);
            }
        }

        //purge all empty clan
        clanIterator = clans.iterator();
        while (clanIterator.hasNext()) {
            Clan clan = clanIterator.next();

            if (clan.getSize() == 0) {
                Logging.debug("Purging clan %s because it has 0 members! (id=%s)", clan.getTag(), clan.getID());
                getDatabase().delete(clan);
                clanIterator.remove();
            }
        }

        plugin.getClanPlayerManager().importClanPlayers(clanPlayers);
        database.saveStoredValues(ClanPlayer.class);
    }

    public List<String> retrieveBB(Clan clan, int start, int end)
    {

        List<String> bb = new ArrayList<String>();

        ResultSet res = null;

        try {
            retrieveBBLimit.set(0, clan.getID());
            retrieveBBLimit.set(1, start);
            retrieveBBLimit.set(2, end);
            res = retrieveBBLimit.query();

            if (res != null) {
                while (res.next()) {
                    bb.add(res.getString("text"));
                }
            }
        } catch (SQLException e) {
            Logging.debug(e, true, "Failed at retrieving bb for %s!", clan.getTag());
        } finally {
            try {
                if (res != null) {
                    res.close();
                }
            } catch (SQLException e) {
                Logging.debug(e, true, "Failed at closing result!");
            }
        }

        return bb;
    }

    public void purgeBB(Clan clan)
    {
        purgeBB.set(0, clan.getID());
        purgeBB.update();
    }

    public void insertBBMessage(Clan clan, String message)
    {
        insertBB.set(0, clan.getID());
        insertBB.set(1, message);
        insertBB.update();
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
        ResultSet res = null;
        try {
            retrieveKillsPerPlayer.set(0, playerId);
            res = retrieveKillsPerPlayer.query();

            while (res.next()) {
                Long victim = res.getLong("victim");
                int kills = res.getInt("kills");
                out.put(kills, victim);
            }
        } catch (SQLException e) {
            Logging.debug(e, true, "Failed at querying the kills of %s!", playerId);
        } finally {
            try {
                if (res != null) {
                    res.close();
                }
            } catch (SQLException e) {
                Logging.debug(e, true, "Failed at closing result!");
            }
        }

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
        ResultSet res = null;
        try {
            res = retrieveMostKills.query();

            while (res.next()) {
                long attacker = res.getLong("attacker");
                long victim = res.getLong("victim");
                int kills = res.getInt("kills");
                out.add(new Conflicts(attacker, victim, kills));
            }
        } catch (SQLException e) {
            Logging.debug(e, true, "Failed at collecting the most killed useres!");
        } finally {
            try {
                if (res != null) {
                    res.close();
                }
            } catch (SQLException e) {
                Logging.debug(e, true, "Failed at closing result!");
            }
        }

        return out;
    }

    public JBDCDatabase getDatabase()
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
