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
import org.bukkit.command.CommandSender;

import java.util.List;

/**
 * Represents a BBResponse
 */
public class BBResponse extends Response {

    private Clan clan;
    private int page, maxLines;
    private String format;

    public BBResponse(SimpleClans plugin, CommandSender sender, Clan clan, int page, int maxLines, String format)
    {
        super(plugin, sender);
        this.clan = clan;
        this.page = page;
        this.maxLines = maxLines;
        this.format = format;
    }

    @Override
    public boolean response()
    {
        DataManager dataManager = plugin.getDataManager();


        List<String> bb;

        if (maxLines != -1 && page == -1) {
            bb = dataManager.retrieveBB(clan, 0, maxLines);
        } else {
            int[] boundings = getBoundings(page);
            bb = dataManager.retrieveBB(clan, boundings[0], boundings[1]);
        }

        if (bb == null) {
            return false;
        } else if (bb.isEmpty()) {
            sender.sendMessage(Language.getTranslation("bb.is.empty.or.not.found"));
            return true;
        }

        for (String message : bb) {
            sender.sendMessage(format == null ? message : String.format(format, message));
        }

        return true;
    }
}
