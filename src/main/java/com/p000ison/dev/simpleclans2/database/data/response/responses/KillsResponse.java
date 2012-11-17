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
 *     Last modified: 01.11.12 19:25
 */

package com.p000ison.dev.simpleclans2.database.data.response.responses;

import com.p000ison.dev.simpleclans2.SimpleClans;
import com.p000ison.dev.simpleclans2.clanplayer.ClanPlayer;
import com.p000ison.dev.simpleclans2.database.data.response.Response;
import com.p000ison.dev.simpleclans2.language.Language;
import com.p000ison.dev.simpleclans2.util.chat.Align;
import com.p000ison.dev.simpleclans2.util.chat.ChatBlock;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

import java.util.Map;
import java.util.SortedMap;

/**
 * Represents a KillsResponse
 */
public class KillsResponse extends Response {

    private ClanPlayer otherPlayer;
    private int page;

    public KillsResponse(SimpleClans plugin, CommandSender sender, ClanPlayer polledPlayer, int page)
    {
        super(plugin, sender);
        this.otherPlayer = polledPlayer;
        this.page = page;
    }

    @Override
    public boolean response()
    {
        SortedMap<Integer, Long> killsPerPlayer = plugin.getDataManager().getKillsPerPlayer(otherPlayer.getId());

        if (killsPerPlayer.isEmpty()) {
            ChatBlock.sendMessage(sender, ChatColor.RED + Language.getTranslation("nokillsfound"));
            return true;
        }

        ChatBlock chatBlock = new ChatBlock();
        chatBlock.setAlignment(Align.LEFT, Align.CENTER);

        chatBlock.addRow(Language.getTranslation("victim"), Language.getTranslation("killcount"));

        int[] boundings = getBoundings(killsPerPlayer.size(), page);

        int i = 0;
        for (Map.Entry<Integer, Long> entry : killsPerPlayer.entrySet()) {
            if (i < boundings[0]) {
                continue;
            } else if (i > boundings[1]) {
                break;
            }

            chatBlock.addRow(plugin.getClanPlayerManager().getClanPlayer(entry.getValue()).getName(), ChatColor.AQUA.toString() + entry.getKey());
        }

        System.out.println("kills");

        ChatBlock.sendHead(sender, plugin.getSettingsManager().getClanColor() + otherPlayer.getName(), Language.getTranslation("kills"));
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
