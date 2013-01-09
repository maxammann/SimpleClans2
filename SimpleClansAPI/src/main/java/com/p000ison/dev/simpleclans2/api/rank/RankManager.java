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
 *     Last modified: 09.01.13 19:15
 */

package com.p000ison.dev.simpleclans2.api.rank;

import com.p000ison.dev.simpleclans2.api.clan.Clan;

/**
 *
 */
public interface RankManager {
    /**
     * Creates a new rank
     *
     * @param clan     The clan
     * @param name     The name
     * @param tag      The tag
     * @param priority The priority
     * @return The created rank
     */
    Rank createRank(Clan clan, String name, String tag, int priority);
}
