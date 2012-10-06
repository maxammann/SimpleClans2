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
 *     Created: 18.09.12 16:52
 */

package com.p000ison.dev.simpleclans2.api;

import com.p000ison.dev.simpleclans2.clan.ClanManager;
import com.p000ison.dev.simpleclans2.clan.ranks.RankManager;
import com.p000ison.dev.simpleclans2.clanplayer.ClanPlayerManager;
import com.p000ison.dev.simpleclans2.database.Database;
import com.p000ison.dev.simpleclans2.requests.RequestManager;
import com.p000ison.dev.simpleclans2.settings.SettingsManager;
import com.p000ison.dev.simpleclans2.teleportation.TeleportManager;

/**
 * Represents a Core
 */
public interface Core {

    /**
     * Gets the manager, which handles everything about clans.
     *
     * @return The clan manager of SimpleClans.
     */
    public ClanManager getClanManager();

    /**
     * Gets the manager, which handles everything about clan players.
     *
     * @return The clan player manager of SimpleClans.
     */
    public ClanPlayerManager getClanPlayerManager();

    /**
     * Gets the manager, which stores all the settings.
     *
     * @return The settings manager of SimpleClans.
     */
    public SettingsManager getSettingsManager();

    /**
     * Gets the manager, which handles all the requests.
     *
     * @return The request manager or SimpleClans.
     */
    public RequestManager getRequestManager();


    /**
     * Gets the manager, which handles everything about ranks.
     *
     * @return The rank manager or SimpleClans.
     */
    public RankManager getRankManager();

    /**
     * Gets the manager, which handles everything about teleports.
     *
     * @return The teleport manager or SimpleClans.
     */
    public TeleportManager getTeleportManager();

    /**
     * Gets the database of SimpleClans.
     *
     * @return The database of SimpleClans.
     */
    public Database getSimpleClansDatabase();
}
