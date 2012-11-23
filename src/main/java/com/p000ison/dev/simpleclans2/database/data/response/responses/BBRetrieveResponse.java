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
import com.p000ison.dev.simpleclans2.util.chat.ChatBlock;
import org.bukkit.command.CommandSender;

import java.util.List;
import java.util.ListIterator;

/**
 * Represents a BBRetrieveResponse
 */
public class BBRetrieveResponse extends Response {

    private Clan clan;
    private int page, maxLines;
    private boolean showError;

    public BBRetrieveResponse(SimpleClans plugin, CommandSender sender, Clan clan, int page, int maxLines, boolean showError)
    {
        super(plugin, sender);
        this.clan = clan;
        this.page = page;
        this.maxLines = maxLines;
        this.showError = showError;
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
            if (showError) {
                ChatBlock.sendMessage(sender, Language.getTranslation("bb.is.empty.or.not.found"));
            }
            return true;
        }

        ListIterator<String> it = bb.listIterator(bb.size());

        while (it.hasPrevious()) {
            ChatBlock.sendMessage(sender, it.previous());
        }

        return true;
    }

    @Override
    public boolean needsRetriever()
    {
        return true;
    }
}
