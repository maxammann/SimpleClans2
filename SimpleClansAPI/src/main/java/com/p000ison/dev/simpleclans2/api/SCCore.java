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

package com.p000ison.dev.simpleclans2.api;

import com.p000ison.dev.simpleclans2.api.clan.ClanManager;
import com.p000ison.dev.simpleclans2.api.clanplayer.ClanPlayerManager;
import com.p000ison.dev.simpleclans2.api.command.CommandManager;
import com.p000ison.dev.simpleclans2.api.rank.RankManager;
import com.p000ison.dev.simpleclans2.api.request.RequestManager;

/**
 * Represents a SCCore
 */
public interface SCCore {

    /**
     * Gets the manager, which handles everything about clans.
     *
     * @return The clan manager of SimpleClans.
     */
    ClanManager getClanManager();

    /**
     * Gets the manager, which handles everything about clan players.
     *
     * @return The clan player manager of SimpleClans.
     */
    ClanPlayerManager getClanPlayerManager();

    /**
     * Gets the manager, which handles all the requests.
     *
     * @return The request manager or SimpleClans.
     */
    RequestManager getRequestManager();

    /**
     * Gets the manager, which handles everything about ranks.
     *
     * @return The rank manager or SimpleClans.
     */
    RankManager getRankManager();

    /**
     * Gets the manager, which handles everything about commands.
     *
     * @return The command manager or SimpleClans.
     */
    CommandManager getCommandManager();

    String getTranslation(String key, Object... args);
}
