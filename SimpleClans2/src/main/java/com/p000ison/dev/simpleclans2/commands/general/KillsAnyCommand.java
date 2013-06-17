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
 *     Last modified: 02.11.12 16:45
 */

package com.p000ison.dev.simpleclans2.commands.general;

import com.p000ison.dev.commandlib.CallInformation;
import com.p000ison.dev.simpleclans2.SimpleClans;
import com.p000ison.dev.simpleclans2.api.chat.Align;
import com.p000ison.dev.simpleclans2.api.chat.ChatBlock;
import com.p000ison.dev.simpleclans2.api.clanplayer.ClanPlayer;
import com.p000ison.dev.simpleclans2.commands.GenericConsoleCommand;
import com.p000ison.dev.simpleclans2.language.Language;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

import java.util.Map;

/**
 * Represents a KillsAnyCommand
 */
public class KillsAnyCommand extends GenericConsoleCommand {

    public KillsAnyCommand(SimpleClans plugin) {
        super("Kills (Any)", plugin);
        addArgument(Language.getTranslation("argument.player")).addArgument(Language.getTranslation("argument.page"), true, true);
        setDescription(Language.getTranslation("description.kills.any"));
        setIdentifiers(Language.getTranslation("kills.command"));
        addPermission("simpleclans.anyone.kills");
    }

    @Override
    public void execute(CommandSender sender, String[] arguments, CallInformation info) {
        ClanPlayer cp = getPlugin().getClanPlayerManager().getAnyClanPlayer(arguments[0]);

        if (cp == null) {
            ChatBlock.sendMessage(sender, ChatColor.DARK_RED + Language.getTranslation("no.player.matched"));
            return;
        }

        //todo duplicate code
        Map<Integer, Long> killsPerPlayer = getPlugin().getDataManager().getKillsPerPlayer(cp.getID());

        if (killsPerPlayer.isEmpty()) {
            ChatBlock.sendMessage(sender, ChatColor.RED + Language.getTranslation("nokillsfound"));
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

        ChatBlock.sendMessage(sender, Language.getTranslation("displaying.player", cp.getName()));
        ChatBlock.sendBlank(sender);
        chatBlock.sendBlock(sender);
        ChatBlock.sendBlank(sender);
    }
}