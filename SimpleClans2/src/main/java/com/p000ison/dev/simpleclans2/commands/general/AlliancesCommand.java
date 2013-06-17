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

package com.p000ison.dev.simpleclans2.commands.general;

import com.p000ison.dev.commandlib.CallInformation;
import com.p000ison.dev.simpleclans2.SimpleClans;
import com.p000ison.dev.simpleclans2.api.chat.Align;
import com.p000ison.dev.simpleclans2.api.chat.ChatBlock;
import com.p000ison.dev.simpleclans2.api.clan.Clan;
import com.p000ison.dev.simpleclans2.commands.GenericConsoleCommand;
import com.p000ison.dev.simpleclans2.language.Language;
import com.p000ison.dev.simpleclans2.util.GeneralHelper;
import com.p000ison.dev.simpleclans2.util.comparators.KDRComparator;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

/**
 * Represents a AlliancesCommand
 */
public class AlliancesCommand extends GenericConsoleCommand {


    public AlliancesCommand(SimpleClans plugin) {
        super("Alliances", plugin);
        addArgument(Language.getTranslation("argument.page"), true, true);
        setDescription(Language.getTranslation("description.alliances"));
        setIdentifiers(Language.getTranslation("alliances.command"));
        addPermission("simpleclans.anyone.alliances");
    }

    @Override
    public void execute(CommandSender sender, String[] arguments, CallInformation info) {
        ChatColor headColor = getPlugin().getSettingsManager().getHeaderPageColor();
        ChatColor subColor = getPlugin().getSettingsManager().getSubPageColor();


        List<Clan> clans = new ArrayList<Clan>(getPlugin().getClanManager().getClans());

        if (clans.isEmpty()) {
            ChatBlock.sendMessage(sender, ChatColor.RED + Language.getTranslation("no.clans.have.been.created"));
            return;
        }

        int completeSize = clans.size();

        Collections.sort(clans, new KDRComparator());

        ChatBlock chatBlock = new ChatBlock();

        ChatBlock.sendBlank(sender);

        chatBlock.setAlignment(Align.LEFT, Align.LEFT);
        chatBlock.addRow(headColor + Language.getTranslation("clan"), Language.getTranslation("allies"));

        int page = info.getPage(completeSize);
        int start = info.getStartIndex(page, completeSize);
        int end = info.getEndIndex(page, completeSize);

        for (int i = start; i < end; i++) {
            Clan clan = clans.get(i);
            if (!clan.isVerified()) {
                completeSize--;
                continue;
            }

            Set<Clan> allies = clan.getAllies();

            String alliesString;

            if (allies == null || allies.isEmpty()) {
                alliesString = subColor + "None";
            } else {
                alliesString = GeneralHelper.clansToString(clan.getAllies(), ChatColor.DARK_GRAY + " + ");
            }

            chatBlock.addRow("  " + ChatColor.AQUA + clan.getTag(), alliesString);
        }

        chatBlock.sendBlock(sender);

        ChatBlock.sendBlank(sender);
    }
}
