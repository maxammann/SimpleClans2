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
 *     Created: 16.09.12 17:28
 */

package com.p000ison.dev.simpleclans2.database.data;

import com.p000ison.dev.simpleclans2.SimpleClans;
import com.p000ison.dev.simpleclans2.clan.Clan;
import com.p000ison.dev.simpleclans2.clanplayer.ClanPlayer;
import com.p000ison.dev.simpleclans2.util.Logging;

import java.util.LinkedList;
import java.util.Queue;

/**
 * Represents a AutoSaver
 */
public class AutoSaver extends LinkedList<Executable> implements Queue<Executable>, Runnable {

    private DataManager dataManager;
    private SimpleClans plugin;

    public AutoSaver(SimpleClans simpleClans, DataManager dataManager)
    {
        this.plugin = simpleClans;
        this.dataManager = dataManager;
        System.out.println(dataManager);
    }

    @Override
    public void run()
    {
        for (Clan clan : plugin.getClanManager().getClans()) {
            if (clan.needsUpdate()) {
                System.out.println(dataManager);
                dataManager.updateClan(clan);
                clan.update(false);
            }
        }

        for (ClanPlayer clanPlayer : plugin.getClanPlayerManager().getClanPlayers()) {
            if (clanPlayer.needsUpdate()) {
                dataManager.updateClanPlayer(clanPlayer);
                clanPlayer.update(false);
            }
        }

        Executable statement;

        while ((statement = this.poll()) != null) {
            if (!statement.execute(dataManager)) {
                Logging.debug("Failed to execute query!");
            }
        }
    }
}
