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

package com.p000ison.dev.simpleclans2.database.response.responses;

import com.p000ison.dev.simpleclans2.SimpleClans;
import com.p000ison.dev.simpleclans2.clan.Clan;
import com.p000ison.dev.simpleclans2.database.DatabaseManager;
import com.p000ison.dev.simpleclans2.database.response.Response;
import com.p000ison.dev.simpleclans2.language.Language;
import com.p000ison.dev.simpleclans2.util.chat.ChatBlock;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

/**
 * Represents a BBRetrieveResponse
 */
public class BBClearResponse extends Response {

    private Clan clan;

    public BBClearResponse(SimpleClans plugin, CommandSender sender, Clan clan)
    {
        super(plugin, sender);
        this.clan = clan;
    }

    @Override
    public boolean response()
    {
        DatabaseManager dataManager = plugin.getDataManager();

        dataManager.purgeBB(clan);
        ChatBlock.sendMessage(sender, ChatColor.AQUA + Language.getTranslation("cleared.bb"));
        return true;
    }

    @Override
    public boolean needsRetriever()
    {
        return false;
    }
}
