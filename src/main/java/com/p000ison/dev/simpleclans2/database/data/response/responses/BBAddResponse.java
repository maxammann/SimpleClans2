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

package com.p000ison.dev.simpleclans2.database.data.response.responses;

import com.p000ison.dev.simpleclans2.SimpleClans;
import com.p000ison.dev.simpleclans2.clan.Clan;
import com.p000ison.dev.simpleclans2.database.data.DataManager;
import com.p000ison.dev.simpleclans2.database.data.response.Response;
import com.p000ison.dev.simpleclans2.language.Language;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

/**
 * Represents a BBResponse
 */
public class BBAddResponse extends Response {

    private Clan clan;
    private String message;

    public BBAddResponse(SimpleClans plugin, CommandSender sender, Clan clan, String message)
    {
        super(plugin, sender);
        this.clan = clan;
        this.message = message;
    }

    @Override
    public boolean execute()
    {
        DataManager dataManager = plugin.getDataManager();

        dataManager.insertBBMessage(clan, message);
        sender.sendMessage(ChatColor.AQUA + Language.getTranslation("bb.added"));
        return true;
    }
}
