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

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * Represents a AutoSaver
 */
public class AutoSaver implements Runnable {

    private Queue<Executable> queue = new ConcurrentLinkedQueue<Executable>();

    private SimpleClans plugin;
    private DataManager dataManager;

    public AutoSaver(SimpleClans simpleClans, DataManager dataManager)
    {
        this.plugin = simpleClans;
        this.dataManager = dataManager;
    }

    @Override
    public synchronized void run()
    {
        for (Clan clan : plugin.getClanManager().getClans()) {
            if (clan.needsUpdate()) {
                dataManager.getDatabase().save(clan);
                clan.update(false);
            }

            for (Rank rank : clan.getRanks()) {
                if (rank.needsUpdate()) {
                    dataManager.getDatabase().save(rank);
                    rank.update(false);
                }
            }
        }

        for (ClanPlayer clanPlayer : plugin.getClanPlayerManager().getClanPlayers()) {
            if (clanPlayer.needsUpdate()) {
                dataManager.getDatabase().save(clanPlayer);
                clanPlayer.update(false);
            }
        }

        Executable executable;

        while ((executable = queue.poll()) != null) {
            System.out.println(executable);
            executable.execute(dataManager);
        }
    }

    public synchronized void addExecutable(Executable executable)
    {
        queue.add(executable);
    }

    public int size()
    {
        return queue.size();
    }
}
