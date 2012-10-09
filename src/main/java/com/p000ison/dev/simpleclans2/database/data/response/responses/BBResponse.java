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
 *     Created: 09.10.12 21:24
 */

package com.p000ison.dev.simpleclans2.database.data.response.responses;

import com.p000ison.dev.simpleclans2.SimpleClans;
import com.p000ison.dev.simpleclans2.clan.Clan;
import com.p000ison.dev.simpleclans2.database.data.DataManager;
import com.p000ison.dev.simpleclans2.database.data.response.Response;
import org.bukkit.command.CommandSender;

import java.util.List;

/**
 * Represents a BBResponse
 */
public class BBResponse extends Response {

    private Clan clan;
    private int page;

    public BBResponse(SimpleClans plugin, CommandSender sender, Clan clan, int page)
    {
        super(plugin, sender);
        this.clan = clan;
        this.page = page;
    }

    @Override
    public boolean execute()
    {
        DataManager dataManager = plugin.getDataManager();

        int[] boundings = getBoundings(page);
        List<String> bb = dataManager.retrieveBB(clan, boundings[0], boundings[1]);

        for (String s : bb) {
            sender.sendMessage(s);
        }

        return true;
    }
}
