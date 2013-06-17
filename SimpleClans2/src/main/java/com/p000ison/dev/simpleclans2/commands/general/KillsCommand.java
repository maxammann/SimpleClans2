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
 *     Last modified: 01.11.12 22:17
 */

package com.p000ison.dev.simpleclans2.commands.general;

import com.p000ison.dev.commandlib.CallInformation;
import com.p000ison.dev.simpleclans2.SimpleClans;
import com.p000ison.dev.simpleclans2.api.chat.Align;
import com.p000ison.dev.simpleclans2.api.chat.ChatBlock;
import com.p000ison.dev.simpleclans2.api.clanplayer.ClanPlayer;
import com.p000ison.dev.simpleclans2.commands.GenericPlayerCommand;
import com.p000ison.dev.simpleclans2.language.Language;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.Map;

/**
 * Represents a KillsCommand
 */
public class KillsCommand extends GenericPlayerCommand {

    public KillsCommand(SimpleClans plugin) {
        super("Kills", plugin);
        addArgument(Language.getTranslation("argument.page"), true);
        setDescription(Language.getTranslation("description.kills"));
        setIdentifiers(Language.getTranslation("kills.command"));
        addPermission("simpleclans.member.kills");

        setAsynchronous(true);
    }

    @Override
    public void execute(Player player, ClanPlayer unused, String[] arguments, CallInformation info) {
        ClanPlayer cp = getPlugin().getClanPlayerManager().getAnyClanPlayer(player);

        if (cp == null) {
            player.sendMessage("no.player.data.found");
            return;
        }

        Map<Integer, Long> killsPerPlayer = getPlugin().getDataManager().getKillsPerPlayer(cp.getID());

        if (killsPerPlayer.isEmpty()) {
            ChatBlock.sendMessage(player, ChatColor.RED + Language.getTranslation("nokillsfound"));
            return;
        }

        ChatBlock chatBlock = new ChatBlock();
        chatBlock.setAlignment(Align.LEFT, Align.CENTER);

        chatBlock.addRow(Language.getTranslation("victim"), Language.getTranslation("killcount"));

        int page = info.getPage(killsPerPlayer.size());
        int start = info.getStartIndex(page, killsPerPlayer.size());
        int end = info.getEndIndex(page, killsPerPlayer.size());

        int i = 0;
        for (Map.Entry<Integer, Long> entry : killsPerPlayer.entrySet()) {
            if (i < start) {
                continue;
            } else if (i > end) {
                break;
            }

            chatBlock.addRow(getPlugin().getClanPlayerManager().getClanPlayer(entry.getValue()).getName(), ChatColor.AQUA.toString() + entry.getKey());
        }

        ChatBlock.sendBlank(player);
        chatBlock.sendBlock(player);
        ChatBlock.sendBlank(player);
    }
}