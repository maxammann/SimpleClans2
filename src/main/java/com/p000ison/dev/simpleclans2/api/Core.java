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
import com.p000ison.dev.simpleclans2.clanplayer.ClanPlayerManager;
import com.p000ison.dev.simpleclans2.commands.CommandManager;
import com.p000ison.dev.simpleclans2.database.Database;
import com.p000ison.dev.simpleclans2.database.DatabaseManager;
import com.p000ison.dev.simpleclans2.database.data.DataManager;
import com.p000ison.dev.simpleclans2.requests.RequestManager;
import com.p000ison.dev.simpleclans2.settings.SettingsManager;

/**
 * Represents a Core
 */
public interface Core {

    public DatabaseManager getDatabaseManager();

    public ClanManager getClanManager();

    public ClanPlayerManager getClanPlayerManager();

    public SettingsManager getSettingsManager();

    public RequestManager getRequestManager();

    public CommandManager getCommandManager();

    public DataManager getDataManager();

    public Database getSimpleClansDatabase();
}
