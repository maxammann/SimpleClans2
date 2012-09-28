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
 *     Created: 09.09.12 15:06
 */

package com.p000ison.dev.simpleclans2.ranks;

import com.p000ison.dev.simpleclans2.SimpleClans;
import com.p000ison.dev.simpleclans2.clan.Clan;

import java.util.HashSet;
import java.util.Set;

/**
 * Represents a RankManager
 */
public class RankManager {
    private SimpleClans plugin;


    public RankManager(SimpleClans plugin)
    {
        this.plugin = plugin;
    }

    public Rank createRank(Clan clan, String name, int priority)
    {
        Rank rank = new Rank(name, priority);
        plugin.getDataManager().insertRank(clan, rank);

        rank.setId(getRankId(name));

        return rank;
    }

    private long getRankId(String name)
    {
        return plugin.getDataManager().retrieveRankId(name);
    }


}
