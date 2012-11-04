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
 *     Last modified: 04.11.12 00:59
 */

package com.p000ison.dev.simpleclans2.database.data.response.responses;

import com.p000ison.dev.simpleclans2.SimpleClans;
import com.p000ison.dev.simpleclans2.clanplayer.ClanPlayer;
import com.p000ison.dev.simpleclans2.database.data.Conflicts;
import com.p000ison.dev.simpleclans2.database.data.response.Response;
import com.p000ison.dev.simpleclans2.language.Language;
import com.p000ison.dev.simpleclans2.util.chat.Align;
import com.p000ison.dev.simpleclans2.util.chat.ChatBlock;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

import java.util.Set;

/**
 * Represents a KillsResponse
 */
public class MostKilledResponse extends Response {

    private int page;

    public MostKilledResponse(SimpleClans plugin, CommandSender sender, int page)
    {
        super(plugin, sender);
        this.page = page;
    }

    @Override
    public boolean response()
    {
        ChatBlock chatBlock = new ChatBlock();
        ChatColor headColor = plugin.getSettingsManager().getHeaderPageColor();

        chatBlock.setAlignment(Align.LEFT, Align.CENTER, Align.LEFT);

        chatBlock.addRow("  " + headColor + Language.getTranslation("victim"), headColor + Language.getTranslation("killcount"), headColor + Language.getTranslation("attacker"));

        Set<Conflicts> mostKilledSet = plugin.getDataManager().getMostKilled();

        if (mostKilledSet.isEmpty()) {
            ChatBlock.sendMessage(sender, ChatColor.RED + Language.getTranslation("nokillsfound"));
            return true;
        }

        int[] boundings = getBoundings(mostKilledSet.size(), page);

        int i = 0;
        for (Conflicts conflict : mostKilledSet) {
            if (i < boundings[0]) {
                continue;
            } else if (i > boundings[1]) {
                break;
            }

            ClanPlayer attacker = plugin.getClanPlayerManager().getClanPlayer(conflict.getAttacker());
            ClanPlayer victim = plugin.getClanPlayerManager().getClanPlayer(conflict.getVictim());

            chatBlock.addRow("  " + ChatColor.WHITE + victim.getName(), ChatColor.AQUA.toString() + conflict.getConflicts(), ChatColor.YELLOW + attacker.getName());
        }

        ChatBlock.sendHead(sender, plugin.getSettingsManager().getServerName(), Language.getTranslation("mostkilled"));
        ChatBlock.sendBlank(sender);

        chatBlock.sendBlock(sender);

        ChatBlock.sendBlank(sender);
        return true;
    }

    @Override
    public boolean needsRetriever()
    {
        return true;
    }
}
