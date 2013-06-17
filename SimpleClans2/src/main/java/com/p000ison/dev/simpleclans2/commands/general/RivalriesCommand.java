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
 *     Last modified: 11.10.12 15:26
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

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

/**
 * Represents a RivalriesCommand
 */
public class RivalriesCommand extends GenericConsoleCommand {

    public RivalriesCommand(SimpleClans plugin) {
        super("Rivalries", plugin);
        addArgument(Language.getTranslation("argument.page"), true, true);
        setDescription(MessageFormat.format(Language.getTranslation("description.rivalries"), plugin.getSettingsManager().getClanColor()));
        setIdentifiers(Language.getTranslation("rivalries.command"));
        addPermission("simpleclans.anyone.rivalries");
    }

    @Override
    public void execute(CommandSender sender, String[] arguments, CallInformation info) {
        ChatColor headColor = getPlugin().getSettingsManager().getHeaderPageColor();


        Set<Clan> unsortedClans = getPlugin().getClanManager().getClans();
        int completeSize = unsortedClans.size();

        List<Clan> clans = new ArrayList<Clan>(unsortedClans);
        Collections.sort(clans, new KDRComparator());

        ChatBlock chatBlock = new ChatBlock();

        ChatBlock.sendBlank(sender);
        ChatBlock.sendMessage(sender, headColor + Language.getTranslation("legend") + ChatColor.DARK_RED + " [" + Language.getTranslation("war") + "]");
        ChatBlock.sendBlank(sender);

        chatBlock.setAlignment(Align.LEFT, Align.LEFT);
        chatBlock.addRow(Language.getTranslation("clan"), Language.getTranslation("rivals"));

        int page = info.getPage(completeSize);
        int start = info.getStartIndex(page, completeSize);
        int end = info.getEndIndex(page, completeSize);

        for (int i = start; i < end; i++) {
            Clan clan = clans.get(i);
            if (!clan.isVerified()) {
                continue;
            }

            Set<Clan> rivals = clan.getRivals();

            chatBlock.addRow(ChatColor.AQUA + clan.getTag(), rivals == null || rivals.isEmpty() ? Language.getTranslation("none") : GeneralHelper.clansToString(rivals, ","));
        }

        chatBlock.sendBlock(sender);

        ChatBlock.sendBlank(sender);
    }
}