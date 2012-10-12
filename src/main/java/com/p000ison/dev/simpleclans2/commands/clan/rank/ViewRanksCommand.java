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

package com.p000ison.dev.simpleclans2.commands.clan.rank;

import com.p000ison.dev.simpleclans2.SimpleClans;
import com.p000ison.dev.simpleclans2.clan.Clan;
import com.p000ison.dev.simpleclans2.clan.ranks.Rank;
import com.p000ison.dev.simpleclans2.clanplayer.ClanPlayer;
import com.p000ison.dev.simpleclans2.commands.GenericPlayerCommand;
import com.p000ison.dev.simpleclans2.language.Language;
import com.p000ison.dev.simpleclans2.util.chat.Align;
import com.p000ison.dev.simpleclans2.util.chat.ChatBlock;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

/**
 * Represents a ViewPermissionsCommand
 */
public class ViewRanksCommand extends GenericPlayerCommand {

    public ViewRanksCommand(SimpleClans plugin)
    {
        super("ViewPermissions", plugin);
        setArgumentRange(0, 0);
        setUsages(Language.getTranslation("usage.view.ranks", plugin.getSettingsManager().getClanCommand()));
        setIdentifiers(Language.getTranslation("view.ranks.command"));
        setPermission("simpleclans.rank.view.ranks");
        setType(Type.RANK);
    }

    @Override
    public String getMenu(ClanPlayer clanPlayer)
    {
        return null;
    }

    @Override
    public void execute(Player player, String command, String[] args)
    {
        ClanPlayer clanPlayer = plugin.getClanPlayerManager().getClanPlayer(player);

        if (clanPlayer == null) {
            return;
        }

        Clan clan = clanPlayer.getClan();

        Set<Rank> ranks = clan.getRanks();

        List<Rank> sorted = new ArrayList<Rank>(ranks);

        Collections.sort(sorted);

        if (ranks.isEmpty()) {
            return;
        }

        int page = 0;
        int completeSize = ranks.size();

        if (args.length == 1) {
            try {
                page = Integer.parseInt(args[0]) - 1;
            } catch (NumberFormatException e) {
                player.sendMessage(Language.getTranslation("number.format"));
                return;
            }
        }


        ChatBlock chatBlock = new ChatBlock();

        ChatBlock.sendHead(player, plugin.getSettingsManager().getClanColor() + clan.getTag(), Language.getTranslation("ranks"));

        ChatBlock.sendBlank(player);

        chatBlock.setAlignment(Align.CENTER, Align.LEFT, Align.CENTER);
        chatBlock.addRow(Language.getTranslation("id"), Language.getTranslation("tag"), Language.getTranslation("priority"));

        int[] boundings = getBoundings(completeSize, page);

        for (int i = boundings[0]; i < boundings[1]; i++) {
            Rank rank = sorted.get(i);

            chatBlock.addRow(ChatColor.GRAY.toString() + rank.getId(), ChatColor.GRAY + rank.getTag(), ChatColor.GRAY.toString() + rank.getPriority());
        }


        chatBlock.sendBlock(player);
    }
}
