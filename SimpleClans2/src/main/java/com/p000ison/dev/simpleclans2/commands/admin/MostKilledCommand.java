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
 *     Last modified: 04.11.12 00:52
 */

package com.p000ison.dev.simpleclans2.commands.admin;

import com.p000ison.dev.commandlib.CallInformation;
import com.p000ison.dev.simpleclans2.SimpleClans;
import com.p000ison.dev.simpleclans2.api.chat.Align;
import com.p000ison.dev.simpleclans2.api.chat.ChatBlock;
import com.p000ison.dev.simpleclans2.api.clanplayer.ClanPlayer;
import com.p000ison.dev.simpleclans2.commands.GenericConsoleCommand;
import com.p000ison.dev.simpleclans2.database.Conflicts;
import com.p000ison.dev.simpleclans2.language.Language;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

import java.util.List;

/**
 * Represents a MostKilledCommand
 */
public class MostKilledCommand extends GenericConsoleCommand {

    public MostKilledCommand(SimpleClans plugin) {
        super("Most killed players", plugin);
        addArgument(Language.getTranslation("argument.page"), true, true);
        setDescription(Language.getTranslation("description.mostkilled"));
        setIdentifiers(Language.getTranslation("mostkilled.command"));
        addPermission("simpleclans.mod.mostkilled");

        setAsynchronous(true);
    }

    @Override
    public void execute(CommandSender sender, String[] arguments, CallInformation info) {
        ChatBlock chatBlock = new ChatBlock();
        ChatColor headColor = getPlugin().getSettingsManager().getHeaderPageColor();

        chatBlock.setAlignment(Align.LEFT, Align.CENTER, Align.LEFT);

        chatBlock.addRow("  " + headColor + Language.getTranslation("victim"), headColor + Language.getTranslation("killcount"), headColor + Language.getTranslation("attacker"));

        List<Conflicts> mostKilledSet = getPlugin().getDataManager().getMostKilled();

        if (mostKilledSet.isEmpty()) {
            ChatBlock.sendMessage(sender, ChatColor.RED + Language.getTranslation("nokillsfound"));
            return;
        }

        int page = info.getPage(mostKilledSet.size());
        int start = info.getStartIndex(page, mostKilledSet.size());
        int end = info.getEndIndex(page, mostKilledSet.size());

        for (int i = start; i < end; i++) {
            Conflicts conflict = mostKilledSet.get(i);

            ClanPlayer attacker = getPlugin().getClanPlayerManager().getClanPlayer(conflict.getAttacker());
            ClanPlayer victim = getPlugin().getClanPlayerManager().getClanPlayer(conflict.getVictim());

            if (attacker == null || victim == null) {
                continue;
            }

            chatBlock.addRow("  " + ChatColor.WHITE + victim.getName(), ChatColor.AQUA.toString() + conflict.getConflicts(), ChatColor.YELLOW + attacker.getName());
        }

        ChatBlock.sendBlank(sender);
        chatBlock.sendBlock(sender);
        ChatBlock.sendBlank(sender);
    }
}