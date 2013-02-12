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
import com.p000ison.dev.simpleclans2.api.clan.Clan;
import com.p000ison.dev.simpleclans2.api.clanplayer.ClanPlayer;
import com.p000ison.dev.simpleclans2.api.rank.Rank;
import com.p000ison.dev.simpleclans2.clan.CraftClan;
import com.p000ison.dev.simpleclans2.clan.ranks.CraftRank;
import com.p000ison.dev.simpleclans2.clanplayer.CraftClanPlayer;
import com.p000ison.dev.sqlapi.Database;
import com.p000ison.dev.sqlapi.exception.QueryException;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * Represents a AutoSaver
 */
public class AutoSaver implements Runnable {

    private Queue<Executable> queue = new ConcurrentLinkedQueue<Executable>();

    private SimpleClans plugin;
    private DatabaseManager dataManager;

    private long time;
    private long loopedTime;

    public AutoSaver(SimpleClans simpleClans, DatabaseManager dataManager, long loopedTime) {
        this.plugin = simpleClans;
        this.dataManager = dataManager;
        this.loopedTime = loopedTime;
    }

    @Override
    public synchronized void run() {
        Database db = dataManager.getDatabase();
        time += loopedTime;
        if (time >= 12000) {
            time = 0;
            db.sendKeepAliveQuery();
        }

        for (Clan clan : plugin.getClanManager().getModifyAbleClans()) {
            CraftClan craftClan = (CraftClan) clan;

            if (craftClan.needsUpdate()) {
                try {
                    db.addUpdateBatch(craftClan);
                    clan.update(false);
                } catch (QueryException e) {
                    clan.update(true);
                    throw e;
                }
            }

            for (Rank rank : clan.getRanks()) {
                CraftRank craftRank = (CraftRank) rank;
                if (craftRank.needsUpdate()) {
                    try {
                        db.addUpdateBatch(craftRank);
                        rank.update(false);
                    } catch (QueryException e) {
                        rank.update(true);
                        throw e;
                    }
                }
            }
        }

        for (ClanPlayer clanPlayer : plugin.getClanPlayerManager().getClanPlayers()) {
            CraftClanPlayer craftClanPlayer = (CraftClanPlayer) clanPlayer;
            if (craftClanPlayer.needsUpdate()) {

                try {
                    db.addUpdateBatch(craftClanPlayer);
                    clanPlayer.update(false);
                } catch (QueryException e) {
                    clanPlayer.update(true);
                    throw e;
                }
            }
        }

        db.executeBatch(CraftRank.class, (byte) 1);
        db.executeBatch(CraftClan.class, (byte) 1);
        db.executeBatch(CraftClanPlayer.class, (byte) 1);

        Executable executable;

        while ((executable = queue.poll()) != null) {
            executable.execute(dataManager);
        }
    }

    public synchronized void addExecutable(Executable executable) {
        queue.add(executable);
    }

    public int size() {
        return queue.size();
    }
}
