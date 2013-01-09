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

package com.p000ison.dev.simpleclans2.clan.ranks;

import com.p000ison.dev.simpleclans2.SimpleClans;
import com.p000ison.dev.simpleclans2.api.clan.Clan;
import com.p000ison.dev.simpleclans2.api.rank.RankManager;

/**
 * Represents a CraftRankManager
 */
public class CraftRankManager implements RankManager {
    private SimpleClans plugin;

    public CraftRankManager(SimpleClans plugin)
    {
        this.plugin = plugin;
    }

    @Override
    public CraftRank createRank(Clan clan, String name, String tag, int priority)
    {
        CraftRank rank = new CraftRank(name, tag, priority, clan.getID());

        plugin.getDataManager().getDatabase().save(rank);
        return rank;
    }
}
